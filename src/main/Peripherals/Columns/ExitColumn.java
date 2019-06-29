package main.Peripherals.Columns;

import GUIs.ExitColumnGUI2;
import main.Peripherals.ClientCommand;
import main.Peripherals.Observer;
import net.Client;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ExitColumn extends Column {

    private Bar bar;
    private Observer obs;
    private final LinkedBlockingQueue<String> messages;
    private String infoBox;
    private HashMap<String, ClientCommand> commands;

    //Costruttore della classe "ExitColumn"
    public ExitColumn(String hostName, int port)
    {
        ExitColumn col = this;
        createCommands();
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            //Esegue l'interfaccia grafica
            public void run()
            {
                ExitColumnGUI2 g = new ExitColumnGUI2(col);
                col.setObs(g);
                getIdFromMan();
            }
        });
        this.messages = new LinkedBlockingQueue<>();
        this.bar = new Bar();
        new Client(hostName, port, messages, col);
    }

    //Collega il nome del comando con i rispettivi metodi che eseguono le operazioni volute dal server
    private void createCommands()
    {
        commands = new HashMap<>();
        commands.put("id", (String[] args) ->
        {
            System.out.println("id");
            id = args[1];
        });
        commands.put("helpComing", (String[] args) ->
        {
            System.out.println("helpComing");
            infoBox = args[1];
            notifyObs();
        });
        commands.put("exitOk", (String[] args) ->
        {
            System.out.println("exitOk");
            infoBox = args[1];
            notifyObs();
            bar.open();
        });
        commands.put("exitNo", (String[] args) ->
        {
            System.out.println("exitNo");
            infoBox = args[1];
            notifyObs();
            //bar.open();
        });
        commands.put("getTariff", (String[] args) -> System.out.println("getTariff"));
    }

    //I seguenti metodi hanno origine dall'interfaccia grafica e vengono mandati al client
    //Ottiene l'ID
    public void getIdFromMan()
    {
        messages.add("getId--XX");
    }

    //Chiede l'aiuto
    public void help()
    {
        messages.add("help--" + id);
    }

    //Permette di uscire con il ticket o l'abbonamento
    public void exit(String carId)
    {
        if(carId.equals(""))
        {
            messages.add("exit--XX");
        }
        else
        {
            messages.add("exit--" + carId);
        }
    }

    @Override
    //Riceve informazioni dal client
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        commands.get(split[0]).execute(split);
    }

    @Override
    //Notifica la modifica di una variabile
    public void notifyObs()
    {
        obs.update();
    }

    //Imposta l'Observer
    public void setObs(Observer obs)
    {
        this.obs = obs;
    }

    //Restituisce il contenuto dell InfoBox
    public String getInfoBox()
    {
        return infoBox;
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Argomenti errati");
            return;
        }

        new ExitColumn(args[0], Integer.parseInt(args[1]));
    }
}
