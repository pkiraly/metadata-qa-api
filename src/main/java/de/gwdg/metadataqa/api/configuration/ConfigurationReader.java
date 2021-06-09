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

  public static SchemaConfiguration readSchemaJson(String fileName) throws FileNotFoundException {
    var objectMapper = new ObjectMapper();
    var file = new File(fileName);
    SchemaConfiguration config;
    try {
      config = objectMapper.readValue(file, SchemaConfiguration.class);
    } catch (IOException e) {
      throw new FileNotFoundException(e.getMessage());
    }
    return config;
  }

  public static SchemaConfiguration readSchemaYaml(String fileName) throws FileNotFoundException {
    var yaml = new Yaml(new Constructor(SchemaConfiguration.class));
    InputStream inputStream = new FileInputStream(new File(fileName));
    return yaml.load(inputStream);
  }

  public static MeasurementConfiguration readMeasurementYaml(String fileName) throws FileNotFoundException {
    var yaml = new Yaml(new Constructor(MeasurementConfiguration.class));
    InputStream inputStream = new FileInputStream(new File(fileName));
    return yaml.load(inputStream);
  }
}
