package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	List<Term> terms = new ArrayList<>();
	Term clustroid;

	public Cluster(Term term) {
		terms.add(term);
		clustroid = term;
	}

	public Cluster addTerm(Term term) {
		terms.add(term);
		// setClustroid();
		return this;
	}

	public void setClustroid() {
		if (terms.size() == 1)
			clustroid = terms.get(0);
		else {
			double min = 0;
			for (int i = 0; i < terms.size(); i++) {
				double sum = 0;
				for (int j = i + 1; j < terms.size(); j++) {
					sum += terms.get(i).getDistance(terms.get(j));
				}
				if (min == 0 || min > sum) {
					min = sum;
					clustroid = terms.get(i);
				}
			}
		}
	}

	public Term getClustroid() {
		return clustroid;
	}

	public double getDistance(Cluster other) {
		if (clustroid == null)
			setClustroid();
		if (other.getClustroid() == null)
			other.setClustroid();
		return clustroid.getDistance(other.getClustroid());
	}

	public List<Term> getTerms() {
		return terms;
	}

	public Cluster merge(Cluster other) {
		System.err.println(String.format("merge: %s + %s",
			StringUtils.join(terms), StringUtils.join(other.getTerms())));

		for (Term term : other.getTerms()) {
			addTerm(term);
		}
		setClustroid();
		return this;
	}

	/**
	 * maximum distance between points in the cluster
	 * @return
	 */
	public double getDiameter() {
		return 0.0;
	}

	/**
	 * maximum distance between points from the centroid (or clustroid)
	 * @return
	 */
	public double getRadius() {
		return 0.0;
	}

	/**
	 * Density = number of points per unit volume
	 * e.g. the number of points / diameter or radius (or radius^2, radius^3)
	 * @return
	 */
	public double getDensity() {
		return 0.0;
	}



	@Override
	public String toString() {
		return "Cluster{" +
			"terms=" + terms +
			", clustroid=" + clustroid +
			'}';
	}
}
