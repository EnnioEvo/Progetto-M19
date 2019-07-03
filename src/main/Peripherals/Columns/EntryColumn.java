package main.Peripherals.Columns;

import GUIs.EntryColumnGUI2;
import main.Peripherals.ClientCommand;
import main.Utilities.Observer;
import net.Client;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class EntryColumn extends Column
{
    private Bar bar;
    private Observer obs;
    private final LinkedBlockingQueue<String> messages;
    private double tariff, monthlySubTariff, semestralSubTariff, annualSubTariff;
    private String infoBox;
    private HashMap<String, ClientCommand> commands;

    //Costruttore della classe "EntryColumn"
    public EntryColumn(String hostName, int port)
    {
        EntryColumn col = this;
        createCommands();
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            //Esegue l'interfaccia grafica
            public void run()
            {
                EntryColumnGUI2 g = new EntryColumnGUI2(col);
                col.setObs(g);
                getIdFromMan();
                getTariffOfMan();
                getSubTariffsOfMan();
            }
        });
        this.messages = new LinkedBlockingQueue<>();
        this.bar = new Bar();
        new Client(hostName, port, messages, col);
    }

    //Associa un identificativo ad ogni comando eseguito dal Manager che viene trassmesso mediante il
    //collegamento server-client tramite il metodo reciveInfo()
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
        commands.put("entryOk", (String[] args) ->
        {
            System.out.println("entryOk");
            infoBox = args[1];
            notifyObs();
            bar.open();
        });
        commands.put("entryNo", (String[] args) ->
        {
            System.out.println("entryNo");
            infoBox = args[1];
            notifyObs();
        });
        commands.put("tariff", (String[] args) ->
        {
            System.out.println("tariff" + Double.parseDouble(args[1]));
            infoBox = "";
            tariff = Double.parseDouble(args[1]);
            notifyObs();
        });
        commands.put("subTariffs", (String[] args) ->
        {
            System.out.println("subTariffs");
            infoBox = "";
            String s = args[1];
            List<String> list = Arrays.asList(s.substring(1, s.length() - 1).split(", "));
            monthlySubTariff = Double.parseDouble(list.get(0));
            semestralSubTariff = Double.parseDouble(list.get(1));
            annualSubTariff = Double.parseDouble(list.get(2));
            notifyObs();
        });
        commands.put("getTariff", (String[] args) -> getTariffOfMan());
        commands.put("getSubTariffs", (String[] args) -> getSubTariffsOfMan());
    }

    //I seguenti metodi hanno origine dall'interfaccia grafica, arrivano alla colonnina e vengono mandati al client
    //Ottiene l'ID
    public void getIdFromMan()
    {
        messages.add("getId--XX");
    }

    //Chiama l'aiuto
    public void help()
    {
        messages.add("help--" + id);
    }

    //Permette l'entrata tramite l'acquisto del ticket
    public void entryTicket(String id)
    {
        if(id.equals(""))
        {
            messages.add("entry--XX");
        }
        else
        {
            messages.add("entry--" + id);
        }
    }

    //Permette l'entrata tramite l'utilizzo o l'acquisto dell'abbonamento
    public void entrySub(String id, String type)
    {
        messages.add("entrySub--" + id + "--" + type);
    }

    //Ottiene la tariffa del ticket
    public void getTariffOfMan()
    {
        messages.add("getTariff--XX");
    }

    //Ottiene la tariffa dell'abbonamento
    public void getSubTariffsOfMan()
    {
        messages.add("getSubTariffs--XX");
    }

    @Override
    //Riceve informazioni dal Manager e le passa alla Hashmap
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

    //Restituisce la tariffa del ticket
    public double getTariff()
    {
        return tariff;
    }

    //Restituisce la tariffa dell'abbonamento mensile
    public double getMonthlySubTariff()
    {
        return monthlySubTariff;
    }

    //Restituisce la tariffa dell'abbonamento semestrale
    public double getSemestralSubTariff()
    {
        return semestralSubTariff;
    }

    //Restituisce la tariffa dell'abbonamento annuale
    public double getAnnualSubTariff()
    {
        return annualSubTariff;
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

        new EntryColumn(args[0], Integer.parseInt(args[1]));
    }

}
