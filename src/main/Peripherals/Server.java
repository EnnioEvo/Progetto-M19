package main.Peripherals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket Server;
    public static void main(String argv[]) throws Exception
    {
        new Server();
    }
    public Server() throws Exception
    {
        Server = new ServerSocket(4000);
        System.out.println("server in attesa sulla porta 4000.");
        this.start();
    }
    public void run()
    {
        while(true)
        {
            try {
                System.out.println("in attesa di connessione...");
                Socket client = Server.accept();
                System.out.println("connessione accettata da: "+ client.getInetAddress());
                Connect c = new Connect(client);
            }
            catch(Exception e) {}
        }
    }
}

class Connect extends Thread
{
    private Socket client = null;
    BufferedReader in = null;
    PrintStream out = null;
    public Connect() {}
    public Connect(Socket clientSocket)
    {
        client = clientSocket;
        try
        {
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            out = new PrintStream(client.getOutputStream(), true);
        }
        catch(Exception e1)
        {
            try { client.close();
            }
            catch(Exception e) { System.out.println(e.getMessage());}
            return;
        }
        this.start();
    }
    public void run()
    {
        try
        {
            out.println("client, ti sei connesso al server!");
            out.flush();

            //chiudo tutti gli stream e le connessioni
            out.close();
            in.close();
            client.close();
        }
        catch(Exception e) {}
    }
}
