package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.problemcatalog.EmptyStrings;
import de.gwdg.metadataqa.api.problemcatalog.LongSubject;
import de.gwdg.metadataqa.api.problemcatalog.ProblemCatalog;
import de.gwdg.metadataqa.api.problemcatalog.TitleAndDescriptionAreSame;
import de.gwdg.metadataqa.api.rule.RuleCatalog;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;
import de.gwdg.metadataqa.api.uniqueness.DefaultSolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static de.gwdg.metadataqa.api.calculator.MultilingualitySaturationCalculator.ResultTypes.EXTENDED;

public class CalculatorFactory {

  private MeasurementConfiguration configuration;
  private Schema schema;
  private List<Calculator> calculators = new ArrayList<>();

  private CalculatorFactory(MeasurementConfiguration configuration, Schema schema) {
    this.configuration = configuration;
    this.schema = schema;
    configure();
  }

  public static List<Calculator> create(MeasurementConfiguration configuration, Schema schema) {
    CalculatorFactory factory = new CalculatorFactory(configuration, schema);
    return factory.getCalculators();
  }

  private void configure() {
    addExtractor();
    addCompleteness();
    addTfIdfMeasurement();
    addProblemCatalogMeasurement();
    addRuleCatalogMeasurement();
    addLanguageMeasurement();
    addMultilingualSaturationMeasurement();
    addUniquenessMeasurement();
    addEcho();
  }

  private void addExtractor() {
    if (configuration.isFieldExtractorEnabled())
      calculators.add(new FieldExtractor(schema));
  }

  private void addEcho() {
    if (configuration.isEchoEnabled())
      calculators.add(new EchoField(schema));
  }

  private void addCompleteness() {
    if (configuration.isCompletenessMeasurementEnabled()) {
      CompletenessCalculator completenessCalculator = new CompletenessCalculator(schema);
      completenessCalculator.collectFields(configuration.isCompletenessFieldCollectingEnabled());
      completenessCalculator.setExistence(configuration.isFieldExistenceMeasurementEnabled());
      completenessCalculator.setCardinality(configuration.isFieldCardinalityMeasurementEnabled());
      calculators.add(completenessCalculator);
    }
  }

  private void addTfIdfMeasurement() {
    if (configuration.isTfIdfMeasurementEnabled()) {
      TfIdfCalculator tfidfCalculator = new TfIdfCalculator(schema);
      if (StringUtils.isNotBlank(configuration.getSolrHost())
        && StringUtils.isNotBlank(configuration.getSolrPort())
        && StringUtils.isNotBlank(configuration.getSolrPath())) {
        tfidfCalculator.setSolrConfiguration(new SolrConfiguration(configuration.getSolrHost(), configuration.getSolrPort(), configuration.getSolrPath()));
      } else {
        throw new IllegalArgumentException("If TF-IDF measurement is enabled, Solr configuration should not be null.");
      }
      tfidfCalculator.enableTermCollection(configuration.collectTfIdfTerms());
      calculators.add(tfidfCalculator);
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

  private void addRuleCatalogMeasurement() {
    if (configuration.isRuleCatalogMeasurementEnabled())
      calculators.add(new RuleCatalog(schema));
  }

  private void addLanguageMeasurement() {
    if (configuration.isLanguageMeasurementEnabled())
      calculators.add(new LanguageCalculator(schema));
  }

  private void addMultilingualSaturationMeasurement() {
    if (configuration.isMultilingualSaturationMeasurementEnabled()) {
      MultilingualitySaturationCalculator multilingualSaturationCalculator = new MultilingualitySaturationCalculator(schema);
      if (configuration.isSaturationExtendedResult())
        multilingualSaturationCalculator.setResultType(EXTENDED);

      calculators.add(multilingualSaturationCalculator);
    }
  }

  private void addUniquenessMeasurement() {
    if (configuration.isUniquenessMeasurementEnabled()) {
      if (configuration.getSolrClient() == null && configuration.getSolrConfiguration() == null) {
        throw new IllegalArgumentException(
          "If Uniqueness measurement is enabled, Solr configuration should not be null."
        );
      }
      if (configuration.getSolrClient() == null) {
        configuration.setSolrClient(new DefaultSolrClient(configuration.getSolrConfiguration()));
      }
      calculators.add(new UniquenessCalculator(configuration.getSolrClient(), schema));
    }
  }

  public List<Calculator> getCalculators() {
    return calculators;
  }
}
