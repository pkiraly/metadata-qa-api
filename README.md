# Metadata Quality Assessment Framework API

This project is the central piece of the Metadata Quality Assurance
Framework, every other project is built on top of it. It provides
a general framework for measuring metadata quality in different 
digital collections.

If you want to implement it to your collection you have to define
a schema, which presentats an existing metadata schema, and
configure the basic facade, which will run the calculation.

Define a configuration:
```Java
MeasurementConfiguration config = new MeasurementConfiguration()
  // we will measure completeness now
  .enableCompletenessMeasurement();
```

You can create a <a href="#defining-measurementconfiguration-with-a-configuration-file">configuration file</a>.

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

Build a `CalculatorFacade` object:

```Java
CalculatorFacade facade = new CalculatorFacade(config)
  // set the schema which describes the source
  .setSchema(schema)
  // right now it is a CSV source, so we set how to parse it
  .setCsvReader(
    new CsvReader()
      .setHeader(((CsvAwareSchema) schema).getHeader()))
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
into list of cells, you could use the same methods:

* `String measure(List<String> record) throws InvalidJsonException`
* `List<String> measureAsList(List<String> record) throws InvalidJsonException`
* `List<Object> measureAsListOfObjects(List<String> record) throws InvalidJsonException`
* `Map<String, Object> measureAsMap(List<String> record) throws InvalidJsonException`

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

## Defining schema with a configuration file

It is possible to define the schema with a YAML or JSON 
configuration file.

```
Schema schema = ConfigurationReader
  .readSchemaYaml("path/to/some/configuration.yaml")
  .asSchema();
```

A YAML example
```yaml
format: json
fields:
  - name: edm:ProvidedCHO/@about
    path:  $.['providedCHOs'][0]['about']
    indexField: id
    extractable: true
    rules:
      - and:
        - minCount: 1 
        - maxCount: 1
        failureScore: -10
      - pattern: ^https?://.*$
        successScore: 3
    categories:
      - MANDATORY
  - name: Proxy/dc:title
    path: $.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']
    categories:
      - DESCRIPTIVENESS
      - SEARCHABILITY
      - IDENTIFICATION
      - MULTILINGUALITY
      - CUSTOM
  - name: Proxy/dcterms:alternative
    path: $.['proxies'][?(@['europeanaProxy'] == false)]['dctermsAlternative']
    categories:
      - DESCRIPTIVENESS
      - SEARCHABILITY
      - IDENTIFICATION
      - MULTILINGUALITY
groups:
  - fields:
      - Proxy/dc:title
      - Proxy/dc:description
    categories:
      - MANDATORY
```

The same in JSON:

```json
{
  "format": "json",
  "fields": [
    {
      "name": "edm:ProvidedCHO/@about",
      "path":  "$.['providedCHOs'][0]['about']",
      "indexField": "id",
      "extractable": true,
      "rules": [
        {
          "and": [
            {"minCount": 1},
            {"maxCount": 1}
          ],
          "failureScore": -10
        },
        {
          "pattern": "^https?://.*$",
          "successScore": 3
        }
      ],
      "categories": ["MANDATORY"]
    },
    {
      "name": "Proxy/dc:title",
      "path": "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']",
      "categories": [
        "DESCRIPTIVENESS",
        "SEARCHABILITY",
        "IDENTIFICATION",
        "MULTILINGUALITY"
      ]
    },
    {
      "name": "Proxy/dcterms:alternative",
      "path": "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsAlternative']",
      "categories": [
        "DESCRIPTIVENESS",
        "SEARCHABILITY",
        "IDENTIFICATION",
        "MULTILINGUALITY"
      ]
    }
  ],
  "groups": [
    {
      "fields": [
        "Proxy/dc:title",
        "Proxy/dc:description"
      ],
      "categories": [
        "MANDATORY"
      ]
    }
  ]
}
```

The central piece is the `fields` array. Each item represents the properties of a signgle data elements (a JsonBranch in the API). Its properties are:
* `name` (String): the name or label of the data element
* `path` (String): a address of the data element. If the format is XML, ir should be an XPath expression. If format is JSON, it should be a JSONPath expression. If the format is CSV, it should be the name of the column. 
* `categories` (List<String>): a list of categories this field belongs to. Categories can be anything, in Europeana's use case these are the core functionalities the field supports
* `extractable` (boolean): whether the field can be extracted if field etraction is turned on
* `rules` (List<Rule>): a set of rules or constraints which will be checked against
* `indexField` (String): the name which can be used in a search engine connected to the application (at the time of writing Apache Solr is supported)

Optionaly you can set the "canonical list" of categories. It provides
two additional functionalities 
* if a field contains a category which is not listed in the list, 
that will be excluded (with a warning in the log)
* the order of the categories in the output follows the order set 
in the configuration.

Here is an example (in YAML):

```yaml
format: json
...
categories:
  - MANDATORY
  - DESCRIPTIVENESS
  - SEARCHABILITY
  - IDENTIFICATION
  - CUSTOM
  - MULTILINGUALITY

