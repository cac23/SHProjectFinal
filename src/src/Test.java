package src; /**
 * Created by cclapp on 01/02/2017.
 */

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

final public class Test {
    static JFrame frame;
    static DrawPanel drawPanel;

    public static void main(String... args) {
       Test.go();

    }

    private static void go() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));

        drawPanel = new DrawPanel();

        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setResizable(false);
        frame.setSize(200, 200);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        while(true) frame.repaint();
    }

    static class DrawPanel extends JPanel {

        int speed = 0;
        ServerSocket listener;
        Socket socket;
        BufferedReader reader;
        int[] leftTurnTime = new int[]{3,5,7,9,10,20};
        boolean speedFirstReceived = false;
        long startTime;

        public DrawPanel() {
            try {
                listener = new ServerSocket(3001);
                socket = listener.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("Client Connected!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void paintComponent(Graphics g) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;


            for(int i =0; i < leftTurnTime.length; i++) {
                long time1 = leftTurnTime[i]*1000;
                if (elapsedTime > time1 && (elapsedTime<time1 +1000)) {
                    g.setColor(Color.WHITE);
                    g.drawString("output", 100, 150);
                }
            }

            String line;
            try {
                if (reader != null) {
                    if ((line = reader.readLine()) != null) {
                        if (!speedFirstReceived) {
                            speedFirstReceived = true;
                            this.startTime = System.currentTimeMillis();
                        }
                        g.setColor(Color.WHITE);
                        g.drawString("Speed: " + line, 100, 100);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    }