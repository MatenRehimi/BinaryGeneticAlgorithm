import java.io.IOException;

public class GoalImage extends Image {

  public GoalImage(String path) throws IOException {
    super(path);
    readPixels();
  }
}
