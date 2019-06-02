package net;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This server is multi-threaded.
 *
 */

public class Server
{
    private int port;
    private Map<String, ArrayList<Integer>> info;

    public Server(int port)
    {
        this.port = port;
        this.info = new HashMap<>();
    }

    public static void main(String[] args)
    {
        if (args.length < 1) return;
        Server server = new Server(Integer.parseInt(args[0]));
        server.startServer();
    }

    public void startServer()
    {
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Server is listening on port " + port);

            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ArrayList<Integer> data = new ArrayList<>();
                data = getInfo(socket.getRemoteSocketAddress().toString());
                if(data == null)
                {
                    data = new ArrayList<>();
                    data.add(0);
                    data.add(0);
                }
                info.put(socket.getRemoteSocketAddress().toString(), data);

                ServerThread t = new ServerThread(socket, this);
                t.start();
            }

        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public ArrayList<Integer> getInfo(String ip)
    {
        return info.get(ip);
    }

}