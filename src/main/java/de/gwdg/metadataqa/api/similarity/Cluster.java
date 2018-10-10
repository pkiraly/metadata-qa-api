package de.gwdg.metadataqa.api.similarity;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private List<Term> terms = new ArrayList<>();
	private boolean isActive = true;

	public Cluster(Term term) {
		terms.add(term);
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
		for (Term term : other.getTerms())
			addTerm(term);

		return this;
	}

	public boolean isSimilarTo(Cluster other, double treshold) {
		boolean isSimilar = false;
		for (Term term : terms) {
			for (Term otherTerm : other.getTerms()) {
				/*
				System.err.println(
					String.format(
						"%s - %s: %f (%s)", term.value, otherTerm.value,
						term.getDistance(otherTerm),
						(term.getDistance(otherTerm) > treshold))
				);
				*/
				if (term.getDistance(otherTerm) > treshold)
					isSimilar = true;
				else {
					isSimilar = false;
					break;
				}
			}
			if (!isSimilar)
				break;
		}
		// System.err.println("->" + isSimilar);

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
		return "Cluster{" +
			"terms=" + terms +
			", active=" + isActive +
			'}';
	}

}
