package main.Peripherals.Columns;

import main.Manager.Manager;
import main.Peripherals.Observer;

public class EntryColumn extends Column
{
    private Bar bar;
    private Observer obs;

    public EntryColumn(String id, Manager man)
    {
        super(id, man);
        bar = new Bar();
    }

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
}
