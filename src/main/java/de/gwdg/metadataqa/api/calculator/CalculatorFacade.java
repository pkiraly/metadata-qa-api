package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.calculator.output.MetricCollector;
import de.gwdg.metadataqa.api.calculator.output.OutputCollector;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.interfaces.Shutdownable;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.TfIdf;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.gwdg.metadataqa.api.util.CsvReader;

/**
 * The central entry point of the application. It provides a facade to the
 * subsystems.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CalculatorFacade implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(
      CalculatorFacade.class.getCanonicalName()
  );
  private static final long serialVersionUID = -5956665711362465908L;

  protected MeasurementConfiguration configuration;

  /**
   * Is it the first record?
   */
  protected boolean isFirstRecord = true;

  protected CompressionLevel compressionLevel = CompressionLevel.NORMAL;

  /**
   * Flag to detect status changes (default: false).
   */
  private boolean changed = false;

  /**
   * The list of registered calculator objects. Those will do the measurements
   */
  protected List<Calculator> calculators = new ArrayList<>();

  /**
   * The completeness calculator.
   */
  protected CompletenessCalculator completenessCalculator;

  /**
   * The TF-IDF calculator.
   */
  protected TfIdfCalculator tfidfCalculator;

  // protected Format format = Format.JSON;
  protected Selector<? extends XmlFieldInstance> cache;
  protected Schema schema;
  protected CsvReader csvReader;

  /**
   * Create calculator facade with the default configuration.
   */
  public CalculatorFacade() {
    this.configuration = new MeasurementConfiguration();
  }

  public CalculatorFacade(MeasurementConfiguration configuration) {
    this.configuration = configuration;
  }

  protected void conditionalConfiguration() {
    if (changed) {
      configure();
      changed = false;
    }
  }

  /**
   * Run the configuration based on the previously set flags.
   */
  public void configure() {
    LOGGER.info("configure()");
    calculators = CalculatorFactory.create(configuration, schema);
  }

  /**
   * Run the measurements with each Calculator then returns the result as CSV.
   *
   * @param inputRecord The JSON record string
   * @return The result of measurements as a CSV string
   * @throws InvalidJsonException If the JSON is invalid
   */
  public String measure(String inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureWithGenerics(inputRecord);
  }

  public String measure(List<String> inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureCsvWithGenerics(inputRecord, OutputCollector.TYPE.STRING);
  }

  public List<String> measureAsList(String inputRecord) throws InvalidJsonException {
    return (List<String>) this.<XmlFieldInstance>measureWithGenerics(inputRecord, OutputCollector.TYPE.STRING_LIST);
  }

  public List<String> measureAsList(List<String> inputRecord) throws InvalidJsonException {
    return (List<String>) this.<XmlFieldInstance>measureCsvWithGenerics(inputRecord, OutputCollector.TYPE.STRING_LIST);
  }

  public List<Object> measureAsListOfObjects(String inputRecord) throws InvalidJsonException {
    return (List<Object>) this.<XmlFieldInstance>measureWithGenerics(inputRecord, OutputCollector.TYPE.OBJECT_LIST);
  }

  public List<Object> measureAsListOfObjects(List<String> inputRecord) throws InvalidJsonException {
    return (List<Object>) this.<XmlFieldInstance>measureCsvWithGenerics(inputRecord, OutputCollector.TYPE.OBJECT_LIST);
  }

  public Map<String, Object> measureAsMap(String inputRecord) throws InvalidJsonException {
    return (Map<String, Object>) this.<XmlFieldInstance>measureWithGenerics(inputRecord, OutputCollector.TYPE.MAP);
  }

  public Map<String, Object> measureAsMap(List<String> inputRecord) throws InvalidJsonException {
    return (Map<String, Object>) this.<XmlFieldInstance>measureCsvWithGenerics(inputRecord, OutputCollector.TYPE.MAP);
  }

  public String measureAsJson(String inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureWithGenerics(inputRecord, OutputCollector.TYPE.JSON);
  }

  public String measureAsJson(List<String> inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureCsvWithGenerics(inputRecord, OutputCollector.TYPE.JSON);
  }

  public Map<String, List<MetricResult>> measureAsMetricResult(String inputRecord) throws InvalidJsonException {
    return (Map<String, List<MetricResult>>) this.<XmlFieldInstance>measureWithGenerics(inputRecord, OutputCollector.TYPE.METRIC);
  }

  public Map<String, List<MetricResult>> measureAsMetricResult(List<String> inputRecord) throws InvalidJsonException {
    return (Map<String, List<MetricResult>>) this.<XmlFieldInstance>measureCsvWithGenerics(inputRecord, OutputCollector.TYPE.METRIC);
  }

  /**
   * The generic version of measure.
   *
   * The class to pass should define the individual field's representations, so
   * it is bound to the schema the record in. The class should be the extension
   * of XmlFieldInstance.
   *
   * @param <T>
   *   A class defining the internal representation of a field. It should be
   *   an extension of XmlFieldInstance
   * @param content The JSON record
   * @return The result of measurements as a CSV string
   * @throws InvalidJsonException If the JSON is invalid
   */
  protected <T extends XmlFieldInstance> Object measureWithGenerics(String content)
    throws InvalidJsonException {
    return measureWithGenerics(content, OutputCollector.TYPE.STRING);
  }

  protected <T extends XmlFieldInstance> Object measureWithGenerics(String content,
                                                                    OutputCollector.TYPE type)
      throws InvalidJsonException {
    conditionalConfiguration();

    MetricCollector collector = new MetricCollector();

    if (schema == null) {
      throw new IllegalStateException("schema is missing");
    } else {
      var format = schema.getFormat();
      if (format != null && content != null) {
        cache = SelectorFactory.getInstance(schema.getFormat(), content, schema.getNamespaces());
        if (schema.getFormat().equals(Format.CSV))
          initializeCsvCache(content);

        if (!isCsvHeaderLine())
          runMeasurements(collector);

        isFirstRecord = false;
      }
    }

    return collector.createOutput(type, compressionLevel);
  }

  private boolean isCsvHeaderLine() {
    return isFirstRecord
      && schema.getFormat().equals(Format.CSV)
      && csvReader.isHeaderAware();
  }

  private void initializeCsvCache(String content) {
    if (isFirstRecord && csvReader.isHeaderAware())
      try {
        csvReader.setHeader(content);
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "initializeCsvCache", e);
      }

    ((CsvSelector)cache).setCsvReader(csvReader);
  }

  protected <T extends XmlFieldInstance> Object measureCsvWithGenerics(List<String> content,
                                                                       OutputCollector.TYPE type)
      throws InvalidJsonException {

    if (schema == null)
      throw new IllegalStateException("schema is missing");

    var format = schema.getFormat();
    if (format == null || format != Format.CSV)
      throw new IllegalStateException("Format is not CSV");

    conditionalConfiguration();
    MetricCollector collector = new MetricCollector();

    if (content != null) {
      cache = new CsvSelector<>(csvReader, content);
      runMeasurements(collector);
    }

    return collector.createOutput(type, compressionLevel);
  }

  private void runMeasurements(OutputCollector collector) {
    if (calculators != null)
      for (Calculator calculator : getCalculators()) {
        List<MetricResult> result = calculator.measure(cache);
        collector.addResult(calculator, result, compressionLevel);
      }
  }

  /**
   * Return the list of all registered calculators.
   *
   * @return
   *   The calculators
   */
  public List<Calculator> getCalculators() {
    return calculators;
  }

  /**
   * Returns the list of existing fields.
   *
   * @return
   *   The list of existing fields
   */
  public List<String> getExistingFields() {
    return completenessCalculator.getExistingFields();
  }

  /**
   * Returns the list of empty fields.
   *
   * @return
   *   The list of empty fields
   */
  public List<String> getEmptyFields() {
    return completenessCalculator.getEmptyFields();
  }

  /**
   * Returns the list of missing fields.
   *
   * @return
   *    The list of missing fields
   */
  public List<String> getMissingFields() {
    return completenessCalculator.getMissingFields();
  }

  /**
   * Returns the TF-IDF term map. The keys are the field names, the values are
   * lists of TfIdf objects.
   *
   * @return
   *   TF-IDF term list
   * @see
   *   TfIdfCalculator#getTermsCollection()
   */
  public Map<String, List<TfIdf>> getTermsCollection() {
    return tfidfCalculator.getTermsCollection();
  }

  public List<String> getHeader() {
    conditionalConfiguration();
    List<String> header = new ArrayList<>();
    if (calculators != null)
      for (Calculator calculator : getCalculators())
        header.addAll(calculator.getHeader());

    return header;
  }

  public CompressionLevel getCompressionLevel() {
    return compressionLevel;
  }

  public CalculatorFacade setCompressionLevel(CompressionLevel compressionLevel) {
    this.compressionLevel = compressionLevel;
    changed = true;
    return this;
  }

  public Selector<? extends XmlFieldInstance> getCache() {
    return cache;
  }

  public Schema getSchema() {
    return schema;
  }

  public CalculatorFacade setSchema(Schema schema) {
    this.schema = schema;
    changed = true;
    return this;
  }

  public CalculatorFacade setCsvReader() {
    return setCsvReader(false);
  }

  public CalculatorFacade setCsvReader(boolean headerAware) {
    this.csvReader = new CsvReader().setHeaderAware(headerAware);
    changed = true;
    return this;
  }

  public CalculatorFacade setCsvReader(CsvReader csvReader) {
    this.csvReader = csvReader;
    changed = true;
    return this;
  }

  /**
   * Calls the shutDown() method on all calculator which implement Shutdownable interface
   */
  public void shutDown() {
    for (Calculator calculator : getCalculators())
      if (calculator instanceof Shutdownable)
        ((Shutdownable)calculator).shutDown();
  }

}
