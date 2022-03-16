package de.gwdg.metadataqa.api.cli;

public class Version {
  public static void main(String[] args) {
    System.err.println(Version.class.getPackage().getImplementationVersion());
  }
}
