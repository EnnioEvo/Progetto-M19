package net;

import main.Parking.Parking;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This server is multi-threaded.
 *
 */

public class Server
{
    private int port;
    private Parking man;
    private ArrayList<Socket> socketList;

    public Server(int port, Parking man)
    {
        this.port = port;
        this.man = man;
        this.socketList = new ArrayList<>();
    }

    public void startServer()
    {
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Server is listening on port " + port);

            while (true)
            {
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                System.out.println("New client connected");

                // Nuovo thread per ogni connessione
                ServerThread t = new ServerThread(socket, man);
                t.start();
            }

        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Lo uso per mandare aggiornamenti alle periferiche (es tariffa)
    public void updatePeripherals(String command)
    {
        for (Socket s : socketList)
        {
            System.out.println("Update " + command);
            try
            {
                OutputStream output = s.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(command);
            }
            catch (IOException ex)
            {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}