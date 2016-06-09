package de.gwdg.metadataqa.api.uniqueness;

/**
 * A value object holding information about TF-IDF elements.
 * 
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdf {

	/**
	 * The indexed term
	 */
	private String term;

	/**
	 * Term frequency (the number of times the term occurs in the field).
	 */
	private int tf;

	/**
	 * Document frequency (the number of documents the term occurs in).
	 */
	private int df;

	/**
	 * The TF-IDF number read from Apache Solr.
	 */
	private double tfIdf;

	/**
	 * Creates the TF-IDF object
	 * @param term
	 *    The indexed term
	 * @param tf
	 *    Term frequency (the number of times the term occurs in the field).
	 * @param df
	 *    Document frequency (the number of documents the term occurs in).
	 * @param tfIdf
	 *    The TF-IDF number read from Apache Solr.
	 */
	public TfIdf(String term, int tf, int df, double tfIdf) {
		this.term = term;
		this.tf = tf;
		this.df = df;
		this.tfIdf = tfIdf;
	}

	/**
	 * Returns the indexed term
	 * @return
	 *    The indexed term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Set the indexed term
	 * @param term
	 *    The indexed term
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Returns term frequency (the number of times the term occurs in the field).
	 * @return
	 *   Term frequency (the number of times the term occurs in the field).
	 */
	public int getTf() {
		return tf;
	}

	/**
	 * Sets term frequency (the number of times the term occurs in the field).
	 * @param tf
	 *    Term frequency (the number of times the term occurs in the field).
	 */
	public void setTf(int tf) {
		this.tf = tf;
	}

	/**
	 * Returns the document frequency (the number of documents the term occurs in).
	 * @return
	 *    Document frequency (the number of documents the term occurs in).
	 */
	public int getDf() {
		return df;
	}

	/**
	 * Sets document frequency (the number of documents the term occurs in).
	 * @param df
	 *    Document frequency (the number of documents the term occurs in).
	 */
	public void setDf(int df) {
		this.df = df;
	}

	/**
	 * Returns the TF-IDF number read from Apache Solr.
	 * @return
	 *    The TF-IDF number read from Apache Solr.
	 */
	public double getTfIdf() {
		return tfIdf;
	}

	/**
	 * Sets the TF-IDF number read from Apache Solr.
	 * @param tfIdf
	 *    The TF-IDF number read from Apache Solr.
	 */
	public void setTfIdf(double tfIdf) {
		this.tfIdf = tfIdf;
	}

	/**
	 * Returns a string representation of the object
	 * @return
	 *   The string representation
	 */
	@Override
	public String toString() {
		return term + "(tf=" + tf + ", df=" + df + ", tfIdf=" + tfIdf + ')';
	}
}
