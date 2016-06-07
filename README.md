# Metadata Quality Assurance Framework API

This project is the central piece of the Metadata Quality Assurance Framework, every other project is 
built on top of it. It provides a general framework for measuring metadata quality in different 
digital collections.

If you want to implement it to your collection you have to define a schema, which is a kind of
presentation of an existing metadata schema, and configure the basic facade, which will run the calculation.

```Java
CalculatorFacade calculator = new CalculatorFacade();
// do some configuration with the accessor of calculator Facade
for (String jsonRecord : jsonRecords) {
    try {
        String csv = calculator.measure(jsonRecord);
        // save csv
    } catch (InvalidJsonException e) {
        // handle exception
    }
}

```
