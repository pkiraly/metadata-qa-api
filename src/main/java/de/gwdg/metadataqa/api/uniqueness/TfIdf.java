package de.gwdg.metadataqa.api.uniqueness;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdf {

	private String term;
	private int tf;
	private int df;
	private double tfIdf;

	public TfIdf(String term, int tf, int df, double tfIdf) {
		this.term = term;
		this.tf = tf;
		this.df = df;
		this.tfIdf = tfIdf;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	public double getTfIdf() {
		return tfIdf;
	}

	public void setTfIdf(double tfIdf) {
		this.tfIdf = tfIdf;
	}

	@Override
	public String toString() {
		return term + "(tf=" + tf + ", df=" + df + ", tfIdf=" + tfIdf + ')';
	}
}
