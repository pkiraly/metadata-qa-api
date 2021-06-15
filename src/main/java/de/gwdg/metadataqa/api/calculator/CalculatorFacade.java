package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.calculator.output.OutputCollector;
import de.gwdg.metadataqa.api.calculator.output.OutputFactory;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.problemcatalog.EmptyStrings;
import de.gwdg.metadataqa.api.problemcatalog.LongSubject;
import de.gwdg.metadataqa.api.problemcatalog.ProblemCatalog;
import de.gwdg.metadataqa.api.problemcatalog.TitleAndDescriptionAreSame;
import de.gwdg.metadataqa.api.rule.RuleCatalog;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.DefaultSolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.uniqueness.TfIdf;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.gwdg.metadataqa.api.util.CsvReader;
import org.apache.commons.lang3.StringUtils;

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

  private MeasurementConfiguration configuration;

  /**
   * Is it the first record?
   */
  protected boolean isFirstRecord = true;

  protected CompressionLevel compressionLevel = CompressionLevel.NORMAL;

  protected SolrClient solrClient;
  protected SolrConfiguration solrConfiguration = null;

  /**
   * Flag to detect status changes (default: false).
   */
  private boolean changed = false;

  /**
   * The list of registered calculator objects. Those will do the measurements
   */
  protected List<Calculator> calculators = new ArrayList<>();

  /**
   * The field extractor object.
   */
  protected FieldExtractor fieldExtractor;

  /**
   * The completeness calculator.
   */
  protected CompletenessCalculator completenessCalculator;

  /**
   * The TF-IDF calculator.
   */
  protected TfIdfCalculator tfidfCalculator;

  /**
   * The language detector.
   */
  protected LanguageCalculator languageCalculator;

  /**
   * The language detector.
   */
  protected MultilingualitySaturationCalculator multilingualSaturationCalculator;

  // protected Format format = Format.JSON;
  protected PathCache<? extends XmlFieldInstance> cache;
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
    calculators = new ArrayList<>();

    addExtractor();
    addCompleteness();
    addTfIdfMeasurement();
    addProblemCatalogMeasurement();
    addRuleCatalogMeasurement();
    addLanguageMeasurement();
    addMultilingualSaturationMeasurement();
    addUniquenessMeasurement();
  }

  private void addUniquenessMeasurement() {
    if (configuration.isUniquenessMeasurementEnabled()) {
      if (solrClient == null && solrConfiguration == null) {
        throw new IllegalArgumentException(
          "If Uniqueness measurement is enabled, Solr configuration should not be null."
        );
      }
      if (solrClient == null) {
        solrClient = new DefaultSolrClient(solrConfiguration);
      }
      calculators.add(new UniquenessCalculator(solrClient, schema));
    }
  }

  private void addMultilingualSaturationMeasurement() {
    if (configuration.isMultilingualSaturationMeasurementEnabled()) {
      multilingualSaturationCalculator =
          new MultilingualitySaturationCalculator(schema);
      if (configuration.isSaturationExtendedResult()) {
        multilingualSaturationCalculator
          .setResultType(
              MultilingualitySaturationCalculator.ResultTypes.EXTENDED);
      }
      calculators.add(multilingualSaturationCalculator);
    }
  }

  private void addLanguageMeasurement() {
    if (configuration.isLanguageMeasurementEnabled()) {
      languageCalculator = new LanguageCalculator(schema);
      calculators.add(languageCalculator);
    }
  }

  private void addRuleCatalogMeasurement() {
    if (configuration.isRuleCatalogMeasurementEnabled()) {
      calculators.add(new RuleCatalog(schema));
    }
  }

  private void addProblemCatalogMeasurement() {
    // TODO: move it into europeana lib
    if (configuration.isProblemCatalogMeasurementEnabled() && schema instanceof EdmSchema) {
      var problemCatalog = new ProblemCatalog((EdmSchema) schema);
      new LongSubject(problemCatalog);
      new TitleAndDescriptionAreSame(problemCatalog);
      new EmptyStrings(problemCatalog);
      calculators.add(problemCatalog);
    }
  }

  private void addTfIdfMeasurement() {
    if (configuration.isTfIdfMeasurementEnabled()) {
      tfidfCalculator = new TfIdfCalculator(schema);
      if (solrConfiguration != null) {
        tfidfCalculator.setSolrConfiguration(solrConfiguration);
      } else {
        throw new IllegalArgumentException("If TF-IDF measurement is enabled, Solr configuration should not be null.");
      }
      tfidfCalculator.enableTermCollection(configuration.collectTfIdfTerms());
      calculators.add(tfidfCalculator);
    }
  }

  private void addCompleteness() {
    if (configuration.isCompletenessMeasurementEnabled()) {
      completenessCalculator = new CompletenessCalculator(schema);
      completenessCalculator.collectFields(configuration.isCompletenessFieldCollectingEnabled());
      completenessCalculator.setExistence(configuration.isFieldExistenceMeasurementEnabled());
      completenessCalculator.setCardinality(configuration.isFieldCardinalityMeasurementEnabled());
      calculators.add(completenessCalculator);
    }
  }

  private void addExtractor() {
    if (configuration.isFieldExtractorEnabled()) {
      fieldExtractor = new FieldExtractor(schema);
      calculators.add(fieldExtractor);
    }
  }

  /**
   * Run the measurements with each Calculator then returns the result as CSV.
   * @param inputRecord
   *   The JSON record string
   * @return
   *   The result of measurements as a CSV string
   * @throws InvalidJsonException
   *   Invalid Json exception
   */
  public String measure(String inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureWithGenerics(inputRecord);
  }

  public String measure(List<String> inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureCsvWithGenerics(
      inputRecord, OutputCollector.TYPE.STRING);
  }

  public String measureAsJson(String inputRecord) throws InvalidJsonException {
    return (String) this.<XmlFieldInstance>measureWithGenerics(
      inputRecord, OutputCollector.TYPE.JSON);
  }

  public Map<String, List<MetricResult>> measureAsMetricResult(String inputRecord) throws InvalidJsonException {
    return (Map<String, List<MetricResult>>) this.<XmlFieldInstance>measureWithGenerics(
      inputRecord, OutputCollector.TYPE.METRIC);
  }

  public List<String> measureAsList(String inputRecord) throws InvalidJsonException {
    return (List<String>) this.<XmlFieldInstance>measureWithGenerics(
      inputRecord, OutputCollector.TYPE.STRING_LIST);
  }

  public List<String> measureAsList(List<String> inputRecord) throws InvalidJsonException {
    return (List<String>) this.<XmlFieldInstance>measureCsvWithGenerics(
      inputRecord, OutputCollector.TYPE.STRING_LIST);
  }

  public List<Object> measureAsListOfObjects(String inputRecord) throws InvalidJsonException {
    return (List<Object>) this.<XmlFieldInstance>measureWithGenerics(
      inputRecord, OutputCollector.TYPE.OBJECT_LIST);
  }

  public List<Object> measureAsListOfObjects(List<String> inputRecord) throws InvalidJsonException {
    return (List<Object>) this.<XmlFieldInstance>measureCsvWithGenerics(
      inputRecord, OutputCollector.TYPE.OBJECT_LIST);
  }

  public Map<String, Object> measureAsMap(String inputRecord) throws InvalidJsonException {
    return (Map<String, Object>) this.<XmlFieldInstance>measureWithGenerics(
      inputRecord, OutputCollector.TYPE.MAP);
  }

  public Map<String, Object> measureAsMap(List<String> inputRecord) throws InvalidJsonException {
    return (Map<String, Object>) this.<XmlFieldInstance>measureCsvWithGenerics(
      inputRecord, OutputCollector.TYPE.MAP);
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
   * @param content
   *   The JSON record
   * @return
   *   The result of measurements as a CSV string
   * @throws InvalidJsonException
   */
  protected <T extends XmlFieldInstance> Object measureWithGenerics(String content)
    throws InvalidJsonException {
    return measureWithGenerics(content, OutputCollector.TYPE.STRING);
  }

  protected <T extends XmlFieldInstance> Object measureWithGenerics(String content,
                                                                    OutputCollector.TYPE type)
      throws InvalidJsonException {
    conditionalConfiguration();

    OutputCollector collector = OutputFactory.createOutput(type);

    if (schema == null) {
      throw new IllegalStateException("schema is missing");
    } else {
      var format = schema.getFormat();
      if (format != null && content != null) {
        cache = PathCacheFactory.getInstance(schema.getFormat(), content);
        if (schema.getFormat().equals(Format.CSV))
          initializeCsvCache(content);

        if (!(isFirstRecord
            && schema.getFormat().equals(Format.CSV)
            && csvReader.isHeaderAware()))
          runMeasurements(collector);

        isFirstRecord = false;
      }
    }

    return collector.getResults();
  }

  private void initializeCsvCache(String content) {
    if (isFirstRecord && csvReader.isHeaderAware())
      try {
        csvReader.setHeader(content);
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "initializeCsvCache", e);
      }

    ((CsvPathCache)cache).setCsvReader(csvReader);
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
    OutputCollector collector = OutputFactory.createOutput(type);

    if (content != null) {
      cache = new CsvPathCache<>(csvReader, content);
      runMeasurements(collector);
    }

    return collector.getResults();
  }

  private void runMeasurements(OutputCollector collector) {
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

  /**
   * Returns the result map.
   *
   * @return
   *   The result map
   */
  /*
  public Map<String, Object> getResults() {
    Map<String, Object> results = new LinkedHashMap<>();
    for (Calculator calculator : calculators) {
      results.putAll(calculator.getResultMap());
    }
    return results;
  }
  */

  /*
  public Map<String, Map<String, ? extends Object>> getLabelledResults() {
    Map<String, Map<String, ? extends Object>> results = new LinkedHashMap<>();
    for (Calculator calculator : calculators) {
      results.putAll(calculator.getLabelledResultMap());
    }
    return results;
  }
  */

  /*
  public String getCsv(boolean withLabels, CompressionLevel compressionLevel) {
    List<String> results = new ArrayList<>();
    for (Calculator calculator : calculators) {
      results.add(calculator.getCsv(withLabels, compressionLevel));
    }
    return StringUtils.join(results, ",");
  }
  */

  public CalculatorFacade configureSolr(String solrHost, String solrPort, String solrPath) {
    solrConfiguration = new SolrConfiguration(solrHost, solrPort, solrPath);
    if (this.tfidfCalculator != null) {
      this.tfidfCalculator.setSolrConfiguration(solrConfiguration);
    }
    changed = true;
    return this;
  }

  public List<String> getHeader() {
    conditionalConfiguration();
    List<String> header = new ArrayList<>();
    for (Calculator calculator : getCalculators()) {
      header.addAll(calculator.getHeader());
    }

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

  public PathCache<? extends XmlFieldInstance> getCache() {
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

  public CalculatorFacade setSolrClient(SolrClient solrClient) {
    this.solrClient = solrClient;
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
}
