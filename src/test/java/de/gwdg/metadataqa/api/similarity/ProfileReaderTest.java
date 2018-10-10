package de.gwdg.metadataqa.api.similarity;

import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class ProfileReaderTest {

	private List<String> canonicalFieldList;
	private List<String> profiles;

	@Before
	public void setUp() {
		try {
			canonicalFieldList = Arrays.asList(FileUtils.readLines("profiles/d988-fields.csv").get(0).split(";"));
			profiles = FileUtils.readLines("profiles/d988-profiles.csv");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExtraction() {
		ProfileReader profileReader = new ProfileReader(canonicalFieldList, profiles);
		Map<List<ProfileReader.Row>, Double> sortedClusters = profileReader
			.buildCluster();

		sortedClusters.entrySet().stream().forEach((cluster) -> {
			int i = profileReader.getNext();
			int sum = profileReader.count(cluster.getKey());
			System.err.printf("#%d=%d\n", i, sum);
			cluster.
					getKey().
					forEach((row) -> {
						System.err.printf("%d,%s\n", i, row.asCsv());
					});
				// System.err.printf("=%.2f%%\n", cluster.getValue());
			});
	}
}
