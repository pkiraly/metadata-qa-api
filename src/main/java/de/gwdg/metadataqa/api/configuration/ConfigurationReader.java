package de.gwdg.metadataqa.api.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class ConfigurationReader {

  private ConfigurationReader() {
  }

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
    InputStream inputStream = new FileInputStream(fileName);
    return yaml.load(inputStream);
  }

  public static <T> String toJson(T object) {
    var objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> String toYaml(T object) {
    DumperOptions options = new DumperOptions();
    // options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    Representer representer = new Representer(options);
    representer.addClassTag(DataElement.class, Tag.MAP);
    representer.addClassTag(BaseSchema.class, Tag.MAP);
    representer.addClassTag(Rule.class, Tag.MAP);
    Yaml yaml = new Yaml(representer, new DumperOptions());
    return yaml.dumpAs(object, Tag.MAP, DumperOptions.FlowStyle.AUTO);
  }
}