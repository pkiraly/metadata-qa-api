# Metadata Quality Assessment Framework API Changelog

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
