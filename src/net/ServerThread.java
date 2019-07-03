package net;


import main.Manager.Manager;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible to handle client connection.
 *
 */

public class ServerThread extends Thread
{
    private Socket socket;
    private PrintWriter writer;
    private Manager man;
    private boolean isRunning = true;

    public ServerThread(Socket socket, Manager man)
    {
        this.socket = socket;
        this.man = man;
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
            String split[];
            do
            {
                // Leggo comandi dai client
                line = reader.readLine();
                split = line.split("--");
                if (split.length < 2)
                {
                    System.out.println("Comando con argomenti errati");
                }
                else
                {
                    // Invio la risposta elaborata dal manager
                    writer.println(man.executeCommand(split));
                }
            }while (isRunning);

            System.out.println("finish");
            socket.close();
        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void stopThread()
    {
        isRunning = false;
        if (!isRunning)
        {
            interrupt();
        }
    }

}