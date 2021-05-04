package de.gwdg.metadataqa.api.similarity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Cluster object.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Cluster implements Iterable<Term> {
  private List<Term> terms = new ArrayList<>();
  private boolean isActive = true;

  public Cluster(Term term) {
    terms.add(term);
  }

  @Override
  public Iterator<Term> iterator() {
    return terms.iterator();
  }

  public Cluster addTerm(Term term) {
    terms.add(term);
    return this;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public List<Term> getTerms() {
    return terms;
  }

  public Cluster merge(Cluster other) {
    for (Term term : other.getTerms()) {
      addTerm(term);
    }

    return this;
  }

  public boolean isSimilarTo(Cluster other, double treshold) {
    var isSimilar = false;
    for (Term term : terms) {
      for (Term otherTerm : other.getTerms()) {
        if (term.hasDistance(otherTerm)
            && term.getDistance(otherTerm) > treshold) {
          isSimilar = true;
        } else {
          isSimilar = false;
          break;
        }
      }
      if (!isSimilar) {
        break;
      }
    }

    return isSimilar;
  }

  public List<String> getTermList() {
    List<String> termList = new ArrayList<>();
    for (Term term : terms) {
      termList.add(term.value);
    }
    return termList;
  }

  @Override
  public String toString() {
    return "Cluster{"
      + "terms=" + terms
      + ", active=" + isActive
      + "}";
  }

}
