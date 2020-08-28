package de.gwdg.metadataqa.api.schema;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BaseSchemaTest {

  @Test
  public void testConstructor() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url", Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
      .addField(new JsonBranch("description"))
      .addField(new JsonBranch("variablesMeasured"))
      .addField(new JsonBranch("measurementTechnique"))
      .addField(new JsonBranch("sameAs"))
      .addField(new JsonBranch("doi"))
      .addField(new JsonBranch("identifier"))
      .addField(new JsonBranch("author"))
      .addField(new JsonBranch("isAccessibleForFree"))
      .addField(new JsonBranch("dateModified"))
      .addField(new JsonBranch("distribution"))
      .addField(new JsonBranch("spatialCoverage"))
      .addField(new JsonBranch("provider"))
      .addField(new JsonBranch("funder"))
      .addField(new JsonBranch("temporalCoverage"));

    assertEquals(
      Arrays.asList("url", "name", "alternateName", "description", "variablesMeasured",
        "measurementTechnique", "sameAs", "doi", "identifier", "author", "isAccessibleForFree",
        "dateModified", "distribution", "spatialCoverage", "provider", "funder", "temporalCoverage"
      ),
      ((BaseSchema) schema).getHeader()
    );

    Map<String, String> extractableFields = new HashMap<>();
    extractableFields.put("url", "url");
    assertEquals(extractableFields, schema.getExtractableFields());

    assertEquals("url", schema.getPaths().get(0).getLabel());
    assertTrue(schema.getPaths().get(0).isMandatory());
    assertFalse(schema.getPaths().get(1).isMandatory());
  }

  @Test
  public void testCalculation() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url", Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
      .addField(new JsonBranch("description"))
      .addField(new JsonBranch("variablesMeasured"))
      .addField(new JsonBranch("measurementTechnique"))
      .addField(new JsonBranch("sameAs"))
      .addField(new JsonBranch("doi"))
      .addField(new JsonBranch("identifier"))
      .addField(new JsonBranch("author"))
      .addField(new JsonBranch("isAccessibleForFree"))
      .addField(new JsonBranch("dateModified"))
      .addField(new JsonBranch("distribution"))
      .addField(new JsonBranch("spatialCoverage"))
      .addField(new JsonBranch("provider"))
      .addField(new JsonBranch("funder"))
      .addField(new JsonBranch("temporalCoverage"));

    CalculatorFacade facade = new CalculatorFacade()
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()))
      .enableCompletenessMeasurement()
      .enableFieldCardinalityMeasurement();
    // facade.configure();

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName", "existence:description",
        "existence:variablesMeasured", "existence:measurementTechnique", "existence:sameAs",
        "existence:doi", "existence:identifier", "existence:author", "existence:isAccessibleForFree",
        "existence:dateModified", "existence:distribution", "existence:spatialCoverage",
        "existence:provider", "existence:funder", "existence:temporalCoverage",
        "cardinality:url", "cardinality:name", "cardinality:alternateName",
        "cardinality:description", "cardinality:variablesMeasured",
        "cardinality:measurementTechnique", "cardinality:sameAs", "cardinality:doi",
        "cardinality:identifier", "cardinality:author", "cardinality:isAccessibleForFree",
        "cardinality:dateModified", "cardinality:distribution", "cardinality:spatialCoverage",
        "cardinality:provider", "cardinality:funder", "cardinality:temporalCoverage"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
      StringBuffer result = new StringBuffer();
      while (iterator.hasNext()) {
        String line = CsvReader.toCsv(iterator.next());
        String metrics = facade.measure(line);
        result.append(metrics + "\n");
      }
      String expected = "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.411765,1.0,1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0,1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0\n"
        + "0.352941,1.0,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.294118,1.0,1,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,0\n";
      assertEquals(expected, result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

}