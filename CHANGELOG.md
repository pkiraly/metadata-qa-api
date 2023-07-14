# Metadata Quality Assessment Framework API Changelog

## v0.9.3 (2023-07-14)

Important API change:
- in `pattern` the tool use find() instead of matches(), so one should not specify a complex pattern from the beginning to the end

The release contains dependency updates.

The artefacts of the release are available in Maven Central as well: https://central.sonatype.dev/artifact/de.gwdg.metadataqa/metadata-qa-api/0.9.3

## v0.9.2 (2023-06-09)

- Set AND, NOT, OR NA if the data is NA.

## v0.9.1 (2023-05-16)

- rename PathCache to Selector
- adding constants for using in QA catalogue
- update dependencies and adapt code to the API changes

## v0.9.0 (2022-11-21)

This release contains the results of two important developments adding a command line interface created by 
Miel Vander Sande (@mielvds) and applying the framework on the data of Deutsche Digitale Bibliothek. These two 
developments made the tool more robust, and more flexible so became applicable to different situations.

To use the command line interface, download mqa and metadata-qa-api-0.9.0-shaded.jar, and follow the suggestions of the README.md file.

new rules:
- unique: checks if the value of the field is unique
- dependencies: checks if other rules has already checked and passed
- dimension: checks if a linked image fits to some dimension constraints (unit in pixel)
- hidden: if the rule is hidden it will be calculated, but its output will not be present in the overall output
- skip: prevents a particular rule to be part of calculation
- debug: log the rule ID, value and result

The schema is called MQA Schema. Some instances are available in the metadata-qa-ddb repository: https://github.com/pkiraly/metadata-qa-ddb/tree/main/src/main/resources

Important API changes:
- the JsonBranch class has been renamed to DataElement
- the OaiPmhXPath class has been renamed to XPathWrapper

The artefacts of the release are available in Maven Central as well: https://central.sonatype.dev/artifact/de.gwdg.metadataqa/metadata-qa-api/0.9.0

## v0.8 (2022-03-16)

- improve the rule checking mechanisms: adding IDs, minWords and maxWords checkers, content type checker, optimizing OR checker and range rules, successScore and failureScore
- improve rule output
- new schema parameters: indexField, unique, inactive
- new measurement configuration parameters: onlyIdInHeader, indexer, AnnotationCalculator
- field extractor is independent from recordId and is enabled only if there are extractable fields in the schema
- improve documentation: a section about quality diemnsions
- improving XML namespace handling.
- better CSV support
- changing to Java 11
- internal refactoring and decoupling of classes
- adding several tools to developer workflow: sonarcloud.io, coveralls, codecov, GitHub action
