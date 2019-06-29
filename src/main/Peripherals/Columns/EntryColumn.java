package main.Peripherals.Columns;

import GUIs.EntryColumnGUI2;
import main.Peripherals.ClientCommand;
import main.Peripherals.Observer;
import net.Client;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EntryColumn extends Column
{
    private Bar bar;
    private Observer obs;
    private final LinkedBlockingQueue<String> messages;
    private double tariff, monthlySubTariff, semestralSubTariff, annualSubTariff;
    private String infoBox;
    private HashMap<String, ClientCommand> commands;

    public EntryColumn(String hostName, int port)
    {
        EntryColumn col = this;
        createCommands();
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            @Override
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

    public void getIdFromMan()
    {
        messages.add("getId--XX");
    }

    public void help()
    {
        messages.add("help--" + id);
    }

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

    public void entrySub(String id, String type)
    {
        messages.add("entrySub--" + id + "--" + type);
    }

    public void getTariffOfMan()
    {
        messages.add("getTariff--XX");
    }

    public void getSubTariffsOfMan()
    {
        messages.add("getSubTariffs--XX");
    }

    @Override
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        commands.get(split[0]).execute(split);
    }

    @Override
    public void notifyObs()
    {
        obs.update();
    }

    public void setObs(Observer obs)
    {
        this.obs = obs;
    }

    public double getTariff()
    {
        return tariff;
    }

    public double getMonthlySubTariff()
    {
        return monthlySubTariff;
    }

    public double getSemestralSubTariff()
    {
        return semestralSubTariff;
    }

    public double getAnnualSubTariff()
    {
        return annualSubTariff;
    }

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
