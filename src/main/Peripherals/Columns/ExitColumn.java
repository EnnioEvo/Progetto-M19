package main.Peripherals.Columns;

import GUIs.ExitColumnGUI;
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

    public ExitColumn(String hostName, int port)
    {
        ExitColumn col = this;
        createCommands();
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            @Override
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

    public void getIdFromMan()
    {
        messages.add("getId--XX");
    }

    public void help()
    {
        messages.add("help--" + id);
    }

    public void exit(String carId)
    {
        messages.add("exit--" + carId);
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
