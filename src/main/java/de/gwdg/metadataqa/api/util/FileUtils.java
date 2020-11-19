package de.gwdg.metadataqa.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
public abstract class FileUtils {

  private static final ClassLoader classLoader = FileUtils.class.getClassLoader();

  private FileUtils() {}

  public static List<String> readLinesFromResource(String fileName)
      throws URISyntaxException, IOException {
    return Files.readAllLines(getPath(fileName), StandardCharsets.UTF_8);
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

  public static String readFromUrl(String url) throws IOException {
    try (InputStream is = new URL(url).openStream()) {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      return readAll(rd);
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
