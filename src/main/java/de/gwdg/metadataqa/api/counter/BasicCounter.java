package de.gwdg.metadataqa.api.counter;

import java.io.Serializable;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class BasicCounter implements Serializable {

  private static final long serialVersionUID = 5660662598713202430L;

  private double total = 0.0;
  private double instance = 0.0;
  private double result = 0.0;

  public BasicCounter() {
  }

  public BasicCounter(double total) {
    this.total = total;
  }

  public void increaseTotal() {
    total++;
  }

  public void increaseInstance() {
    instance++;
  }

  public void decreaseTotal(int decrease) {
    total -= decrease;
  }

  public void decreaseInstance(int decrease) {
    instance -= decrease;
  }

  public void calculate() {
    result = (instance / total);
  }

  public double getTotal() {
    return total;
  }

  public int getTotalAsInt() {
    return ((Double) total).intValue();
  }

  public double getInstance() {
    return instance;
  }

  public double getResult() {
    return result;
  }

  @Override
  public String toString() {
    calculate();
    return "BasicCounter{"
      + "total=" + total
      + ", instance=" + instance
      + ", result=" + result
      + '}';
  }
}
