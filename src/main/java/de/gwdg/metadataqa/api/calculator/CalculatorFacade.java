package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.Counters;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.problemcatalog.EmptyStrings;
import de.gwdg.metadataqa.api.problemcatalog.LongSubject;
import de.gwdg.metadataqa.api.problemcatalog.ProblemCatalog;
import de.gwdg.metadataqa.api.problemcatalog.TitleAndDescriptionAreSame;
import de.gwdg.metadataqa.api.schema.EdmSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.DefaultSolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.uniqueness.TfIdf;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

  /**
   * Flag whether or not the field extractor is enabled (default: false).
   */
  protected boolean fieldExtractorEnabled = false;

  /**
   * Flag whether or not run the field existence measurement
   * (default: true).
   */
  protected boolean fieldExistenceMeasurementEnabled = true;

  /**
   * Flag whether or not run the field cardinality measurement
   * (default: true).
   */
  protected boolean fieldCardinalityMeasurementEnabled = true;

  /**
   * Flag whether or not run the completeness measurement
   * (default: true).
   */
  protected boolean completenessMeasurementEnabled = true;

  /**
   * Flag whether or not run the uniqueness measurement
   * (default: false).
   */
  protected boolean tfIdfMeasurementEnabled = false;

  /**
   * Flag whether or not run the problem catalog
   * (default: false).
   */
  protected boolean problemCatalogMeasurementEnabled = false;

  /**
   * Flag whether or not run the language detector
   * (default: false).
   */
  protected boolean languageMeasurementEnabled = false;

  /**
   * Flag whether or not run the multilingual saturation measurement
   * (default: false).
   */
  protected boolean multilingualSaturationMeasurementEnabled = false;

  /**
   * Flag whether or not collect TF-IDF terms in uniqueness measurement
   * (default: false).
   */
  protected boolean collectTfIdfTerms = false;

  /**
   * Flag whether or not run missing/empty/existing field collection in
   * completeness (default: false).
   */
  protected boolean completenessCollectFields = false;
  protected boolean saturationExtendedResult = false;
  protected boolean checkSkippableCollections = false;

  protected boolean uniquenessMeasurementEnabled = false;

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

  protected Format format = Format.JSON;
  protected PathCache<? extends XmlFieldInstance> cache;
  protected Schema schema;
  protected CsvReader csvReader;

  /**
   * Create calculator facade with the default configuration.
   */
  public CalculatorFacade() {
  }

  /**
   * Create calculator facade with configuration.
   * @param runFieldExistence
   *   Flag whether or not run the field existence measurement
   * @param runFieldCardinality
   *   Flag whether or not run the field cardinality measurement
   * @param runCompleteness
   *   Flag whether or not run the completeness measurement
   * @param runTfIdf
   *   Flag whether or not run the uniqueness measurement
   * @param runProblemCatalog
   *   Flag whether or not run the problem catalog
   */
  public CalculatorFacade(final boolean runFieldExistence,
                          final boolean runFieldCardinality,
                          final boolean runCompleteness,
                          final boolean runTfIdf,
                          final boolean runProblemCatalog) {
    this.fieldExistenceMeasurementEnabled = runFieldExistence;
    this.fieldCardinalityMeasurementEnabled = runFieldCardinality;
    this.completenessMeasurementEnabled = runCompleteness;
    this.tfIdfMeasurementEnabled = runTfIdf;
    this.problemCatalogMeasurementEnabled = runProblemCatalog;
  }

  protected void changed() {
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

    if (fieldExtractorEnabled) {
      fieldExtractor = new FieldExtractor(schema);
      calculators.add(fieldExtractor);
    }

    if (completenessMeasurementEnabled) {
      completenessCalculator = new CompletenessCalculator(schema);
      completenessCalculator.collectFields(completenessCollectFields);
      completenessCalculator.setExistence(fieldExistenceMeasurementEnabled);
      completenessCalculator.setCardinality(fieldCardinalityMeasurementEnabled);
      calculators.add(completenessCalculator);
    }

    if (tfIdfMeasurementEnabled) {
      tfidfCalculator = new TfIdfCalculator(schema);
      if (solrConfiguration != null) {
        tfidfCalculator.setSolrConfiguration(solrConfiguration);
      } else {
        throw new IllegalArgumentException("If TF-IDF measurement is enabled, Solr configuration should not be null.");
      }
      tfidfCalculator.enableTermCollection(collectTfIdfTerms);
      calculators.add(tfidfCalculator);
    }

    if (problemCatalogMeasurementEnabled) {
      if (schema instanceof EdmSchema) {
        ProblemCatalog problemCatalog = new ProblemCatalog((EdmSchema) schema);
        new LongSubject(problemCatalog);
        new TitleAndDescriptionAreSame(problemCatalog);
        new EmptyStrings(problemCatalog);
        calculators.add(problemCatalog);
      }
    }

    if (languageMeasurementEnabled) {
      languageCalculator = new LanguageCalculator(schema);
      calculators.add(languageCalculator);
    }

    if (multilingualSaturationMeasurementEnabled) {
      multilingualSaturationCalculator =
          new MultilingualitySaturationCalculator(schema);
      if (saturationExtendedResult) {
        multilingualSaturationCalculator
          .setResultType(
              MultilingualitySaturationCalculator.ResultTypes.EXTENDED);
      }
      calculators.add(multilingualSaturationCalculator);
    }

    if (uniquenessMeasurementEnabled) {
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

  /**
   * Run the measurements with each Calculator then returns the result as CSV.
   * @param jsonRecord
   *   The JSON record string
   * @return
   *   The result of measurements as a CSV string
   * @throws InvalidJsonException
   *   Invalid Json exception
   */
  public String measure(String jsonRecord) throws InvalidJsonException {
    return this.<XmlFieldInstance>measureWithGenerics(jsonRecord);
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
  protected <T extends XmlFieldInstance> String measureWithGenerics(String content)
      throws InvalidJsonException {
    changed();

    List<String> items = new ArrayList<>();

    if (schema == null) {
      throw new IllegalStateException("schema is missing");
    } else {
      Format format = schema.getFormat();
      if (format != null && content != null) {
        cache = PathCacheFactory.getInstance(schema.getFormat(), content);
        if (schema.getFormat().equals(Format.CSV)) {
          ((CsvPathCache)cache).setCsvReader(csvReader);
        }

        for (Calculator calculator : getCalculators()) {
          calculator.measure(cache);
          items.add(calculator.getCsv(false, compressionLevel));
        }
      }
    }

    return StringUtils.join(items, ",");
  }

  public void enableFieldExtractor(boolean flag) {
    this.fieldExtractorEnabled = flag;
  }

  public boolean isFieldExtractorEnabled() {
    return fieldExtractorEnabled;
  }

  /**
   * Returns whether or not to run the field existence measurement.
   * @return
   *   field existence measurement flag
   */
  public boolean isFieldExistenceMeasurementEnabled() {
    return fieldExistenceMeasurementEnabled;
  }

  /**
   * Sets whether or not to run the field existence measurement.
   * @param runFieldExistence
   *    field existence measurement flag
   */
  public void enableFieldExistenceMeasurement(boolean runFieldExistence) {
    if (this.fieldExistenceMeasurementEnabled != runFieldExistence) {
      this.fieldExistenceMeasurementEnabled = runFieldExistence;
      changed = true;
    }
  }

  /**
   * Returns whether or not to run cardinality measurement.
   * @return
   *   Flag to run cardinality measurement
   */
  public boolean isFieldCardinalityMeasurementEnabled() {
    return fieldCardinalityMeasurementEnabled;
  }

  /**
   * configure to run the cardinality measurement.
   * @param runFieldCardinality
   *    cardinality measurement flag
   */
  public void enableFieldCardinalityMeasurement(boolean runFieldCardinality) {
    if (this.fieldCardinalityMeasurementEnabled != runFieldCardinality) {
      this.fieldCardinalityMeasurementEnabled = runFieldCardinality;
      changed = true;
    }
  }

  /**
   * Returns the flag whether or not run the completeness measurement.
   * @return
   *   Flag whether or not run the completeness measurement
   */
  public boolean isCompletenessMeasurementEnabled() {
    return completenessMeasurementEnabled;
  }

  /**
   * Sets the flag whether or not run the completeness measurement.
   * @param runCompleteness
   *    flag whether or not run the completeness measurement
   */
  public void enableCompletenessMeasurement(boolean runCompleteness) {
    if (this.completenessMeasurementEnabled != runCompleteness) {
      this.completenessMeasurementEnabled = runCompleteness;
      changed = true;
    }
  }

  /**
   * Returns the flag whether or not run the language detector.
   *
   * @return
   *   language detector flag
   */
  public boolean isLanguageMeasurementEnabled() {
    return languageMeasurementEnabled;
  }

  /**
   * Configure whether or not run the language detector.
   *
   * @param runLanguage
   */
  public void enableLanguageMeasurement(boolean runLanguage) {
    this.languageMeasurementEnabled = runLanguage;
  }

  /**
   * Returns the flag whether or not run the language detector.
   *
   * @return
   *   language detector flag
   */
  public boolean isMultilingualSaturationMeasurementEnabled() {
    return multilingualSaturationMeasurementEnabled;
  }

  /**
   * Configure whether or not run the language detector.
   *
   * @param runMultilingualSaturation
   */
  public void enableMultilingualSaturationMeasurement(boolean runMultilingualSaturation) {
    this.multilingualSaturationMeasurementEnabled = runMultilingualSaturation;
  }

  /**
   * Returns whether or not run the uniqueness measurement.
   *
   * @return
   *   uniqueness measurement flag
   */
  public boolean isTfIdfMeasurementEnabled() {
    return tfIdfMeasurementEnabled;
  }

  /**
   * Configure whether or not run the uniqueness measurement.
   * @param runTfIdf
   *   uniqueness measurement flag
   */
  public void enableTfIdfMeasurement(boolean runTfIdf) {
    if (this.tfIdfMeasurementEnabled != runTfIdf) {
      this.tfIdfMeasurementEnabled = runTfIdf;
      changed = true;
    }
  }

  /**
   * Gets flag whether to run the problem catalog measurement.
   * @return
   *   problem catalog measurement flag
   */
  public boolean isProblemCatalogMeasurementEnabled() {
    return problemCatalogMeasurementEnabled;
  }

  /**
   * Configure to run the problem catalog measurement.
   * @param runProblemCatalog
   *   problem catalog measurement flag
   */
  public void enableProblemCatalogMeasurement(boolean runProblemCatalog) {
    if (this.problemCatalogMeasurementEnabled != runProblemCatalog) {
      this.problemCatalogMeasurementEnabled = runProblemCatalog;
      changed = true;
    }
  }

  /**
   * Is uniqueness measurement enabled?
   * @return uniqueness measurement flag
   */
  public boolean isUniquenessMeasurementEnabled() {
    return uniquenessMeasurementEnabled;
  }

  /**
   * Flag to enable uniqueness measurement.
   * @param uniquenessMeasurementEnabled The flag
   */
  public void enableUniquenessMeasurement(boolean uniquenessMeasurementEnabled) {
    this.uniquenessMeasurementEnabled = uniquenessMeasurementEnabled;
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
   * Returns the flag whether the measurement should collect each individual
   * terms with their Term Ferquency and Invers Document Frequency scores.
   *
   * @return
   *   The TF-IDF collector flag
   */
  public boolean collectTfIdfTerms() {
    return collectTfIdfTerms;
  }

  /**
   * Sets the flag whether the measurement should collect each individual
   * terms with their Term Ferquency and Invers Document Frequency scores.
   *
   * @param collectTfIdfTerms
   *   The TF-IDF collector flag
   */
  public void collectTfIdfTerms(boolean collectTfIdfTerms) {
    if (this.collectTfIdfTerms != collectTfIdfTerms) {
      this.collectTfIdfTerms = collectTfIdfTerms;
      changed = true;
      if (tfidfCalculator != null) {
        tfidfCalculator.enableTermCollection(collectTfIdfTerms);
      }
    }
  }

  /**
   * Get the completenessCollectFields flag.
   *
   * @return
   *   completenessCollectFields flag
   */
  public boolean completenessCollectFields() {
    return completenessCollectFields;
  }

  /**
   * The completeness calculation will collect empty, existent and missing fields.
   *
   * @param completenessCollectFields
   *   The completenessCollectFields flag
   */
  public void completenessCollectFields(boolean completenessCollectFields) {
    if (this.completenessCollectFields != completenessCollectFields) {
      this.completenessCollectFields = completenessCollectFields;
      changed = true;
    }
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
   * @see
   *   Counters#getResults()
   */
  public Map<String, Object> getResults() {
    Map<String, Object> results = new LinkedHashMap<>();
    for (Calculator calculator : calculators) {
      results.putAll(calculator.getResultMap());
    }
    return results;
  }

  public Map<String, Map<String, ? extends Object>> getLabelledResults() {
    Map<String, Map<String, ? extends Object>> results = new LinkedHashMap<>();
    for (Calculator calculator : calculators) {
      results.putAll(calculator.getLabelledResultMap());
    }
    return results;
  }

  public String getCsv(boolean withLabels, CompressionLevel compressionLevel) {
    List<String> results = new ArrayList<>();
    for (Calculator calculator : calculators) {
      results.add(calculator.getCsv(withLabels, compressionLevel));
    }
    return StringUtils.join(results, ",");
  }

  public void configureSolr(String solrHost, String solrPort, String solrPath) {
    solrConfiguration = new SolrConfiguration(solrHost, solrPort, solrPath);
    if (this.tfidfCalculator != null) {
      this.tfidfCalculator.setSolrConfiguration(solrConfiguration);
    }
  }

  public List<String> getHeader() {
    List<String> header = new ArrayList<>();
    for (Calculator calculator : getCalculators()) {
      header.addAll(calculator.getHeader());
    }

    return header;
  }

  public boolean isSaturationExtendedResult() {
    return saturationExtendedResult;
  }

  public void setSaturationExtendedResult(boolean saturationExtendedResult) {
    this.saturationExtendedResult = saturationExtendedResult;
  }

  public CompressionLevel getCompressionLevel() {
    return compressionLevel;
  }

  public void setCompressionLevel(CompressionLevel compressionLevel) {
    this.compressionLevel = compressionLevel;
  }

  public PathCache<? extends XmlFieldInstance> getCache() {
    return cache;
  }

  public boolean isCheckSkippableCollections() {
    return checkSkippableCollections;
  }

  public void setCheckSkippableCollections(boolean checkSkippableCollections) {
    this.checkSkippableCollections = checkSkippableCollections;
  }

  public Schema getSchema() {
    return schema;
  }

  public void setSchema(Schema schema) {
    this.schema = schema;
  }

  public void setSolrClient(SolrClient solrClient) {
    this.solrClient = solrClient;
  }

  public void setCsvReader(CsvReader csvReader) {
    this.csvReader = csvReader;
  }
}
