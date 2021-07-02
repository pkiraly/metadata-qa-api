# Metadata Quality Assessment Framework API

This project is the central piece of the Metadata Quality Assurance
Framework, every other project is built on top of it. It provides
a general framework for measuring metadata quality in different 
digital collections.

If you want to implement it to your collection you have to define
a schema, which presentats an existing metadata schema, and
configure the basic facade, which will run the calculation.

First, add the library into your project's `pom.xml` file:

```xml
<dependencies>
  ...
  <dependency>
    <grroupId>de.gwdg.metadata</grroupId>
    <artifactId>metadata-qa-api</artifactId>
    <version>0.7</version>
  </dependency>
</dependencies>
```

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
* `String measureAsJson(String inputRecord) throws InvalidJsonException`
* `Map<String, List<MetricResult>> measureAsMetricResult(String inputRecord) throws InvalidJsonException`

If your input is a CSV file, and you already processed the lines 
into list of cells, you could use the same methods:

* `String measure(List<String> record) throws InvalidJsonException`
* `List<String> measureAsList(List<String> record) throws InvalidJsonException`
* `List<Object> measureAsListOfObjects(List<String> record) throws InvalidJsonException`
* `Map<String, Object> measureAsMap(List<String> record) throws InvalidJsonException`
* `String measureAsJson(List<String> inputRecord) throws InvalidJsonException`
* `Map<String, List<MetricResult>> measureAsMetricResult(List<String> inputRecord) throws InvalidJsonException`

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
One can specify with this properties how many occurrences of a data elemens a record can have.

 * `minCount <number>` - specifies the minimum number of field occurence (API: `setMinCount()` or `withMinCount()`)

Example: the field should have at least one occurrence

```yaml
- name: about
  path:  $.['about']
  rules:
  - minCount: 1
```
 
 * `maxCount <number>` - specifies the maximum number of field occurence (API: `setMaxCount()` or `withMaxCount()`)

Example: the field might have maximum one occurrence

```yaml
- name: about
  path:  $.['about']
  rules:
  - maxCount: 1
```

#### Value Range

You can set a range of value within the field's value should remain. You can set a lower and higher bound with boolean 
operators. You can specify either integers or floating point numbers.

 * `minExclusive <number>` - The minimum exclusive value ([field value] > limit, API: `setMinExclusive(Double)` or `withMinExclusive(Double)`)
 * `minInclusive <number>` - The minimum inclusive value ([field value] >= limit, API: `setMinInclusive(Double)` or `withMinExclusive(Double)`)
 * `maxExclusive <number>` - The maximum exclusive value ([field value] < limit, API: `setMaxExclusive(Double)` or `withMaxExclusive(Double)`)
 * `maxInclusive <number>` - The maximum inclusive value ([field value] <= limit, API: `setMaxInclusive(Double)` or `withMaxInclusive(Double)`)

Example: 1.0 <= price <= 2.0

```yaml
- name: price
  path:  $.['price']
  rules:
    - and:
      - minInclusive: 1.0
      - maxInclusive: 2.0
```

Example: 1.0 < price < 2.0

```yaml
- name: price
  path:  $.['price']
  rules:
    - and:
      - minExclusive: 1
      - maxExclusive: 2
```
Note: integers will be interpreted as floating point numbers.

#### String constraints

 * `minLength <number>` - The minimum string length of each field value (API: `setMinLength(Integer)` or `withMinLength(Integer)`)

Example: the field value should not be empty
```yaml
- name: about
  path:  $.['about']
  rules:
    - minLength: 1
```

 * `maxLength <number>` - The maximum string length of each field value (API: `setMinLength(Integer)` or `withMaxLength(Integer)`)

Example: the value should be 3, 4, or 5 character long.

```yaml
- name: about
  path:  $.['about']
  rules:
    - and:
      - minLength: 3
      - maxLength: 5
```

* `hasValue value` - The value should be equal to the provided value (API: `setHasValue(String)` or `withHasValue(String)`)

Example: the status should be "published".

