import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener {

  private Timer timer;
  private int generationNumber;
  private JLabel testImageLabel;
  private JLabel goalImageLabel;
  private GoalImage goalImage;
  private TestImage testImage;
  private BGA algorithm;

    public GUI() throws IOException {
      super("BGA algorithm");
      createImages();
      display();
      generationNumber=1;
      algorithm = new BGA(goalImage.getRedPixels(),goalImage.getGreenPixels(),goalImage.getBluePixels(),4);
    }

    public void display() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());
      timer = new Timer(200,this);
      setSize(900,693);
      setLocationRelativeTo(null);
      setResizable(false);
      goalImageLabel = new JLabel(new ImageIcon("Picture/Goal.jpg"));
      testImageLabel = new JLabel(new ImageIcon("Picture/Test.jpg"));
      add(goalImageLabel,BorderLayout.WEST);
      add(testImageLabel,BorderLayout.EAST);
      setVisible(true);
      pack();
      timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (generationNumber == 100) timer.stop();
      if (e.getSource() == timer) {
        setTitle("BGA algorithm - generation:" + generationNumber);
        ImageIcon testImageIcon = new ImageIcon("Picture/Test.jpg");
        testImageIcon.getImage().flush();
        testImageLabel.setIcon(testImageIcon);
        try {
          //create better chromosomes
          System.out.println("generation:"+generationNumber);
          System.out.println("");
          algorithm.iteration();
          testImage.setRGBvalues(algorithm.getRedPixels(),algorithm.getGreenPixels(),algorithm.getBluePixels());
          testImage.writePixels();
        }catch(Exception error) {
          System.out.println(error);
          System.out.println("problems");
          System.exit(0);
        }
        generationNumber++;
      }
    }



    public void createImages() throws IOException {
      goalImage = new GoalImage("Picture/Goal.jpg");
      testImage = new TestImage("Picture/Goal.jpg");
    }

}
