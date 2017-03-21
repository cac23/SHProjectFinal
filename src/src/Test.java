package src;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

final public class Test {
    static JFrame frame;
    static DrawPanel drawPanel;

    public static void main(String... args) throws FileNotFoundException {
        System.out.println("Game started:" + System.currentTimeMillis());
        Test.go();
    }

    private static void go() throws FileNotFoundException {
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

        int[] navTime = new int[]{8};
        int[] navTimeDuration = new int[]{10};

        //pause 4 seconds
        int[] currentStreetTime = new int[]{22};
        int[] currentStreetTimeDuration = new int[]{5};
        //pause

        int[] lowTireTime = new int[]{30};
        int[] lowTireTimeDuration = new int[]{5};
        //pause

        int[] directionTime = new int[]{40};
        int[] directionTimeDuration = new int[]{5};
        //ends 39s
        //pause

        int[] firstSpeedLimitTime = new int[]{50};
        int[] firstSpeedLimitTimeDuration = new int[]{8};
        //ends 45s
        //pause


        int[] allShowAtOnceTime = new int[]{60};
        int[] allShowAtOnceDuration = new int[]{12};



        boolean speedFirstReceived = false;
        long startTime = System.currentTimeMillis();

        Image turnLeftImage, speedLimitImageOne, speedLimitImageTwo, lowTirePressureImage, compassImage, navImage;

        public DrawPanel() throws FileNotFoundException {
            try {
                writer = new PrintWriter(new File("participant20.txt"));
                listener = new ServerSocket(3001);
                socket = listener.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Client Connected!");
                System.out.println("Game actually started: " + System.currentTimeMillis());
                    turnLeftImage = ImageIO.read(getClass().getResource("leftTurn_white.png"));
                    speedLimitImageOne = ImageIO.read(getClass().getResource("firstSpeedLimitSign.png"));
                    speedLimitImageTwo = ImageIO.read(getClass().getResource("secondSpeedLimitSign.png"));
                    compassImage = ImageIO.read(getClass().getResource("directionAndCompass.png"));
                    lowTirePressureImage = ImageIO.read(getClass().getResource("lowTirePressure.png"));
                    navImage = ImageIO.read(getClass().getResource("nav_white.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PrintWriter writer;
        public void paintComponent(Graphics g) {
            //System.out.println("Current time: " + System.currentTimeMillis());
            DateFormat formatter1;


            formatter1 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            System.out.println(formatter1.format(date));
            writer.write(formatter1.format(date) + "\n");
            writer.flush();

            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;


            /* first speed limit */
            for(int i =0; i < firstSpeedLimitTime.length; i++) {
                long time2 = firstSpeedLimitTime[i]*1000;
                int firstWidth = (speedLimitImageTwo.getWidth(null)/3);
                int firstHeight = (speedLimitImageTwo.getHeight(null)/3);
                if (elapsedTime > time2 && (elapsedTime<(time2 +1000*firstSpeedLimitTimeDuration[i]))) {
                    writer.write(formatter1.format(date) + "speed limit" + "\n");
                    writer.flush();
                    g.drawImage(speedLimitImageTwo, 900, 1300, firstWidth, firstHeight, null);
                }
            }

            /* current street */
            for(int i = 0; i < currentStreetTime.length; i++) {
                long time4 = currentStreetTime[i]*1000;
                if (elapsedTime > time4 && (elapsedTime<(time4 +1000*currentStreetTimeDuration[i]))) {
                    writer.write(formatter1.format(date) + "current street" + "\n");
                    writer.flush();
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("TimesRoman", Font.BOLD, 80));
                        g.drawString("Market St", 700, 1400);
                }
            }

            /* low tire pressure */
            for(int i =0; i < lowTireTime.length; i++) {
                long time5 = lowTireTime[i]*1000;
                if (elapsedTime > time5 && (elapsedTime<(time5 +1000*lowTireTimeDuration[i]))) {
                    writer.write(formatter1.format(date) + "tire pressure" + "\n");
                    writer.flush();
                    g.drawImage(lowTirePressureImage, 900, 1180, lowTirePressureImage.getWidth(null), lowTirePressureImage.getHeight(null), null);
                }
            }

            /* direction */
            for(int i =0; i < directionTime.length; i++) {
                long time6 = directionTime[i]*1000;
                int compassWidth = (compassImage.getWidth(null))/6;
                int compassHeight = (compassImage.getHeight(null))/6;
                if (elapsedTime > time6 && (elapsedTime<(time6 +1000*directionTimeDuration[i]))) {
                    writer.write(formatter1.format(date) + "compass" + "\n");
                    writer.flush();
                    g.drawImage(compassImage, 1000, 1190, compassWidth, compassHeight, null);
                }
            }

            /* navigation */
            for(int i =0; i < navTime.length; i++) {
                long time7 = navTime[i]*1000;
                int navWidth = (navImage.getWidth(null))/4;
                int navHeight = (navImage.getHeight(null))/4;
                if (elapsedTime > time7 && (elapsedTime<(time7 +1000*navTimeDuration[i]))) {
                    writer.write(formatter1.format(date) + "navigation" + "\n");
                    writer.flush();
                    g.drawImage(navImage, 900, 1250, navWidth, navHeight, null);
                }
            }


            /* all at once */
            for(int i=0; i <allShowAtOnceTime.length; i++) {
                long time8 = allShowAtOnceTime[i]*1000;
                if (elapsedTime > time8 && (elapsedTime<(time8 + 1000*allShowAtOnceDuration[i]))) {
                    int navWidth = (navImage.getWidth(null))/4;
                    int navHeight = (navImage.getHeight(null))/4;
                    int compassWidth = (compassImage.getWidth(null))/7;
                    int compassHeight = (compassImage.getHeight(null))/7;
                    int firstWidth = (turnLeftImage.getWidth(null)/6);
                    int firstHeight = (turnLeftImage.getHeight(null)/6);
                    g.drawImage(navImage, 1000, 1250, navWidth, navHeight, null);
                    writer.write(formatter1.format(date) + "all at once" + "\n");
                    writer.flush();
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                    g.drawString("Market St", 1000, 1200);
                    g.drawImage(lowTirePressureImage, 600, 1200, lowTirePressureImage.getWidth(null), lowTirePressureImage.getHeight(null), null);
                    g.drawImage(compassImage, 400, 1150, compassWidth, compassHeight, null);
                    g.drawImage(speedLimitImageTwo, 1280, 1200, firstWidth, firstHeight, null);

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