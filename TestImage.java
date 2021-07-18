import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TestImage extends Image {

  public TestImage(String path) throws IOException {
    super(path);
    System.out.println("width:"+width);
    System.out.println("height:"+height);
    writePixels();
  }

  public void writePixels() throws IOException {
    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < width; i++) {
     for (int j = 0; j < height; j++) {
        int rgb = redPixels[i][j];
        rgb = (rgb << 8) + greenPixels[i][j];
        rgb = (rgb << 8) + bluePixels[i][j];
        newImage.setRGB(i, j, rgb);
     }
   }
   File outputFile = new File("Picture/Test.jpg");
   ImageIO.write(newImage, "jpeg", outputFile);
  }

  public void setRGBvalues(int[][] redPixels, int[][] greenPixels, int[][] bluePixels) {
    this.redPixels = redPixels;
    this.greenPixels = greenPixels;
    this.bluePixels = bluePixels;
  }
}
