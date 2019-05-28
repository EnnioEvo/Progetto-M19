package main.Peripherals.Columns;

import main.Manager.Manager;
import main.Peripherals.Observer;
import net.Client;
import net.ColumnClient;

import java.awt.*;

public class EntryColumn extends Column implements ColumnClient
{
    private Bar bar;
    private Observer obs;

    public EntryColumn() {}

    public void entryTicket(String id)
    {
        if(man.entryTicket(id))
        {
            bar.open();
        }
    }

    public void entrySub(String id)
    {
        if(man.entrySub(id))
        {
            bar.open();
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

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Argomenti errati");
            return;
        }

        EntryColumn col = new EntryColumn();
        //new Client(args[0], args[1], )
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                EntryColumnGUI g = new EntryColumnGUI(col);
                col.setObs(g);
            }
        });



    }

}
