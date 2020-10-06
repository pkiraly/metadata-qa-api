package de.gwdg.metadataqa.api.util;

import com.jayway.jsonpath.Configuration;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public final class FileUtils {

  private static ClassLoader classLoader = FileUtils.class.getClassLoader();

  public static List<String> readLinesFromResource(String fileName)
      throws URISyntaxException, IOException {
    return Files.readAllLines(getPath(fileName), Charset.defaultCharset());
  }

  public static Path getPath(String fileName) throws IOException, URISyntaxException {
    URL url = classLoader.getResource(fileName);
    if (url == null) {
      throw new IOException(String.format("File %s is not available", fileName));
    }
    return Paths.get(url.toURI());
  }

  public static String readFirstLineFromResource(String fileName)
      throws URISyntaxException, IOException {
    List<String> lines = readLinesFromResource(fileName);
    return lines.get(0);
  }

  public static String readContentFromResource(String fileName)
      throws URISyntaxException, IOException {
    return StringUtils.join(readLinesFromResource(fileName), "");
  }

  public static Object buildDoc(String fileName) throws URISyntaxException, IOException {
    String jsonString = readFirstLineFromResource(fileName);
    Object jsonDoc = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
    return jsonDoc;
  }

  public static String readFromUrl(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      return readAll(rd);
    } finally {
      is.close();
    }
  }

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static List<String> readLinesFromFile(String fileName) throws IOException {
    List<String> lines = new ArrayList<>();
    Scanner scanner = new Scanner(new File(fileName));
    while (scanner.hasNextLine()) {
      lines.add(scanner.nextLine());
    }
    scanner.close();
    return lines;
  }

  public static String readFirstLineFromFile(String fileName) throws IOException {
    List<String> lines = FileUtils.readLinesFromFile(fileName);
    return lines.get(0);
  }
}
