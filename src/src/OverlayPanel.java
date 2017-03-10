package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by cclapp on 30/01/2017.
 */
public class OverlayPanel extends JPanel {

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            //getting image
            //Image img1 = Toolkit.getDefaultToolkit().getImage("turnRight.png");
            //also another option:
            try {

                Image turnLeftImage = ImageIO.read(this.getClass().getResource("turnRight.png"));
                //Image speedLimitImage = ImageIO.read(this.getClass().getResource("speedLimit.png"));

                int width = turnLeftImage.getWidth(null);
                int height = turnLeftImage.getHeight(null);
                g2d.drawImage(turnLeftImage, 200, 200, width, height, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //g2d.setColor(Color.RED);
            //g2d.fillOval(0, 0, 30, 30);
            //g2d.drawOval(0, 50, 30, 30);
            //g2d.fillRect(50, 0, 30, 30);
            //g2d.drawRect(50, 50, 30, 30);

            //g2d.drawString();


            //g2d.drawImage(img1,10,10,this); //arbitrary size


            //g2d.draw(new Ellipse2D.Double(0, 100, 30, 30));
        }

    public void moveIt()
    {
        while (true)
        {

            try
            {
                Thread.sleep(10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            this.repaint();
        }
    }


    }
