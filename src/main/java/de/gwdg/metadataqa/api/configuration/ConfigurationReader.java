package de.gwdg.metadataqa.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class ConfigurationReader {

  public static Configuration readJson(String fileName) throws FileNotFoundException {
    var objectMapper = new ObjectMapper();
    var file = new File(fileName);
    Configuration config;
    try {
      config = objectMapper.readValue(file, Configuration.class);
    } catch (IOException e) {
      throw new FileNotFoundException(e.getMessage());
    }
    return config;
  }

  public static Configuration readYaml(String fileName) throws FileNotFoundException {
    var yaml = new Yaml(new Constructor(Configuration.class));
    InputStream inputStream = new FileInputStream(new File(fileName));
    return yaml.load(inputStream);
  }
}
