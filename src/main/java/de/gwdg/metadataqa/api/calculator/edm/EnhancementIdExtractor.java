package de.gwdg.metadataqa.api.calculator.edm;

import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public final class EnhancementIdExtractor implements Serializable {

  private static final List<String> TECHNICAL_PROPERTIES = Arrays.asList(
    "@about",
    "edm:europeanaProxy",
    "ore:proxyFor",
    "ore:proxyIn",
    "edm:type"
  );

  private static final String PATH = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'true')]";

  private EnhancementIdExtractor() {
  }

  public static List<String> extractIds(JsonPathCache cache) {
    List<String> enhancementIds = new ArrayList<>();
    Object rawJsonFragment = cache.getFragment(PATH);
    List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment);
    Map<String, Object> jsonFragment = (Map) jsonFragments.get(0);
    for (String fieldName : jsonFragment.keySet()) {
      if (isEnrichmentField(fieldName)) {
        List<EdmFieldInstance> fieldInstances =
          (List<EdmFieldInstance>) JsonUtils.extractFieldInstanceList(
            jsonFragment.get(fieldName), null, null
          );
        for (EdmFieldInstance fieldInstance : fieldInstances) {
          if (fieldInstance.isUrl()) {
            enhancementIds.add(fieldInstance.getUrl());
          }
        }
      }
    }
    return enhancementIds;
  }

  private static boolean isEnrichmentField(String fieldName) {
    return !TECHNICAL_PROPERTIES.contains(fieldName);
  }
}
