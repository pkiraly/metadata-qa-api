package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * http://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/package-summary.html
 */
public class SimilarityText {

	@Test
	public void clustering() {
		List<String> patterns = Arrays.asList("0001", "0011", "0000", "1001");
		double treshold = 0.70;
		Clustering clustering = new Clustering(patterns, treshold);
		List<List<String>> clusters = clustering.getClusters();

		assertEquals(Arrays.asList("0011", "0001", "1001"), clusters.get(0));
		assertEquals(Arrays.asList("0000"), clusters.get(1));
	}

	@Test
	public void clustering2() {
		List<String> patterns = Arrays.asList(
			"Sosztakovics", "Rubens", "Shostakovic", "1001"
		);
		double treshold = 0.70;
		Clustering clustering = new Clustering(patterns, treshold);
		List<List<String>> clusters = clustering.getClusters();

		assertEquals(Arrays.asList("Shostakovic", "Sosztakovics"), clusters.get(0));
		assertEquals(Arrays.asList("Rubens"), clusters.get(1));
		assertEquals(Arrays.asList("1001"), clusters.get(2));
	}

	@Test
	public void JaroWinkler() {
		JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();
		JaccardDistance jaccard = new JaccardDistance();
		LevenshteinDistance levenshtein = new LevenshteinDistance();
		LevenshteinDetailedDistance LevenshteinDetailed = new LevenshteinDetailedDistance();

		List<String> patterns = Arrays.asList("0001", "0011", "0000", "1001");
		Map<List<String>, Double> atomicDistance = new HashMap<>();
		List<Cluster> clusters = new ArrayList<>();
		for (int i = 0; i < patterns.size(); i++) {
			String a = patterns.get(i);
			Term at = new Term(a);
			clusters.add(new Cluster(at));
			for (int j = i+1; j < patterns.size(); j++) {
				String b = patterns.get(j);
				Term bt = new Term(b);
				double distance = jaroWinkler.apply(a, b);
				atomicDistance.put(Arrays.asList(a, b), distance);
				at.setDistance(bt, distance);
				bt.setDistance(at, distance);
				System.err.println(String.format(
					"%s vs %s: Jaro-Winkler: %f, Jaccard: %f, levenshtein: %d, levenshtein2: %s",
					a, b,
					jaroWinkler.apply(a, b),
					jaccard.apply(a, b),
					levenshtein.apply(a, b),
					LevenshteinDetailed.apply(a, b).getDistance()
				));
			}
		}
	}
}
