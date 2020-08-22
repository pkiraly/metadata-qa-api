package de.gwdg.metadataqa.api.util;

import com.opencsv.CSVParser;
import com.opencsv.ICSVParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {
  private List<String> header;
  private ICSVParser parser;

  public CsvReader() {
    parser = new CSVParser();
  }

  public CsvReader(ICSVParser parser) {
    this.parser = parser;
  }

  public void setHeader(List<String> header) {
    this.header = header;
  }

  public void setHeader(String[] header) {
    this.header = Arrays.asList(header);
  }

  public List<String> getHeader() {
    return header;
  }

  public String[] asArray(String input) throws IOException {
    return parser.parseLine(input);
  }

  public Map<String, String> asMap(String input) throws IOException {
    String[] columns = asArray(input);
    Map<String, String> record = new LinkedHashMap<>();
    if (header != null && columns.length == header.size()) {
      for (int i = 0; i < columns.length; i++) {
        record.put(header.get(i), columns[i]);
      }
    }
    return record;
  }
}
