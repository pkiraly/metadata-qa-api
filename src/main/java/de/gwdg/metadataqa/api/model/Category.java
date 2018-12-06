package de.gwdg.metadataqa.api.model;

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
}
