import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

public class Image {

  protected int width;
  protected int height;
  protected int[][] redPixels;
  protected int[][] greenPixels;
  protected int[][] bluePixels;
  protected BufferedImage image;

  public Image(String path) throws IOException {
    File file = new File(path);
    image = ImageIO.read(file);
    width=image.getWidth();
    height=image.getHeight();
    redPixels = new int[width][height];
    greenPixels = new int[width][height];
    bluePixels = new int[width][height];
  }

  public void readPixels() {
    for (int i = 0;i<width; i++) {
      for (int j=0;j<height; j++) {
        Color c = new Color(image.getRGB(i, j));
        redPixels[i][j]=c.getRed();
        greenPixels[i][j]=c.getGreen();
        bluePixels[i][j]=c.getBlue();
      }
    }
  }

  public int[][] getRedPixels() {
    return redPixels;
  }

  public int[][] getGreenPixels() {
    return greenPixels;
  }

  public int[][] getBluePixels() {
    return bluePixels;
  }

}
