package net;

import main.Peripherals.Columns.Column;

import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    private final ConcurrentLinkedQueue<String> messages;

    public Client(String hostName, int port, ConcurrentLinkedQueue messages, Column col)
    {
        this.hostName = hostName;
        this.port = port;
        this.messages = messages;
        this.col = col;
        console = new BufferedReader(new InputStreamReader(System.in));
        startClient();
    }

    private void startClient()
    {
        try (Socket socket = new Socket(hostName, port))
        {
            BackgroundThread th = new BackgroundThread(socket, col);
            th.start();

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            while (true)
            {
                final String message = messages.poll(); // does not block, returns null if none is available
                if (message != null)
                {
                    writer.println(message);
                }
            }
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

class BackgroundThread extends Thread
{
    private Socket socket;
    private Column col;
    private boolean isRunning = true;

    BackgroundThread(Socket socket, Column col)
    {
        this.socket = socket;
        this.col = col;
    }

    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;

                while (true)
                {
                    while((line = reader.readLine()) != null)
                    {
                        col.receiveInfo(line);
                    }
                }
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

    public void stopThread()
    {
        isRunning = false;
        if (!isRunning)
        {
            interrupt();
        }
    }
}




