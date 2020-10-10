package de.gwdg.metadataqa.api.calculator.output;

public final class OutputFactory {

  public static OutputCollector createOutput(OutputCollector.TYPE type) {
    OutputCollector outputCollector = null;
    switch (type) {
      case STRING: outputCollector = new StringOutputCollector(); break;
      case STRING_LIST: outputCollector = new StringListOutputCollector(); break;
      case OBJECT_LIST: outputCollector = new ObjectListOutputCollector(); break;
      case MAP: outputCollector = new MapOutputCollector(); break;
      default:
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
    return outputCollector;
  }

}
