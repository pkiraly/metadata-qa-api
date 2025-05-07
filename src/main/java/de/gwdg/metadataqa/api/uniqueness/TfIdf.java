package de.gwdg.metadataqa.api.uniqueness;

import java.io.Serializable;

/**
 * A value object holding information about TF-IDF elements.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdf implements Serializable {

  private static final long serialVersionUID = 729655831135477777L;

  /**
   * The indexed term.
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
  private double tfIdfScore;

  /**
   * Creates the TF-IDF object.
   *
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
    this.tfIdfScore = tfIdf;
  }

  /**
   * Returns the indexed term.
   *
   * @return
   *    The indexed term
   */
  public String getTerm() {
    return term;
  }

  /**
   * Returns term frequency (the number of times the term occurs in the field).
   *
   * @return
   *   Term frequency (the number of times the term occurs in the field).
   */
  public int getTf() {
    return tf;
  }

  /**
   * Returns the document frequency (the number of documents the term occurs in).
   *
   * @return
   *    Document frequency (the number of documents the term occurs in).
   */
  public int getDf() {
    return df;
  }

  /**
   * Returns the TF-IDF number read from Apache Solr.
   *
   * @return
   *    The TF-IDF number read from Apache Solr.
   */
  public double getTfIdf() {
    return tfIdfScore;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return
   *   The string representation
   */
  @Override
  public String toString() {
    return term + "(tf=" + tf + ", df=" + df + ", tfIdf=" + tfIdfScore + ')';
  }
}
