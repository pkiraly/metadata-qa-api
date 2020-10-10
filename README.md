# Metadata Quality Assurance Framework API

This project is the central piece of the Metadata Quality Assurance
Framework, every other project is built on top of it. It provides
a general framework for measuring metadata quality in different 
digital collections.

If you want to implement it to your collection you have to define
a schema, which presentats an existing metadata schema, and
configure the basic facade, which will run the calculation.

Define a schema:
```Java
Schema schema = new BaseSchema()
  // this schema will be used for a CSV file
  .setFormat(Format.CSV)
  // JsonBranch represents a data element, which might have 
  // a number of properties
  .addField(
    new JsonBranch("url", Category.MANDATORY)
        .setExtractable()
  )
  .addField(new JsonBranch("name"))
  .addField(new JsonBranch("alternateName"))
  ...
  .addField(new JsonBranch("temporalCoverage"));
```

Define what to measure via a `CalculatorFacade` object:

```Java
CalculatorFacade facade = new CalculatorFacade()
  // set the schema which describes the source
  .setSchema(schema)
  // right now it is a CSV source, so we set how to parse it
  .setCsvReader(
    new CsvReader()
      .setHeader(((CsvAwareSchema) schema).getHeader()))
  // we will measure completeness now
 .enableCompletenessMeasurement();
```

These are the two important requirements for the start of the measuring. The measuring is simple:

```Java
String csv = calculator.measure(input)
```

The `input` should be a string formatted as JSON, XML or CSV. 
The output is a comma separated line. The `calculator.getHeader()` 
returns the list of the column names.

There are a couple of alternatives, if you would like to receive a 
List or a Map:

* `String measure(String record) throws InvalidJsonException`
* `List<String> measureAsList(String record) throws InvalidJsonException`
* `List<Object> measureAsListOfObjects(String record) throws InvalidJsonException`
* `Map<String, Object> measureAsMap(String record) throws InvalidJsonException`

If your input is a CSV file, and you already processed the lines 
into list of cells, you might want to use the following alternative
methods:

* `String measureCsv(List<String> record) throws InvalidJsonException`
* `List<String> measureCsvAsList(List<String> record) throws InvalidJsonException`
* `List<Object> measureCsvAsListOfObjects(List<String> record) throws InvalidJsonException`
* `Map<String, Object> measureCsvAsMap(List<String> record) throws InvalidJsonException`

An example which collects output into a StringBuffer (you can persist lines into a CSV file or into a database):

```Java
// collect the output into a container. The output is a CSV file
StringBuffer output = new StringBuffer();

// get the header of the output CSV
output.append(calculator.getHeader())

// The input could be JSON, XML or CSV. 
// You can set any kind of datasource, as long it returns a String
Iterator iterator = ...;
while (iterator.hasNext()) {
  try {
    // measure the input
    String csv = calculator.measure(iterator.next());
    // save csv
    output.append(csv);
  } catch (InvalidJsonException e) {
    // handle exception
  }
}

// get the output
String metrics = output.toString();
```

For the usage and implementation of the API see https://github.com/pkiraly/europeana-qa-api.

Java doc for the actual development version of the API: https://pkiraly.github.io/metadata-qa-api.

[![Build Status](https://travis-ci.org/pkiraly/metadata-qa-api.svg?branch=master)](https://travis-ci.org/pkiraly/metadata-qa-api)
[![Coverage Status](https://coveralls.io/repos/github/pkiraly/metadata-qa-api/badge.svg?branch=master)](https://coveralls.io/github/pkiraly/metadata-qa-api?branch=master)
[![javadoc](https://javadoc.io/badge2/de.gwdg.metadataqa/metadata-qa-api/javadoc.svg)](https://javadoc.io/doc/de.gwdg.metadataqa/metadata-qa-api)
