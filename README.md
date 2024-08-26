# Metadata Quality Assessment Framework API

This project is the central piece of the Metadata Quality Assurance
Framework, every other project is built on top of it. It provides
a general framework for measuring metadata quality in different 
digital collections.

  * [Quality dimensions](#quality-dimensions)
  * [Running as command-line application](#running-as-command-line-application)
  * [Using the library](#using-the-library)
  * [Defining schema with a configuration file](#defining-schema-with-a-configuration-file)
    + [Rules](#rules)
      - [Cardinality](#cardinality)
        * [`minCount <number>`](#mincount-number)
        * [`maxCount <number>`](#maxcount-number)
      - [Value Range](#value-range)
        * [`minExclusive <number>`](#minexclusive-number)
        * [`minInclusive <number>`](#mininclusive-number)
        * [`maxExclusive <number>`](#maxexclusive-number)
        * [`maxInclusive <number>`](#maxinclusive-number)
      - [String constraints](#string-constraints)
        * [`minLength <number>`](#minlength-number)
        * [`maxLength <number>`](#maxlength-number)
        * [`hasValue <String>`](#hasvalue-string)
        * [`in [String1, ..., StringN]`](#in-string1--stringn)
        * [`pattern <regular expression>`](#pattern-regular-expression)
        * [`minWords <number>`](#minwords-number)
        * [`maxWords <number>`](#maxwords-number)
      - [Comparision of properties](#comparision-of-properties)
        * [`equals <field label>`](#equals-field-label)
        * [`disjoint <field label>`](#disjoint-field-label)
        * [`lessThan <field label>`](#lessthan-field-label)
        * [`lessThanOrEquals <field label>`](#lessthanorequals-field-label)
      - [Logical operators](#logical-operators)
        * [`and [<rule1>, ..., <ruleN>]`](#and-rule1--rulen)
        * [`or [<rule1>, ..., <ruleN>]`](#or-rule1--rulen)
        * [`not [<rule1>, ..., <ruleN>]`](#not-rule1--rulen)
      - [Other constraints](#other-constraints)
        * [`contentType [type1, ..., typeN]`](#contenttype-type1--typen)
        * [`unique <boolean>`](#unique-boolean)
        * [`dependencies [id1, id2, ..., idN]`](#dependencies-id1-id2--idn)
        * [`dimension [criteria1, criteria2, ..., criteriaN]`](#dimension-criteria1-criteria2--criterian)
        * [`hasLanguageTag <anyOf|oneOf|allOf>`](#haslanguagetag-anyofoneofallof)
        * [`isMultilingual <boolean>`](#ismultilingual-boolean)
      - [General properties](#general-properties)
        * [`id <String>`](#id-string)
        * [`description <String>`](#description-string)
        * [`failureScore <integer>`](#failurescore-integer)
        * [`successScore <integer>`](#successscore-integer)
        * [`hidden <boolean>`](#hidden-boolean)
        * [`skip <boolean>`](#skip-boolean)
        * [`debug <boolean>`](#debug-boolean)
      - [Set rules via Java API](#set-rules-via-java-api)
  * [Defining MeasurementConfiguration with a configuration file](#defining-measurementconfiguration-with-a-configuration-file)
  * [Using an experimental version](#using-an-experimental-version)
  * [More info](#more-info)


## Quality dimensions

The framework measures the following features:

* _completeness_: it says how complete your records, i.e. what ratio of data
  elements defined in the metadata schema is available in the records. It
  can also collect information about the extistence of the field, and their 
  cardinality (how many times they occur in a record)
* _uniqueness and TF-IDF score_: it calculates the TF-IDF scores for field
  values. It is useful to learn how unique or how frequent the data values.
* _rule catalogue_: one can set different rules or constraints against the
  data values. It checks if these rules are followed. One can set scores for
  failure and success cases.
* _multilingual saturation_: how multilingual your records. It requires XML
  or RDF based multilingual annotation. It reports the number of tagged
  literals, number of distinct language tags, number of tagged literals per
  language tag, average number of languages per property for which there is
  at least one language-tagged literal
* _language extractor_: it extracts the language tag of data elements if any.

above these there are some helper calculators:

* _extractor_: extracts and outputs values from the record
* _annotator_: injects metadata into the output (e.g. some values, which helps
  further processings, such as file name, date, identifier or other information
  about the measurement, which are not available within the records)
* _indexer_: index particular data elements with Solr before measurement. It
  is a necessary step for measuring TF-IDF or uniqueness

## Running as command-line application

usage:

```
./mqa -i <file> -s <file> -m <file>
      [-f <format>] [-h <arg>] [-o <file>] [-r <path>] [-v <format>] [-w <format>] [-z]
```
* `-i,--input <file>` Input file.
* `-n,--inputFormat <format>` (optional, String) The format of input file. Right now it supports two JSON variants:
  * `ndjson`: line delimited JSON in which every line is a new record (the default value)
  * `json-array`: JSON file that contains an array of objects
* `-s,--schema <file>` Schema file describing the metadata structure to run assessment against.
* `-v,--schemaFormat <format>` Format of schema file: json, yaml. Default: based on file extension, else json.
* `-m,--measurements <file>` Configuration file for measurements.
* `-w,--measurementsFormat <format>` Format of measurements config file: json, yaml. Default: based on file extension, else json.
* `-o,--output <file>` Output file.
* `-f,--outputFormat <format>` Format of the output: json, ndjson (new line delimited JSON), csv, csvjson (json encoded in csv; useful for RDB bulk loading). Default: ndjson.
* `-r,--recordAddress <path>` An XPath or JSONPath expression to separate individual records in an XML or JSON files.
* `-z,--gzip` Flag to indicate that input is gzipped.
* `-h,--headers <arg>` Headers to copy from source

## Using the library

If you want to implement it to your collection you have to define a schema,
which presentats an existing metadata schema, and configure the basic facade,
which will run the calculation.

First, add the library into your project's `pom.xml` file:

```xml
<dependencies>
  ...
  <dependency>
    <groupId>de.gwdg.metadata</groupId>
    <artifactId>metadata-qa-api</artifactId>
    <version>0.9.4</version>
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
  // DataELement represents a data element, which might have 
  // a number of properties
  .addField(
    new DataELement("url", Category.MANDATORY)
        .setExtractable()
  )
  .addField(new DataELement("name"))
  .addField(new DataELement("alternateName"))
  ...
  .addField(new DataELement("temporalCoverage"));
```

Build a `CalculatorFacade` object:

```Java
CalculatorFacade calculator = new CalculatorFacade(config) // use configuration
  .setSchema(schema)   // set the schema which describes the source
  .configure();        // finalize the configuration
```

If you have a CSV source and you would like to reuse the headers use `setCsvReader()`:
```Java
CalculatorFacade calculator = new CalculatorFacade(config) // use configuration
  .setSchema(schema)   // set the schema which describes the source
  .setCsvReader(       // optional, if it is a CSV source
    new CsvReader()
      .setHeader(((CsvAwareSchema) schema).getHeader()))
  .configure();        // finalize the configuration
```

These are the two important requirements for the start of the measuring.
The measuring is simple:

```Java
String csv = calculator.measure(input)
```

The `input` should be a string formatted as JSON, XML or CSV. The output is a
comma separated line. The `calculator.getHeader()` returns the list of the
column names.

There are a couple of alternatives, if you would like to receive a List or a Map:

* `String measure(String record) throws InvalidJsonException`
Returns a CSV string

```Java
"0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0"
```
* `List<String> measureAsList(String record) throws InvalidJsonException`
Returns a list of strings. 

```Java
List.of("0.352941", "1.0", "1", "1", "0", "1", "0", "0", "0", "0", "1", "0", "0", "1", "1",
        "0", "0", "0", "0", "1", "1", "0", "1", "0", "0", "0", "0", "1", "0", "0", "1", "1",
        "0", "0", "0", "0");
```
* `List<Object> measureAsListOfObjects(String record) throws InvalidJsonException`
Returns a list of objects

```Java
List.of(0.35294117647058826, 1.0, true, true, false, true, false, false, false, false, true,
        false, false, true, true, false, false, false, false, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0,
        0, 1, 1, 0, 0, 0, 0);
```
* `Map<String, Object> measureAsMap(String record) throws InvalidJsonException`
Returns a map of objects. The keys of the map are the names of the metrics.

```Java
Map.of(
  "completeness:TOTAL", 0.35294117647058826,
  "completeness:MANDATORY", 1.0,
  "existence:url", true,
  "existence:name", true,
  "existence:alternateName", false,
  "existence:description", true,
  "existence:variablesMeasured", false,
  "existence:measurementTechnique", false,
  "existence:sameAs", false,
  "existence:doi", false,
  "existence:identifier", true,
  "existence:author", false,
  "existence:isAccessibleForFree", false,
  "existence:dateModified", true,
  "existence:distribution", true,
  "existence:spatialCoverage", false,
  "existence:provider", false,
  "existence:funder", false,
  "existence:temporalCoverage", false,
  "cardinality:url", 1.
  "cardinality:name", 1,
  "cardinality:alternateName", 0,
  "cardinality:description", 1,
  "cardinality:variablesMeasured", 0,
  "cardinality:measurementTechnique", 0,
  "cardinality:sameAs", 0,
  "cardinality:doi", 0,
  "cardinality:identifier", 1,
  "cardinality:author", 0,
  "cardinality:isAccessibleForFree", 0,
  "cardinality:dateModified", 1,
  "cardinality:distribution", 1,
  "cardinality:spatialCoverage", 0,
  "cardinality:provider", 0,
  "cardinality:funder", 0,
  "cardinality:temporalCoverage", 0
)
```

* `String measureAsJson(String inputRecord) throws InvalidJsonException`
Returns a JSON representation

```JSON
{
  "completeness":{
    "completeness":{
      "TOTAL":0.35294117647058826,
      "MANDATORY":1.0
    },
    "existence":{
      "url":true,
      "name":true,
      "alternateName":false,
      "description":true,
      "variablesMeasured":false,
      "measurementTechnique":false,
      "sameAs":false,
      "doi":false,
      "identifier":true,
      "author":false,
      "isAccessibleForFree":false,
      "dateModified":true,
      "distribution":true,
      "spatialCoverage":false,
      "provider":false,
      "funder":false,
      "temporalCoverage":false
    },
    "cardinality":{
      "url":1,
      "name":1,
      "alternateName":0,
      "description":1,
      "variablesMeasured":0,
      "measurementTechnique":0,
      "sameAs":0,
      "doi":0,
      "identifier":1,
      "author":0,
      "isAccessibleForFree":0,
      "dateModified":1,
      "distribution":1,
      "spatialCoverage":0,
      "provider":0,
      "funder":0,
      "temporalCoverage":0
    }
  }
}
```

* `Map<String, List<MetricResult>> measureAsMetricResult(String inputRecord) throws InvalidJsonException`
Returns a map with a "raw" format. The keys of the map are the individual
calculators. The values are list of MetricResult objects. Each has a name
(use `getName()` method), and a map of metrics (use `getResultMap()` method).
Since it is rather difficult to illustrate, let me give you some assertions here:

```Java
assertTrue(metrics instanceof Map);
assertEquals(1, metrics.size());
assertEquals("completeness", metrics.keySet().iterator().next());
// the calculator produced three metrics
assertEquals(3, metrics.get("completeness").size());

// first: completeness
assertEquals("completeness", metrics.get("completeness").get(0).getName());
assertEquals(
  Map.of("TOTAL", 0.35294117647058826, "MANDATORY", 1.0),
  metrics.get("completeness").get(0).getResultMap());

// second: existence
assertEquals("existence", metrics.get("completeness").get(1).getName());
assertEquals(
  Set.of("url", "name", "alternateName", "description", "variablesMeasured", "measurementTechnique",
        "sameAs", "doi", "identifier", "author", "isAccessibleForFree", "dateModified",
        "distribution", "spatialCoverage", "provider", "funder", "temporalCoverage"),
  metrics.get("completeness").get(1).getResultMap().keySet());
assertEquals(
  List.of(true, true, false, true, false, false, false, false, true, false, false, true, true,
          false, false, false, false),
  new ArrayList(metrics.get("completeness").get(1).getResultMap().values()));

// third: cardinality
assertEquals("cardinality", metrics.get("completeness").get(2).getName());
assertEquals(
  Set.of("url", "name", "alternateName", "description", "variablesMeasured", "measurementTechnique",
        "sameAs", "doi", "identifier", "author", "isAccessibleForFree", "dateModified",
        "distribution", "spatialCoverage", "provider", "funder", "temporalCoverage"),
  metrics.get("completeness").get(2).getResultMap().keySet());
assertEquals(
  List.of(1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0),
  new ArrayList(metrics.get("completeness").get(2).getResultMap().values()));
```

If your input is a CSV file, and you already processed the lines 
into list of cells, you could use the same methods:

* `String measure(List<String> record) throws InvalidJsonException`
* `List<String> measureAsList(List<String> record) throws InvalidJsonException`
* `List<Object> measureAsListOfObjects(List<String> record) throws InvalidJsonException`
* `Map<String, Object> measureAsMap(List<String> record) throws InvalidJsonException`
* `String measureAsJson(List<String> inputRecord) throws InvalidJsonException`
* `Map<String, List<MetricResult>> measureAsMetricResult(List<String> inputRecord) throws InvalidJsonException`

An example which collects output into a StringBuffer (you can persist lines
into a CSV file or into a database):

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

It is possible to define the schema with a YAML or JSON configuration file.

```
Schema schema = ConfigurationReader
  .readSchemaYaml("path/to/some/configuration.yaml")
  .asSchema();
```

A YAML example:

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

The central piece is the `fields` array. Each item represents the properties of
a single data elements (a DataELement in the API). Its properties are:

* `name` (String): the name or label of the data element
* `path` (String): a address of the data element. If the format is XML, it
  should be an XPath expression. If format is JSON, it should be a JSONPath
  expression. If the format is CSV, it should be the name of the column. 
* `categories` (List<String>): a list of categories this field belongs to.
  Categories can be anything, in Europeana's use case these are the core
  functionalities the field supports
* `extractable` (boolean): whether the field can be extracted if field
  extraction is turned on
* `rules` (List<Rule>): a set of rules or constraints which will be checked
  against
* `indexField` (String): the name which can be used in a search engine connected
  to the application (at the time of writing Apache Solr is supported)
* `inactive` (boolean): the data element is inactive, do not run checks on this
* `identifierField` (boolean): the data element is the identifier of the record
* `asLanguageTagged` (boolean): treat the data element as language tagged. It works 
  for JSON where the content of the data element is encoded with an associated 
  array, where the keys are the language tags.

Optionaly you can set the "canonical list" of categories. It provides
two additional functionalities 

* if a field contains a category which is not listed in the list, that will be
  excluded (with a warning in the log)
* the order of the categories in the output follows the order set in the
  configuration.

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
One can specify with these constraints how many occurrences of a data element
a record can have.

##### `minCount <number>`
Specifies the minimum number of field occurence (API: `setMinCount()` or `withMinCount()`)

Example: the field should have at least one occurrence

```yaml
- name: about
  path:  $.['about']
  rules:
  - minCount: 1
```

##### `maxCount <number>`
Specifies the maximum number of field occurence (API: `setMaxCount()` or `withMaxCount()`)

Example: the field might have maximum one occurrence

```yaml
- name: about
  path:  $.['about']
  rules:
  - maxCount: 1
```

#### Value Range

You can set a range of value within which the field's value should remain. You
can set a lower and higher bound with boolean operators. You can specify either
integers or floating point numbers.

##### `minExclusive <number>`
The minimum exclusive value ([field value] > limit, API: `setMinExclusive(Double)` or `withMinExclusive(Double)`)

##### `minInclusive <number>`
The minimum inclusive value ([field value] >= limit, API: `setMinInclusive(Double)` or `withMinExclusive(Double)`)

##### `maxExclusive <number>`
The maximum exclusive value ([field value] < limit, API: `setMaxExclusive(Double)` or `withMaxExclusive(Double)`)

##### `maxInclusive <number>`
The maximum inclusive value ([field value] <= limit, API: `setMaxInclusive(Double)` or `withMaxInclusive(Double)`)

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

##### `minLength <number>`
The minimum string length of each field value (API: `setMinLength(Integer)` or `withMinLength(Integer)`)

Example: the field value should not be empty
```yaml
- name: about
  path:  $.['about']
  rules:
    - minLength: 1
```

##### `maxLength <number>`
The maximum string length of each field value (API: `setMaxLength(Integer)` or `withMaxLength(Integer)`)

Example: the value should be 3, 4, or 5 characters long.

```yaml
- name: about
  path:  $.['about']
  rules:
    - and:
      - minLength: 3
      - maxLength: 5
```

##### `hasValue <String>`
The value should be equal to the provided value (API: `setHasValue(String)` or `withHasValue(String)`)

Example: the status should be "published".

```yaml
- name: status
  path:  $.['status']
  rules:
    - hasValue: published
```

##### `in [String1, ..., StringN]`
The string value should be one of the listed values (API: `setIn(List<String>)` or `withIn(List<String>)`)

Example: the value should be either "dataverse", "dataset" or "file".

```yaml
- name: type
  path:  $.['type']
  rules:
    - in: [dataverse, dataset, file]
```

##### `pattern <regular expression>`
A regular expression that each field value matches to satisfy the condition.
The expression can match a a part of the whole string (see the Java Matcher 
object's [find](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#find--) method).
(API: `setPattern(String)` or `withPattern(String)`)

Example: the field value should start with http:// or https:// and end with
.jpg, .jpeg, .jpe, .jfif, .png, .tiff, .tif, .gif, .svg, .svgz, or .pdf.

```yaml
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
    - pattern: ^https?://.*\.(jpg|jpeg|jpe|jfif|png|tiff|tif|gif|svg|svgz|pdf)$
```

##### `minWords <number>`
The minimum word count of each field value (API: `setMinWords(Integer)` or `withMinWord(Integer)`)

Example: the field value should have at least one words

```yaml
- name: about
  path:  $.['about']
  rules:
    - minWords: 1
```

##### `maxWords <number>`
The maximum string length of each field value (API: `setMaxWords(Integer)` or `withMaxWords(Integer)`)

Example: the value should be at least 3 character long, but should not contain more than 2 words.

```yaml
- name: about
  path:  $.['about']
  rules:
    - and:
      - minLength: 3
      - maxWords: 2
```

#### Comparision of properties

##### `equals <field label>`
The set of all values of a field is equal to the set of all values of another
field (API: `setEquals(String)` or `withEquals(String)`)

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

##### `disjoint <field label>`
The set of values of a field is disjoint (not equal) with the set of all values
of another field (API: `setDisjoint(String)` or `withDisjoint(String)`)

Example: The title should be different from description.

```yaml
fields:
  - name: title
    path:  $.['title']
    rules:
      - equals: description
  - name: description
    path:  $.['description']
```
##### `lessThan <field label>`

Each values of a field is smaller than each values of another field
(API: `setLessThan(String)` or `withLessThan(String)`)

Example: the date of birth is less than the date of death

```yaml
- name: birthDate
  path: oai:record/dc:date[@type='birth']
  rules:
    - lessThan: deathDate
```

##### `lessThanOrEquals <field label>`

Each values of a field is smaller than or equals to each values of another field
(API: `setLessThanOrEquals(String)` or `withLessThanOrEquals(String)`)

Example: the starting page of the article should be less than or equal to the
ending page:

```yaml
- name: startingPage
  path: startingPage
  rules:
    - lessThan: endingPage
```

#### Logical operators

With logical operators you can build complex rules. Each component should fit to its own rules. 

##### `and [<rule1>, ..., <ruleN>]`

Passes if all the rules in the set passed. (API: `setAnd(List<Rule>)` or `withAnd(List<Rule>)`)

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

##### `or [<rule1>, ..., <ruleN>]`

Passes if at least one of the rules in the set passed. (API: `setOr(List<Rule>)` or `withOr(List<Rule>)`)

Example: The thumbnail should either end with a known image extension or its
content type should be one of the provided MIME image types.

```yaml
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
    - or:
      - pattern: ^.*\.(jpg|jpeg|jpe|jfif|png|tiff|tif|gif|svg|svgz)$
      - contentType: [image/jpeg, image/png, image/tiff, image/tiff-fx, image/gif, image/svg+xml]
```

##### `not [<rule1>, ..., <ruleN>]`

Passes if none of the rules in the set passed. (API: `setNot(List<Rule>)` or `withNot(List<Rule>)`)

Example: make sure that the title and the description is different.

```yaml
- name: title
  path:  $.['title']
  rules:
    - not:
      - equals: description
```

#### Other constraints

These rules don't have parallel in SHACL.


##### `contentType [type1, ..., typeN]`

This rule interprets the value as a URL, fetches it and extracts the HTTP header's
content type, then checks if it is one of those allowed.

Example: The HTTP content type should be image/jpeg, image/png, image/tiff,
image/tiff-fx, image/gif, or image/svg+xml.

```yaml
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
    - contentType: [image/jpeg, image/png, image/tiff, image/tiff-fx, image/gif, image/svg+xml]
```

##### `unique <boolean>`

(since v0.9.0)

This rule checks if the value of the field is unique. Prerequisite: the field
should have indexField property, and the content should be indexed with Apache
Solr.

##### `dependencies [id1, id2, ..., idN]`

(since v0.9.0)

This rule checks if other rules has already checked and passed. It passes if
all dependent rules has passed or resulted NA, otherwise fail. The ids should
be valid, and the dependent rule should take place after the ones from which
it depends.

##### `dimension [criteria1, criteria2, ..., criteriaN]`

(since v0.9.0)

This checks if a linked image fits to some dimension constraints (unit in
pixel) - if the value is an URL for an image. One can check the minimum and
maximum size of width, height and shorter or longer sides (in case it is not
important if width or height is the shorter). The criteria:

- `minWidth`: the minimum width
- `maxWidth`: the maximum width
- `minHeight`: minimum height
- `maxHeight`: maximum height
- `minShortside`: minimum length of the shorter side of the image
- `maxShortside`: maximum length of the shorter side of the image
- `minLongside`: minimum length of the longer side of the image
- `maxLongside`: minimum length of the longer side of the image

example:

```ỳaml
format: csv
fields:
- name: thumbnail
  path: oai:record/dc:identifier[@type='binary']
  rules:
  - id: 3.1
    failureScore: -9
    dimension:
      minWidth: 200
      minHeight: 200
```

##### `hasLanguageTag <anyOf|oneOf|allOf>`

(since v0.9.6)

It checks if the data element value has language tag. In XML the language tag is
found in `@xml:lang` attribute. In JSON it might be encoded differently. Right now 
MQAF suppoert the following encoding:

```json
"description": {
  "de": ["Porträt"]
}
```

Since this kind of structure might be applied not only for the language annotation, at
the field level we should set that the field is expected to have language annotation:

```yaml
format: json
fields:
  - name: description
    path: $.['description']
    asLanguageTagged: true
```

The parameters defines if any, one or all instances should have language annottation:

* `anyOf`: the test passes if at least one instance has language tag
* `oneOf`: the test passes if one and only one instance has language tag
* `allOf`: the test passes if at least all instances have language tag

A full example:

```yaml
format: json
fields:
- name: description
  path: $.['description']
  asLanguageTagged: true
  rules:
  - hasLanguageTag: allOf
```

##### `isMultilingual <boolean>`

(since v0.9.6)

It checks if the data element is multilingual, so it has at least two instances with
different language annotations.

```json
{
  "description":{
    "de":["Portr\u00e4t"],
    "zh":["\u8096\u50cf"]
  }
}

```

an example schema

```yaml
format: json
fields:
  - name: description
    path: $.['description']
    asLanguageTagged: true
    rules:
      - isMultilingual: true
```


#### General properties

##### `id <String>`

You can define an identifier to the rule, which will be reflected in the output.
If you miss it, the system will assign a count number. ID might also help if
you transform a human readable document such as cataloguing rules into a
configuration file, and you want to keep linkage between them. 
(API `setId(String)` or `withId(String)`)

##### `description <String>`

Provide a description to document what the particular rule is doing. It can be
anything reasonable, it does not play a role in the calculation.

##### `failureScore <integer>`

A score which will be calculated if the validation fails. The score should be a
negative or positive integer (including zero). 
(API `setFailureScore(Integer)` or `withFailureScore(Integer)`)

##### `successScore <integer>`

A score which will be calculated if the validation passes. The score should be
a negative or positive integer (including zero).
(API `setSuccessScore(Integer)` or `withSuccessScore(Integer)`)

##### `naScore <integer>`

A score which will be calculated if the value is missing (there is no such data element).
The score should be a negative or positive integer (including zero).
(API `setNaScore(Integer)` or `withNaScore(Integer)`)

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
  - pattern: ^(DE-\d+|DE-MUS-\d+|http://id.zdb-services.de\w+|\d{8}|oai\d{13})$
    failureScore: -3
    naScore: 0
    id: 2.2
  - pattern: ^(DE-\d+|DE-MUS-\d+|http://id.zdb-services.de\w+)$
    successScore: 6
    naScore: 0
    id: 2.4
  - pattern: ^http://id.zdb-services.de\w+$
    successScore: 3
    naScore: 0
    id: 2.5
  - pattern: ^http://d-nb.info/gnd/\w+$
    successScore: 3
    naScore: 0
    id: 2.6
```

##### `hidden <boolean>`

(since v0.9.0)

If the rule is hidden it will be calculated, but its output will not be present
in the overall output. It can be used together width dependencies to set up
compound conditions.  

##### `skip <boolean>`

(since v0.9.0)

This rule prevents a particular rule to be part of calculation. This could be
useful in development phase when you started to create a complex rule but
haven't yet finished, or when the execution of the rule takes long time (e.g.
checking content type or image dimension), and temporary you would like to
turn it off.

##### `debug <boolean>`:

(since v0.9.0)

If set, the tool logs the rule identifier, its value and the rule's result.

#### Set rules via Java API 

```Java
Schema schema = new BaseSchema()
  .setFormat(Format.CSV)
  .addField(
    new DataELement("title", "title")
      .setRule(
        new Rule()
          .withDisjoint("description")
      )
  )
  .addField(
    new DataELement("url", "url")
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

In both cases we defined two fields. `title` has one constraints: it should
not be equal to the value of `description` field (which is masked out from
the example). Note: if this hypothetical `description` field is not available
the API drops an error message into the log. `url` should have one and only
one instance, and its value should start with "http://" or "https://".

As you can see there are two types of setters in the API: `setSomething` and 
`withSomething`. The difference is that `setSomething` returs with void, but
`withSomething` returns with the Rule object, so you can use it in a chain
such as `new Rule().withMinCount(1).withMaxCount(3)` 
(while `new Rule().setMinCount(1).setMaxCount(3)` doesn't work, because
`setMinCount()` does returns nothing, and one can not apply `setMaxCount(3)`
on that nothing).

## Defining MeasurementConfiguration with a configuration file

MeasurementConfiguration can be created from JSON or YAML configuration files
with the following methods:

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

* `fieldExtractorEnabled`: Flag whether or not the field extractor is enabled
  (default: false). (API calls: setters: `enableFieldExtractor()`,
  `disableFieldExtractor()`, getter: `isFieldExtractorEnabled()`)
* `fieldExistenceMeasurementEnabled`: Flag whether or not run the field
  existence measurement (default: true). (API calls: setters:
  `enableFieldExistenceMeasurement()`, `disableFieldExistenceMeasurement()`,
  getter: `isFieldExistenceMeasurementEnabled()`)
* `fieldCardinalityMeasurementEnabled`: Flag whether or not run the field
  cardinality measurement (default: true). (API calls: setters:
  `enableFieldCardinalityMeasurement()`, `disableFieldCardinalityMeasurement()`,
  getter: `isFieldCardinalityMeasurementEnabled()`)
* `completenessMeasurementEnabled`: Flag whether or not run the completeness
  measurement (default: true). (API calls: setters: `enableCompletenessMeasurement()`,
  `disableCompletenessMeasurement()`, getter: `isCompletenessMeasurementEnabled()`)
* `tfIdfMeasurementEnabled`: Flag whether or not run the uniqueness measurement
  (default: false). (API calls: setters: `enableTfIdfMeasurement()`, `disableTfIdfMeasurement()`,
  getter: `isTfIdfMeasurementEnabled()`)
* `problemCatalogMeasurementEnabled`: Flag whether or not run the problem catalog (default: false).
  (API calls: setters: `enableProblemCatalogMeasurement()`, `disableProblemCatalogMeasurement()`,
  getter: `isProblemCatalogMeasurementEnabled()`)
* `ruleCatalogMeasurementEnabled`: Flag whether or not run the rule catalog (default: false).
  (API calls: setters: `enableRuleCatalogMeasurement()`, `disableRuleCatalogMeasurement()`,
  getter: `isRuleCatalogMeasurementEnabled()`)
* `languageMeasurementEnabled`: Flag whether or not run the language detector (default: false).
  (API calls: setters: `enableLanguageMeasurement()`, `disableLanguageMeasurement()`,
  getter: `isLanguageMeasurementEnabled()`)
* `multilingualSaturationMeasurementEnabled`: Flag whether or not run the multilingual
  saturation measurement (default: false).  (API calls: setters:
  `enableMultilingualSaturationMeasurement()`, `disableMultilingualSaturationMeasurement()`,
  getter: `isMultilingualSaturationMeasurementEnabled()`)
* `collectTfIdfTerms`: Flag whether or not collect TF-IDF terms in uniqueness
  measurement (default: false). (API calls: setters: `collectTfIdfTerms(boolean)`,
  getter: `collectTfIdfTerms()`)
* `uniquenessMeasurementEnabled`: Flag whether or not to run in uniqueness
  measurement (default: false). (API calls: setters: `enableUniquenessMeasurement()`,
  `disableUniquenessMeasurement()`, getter: `isUniquenessMeasurementEnabled()`)
* `completenessCollectFields`: Flag whether or not run missing/empty/existing field
  collection in completeness (default: false). (API calls: setters:
  `enableCompletenessFieldCollecting(boolean)`,
  getter: `isCompletenessFieldCollectingEnabled()`)
* `saturationExtendedResult`: Flag whether or not to create extended result in
  multilingual saturation calculation (default: false).
  (API calls: setters: `enableSaturationExtendedResult(boolean)`,
  getter: `isSaturationExtendedResult()`)
* `checkSkippableCollections`: Flag whether or not to check skipable collections (default: false).
  (API calls: setters: `enableCheckSkippableCollections(boolean)`,
  getter: `isCheckSkippableCollections()`)
* `String solrHost`: The hostname of the Solr server.
  (API calls: setters: `setSolrHost(String)`, `withSolrHost(String):MeasurementConfiguration`,
  getter: `getSolrHost()`)
* `String solrPort`: The port of the Solr server.
  (API calls: setters: `setSolrPort(String)`, `withSolrPort(String):MeasurementConfiguration`,
  getter: `getSolrPort()`)
* `String solrPath`: The path part of of the Solr server URL.
  (API calls: setters: `setSolrPath(String)`, `withSolrPath(String):MeasurementConfiguration`,
  getter: `getSolrPath()`)
* `String onlyIdInHeader`: the Rules should return the ID in the header instead of a
  generated value. (API calls: setters: `setOnlyIdInHeader(boolean)`,
  `withOnlyIdInHeader(boolean):MeasurementConfiguration`,
  getter: `isOnlyIdInHeader()`)

## Using an experimental version
  
If you want to try an experimental version (which has `SNAPSHOT` in its
version name), you have to enable the retrieval of those versions in the `pom.xml` file:

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
    <version>0.9-SNAPSHOT</version>
  </dependency>
</dependencies>
```

Thanks to Miel Vander Sande ([@mielvds](https://github.com/mielvds)) for the hint!

## More info

Since version 0.8-SNAPSHOT the project requires Java 11.

For the usage and implementation of the API see https://github.com/pkiraly/europeana-qa-api.

Java doc for the actual development version of the API: https://pkiraly.github.io/metadata-qa-api.

[![Build Status](https://travis-ci.org/pkiraly/metadata-qa-api.svg?branch=main)](https://travis-ci.com/pkiraly/metadata-qa-api)
[![Coverage Status (@coveralls)](https://coveralls.io/repos/github/pkiraly/metadata-qa-api/badge.svg?branch=main)](https://coveralls.io/github/pkiraly/metadata-qa-api?branch=main)
[![Coverage Status (@codecov)](https://codecov.io/gh/pkiraly/metadata-qa-api/branch/main/graph/badge.svg?token=HLLGXJSVZL)](https://codecov.io/gh/pkiraly/metadata-qa-api)
[![javadoc](https://javadoc.io/badge2/de.gwdg.metadataqa/metadata-qa-api/javadoc.svg)](https://javadoc.io/doc/de.gwdg.metadataqa/metadata-qa-api)
[![Maven Central](https://img.shields.io/maven-central/v/de.gwdg.metadataqa/metadata-qa-api)](https://search.maven.org/artifact/de.gwdg.metadataqa/metadata-qa-api)
