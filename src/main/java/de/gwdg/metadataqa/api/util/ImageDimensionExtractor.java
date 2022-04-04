package de.gwdg.metadataqa.api.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDimensionExtractor {
  public static DimensionDao extractRemote(String url) throws IOException {
    URL urlObj = new URL(url);
    URLConnection conn = urlObj.openConnection();
    InputStream in = conn.getInputStream();
    BufferedImage img = ImageIO.read(in);
    if (img == null) {
      return null;
    }
    return new DimensionDao(img.getWidth(), img.getHeight());
  }

  public static DimensionDao extractLocal(String filename) throws IOException {
    BufferedImage img = ImageIO.read(new File(filename));
    return new DimensionDao(img.getWidth(), img.getHeight());
  }
}
