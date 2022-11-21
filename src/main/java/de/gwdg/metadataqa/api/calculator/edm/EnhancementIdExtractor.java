package de.gwdg.metadataqa.api.calculator.edm;

import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Detects whether an EDM field is from the enhancement part of the record
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

  private static final List<String> TECHNICAL_PROPERTIES_LABELS = Arrays.asList(
    "Proxy/rdf:about",
    "Proxy/edm:europeanaProxy",
    "Proxy/ore:proxyFor",
    "Proxy/ore:proxyIn",
    "Proxy/edm:type"
  );
  private static final long serialVersionUID = -7398960966530263051L;

  // private static final String PATH = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'true')]";

  private EnhancementIdExtractor() {
  }

  public static List<String> extractIds(PathCache cache, Schema schema) {
    List<String> enhancementIds = new ArrayList<>();
    String path = schema.getPathByLabel("Proxy").getPath().replace("false", "true");
    Object rawJsonFragment = cache.getFragment(path);
    List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
    if (schema.getFormat().equals(Format.JSON)) {
      processJson(enhancementIds, jsonFragments);
    } else if (schema.getFormat().equals(Format.XML)) {
      processXml(cache, schema, enhancementIds, jsonFragments);
    }
    return enhancementIds;
  }

  public static void processXml(PathCache cache, Schema schema, List<String> enhancementIds, List<Object> jsonFragments) {
    DataElement parent = schema.getPathByLabel("Proxy");
    for (DataElement child : parent.getChildren()) {
      if (isEnrichmentField(child.getLabel())) {
        String address = child.getAbsolutePath(schema.getFormat());
        Object context = jsonFragments.get(0);
        List<EdmFieldInstance> fieldInstances = cache.get(address, child.getPath(), context);
        if (fieldInstances != null && !fieldInstances.isEmpty()) {
          for (EdmFieldInstance fieldInstance : fieldInstances) {
            if (fieldInstance.isUrl()) {
              enhancementIds.add(fieldInstance.getUrl());
            }
          }
        }
      }
    }
  }

  public static void processJson(List<String> enhancementIds, List<Object> jsonFragments) {
    Map<String, Object> jsonFragment = (Map<String, Object>) jsonFragments.get(0);
    for (Map.Entry<String, Object> entry : jsonFragment.entrySet()) {
      if (isEnrichmentField(entry.getKey())) {
        List<EdmFieldInstance> fieldInstances =
          (List<EdmFieldInstance>) JsonUtils.extractFieldInstanceList(entry.getValue(), null, null);
        if (fieldInstances != null)
          for (EdmFieldInstance fieldInstance : fieldInstances)
            if (fieldInstance.isUrl())
              enhancementIds.add(fieldInstance.getUrl());
      }
    }
  }

  private static boolean isEnrichmentField(String fieldName) {
    return !TECHNICAL_PROPERTIES.contains(fieldName)
        && !TECHNICAL_PROPERTIES_LABELS.contains(fieldName);
  }
}