```
### Rules

One can add constraints to the fields. There are content rules, which
the tool will check. In this version the tool mimin SHACL constraints.

#### Cardinality

 * `minCount <number>` - specifies the minimum number of field occurence (API: `setMinCount()` or `withMinCount()`)
 * `maxCount <number>` - specifies the maximum number of field occurence (API: `setMaxCount()` or `withMaxCount()`)

#### Value Range

 * `minExclusive <number>` - The minimum exclusive value ([field value] > limit, API: `setMinExclusive(Double)` or `withMinExclusive(Double)`)
 * `minInclusive <number>` - The minimum inclusive value ([field value] >= limit, API: `setMinInclusive(Double)` or `withMinExclusive(Double)`)
 * `maxExclusive <number>` - The maximum exclusive value ([field value] < limit, API: `setMaxExclusive(Double)` or `withMaxExclusive(Double)`)
 * `maxInclusive <number>` - The maximum inclusive value ([field value] <= limit, API: `setMaxInclusive(Double)` or `withMaxInclusive(Double)`)

#### String constraints

 * `minLength <number>` - The minimum string length of each field value (API: `setMinLength(Integer)` or `withMinLength(Integer)`)
 * `maxLength <number>` - The maximum string length of each field value (API: `setMinLength(Integer)` or `withMaxLength(Integer)`)
 * `pattern <regular expression>` - A regular expression that each field value matches to satisfy the condition (API: `setPattern(String)` or `withPattern(String)`)

#### Property pair

 * `equals <field label>` - The set of all values of a field is equal to the set of all values of another field 
 (API: `setEquals(String)` or `withEquals(String)`)
 * `disjoint <field label>` - The set of values of a field is disjoint (not equal) with the set of all values of another field 
 (API: `setDisjoint(String)` or `withDisjoint(String)`)
 * `lessThan <field label>` - Each values of a field is smaller than each values of another field
  (API: `setLessThan(String)` or `withLessThan(String)`)
 * `lessThanOrEquals <field label>` - Each values of a field is smaller than or equals to each values of another field
  (API: `setLessThanOrEquals(String)` or `withLessThanOrEquals(String)`)

#### Logical functions

* `and [<rule1>, ..., <ruleN>]` - Passes if all the rules in the set passed.
  (API: `setAnd(List<Rule>)` or `withAnd(List<Rule>)`)
* `or [<rule1>, ..., <ruleN>]` - Passes if at least one of the rules in the set passed.
  (API: `setOr(List<Rule>)` or `withOr(List<Rule>)`)
* `not [<rule1>, ..., <ruleN>]` - Passes if all the rules in the set failed.
  (API: `setNot(List<Rule>)` or `withNot(List<Rule>)`)

Set rules via Java API 

```java
Schema schema = new BaseSchema()
  .setFormat(Format.CSV)
  .addField(
    new JsonBranch("title", "title")
      .setRule(
        new Rule()
          .withDisjoint("description")
      )
  )
  .addField(
    new JsonBranch("url", "url")
      .setRule(
        new Rule()
          .withMinCount(1)
          .withMaxCount(1)
          .withPattern("^https?://.*$")
      )
  )
  ;
```

Via configuration file (a YAML example):

```yaml
format: csv
fields:
  - name: title
    categories: [MANDATORY]
    rules:
      disjoint: description
  - name: url
    categories: [MANDATORY]
    extractable: true
    rules:
      minCount: 1
      maxCount: 1
      pattern: ^https?://.*$
```

In both cases we defined two fields. `title` has one constraints: it should not be equal to 
the value of `description` field (which is masked out from the example). Note: if this hypothetical
`description` field is not available the API drops an error message into the log. `url` should have
one and only one instance, and its value should start with "http://" or "https://".

As you can see there are two types of setters in the API: setSomething and withSomething. The
difference is that setSomething returs with void, but withSomething returns with the Rule object,
so you can use it in a chain such as `new Rule().withMinCount(1).withMaxCount(3)` 
(while `new Rule().setMinCount(1).setMaxCount(3)` doesn't work).

## Defining MeasurementConfiguration with a configuration file

MeasurementConfiguration can be created from JSON or YAML configuration files with the following methods:

* `ConfigurationReader.readMeasurementJson(String filePath)`: reading configuration from JSON
* `ConfigurationReader.readMeasurementYaml(String filePath)`: reading configuration from YAML

an example:

```Java
MeasurementConfiguration configuration = ConfigurationReader
  .readMeasurementJson("path/to/some/configuration.json");
