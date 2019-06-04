package net;

import main.Manager.Manager;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This server is multi-threaded.
 *
 */

public class Server
{
    private int port;
    private Manager man;
    private ArrayList<Socket> socketList;
    private final ConcurrentLinkedQueue<String> messages;

    public Server(int port, Manager man)
    {
        this.port = port;
        this.man = man;
        this.socketList = new ArrayList<>();
        this.messages = new ConcurrentLinkedQueue<>();
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

                ServerThread t = new ServerThread(socket, messages, man);
                t.start();
            }

        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

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