package de.gwdg.metadataqa.api;

import com.jayway.jsonpath.Configuration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TestUtils {

	public static List<String> readLines(String fileName)
			throws URISyntaxException, IOException {
		Path path = Paths.get(TestUtils.class.getClassLoader().getResource(fileName).toURI());
		return Files.readAllLines(path, Charset.defaultCharset());
	}

	public static String readFirstLine(String fileName)
			throws URISyntaxException, IOException {
		List<String> lines = readLines(fileName);
		return lines.get(0);
	}

	public static String readContent(String fileName)
			throws URISyntaxException, IOException {
		return StringUtils.join(readLines(fileName), "");
	}

	public static Object buildDoc(String fileName) throws URISyntaxException, IOException {
		String jsonString = readFirstLine(fileName);
		Object jsonDoc = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
		return jsonDoc;
	}
}
