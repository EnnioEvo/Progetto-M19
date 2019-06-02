package net;

import main.Peripherals.Columns.Column;

import java.net.*;
import java.io.*;

/**
 * This program demonstrates a simple TCP/IP socket client that reads input
 * from the user.
 *
 */

public class Client
{
    private String hostName;
    private int port;
    private BufferedReader console;
    private Column col;

    public Client(String hostName, int port)
    {
        this.hostName = hostName;
        this.port = port;
        console = new BufferedReader(new InputStreamReader(System.in));
        startClient();
    }

    public void startClient()
    {
        try (Socket socket = new Socket(hostName, port))
        {
            BackgroundUpdate up = new BackgroundUpdate(socket);
            up.start();

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            Console console = System.console();
            String line;
            boolean end = false;
            do
            {
                while((line = reader.readLine()) != null)
                {
                    if(line.equals("Inserisci una lettera: "))
                    {
                        System.out.print(line);
                    }
                    else if(line.equals("finish"))
                    {
                        up.stopThread();
                        end = true;
                    }
                    else
                    {
                        System.out.println(line);
                    }
                }
            } while(!end);

        }
        catch (UnknownHostException ex)
        {

            System.out.println("Server not found: " + ex.getMessage());

        }
        catch (IOException ex)
        {

            System.out.println("I/O error: " + ex.getMessage());
        }
        catch (Exception ex)
        {

            System.out.println(ex.getMessage());
        }
    }
}

class BackgroundUpdate extends Thread
{
    private Socket socket;
    private boolean isRunning = true;

    BackgroundUpdate(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {

        while (isRunning)
        {
            try
            {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                Console console = System.console();
                String text;
                text = console.readLine();
                writer.println(text);
            }
            catch (IOException ex)
            {

                System.out.println("I/O error: " + ex.getMessage());
            }
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException ex)
            {
                System.out.println("Client closed");
            }
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




