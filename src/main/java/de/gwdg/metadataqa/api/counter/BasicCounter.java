package de.gwdg.metadataqa.api.counter;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class BasicCounter {

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
