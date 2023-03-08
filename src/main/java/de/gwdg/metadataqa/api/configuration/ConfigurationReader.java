package de.gwdg.metadataqa.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class ConfigurationReader {

  private ConfigurationReader() {}

  public static SchemaConfiguration readSchemaJson(String fileName) throws FileNotFoundException {
    return readJson(fileName, SchemaConfiguration.class);
  }

  public static MeasurementConfiguration readMeasurementJson(String fileName) throws FileNotFoundException {
    return readJson(fileName, MeasurementConfiguration.class);
  }

  public static SchemaConfiguration readSchemaYaml(String fileName) throws FileNotFoundException {
    return readYaml(fileName, SchemaConfiguration.class);
  }

  public static MeasurementConfiguration readMeasurementYaml(String fileName) throws FileNotFoundException {
    return readYaml(fileName, MeasurementConfiguration.class);
  }

  private static <T> T readJson(String fileName, Class<T> clazz) throws FileNotFoundException {
    var objectMapper = new ObjectMapper();
    var file = new File(fileName);
    T config;
    try {
      config = objectMapper.readValue(file, clazz);
    } catch (IOException e) {
      throw new FileNotFoundException(e.getMessage());
    }
    return config;
  }

  private static <T> T readYaml(String fileName, Class<T> clazz) throws FileNotFoundException {
    var yaml = new Yaml(new Constructor(clazz, new LoaderOptions()));
    InputStream inputStream = new FileInputStream(new File(fileName));
    return yaml.load(inputStream);
  }
}
