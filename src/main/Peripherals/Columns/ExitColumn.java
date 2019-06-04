package main.Peripherals.Columns;

import GUIs.EntryColumnGUI;
import GUIs.ExitColumnGUI;
import main.Manager.Manager;
import main.Peripherals.Observer;
import net.Client;

import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ExitColumn extends Column {

    private Bar bar;
    private Observer obs;
    private final ConcurrentLinkedQueue<String> messages;
    private String infoBox;

    public ExitColumn(String hostName, int port)
    {
        ExitColumn col = this;
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                ExitColumnGUI g = new ExitColumnGUI(col);
                col.setObs(g);
            }
        });
        this.messages = new ConcurrentLinkedQueue<>();
        this.bar = new Bar();
        new Client(hostName, port, messages, col);
    }

    public void exit(String carId)
    {
        messages.add("exit--" + carId);
    }

    @Override
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        switch (split[0])
        {
            case "exitOk":
                System.out.println("exitOk");
                infoBox = split[1];
                notifyObs();
                bar.open();
                break;
            case "exitNo":
                System.out.println("exitNo");
                infoBox = split[1];
                notifyObs();
                bar.open();
                break;
            case "getTariff":
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
