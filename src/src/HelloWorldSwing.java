package src; /**
 * Created by cclapp on 30/01/2017.
 */
import javax.swing.*;
import java.awt.*;


public class HelloWorldSwing {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        frame.setUndecorated(true);
        frame.setBackground(new Color(1.0f,1.0f,1.0f,0.5f)); //transparency


        OverlayPanel overlayPanel = new OverlayPanel();
        frame.add(overlayPanel);

        //Add the ubiquitous "Hello World" label.
        //JLabel label = new JLabel("Hello World");
        //frame.getContentPane().add(label);

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
        overlayPanel.moveIt();

    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}