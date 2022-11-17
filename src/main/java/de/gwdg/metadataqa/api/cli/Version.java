package de.gwdg.metadataqa.api.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {
  private static String version;

  public static void main(String[] args) {
    System.err.println(Version.getVersion());
  }

  public static String getVersion() {
    if (version == null) {
      initialize();
    }
    return version;
  }

  private static void initialize() {
    String versionCandidate = Version.class.getPackage().getImplementationVersion();
    if (versionCandidate != null)
      version = versionCandidate;
    else {
      version = readVersionFromPropertyFile();
    }
  }

  public static String readVersionFromPropertyFile() {
    String path = "/version.prop";
    InputStream stream = Version.class.getResourceAsStream(path);
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