```yaml
- name: status
  path:  $.['status']
  rules:
    - hasValue: published
```

* `in [value1, ..., valueN]` - The string value should be one of the listed values (API: `setIn(List<String>)` or `withIn(List<String>)`)

Example: the value should be either "dataverse", "dataset" or "file".

```yaml
- name: type
  path:  $.['type']
  rules:
    - in: [dataverse, dataset, file]
```

 * `pattern <regular expression>` - A regular expression that each field value matches to satisfy the condition. The expression should cover
the whole string, not only a part of it (API: `setPattern(String)` or `withPattern(String)`)

Example: the field value should start with http:// or https:// and end with .jpg, .jpeg, .jpe, .jfif, .png, .tiff, .tif, .gif, .svg, .svgz, or .pdf.

```yaml
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
    - pattern: ^https?://.*\.(jpg|jpeg|jpe|jfif|png|tiff|tif|gif|svg|svgz|pdf)$
```

#### Comparision of properties

 * `equals <field label>` - The set of all values of a field is equal to the set of all values of another field 
 (API: `setEquals(String)` or `withEquals(String)`)

Example: The ID should be equal to the ISBN number.

```yaml
fields:
  - name: id
    path:  $.['id']
    rules:
      - equals: isbn
  - name: isbn
    path:  $.['isbn']
```

 * `disjoint <field label>` - The set of values of a field is disjoint (not equal) with the set of all values of another field 
 (API: `setDisjoint(String)` or `withDisjoint(String)`)

Example: The title should be different then description.

```yaml
fields:
  - name: title
    path:  $.['title']
    rules:
      - equals: description
  - name: description
    path:  $.['description']
```
 * `lessThan <field label>` - Each values of a field is smaller than each values of another field
  (API: `setLessThan(String)` or `withLessThan(String)`)

Example: the date of birth is less than the date of death

```yaml
- name: birthDate
  path: oai:record/dc:date[@type='birth']
  rules:
    - lessThan: deathDate
```

 * `lessThanOrEquals <field label>` - Each values of a field is smaller than or equals to each values of another field
  (API: `setLessThanOrEquals(String)` or `withLessThanOrEquals(String)`)

Example: the starting page of the article should be less than or equal to the ending page

```yaml
- name: startingPage
  path: startingPage
  rules:
    - lessThan: endingPage
```

#### Logical operators

With logical operators you can build complex rules. Each component should fit to its own rules. 

* `and [<rule1>, ..., <ruleN>]` - Passes if all the rules in the set passed.
  (API: `setAnd(List<Rule>)` or `withAnd(List<Rule>)`)

Example: The ID should have one and only one occurrence, and is should not be an empty string.

```yaml
- name: id
  path: oai:record/dc:identifier[@type='providerItemId']
  rules:
    - and:
      - minCount: 1
      - maxCount: 1
      - minLength: 1
```

* `or [<rule1>, ..., <ruleN>]` - Passes if at least one of the rules in the set passed.
  (API: `setOr(List<Rule>)` or `withOr(List<Rule>)`)

Example: The thumbnail should either end with a known image extension or its content type should be one of the provided MIME image types.

```yaml
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
    - or:
      - pattern: ^.*\.(jpg|jpeg|jpe|jfif|png|tiff|tif|gif|svg|svgz)$
      - contentType: [image/jpeg, image/png, image/tiff, image/tiff-fx, image/gif, image/svg+xml]
```

* `not [<rule1>, ..., <ruleN>]` - Passes if none of the rules in the set passed.
  (API: `setNot(List<Rule>)` or `withNot(List<Rule>)`)

Example: make sure that the title and the description is different.

```yaml
- name: title
  path:  $.['title']
  rules:
    - not:
      - equals: description
```

#### Other constraints

These rules don't have paralel in SHACL.

* `contentType [type1, ..., typeN]` - This rule interprets the value as a URL, fetches it and extracts the HTTP header's
content type, then checks if it is one of those allowed.

Example: The HTTP content type should be image/jpeg, image/png, image/tiff, image/tiff-fx, image/gif, or image/svg+xml.

