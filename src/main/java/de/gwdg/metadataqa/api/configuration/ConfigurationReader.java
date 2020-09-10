package de.gwdg.metadataqa.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class ConfigurationReader {

  public static Configuration readJson(String fileName) throws FileNotFoundException {
    ObjectMapper objectMapper = new ObjectMapper();

    File file = new File(fileName);
    Configuration config = null;
    try {
      config = objectMapper.readValue(file, Configuration.class);
    } catch (IOException e) {
      throw new FileNotFoundException(e.getMessage());
    }
    return config;
  }

  public static Configuration readYaml(String fileName) throws FileNotFoundException {
    Yaml yaml = new Yaml(new Constructor(Configuration.class));
    InputStream inputStream = new FileInputStream(new File(fileName));
    Configuration config = (Configuration) yaml.load(inputStream);
    return config;
  }

}
