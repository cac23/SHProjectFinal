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
        System.out.println("Game started:" + System.currentTimeMillis());
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
        int[] leftTurnTime = new int[]{3};
        int[] leftTurnTimeDuration = new int[]{5};

        int[] currentStreetTime = new int[]{10};
        int[] currentStreetTimeDuration = new int[]{5};

        int[] lowTireTime = new int[]{20};
        int[] lowTireTimeDuration = new int[]{5};

        int[] directionTime = new int[]{30};
        int[] directionTimeDuration = new int[]{5};


        int[] firstSpeedLimitTime = new int[]{40};
        int[] firstSpeedLimitTimeDuration = new int[]{3};

        int[] secondSpeedLimitTime = new int[]{45};
        int[] secondSpeedLimitTimeDuration = new int[]{5};


        boolean speedFirstReceived = false;
        long startTime = System.currentTimeMillis();

        Image turnLeftImage, speedLimitImageOne, speedLimitImageTwo, lowTirePressureImage, compassImage;

        public DrawPanel() {
            try {
                listener = new ServerSocket(3001);
                socket = listener.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Client Connected!");
                System.out.println("Game actually started: " + System.currentTimeMillis());
                    turnLeftImage = ImageIO.read(getClass().getResource("nav.png"));
                    speedLimitImageOne = ImageIO.read(getClass().getResource("firstSpeedLimitSign.png"));
                    speedLimitImageTwo = ImageIO.read(getClass().getResource("secondSpeedLimitSign.png"));
                    compassImage = ImageIO.read(getClass().getResource("directionAndCompass.png"));
                    lowTirePressureImage = ImageIO.read(getClass().getResource("lowTirePressure.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void paintComponent(Graphics g) {
            System.out.println("paintcomponent");
            System.out.println("Current time: " + System.currentTimeMillis());
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            /* left turn signal */
            for(int i =0; i < leftTurnTime.length; i++) {
                long time1 = leftTurnTime[i]*1000;
                if (elapsedTime > time1 && (elapsedTime<time1 +1000*leftTurnTimeDuration[i])) {
                    g.drawImage(turnLeftImage, 900, 1100, turnLeftImage.getWidth(null), turnLeftImage.getHeight(null), null);
                }
            }

             /* first speed limit */
            for(int i =0; i < firstSpeedLimitTime.length; i++) {
                long time2 = firstSpeedLimitTime[i]*1000;
                if (elapsedTime > time2 && (elapsedTime<(time2 +1000*firstSpeedLimitTimeDuration[i]))) {
                    g.drawImage(speedLimitImageOne, 1000, 1100, speedLimitImageOne.getWidth(null), speedLimitImageOne.getHeight(null), null);
                }
            }

             /* second speed limit */
            for(int i =0; i < secondSpeedLimitTime.length; i++) {
                long time3 = secondSpeedLimitTime[i]*1000;
                if (elapsedTime > time3 && (elapsedTime<(time3 +1000*secondSpeedLimitTimeDuration[i]))) {
                    g.drawImage(speedLimitImageTwo, 1000, 1100, speedLimitImageTwo.getWidth(null), speedLimitImageTwo.getHeight(null), null);
                }
            }

            /* current street */
            for(int i = 0; i < currentStreetTime.length; i++) {
                long time4 = currentStreetTime[i]*1000;
                if (elapsedTime > time4 && (elapsedTime<(time4 +1000*currentStreetTimeDuration[i]))) {
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("TimesRoman", Font.BOLD, 100));
                        g.drawString("Market St", 1100, 1200);
                }
            }

            /* low tire pressure */
            for(int i =0; i < lowTireTime.length; i++) {
                long time5 = lowTireTime[i]*1000;
                if (elapsedTime > time5 && (elapsedTime<(time5 +1000*lowTireTimeDuration[i]))) {
                    g.drawImage(lowTirePressureImage, 900, 1200, lowTirePressureImage.getWidth(null), lowTirePressureImage.getHeight(null), null);
                }
            }

            /* direction */
            for(int i =0; i < directionTime.length; i++) {
                long time6 = directionTime[i]*1000;
                if (elapsedTime > time6 && (elapsedTime<(time6 +1000*directionTimeDuration[i]))) {
                    g.drawImage(compassImage, 1000, 1100, compassImage.getWidth(null), compassImage.getHeight(null), null);
                }
            }

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
                        g.setFont(new Font("TimesRoman", Font.BOLD, 150));
                        g.drawString(line, 1280, 1400);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}