```yaml
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
    - contentType: [image/jpeg, image/png, image/tiff, image/tiff-fx, image/gif, image/svg+xml]
```

#### General properties

* `id value` - you can define an identifier to the rule, which will be reflected in the output. If you miss it, the
system will assign a count number. ID might also help if you transform a human readable document such as cataloguing 
rules into a configuration file, and you want to keep linkage between them. (API `setId(String)` or `withId(String)`)

* `failureScore score` - a score which will be calculated if the validation fails. The score should be a negative o positive integer (including zero). 
(API `setFailureScore(nteger)` or `withFailureScore(Integer)`)

* `successScore score` - a score which will be calculated if the validation passes. The score should be a negative o positive integer (including zero).
 (API `setSuccessScore(nteger)` or `withSuccessScore(Integer)`)

Example: set of rules with IDs and scores.

```yaml
  - name: providerid
    path: oai:record/dc:identifier[@type='providerid']
    rules:
    - and:
      - minCount: 1
      - minLength: 1
        failureScore: -6
        id: 2.1
    - pattern: ^(DE-\d+|DE-MUS-\d+|http://id.zdb-services.de\w+|\d{8}|oai\d{13}|http://d-nb.info/gnd/\w+)$
      failureScore: -3
      id: 2.2
    - pattern: ^(DE-\d+|DE-MUS-\d+|http://id.zdb-services.de\w+)$
      successScore: 6
      id: 2.4
    - pattern: ^http://id.zdb-services.de\w+$
      successScore: 3
      id: 2.5
    - pattern: ^http://d-nb.info/gnd/\w+$
      successScore: 3
      id: 2.6
```

#### Set rules via Java API 

```Java
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
* `String solrHost`: The hostname of the Solr server.
  (API calls: setters: `setSolrHost(String)`, `withSolrHost(String):MeasurementConfiguration`, getter: `getSolrHost()`)
* `String solrPort`: The port of the Solr server.
  (API calls: setters: `setSolrPort(String)`, `withSolrPort(String):MeasurementConfiguration`, getter: `getSolrPort()`)
* `String solrPath`: The path part of of the Solr server URL.
  (API calls: setters: `setSolrPath(String)`, `withSolrPath(String):MeasurementConfiguration`, getter: `getSolrPath()`)

## Using an experimental version
  
If you want to try an experimental version (which has `SNAPSHOT` in its version name), you have to enable the retrieval of those versions in the `pom.xml` file:
```xml
<repositories>
  <repository>
    <id>sonatypeSnapshots</id>
    <name>Sonatype Snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
     <enabled>false</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>

<dependencies>
  ...
  <dependency>
    <grroupId>de.gwdg.metadata</grroupId>
    <artifactId>metadata-qa-api</artifactId>
    <version>0.8-SNAPSHOT</version>
  </dependency>
</dependencies>
```

Thanks Miel Vander Sande ([@mielvds](https://github.com/mielvds)) for the hint!

## More info

Since version 0.8-SNAPSHOT the project requires Java 11.

For the usage and implementation of the API see https://github.com/pkiraly/europeana-qa-api.

Java doc for the actual development version of the API: https://pkiraly.github.io/metadata-qa-api.

[![Build Status](https://travis-ci.org/pkiraly/metadata-qa-api.svg?branch=main)](https://travis-ci.com/pkiraly/metadata-qa-api)
[![Coverage Status](https://coveralls.io/repos/github/pkiraly/metadata-qa-api/badge.svg?branch=main)](https://coveralls.io/github/pkiraly/metadata-qa-api?branch=main)
[![javadoc](https://javadoc.io/badge2/de.gwdg.metadataqa/metadata-qa-api/javadoc.svg)](https://javadoc.io/doc/de.gwdg.metadataqa/metadata-qa-api)
[![Maven Central](https://img.shields.io/maven-central/v/de.gwdg.metadataqa/metadata-qa-api)](https://search.maven.org/artifact/de.gwdg.metadataqa/metadata-qa-api)