```

An example JSON file:

```JSON
{
  "fieldExtractorEnabled": false,
  "fieldExistenceMeasurementEnabled": true,
  "fieldCardinalityMeasurementEnabled": true,
  "completenessMeasurementEnabled": true,
  "tfIdfMeasurementEnabled": false,
  "problemCatalogMeasurementEnabled": false,
  "ruleCatalogMeasurementEnabled": false,
  "languageMeasurementEnabled": false,
  "multilingualSaturationMeasurementEnabled": false,
  "collectTfIdfTerms": false,
  "uniquenessMeasurementEnabled": false,
  "completenessCollectFields": false,
  "saturationExtendedResult": false,
  "checkSkippableCollections": false
}
```

* `fieldExtractorEnabled`: Flag whether or not the field extractor is enabled (default: false).
  (API calls: setters: `enableFieldExtractor()`, `disableFieldExtractor()`, getter: `isFieldExtractorEnabled()`)
* `fieldExistenceMeasurementEnabled`: Flag whether or not run the field existence measurement (default: true).
  (API calls: setters: `enableFieldExistenceMeasurement()`, `disableFieldExistenceMeasurement()`, getter: `isFieldExistenceMeasurementEnabled()`)
* `fieldCardinalityMeasurementEnabled`: Flag whether or not run the field cardinality measurement (default: true).
  (API calls: setters: `enableFieldCardinalityMeasurement()`, `disableFieldCardinalityMeasurement()`, getter: `isFieldCardinalityMeasurementEnabled()`)
* `completenessMeasurementEnabled`: Flag whether or not run the completeness measurement (default: true).
  (API calls: setters: `enableCompletenessMeasurement()`, `disableCompletenessMeasurement()`, getter: `isCompletenessMeasurementEnabled()`)
* `tfIdfMeasurementEnabled`: Flag whether or not run the uniqueness measurement (default: false).
  (API calls: setters: `enableTfIdfMeasurement()`, `disableTfIdfMeasurement()`, getter: `isTfIdfMeasurementEnabled()`)
* `problemCatalogMeasurementEnabled`: Flag whether or not run the problem catalog (default: false).
  (API calls: setters: `enableProblemCatalogMeasurement()`, `disableProblemCatalogMeasurement()`, getter: `isProblemCatalogMeasurementEnabled()`)
* `ruleCatalogMeasurementEnabled`: Flag whether or not run the rule catalog (default: false).
  (API calls: setters: `enableRuleCatalogMeasurement()`, `disableRuleCatalogMeasurement()`, getter: `isRuleCatalogMeasurementEnabled()`)
* `languageMeasurementEnabled`: Flag whether or not run the language detector (default: false).
  (API calls: setters: `enableLanguageMeasurement()`, `disableLanguageMeasurement()`, getter: `isLanguageMeasurementEnabled()`)
* `multilingualSaturationMeasurementEnabled`: Flag whether or not run the multilingual saturation measurement (default: false).
  (API calls: setters: `enableMultilingualSaturationMeasurement()`, `disableMultilingualSaturationMeasurement()`, getter: `isMultilingualSaturationMeasurementEnabled()`)
* `collectTfIdfTerms`: Flag whether or not collect TF-IDF terms in uniqueness measurement (default: false).
  (API calls: setters: `collectTfIdfTerms(boolean)`, getter: `collectTfIdfTerms()`)
* `uniquenessMeasurementEnabled`: Flag whether or not to run in uniqueness measurement (default: false).
  (API calls: setters: `enableUniquenessMeasurement()`, `disableUniquenessMeasurement()`, getter: `isUniquenessMeasurementEnabled()`)
* `completenessCollectFields`: Flag whether or not run missing/empty/existing field collection in completeness (default: false).
  (API calls: setters: `enableCompletenessFieldCollecting(boolean)`, getter: `isCompletenessFieldCollectingEnabled()`)
* `saturationExtendedResult`: Flag whether or not to create extended result in multilingual saturation calculation (default: false).
  (API calls: setters: `enableSaturationExtendedResult(boolean)`, getter: `isSaturationExtendedResult()`)
* `checkSkippableCollections`: Flag whether or not to check skipable collections (default: false).
  (API calls: setters: `enableCheckSkippableCollections(boolean)`, getter: `isCheckSkippableCollections()`)

## More info

Since version 0.8-SNAPSHOT the project requires Java 11.

For the usage and implementation of the API see https://github.com/pkiraly/europeana-qa-api.

Java doc for the actual development version of the API: https://pkiraly.github.io/metadata-qa-api.

[![Build Status](https://travis-ci.org/pkiraly/metadata-qa-api.svg?branch=main)](https://travis-ci.com/pkiraly/metadata-qa-api)
[![Coverage Status](https://coveralls.io/repos/github/pkiraly/metadata-qa-api/badge.svg?branch=main)](https://coveralls.io/github/pkiraly/metadata-qa-api?branch=main)
[![javadoc](https://javadoc.io/badge2/de.gwdg.metadataqa/metadata-qa-api/javadoc.svg)](https://javadoc.io/doc/de.gwdg.metadataqa/metadata-qa-api)
[![Maven Central](https://img.shields.io/maven-central/v/de.gwdg.metadataqa/metadata-qa-api)](https://search.maven.org/artifact/de.gwdg.metadataqa/metadata-qa-api)

