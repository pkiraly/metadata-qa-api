package de.gwdg.metadataqa.api.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {
  private static final String PATH = "/version.prop";
  private static String currentVersion;

  public static void main(String[] args) {
    System.err.println(Version.getVersion());
  }

  public static String getVersion() {
    if (currentVersion == null) {
      initialize();
    }
    return currentVersion;
  }

  private static void initialize() {
    String versionCandidate = Version.class.getPackage().getImplementationVersion();
    if (versionCandidate != null)
      currentVersion = versionCandidate;
    else {
      currentVersion = readVersionFromPropertyFile();
    }
  }

  public static String readVersionFromPropertyFile() {
    InputStream stream = Version.class.getResourceAsStream(PATH);
    if (stream == null)
      return "UNKNOWN";
    Properties props = new Properties();
    try {
      props.load(stream);
      stream.close();
      return (String) props.get("version");
    } catch (IOException e) {
      return "UNKNOWN";
    }
  }
}
