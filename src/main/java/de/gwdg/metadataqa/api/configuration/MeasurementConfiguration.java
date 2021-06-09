package de.gwdg.metadataqa.api.configuration;

public class MeasurementConfiguration {

  /**
   * Flag whether or not the field extractor is enabled (default: false).
   */
  private boolean fieldExtractorEnabled = false;

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
   * Flag whether or not run the rule catalog
   * (default: false).
   */
  protected boolean ruleCatalogMeasurementEnabled = false;

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

  protected boolean uniquenessMeasurementEnabled = false;

  /**
   * Flag whether or not run missing/empty/existing field collection in
   * completeness (default: false).
   */
  protected boolean completenessCollectFields = false;
  protected boolean saturationExtendedResult = false;
  protected boolean checkSkippableCollections = false;

  public MeasurementConfiguration() {}

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
  public MeasurementConfiguration(final boolean runFieldExistence,
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

  public MeasurementConfiguration enableFieldExtractor() {
    return enableFieldExtractor(true);
  }

  public MeasurementConfiguration disableFieldExtractor() {
    return enableFieldExtractor(false);
  }

  public MeasurementConfiguration enableFieldExtractor(boolean flag) {
    this.fieldExtractorEnabled = flag;
    return this;
  }

  public void setFieldExtractorEnabled(boolean fieldExtractorEnabled) {
    this.fieldExtractorEnabled = fieldExtractorEnabled;
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

  public MeasurementConfiguration enableFieldExistenceMeasurement() {
    return enableFieldExistenceMeasurement(true);
  }

  public MeasurementConfiguration disableFieldExistenceMeasurement() {
    return enableFieldExistenceMeasurement(false);
  }

  public void setFieldExistenceMeasurementEnabled(boolean fieldExistenceMeasurementEnabled) {
    this.fieldExistenceMeasurementEnabled = fieldExistenceMeasurementEnabled;
  }

  /**
   * Sets whether or not to run the field existence measurement.
   * @param runFieldExistence
   *    field existence measurement flag
   * @return
   */
  public MeasurementConfiguration enableFieldExistenceMeasurement(boolean runFieldExistence) {
    this.fieldExistenceMeasurementEnabled = runFieldExistence;
    return this;
  }

  /**
   * Returns whether or not to run cardinality measurement.
   * @return
   *   Flag to run cardinality measurement
   */
  public boolean isFieldCardinalityMeasurementEnabled() {
    return fieldCardinalityMeasurementEnabled;
  }

  public void setFieldCardinalityMeasurementEnabled(boolean fieldCardinalityMeasurementEnabled) {
    this.fieldCardinalityMeasurementEnabled = fieldCardinalityMeasurementEnabled;
  }

  public MeasurementConfiguration enableFieldCardinalityMeasurement() {
    return enableFieldCardinalityMeasurement(true);
  }

  public MeasurementConfiguration disableFieldCardinalityMeasurement() {
    return enableFieldCardinalityMeasurement(false);
  }

  /**
   * configure to run the cardinality measurement.
   * @param runFieldCardinality
   *    cardinality measurement flag
   * @return
   */
  public MeasurementConfiguration enableFieldCardinalityMeasurement(boolean runFieldCardinality) {
    this.fieldCardinalityMeasurementEnabled = runFieldCardinality;
    return this;
  }

  /**
   * Returns the flag whether or not run the completeness measurement.
   * @return
   *   Flag whether or not run the completeness measurement
   */
  public boolean isCompletenessMeasurementEnabled() {
    return completenessMeasurementEnabled;
  }

  public void setCompletenessMeasurementEnabled(boolean completenessMeasurementEnabled) {
    this.completenessMeasurementEnabled = completenessMeasurementEnabled;
  }

  public MeasurementConfiguration enableCompletenessMeasurement() {
    return enableCompletenessMeasurement(true);
  }

  public MeasurementConfiguration disableCompletenessMeasurement() {
    return enableCompletenessMeasurement(false);
  }

  /**
   * Sets the flag whether or not run the completeness measurement.
   * @param runCompleteness
   *    flag whether or not run the completeness measurement
   * @return
   */
  public MeasurementConfiguration enableCompletenessMeasurement(boolean runCompleteness) {
    this.completenessMeasurementEnabled = runCompleteness;
    return this;
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

  public void setLanguageMeasurementEnabled(boolean languageMeasurementEnabled) {
    this.languageMeasurementEnabled = languageMeasurementEnabled;
  }

  public MeasurementConfiguration enableLanguageMeasurement() {
    return enableLanguageMeasurement(true);
  }

  public MeasurementConfiguration disableLanguageMeasurement() {
    return enableLanguageMeasurement(false);
  }

  /**
   * Configure whether or not run the language detector.
   *
   * @param runLanguage
   * @return
   */
  public MeasurementConfiguration enableLanguageMeasurement(boolean runLanguage) {
    this.languageMeasurementEnabled = runLanguage;
    return this;
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

  public void setMultilingualSaturationMeasurementEnabled(boolean multilingualSaturationMeasurementEnabled) {
    this.multilingualSaturationMeasurementEnabled = multilingualSaturationMeasurementEnabled;
  }

  public MeasurementConfiguration enableMultilingualSaturationMeasurement() {
    return enableMultilingualSaturationMeasurement(true);
  }

  public MeasurementConfiguration disableMultilingualSaturationMeasurement() {
    return enableMultilingualSaturationMeasurement(false);
  }

  /**
   * Configure whether or not run the language detector.
   *
   * @param runMultilingualSaturation
   * @return
   */
  public MeasurementConfiguration enableMultilingualSaturationMeasurement(boolean runMultilingualSaturation) {
    this.multilingualSaturationMeasurementEnabled = runMultilingualSaturation;
    return this;
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

  public void setTfIdfMeasurementEnabled(boolean tfIdfMeasurementEnabled) {
    this.tfIdfMeasurementEnabled = tfIdfMeasurementEnabled;
  }

  public MeasurementConfiguration enableTfIdfMeasurement() {
    return enableTfIdfMeasurement(true);
  }

  public MeasurementConfiguration disableTfIdfMeasurement() {
    return enableTfIdfMeasurement(false);
  }

  /**
   * Configure whether or not run the uniqueness measurement.
   * @param runTfIdf
   *   uniqueness measurement flag
   * @return
   */
  public MeasurementConfiguration enableTfIdfMeasurement(boolean runTfIdf) {
    this.tfIdfMeasurementEnabled = runTfIdf;
    return this;
  }

  /**
   * Gets flag whether to run the problem catalog measurement.
   * @return
   *   problem catalog measurement flag
   */
  public boolean isProblemCatalogMeasurementEnabled() {
    return problemCatalogMeasurementEnabled;
  }

  public void setProblemCatalogMeasurementEnabled(boolean problemCatalogMeasurementEnabled) {
    this.problemCatalogMeasurementEnabled = problemCatalogMeasurementEnabled;
  }

  public MeasurementConfiguration enableProblemCatalogMeasurement() {
    return enableProblemCatalogMeasurement(true);
  }

  public MeasurementConfiguration disableProblemCatalogMeasurement() {
    return enableProblemCatalogMeasurement(false);
  }

  /**
   * Configure to run the problem catalog measurement.
   * @param runProblemCatalog
   *   problem catalog measurement flag
   * @return
   */
  public MeasurementConfiguration enableProblemCatalogMeasurement(boolean runProblemCatalog) {
    this.problemCatalogMeasurementEnabled = runProblemCatalog;
    return this;
  }

  /**
   * Gets flag whether to run the rule catalog measurement.
   * @return
   *   problem catalog measurement flag
   */
  public boolean isRuleCatalogMeasurementEnabled() {
    return ruleCatalogMeasurementEnabled;
  }

  public void setRuleCatalogMeasurementEnabled(boolean ruleCatalogMeasurementEnabled) {
    this.ruleCatalogMeasurementEnabled = ruleCatalogMeasurementEnabled;
  }

  public MeasurementConfiguration enableRuleCatalogMeasurement() {
    return enableRuleCatalogMeasurement(true);
  }

  public MeasurementConfiguration disableRuleCatalogMeasurement() {
    return enableRuleCatalogMeasurement(false);
  }

  /**
   * Configure to run the problem catalog measurement.
   * @param run
   *   problem catalog measurement flag
   * @return
   */
  public MeasurementConfiguration enableRuleCatalogMeasurement(boolean run) {
    this.ruleCatalogMeasurementEnabled = run;
    return this;
  }

  /**
   * Is uniqueness measurement enabled?
   * @return uniqueness measurement flag
   */
  public boolean isUniquenessMeasurementEnabled() {
    return uniquenessMeasurementEnabled;
  }

  public void setUniquenessMeasurementEnabled(boolean uniquenessMeasurementEnabled) {
    this.uniquenessMeasurementEnabled = uniquenessMeasurementEnabled;
  }

  public MeasurementConfiguration enableUniquenessMeasurement() {
    return enableUniquenessMeasurement(true);
  }

  public MeasurementConfiguration disableUniquenessMeasurement() {
    return enableUniquenessMeasurement(false);
  }

  /**
   * Flag to enable uniqueness measurement.
   * @param uniquenessMeasurementEnabled The flag
   * @return
   */
  public MeasurementConfiguration enableUniquenessMeasurement(boolean uniquenessMeasurementEnabled) {
    this.uniquenessMeasurementEnabled = uniquenessMeasurementEnabled;
    return this;
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

  public void setCollectTfIdfTerms(boolean collectTfIdfTerms) {
    this.collectTfIdfTerms = collectTfIdfTerms;
  }

  /**
   * Sets the flag whether the measurement should collect each individual
   * terms with their Term Ferquency and Invers Document Frequency scores.
   *
   * @param collectTfIdfTerms
   *   The TF-IDF collector flag
   * @return
   */
  public MeasurementConfiguration collectTfIdfTerms(boolean collectTfIdfTerms) {
    this.collectTfIdfTerms = collectTfIdfTerms;
    // if (tfidfCalculator != null) {
    //  tfidfCalculator.enableTermCollection(collectTfIdfTerms);
    //}
    return this;
  }

  /**
   * Get the completenessCollectFields flag.
   *
   * @return
   *   completenessCollectFields flag
   */
  public boolean isCompletenessFieldCollectingEnabled() {
    return completenessCollectFields;
  }

  public void setCompletenessCollectFields(boolean completenessCollectFields) {
    this.completenessCollectFields = completenessCollectFields;
  }

  /**
   * The completeness calculation will collect empty, existent and missing fields.
   *
   * @param completenessCollectFields
   *   The completenessCollectFields flag
   * @return
   */
  public MeasurementConfiguration enableCompletenessFieldCollecting(boolean completenessCollectFields) {
    this.completenessCollectFields = completenessCollectFields;
    return this;
  }

  public boolean isSaturationExtendedResult() {
    return saturationExtendedResult;
  }

  public void setSaturationExtendedResult(boolean saturationExtendedResult) {
    this.saturationExtendedResult = saturationExtendedResult;
  }

  public MeasurementConfiguration enableSaturationExtendedResult(boolean saturationExtendedResult) {
    this.saturationExtendedResult = saturationExtendedResult;
    return this;
  }

  public boolean isCheckSkippableCollections() {
    return checkSkippableCollections;
  }

  public void setCheckSkippableCollections(boolean checkSkippableCollections) {
    this.checkSkippableCollections = checkSkippableCollections;
  }

  public MeasurementConfiguration enableCheckSkippableCollections(boolean checkSkippableCollections) {
    this.checkSkippableCollections = checkSkippableCollections;
    return this;
  }
}
