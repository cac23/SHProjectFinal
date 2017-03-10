package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cclapp on 22/02/2017.
 */
public class TestServer {
    public int output;

    public void setupServer() throws IOException {
        ServerSocket listener = new ServerSocket(3000); //server
        //tcp server that runs on port 3000 (the same as the client in game.cpp)

        try {
            while(true) {
                Socket socket = listener.accept();
                try {
                    System.out.println("Listening");
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //System.out.println(reader.readLine()); //this prints it out all as one line
                    this.output = Integer.parseInt(reader.readLine());
                } finally {
                    socket.close();
                }
            }
        } finally {
            listener.close();
        }
    }

    public int getOutput() {
        return this.output;
    }
}