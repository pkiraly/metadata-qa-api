package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.configuration.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ConfigurationReader {

  public static Configuration readYaml(String fileName) throws FileNotFoundException {
    Yaml yaml = new Yaml(new Constructor(Configuration.class));
    InputStream inputStream = new FileInputStream(new File(fileName));
    Configuration config = (Configuration) yaml.load(inputStream);
    return config;
  }

}
