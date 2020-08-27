package de.gwdg.metadataqa.api.schema;

import java.util.List;

public interface CsvAwareSchema {
  List<String> getHeader();
}
