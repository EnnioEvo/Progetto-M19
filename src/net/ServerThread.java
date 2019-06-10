package net;


import main.Manager.Manager;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

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
                line = reader.readLine();
                split = line.split("--");
                if (split.length < 2)
                {
                    System.out.println("Comando con argomenti errati");
                }
                else
                {
                    switch (split[0])
                    {
                        case "entry":
                            writer.println(man.entryTicket(split[1]));
                            break;
                        case "entrySub":
                            writer.println(man.entrySub(split[1]));
                            break;
                        case "getTariff":
                            writer.println("tariff--" + man.getTariff());
                            break;
                        case "exit":
                            writer.println(man.exit(split[1]));
                            break;
                        default:
                            System.out.println("Comando errato");
                            break;
                    }
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