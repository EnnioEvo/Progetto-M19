package main.Peripherals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    public static void main(String argv[])
    {
        BufferedReader in = null;
        PrintStream out = null;
        Socket socket = null;
        String message;
        try
        {
            //apro una connessione socket
            socket = new Socket("localhost", 4000);

            //apro i canali
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream(), true);

            //leggo dal server
            message = in.readLine();
            System.out.print("messaggio ricevuto : " + message);
            out.close();
            in.close();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
