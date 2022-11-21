package de.gwdg.metadataqa.api.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDimensionExtractor {
  public static DimensionDao extractRemote(String url) throws IOException {
    URL urlObj = new URL(url);

    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

    int timeout = 1000;
    urlConnection.setConnectTimeout(timeout);
    urlConnection.setReadTimeout(timeout);
    urlConnection.connect();
    int responseCode = urlConnection.getResponseCode();
    if (responseCode == 200) {
      InputStream in = urlConnection.getInputStream();
      BufferedImage img = ImageIO.read(in);
      if (img == null) {
        return null;
      }
      return new DimensionDao(img.getWidth(), img.getHeight());
    } else if (responseCode == 301 || responseCode == 303) {
      String location = urlConnection.getHeaderField("Location");
      return extractRemote(location);
    }
    return null;
  }

  public static DimensionDao extractLocal(String filename) throws IOException {
    BufferedImage img = ImageIO.read(new File(filename));
    return new DimensionDao(img.getWidth(), img.getHeight());
  }
}
