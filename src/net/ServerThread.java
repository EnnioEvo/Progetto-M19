package net;


import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This thread is responsible to handle client connection.
 *
 */

public class ServerThread extends Thread
{
    private Socket socket;
    private Server server;
    private PrintWriter writer;

    public ServerThread(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
    }

    public void run()
    {
        try
        {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            System.out.println("start");

            String line;
            do
            {
                writer.println("Cosa vuoi fare? (info) (gioca)");
                line = reader.readLine();
                switch(line)
                {
                    case "info":
                        ArrayList<Integer> data = server.getInfo(socket.getRemoteSocketAddress().toString());
                        writer.println("Partite perse: " + data.get(0) + "\nPartite vinte: " + data.get(1));
                        break;
                    case "gioca":
                        ArrayList<Integer> dat = server.getInfo(socket.getRemoteSocketAddress().toString());
                        writer.println("Partite perse: " + dat.get(0) + "\nPartite vinte: " + dat.get(1));
                        break;
                    default:
                        writer.println("Comando errato");
                        break;
                }
            }while(!line.equals("gioca"));

            System.out.println("finish");
            socket.close();
        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}