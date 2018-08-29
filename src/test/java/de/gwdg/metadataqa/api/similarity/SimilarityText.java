package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.text.similarity.*;
import org.junit.Test;

import java.util.*;

/**
 * http://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/package-summary.html
 */
public class SimilarityText {

	// @Test
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
					"%s vs %s: Jaro-Winkler: %f, Jaccard: %f, levenshtein: %d, levenshtei2: %s",
					a, b,
					jaroWinkler.apply(a, b),
					jaccard.apply(a, b),
					levenshtein.apply(a, b),
					LevenshteinDetailed.apply(a, b).getDistance()
				));
			}
		}

		while (clusters.size() > 1) {
			System.err.println("###");
			double max = 0;
			Cluster[] maxKey = null;
			for (int i = 0; i < clusters.size(); i++) {
				Cluster a = clusters.get(i);
				for (int j = i+1; j < clusters.size(); j++) {
					Cluster b = clusters.get(j);
					// System.err.println("get distance: " + a + " vs " + b);
					double dist = a.getDistance(b);
					if (dist > max) {
						max = dist;
						maxKey = new Cluster[]{a, b};
					}
				}
			}
			maxKey[0].merge(maxKey[1]);
			clusters.remove(maxKey[1]);
			System.err.println("size: " + clusters.size());
		}
	}
}
