package main.Peripherals.Columns;

import GUIs.EntryColumnGUI;
import main.Manager.Manager;
import main.Peripherals.Observer;
import net.Client;
import net.ColumnClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EntryColumn extends Column
{
    private Bar bar;
    private Observer obs;
    private final ConcurrentLinkedQueue<String> messages;
    private double tariff, monthlySubTariff, semestralSubTariff, annualSubTariff;
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
                getTariffOfMan();
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
            case "subTariffs":
                System.out.println("Subtariffs");
                infoBox = "";
                String s = split[1];
                List<String> list = Arrays.asList(s.substring(1, s.length() - 1).split(", "));
                monthlySubTariff = Double.parseDouble(list.get(0));
                semestralSubTariff = Double.parseDouble(list.get(1));
                annualSubTariff = Double.parseDouble(list.get(2));
                notifyObs();
                break;
            case "getTariff":
                getTariffOfMan();
                break;
            case "getSubTariffs":
                getSubTariffsOfMan();
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
