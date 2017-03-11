package src;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

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
        int[] leftTurnTime = new int[]{2,6};
        int[] rightTurnTime = new int[]{4,10};
        boolean speedFirstReceived = false;
        long startTime;

        Image turnLeftImage, speedLimitImageOne, speedLimitImageTwo, lowTirePressureImage, compassImage;

        //String baseDirectory = "images/";

        public DrawPanel() {
            try {
                listener = new ServerSocket(3001);
                socket = listener.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Client Connected!");
                //added in
                    turnLeftImage = ImageIO.read(getClass().getResource("turnRight.png"));
//                    speedLimitImageOne = ImageIO.read(getClass().getResource("firstSpeedLimitSign.png"));
//                    speedLimitImageTwo = ImageIO.read(getClass().getResource("secondSpeedLimitSign.png"));
//                    //trafficAheadImage = ImageIO.read(getClass().getResource("trafficAhead.png"));
//                    //trafficAheadImage = ImageIO.read(getClass().getResource(baseDirectory + "lowTirePressure.png"));
                    //lowTirePressureImage = ImageIO.read(getClass().getResource("lowTirePressure.png"));
//                    compassImage = ImageIO.read(getClass().getResource("directionAndCompass.png"));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void paintComponent(Graphics g) {
            //DrawPanel drawPanel = new DrawPanel();
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            /* left turn signal */
            for(int i =0; i < leftTurnTime.length; i++) {
                long time1 = leftTurnTime[i]*1000;
                if (elapsedTime > time1 && (elapsedTime<time1 +1000)) {
                    g.drawImage(turnLeftImage, 400, 400, turnLeftImage.getWidth(null), turnLeftImage.getHeight(null), null);
                }
            }

            /* current street */
            for(int i =0; i < rightTurnTime.length; i++) {
                long time1 = rightTurnTime[i]*1000;
                if (elapsedTime > time1 && (elapsedTime<time1 +1000)) {
                        g.setColor(Color.GREEN);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 80));
                        g.drawString("Market St", 500, 800);
                }
            }

//            /* low tire pressure */
//            for(int i =0; i < leftTurnTime.length; i++) {
//                long time1 = leftTurnTime[i]*1000;
//                if (elapsedTime > time1 && (elapsedTime<time1 +1000)) {
//                    g.drawImage(lowTirePressureImage, 400, 400, lowTirePressureImage.getWidth(null), lowTirePressureImage.getHeight(null), null);
//                }
//            }

            /* speed */
            String line;
            try {
                if (reader != null) {
                    if ((line = reader.readLine()) != null) {
                        if (!speedFirstReceived) {
                            speedFirstReceived = true;
                            this.startTime = System.currentTimeMillis();
                        }
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 80));
                        g.drawString("Speed: " +line, 700, 1200);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}