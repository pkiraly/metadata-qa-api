package de.gwdg.metadataqa.api.model;

import com.jayway.jsonpath.JsonPath;
import de.gwdg.metadataqa.api.json.JsonBranch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The functional categories.
 */
public enum Category {

  MANDATORY("mandatory"),
  DESCRIPTIVENESS("descriptiveness"),
  SEARCHABILITY("searchability"),
  CONTEXTUALIZATION("contextualization"),
  IDENTIFICATION("identification"),
  BROWSING("browsing"),
  VIEWING("viewing"),
  REUSABILITY("re-usability"),
  MULTILINGUALITY("multilinguality");

  private final String name;

  Category(String name) {
    this.name = name;
  }

  public static List<Category> extractCategories(Collection<JsonBranch> paths) {
    List<Category> existingCategories = new ArrayList<>();
    for (JsonBranch branch : paths)
      if (!branch.getCategories().isEmpty())
        for (Category category : branch.getCategories())
          if (!existingCategories.contains(category))
            existingCategories.add(category);

    List<Category> goodOrder = new ArrayList<>();
    for (Category category : values())
      if (existingCategories.contains(category))
        goodOrder.add(category);

    return goodOrder;
  }
}
