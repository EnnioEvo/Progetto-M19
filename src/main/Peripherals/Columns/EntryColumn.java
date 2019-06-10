package main.Peripherals.Columns;

import GUIs.EntryColumnGUI;
import main.Manager.Manager;
import main.Peripherals.Observer;
import net.Client;
import net.ColumnClient;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EntryColumn extends Column
{
    private Bar bar;
    private Observer obs;
    private final ConcurrentLinkedQueue<String> messages;
    private double tariff;
    private String infoBox;

    public EntryColumn(String hostName, int port)
    {
        EntryColumn col = this;
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                EntryColumnGUI g = new EntryColumnGUI(col);
                col.setObs(g);
                getTariffofMan();
            }
        });
        this.messages = new ConcurrentLinkedQueue<>();
        this.bar = new Bar();
        new Client(hostName, port, messages, col);
    }

    public void entryTicket(String id)
    {
        messages.add("entry--" + id);
    }

    public void entrySub(String id)
    {
        messages.add("entrySub--" + id);
    }

    public void getTariffofMan()
    {
        messages.add("getTariff--XX");
    }

    @Override
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        switch (split[0])
        {
            case "entryOk":
                System.out.println("entryOk");
                infoBox = split[1];
                notifyObs();
                bar.open();
                break;
            case "entryNo":
                System.out.println("entryNo");
                infoBox = split[1];
                notifyObs();
                bar.open();
                break;
            case "tariff":
                System.out.println("tariff" + Double.parseDouble(split[1]));
                infoBox = "";
                tariff = Double.parseDouble(split[1]);
                notifyObs();
                break;
            case "getTariff":
                getTariffofMan();
                break;
        }
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
