package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.interfaces.Observer;
import de.gwdg.metadataqa.api.interfaces.Observable;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.schema.ProblemCatalogSchema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Problem catalogue
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class ProblemCatalog extends BaseProblemCatalog<Double> implements Serializable, Observable {

  private static final long serialVersionUID = -8099737126539035900L;

  private static final String CALCULATOR_NAME = "problemCatalog";

  private final List<Observer> problems = new ArrayList<>();
  private Selector cache;
  private ProblemCatalogSchema schema;

  public ProblemCatalog(ProblemCatalogSchema schema) {
    this.schema = schema;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public void addObserver(Observer observer) {
    problems.add(observer);
  }

  @Override
  public void deleteObserver(Observer observer) {
    if (problems.contains(observer)) {
      problems.remove(observer);
    }
  }

  @Override
  public void notifyObservers(FieldCounter<Double> fieldCounter) {
    for (Observer observer : problems) {
      observer.update(cache, fieldCounter);
    }
  }

  @Override
  public List<MetricResult> measure(Selector cache) {
    this.cache = cache;
    FieldCounter<Double> fieldCounter = new FieldCounter<>();
    notifyObservers(fieldCounter);
    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), fieldCounter));
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (Observer observer : problems) {
      headers.add(observer.getHeader());
    }
    return headers;
  }

  public ProblemCatalogSchema getSchema() {
    return schema;
  }
}
