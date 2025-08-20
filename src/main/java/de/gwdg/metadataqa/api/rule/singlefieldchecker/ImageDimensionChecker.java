package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.configuration.schema.Dimension;
import de.gwdg.metadataqa.api.util.DimensionDao;
import de.gwdg.metadataqa.api.util.ImageDimensionExtractor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ImageDimensionChecker extends SingleFieldChecker {

  public static final String PREFIX = "imageDimension";
  protected Dimension dimensionRule;

  private static final Logger LOGGER = Logger.getLogger(ImageDimensionChecker.class.getCanonicalName());

  public ImageDimensionChecker(DataElement field, Dimension dimension) {
    this(field, field.getLabel(), dimension);
  }

  public ImageDimensionChecker(DataElement field, String header, Dimension dimension) {
    super(field, header + ":" + PREFIX);
    this.dimensionRule = dimension;
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          try {
            if (isDebug())
              LOGGER.info("value: " + instance.getValue());
            DimensionDao dimension = ImageDimensionExtractor.extractRemote(instance.getValue());
            if (dimension == null
              || (dimensionRule.getMinWidth()  != null && dimension.getWidth()  < dimensionRule.getMinWidth())
              || (dimensionRule.getMaxWidth()  != null && dimension.getWidth()  > dimensionRule.getMaxWidth())
              || (dimensionRule.getMinHeight() != null && dimension.getHeight() < dimensionRule.getMinHeight())
              || (dimensionRule.getMaxHeight() != null && dimension.getHeight() > dimensionRule.getMaxHeight())
              || (dimensionRule.getMinShortside()  != null && dimension.getShort()  < dimensionRule.getMinShortside())
              || (dimensionRule.getMaxShortside()  != null && dimension.getShort()  > dimensionRule.getMaxShortside())
              || (dimensionRule.getMinLongside()   != null && dimension.getLong()   < dimensionRule.getMinLongside())
              || (dimensionRule.getMaxLongside()   != null && dimension.getLong()   > dimensionRule.getMaxLongside())
            ) {
              allPassed = false;
            }
          } catch (IOException e) {
            allPassed = false;
          }
          if (!allPassed)
            break;
        }
      }
    }

    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }
}
