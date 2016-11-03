package de.gwdg.metadataqa.api.util;

import com.jayway.jsonpath.Configuration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
public class FileUtils {
	private static ClassLoader classLoader = FileUtils.class.getClassLoader();

	public static List<String> readLines(String fileName)
			throws URISyntaxException, IOException {
		URL url = classLoader.getResource(fileName);
		if (url == null)
			throw new IOException(String.format("File %s in not available", fileName));
		Path path = Paths.get(url.toURI());
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
