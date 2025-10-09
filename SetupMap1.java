
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * This class draws the visuals for the first map.
 * 
 * And creates the backgroun logic for buttons? Maybe? idk.
 */

public class SetupMap1 {
    JFrame frame;
    JPanel panel;
    BufferedImage backgroundPrime;

    /**
     * Constructor module that initializes SetupMap1 object in the specific frame and panel.
     * @param frame
     *     JFrame in which the map is being setup.
     * @param panel
     *     Right now it is here because it is there in the tutorial. Probably, has to do with
     *     the layout or something.
     */

    SetupMap1(JFrame frame, JPanel panel) {
        this.frame = frame;
        this.panel = panel;
    }

    /**
     * This method does the reading of the picture and the drawing of the picture.
     * (Well, If it works...)
     */
    public void setUp() {
        try {
            backgroundPrime = ImageIO.read(new File("assets/map1.png"));

            System.out.println("Success? Idk, the pic is read");

            JLabel background = new JLabel(new ImageIcon(backgroundPrime));
            background.setBounds(0, 0, 800, 600);
            panel.add(background);
            
            System.out.println("Yeah, kinda looks like success, image added?!?!");

        } catch (Exception e) {
            System.out.println("Womp womp");
            System.out.println(e);
        }
        
    }
    

    
}
