package main.Peripherals;

import main.Manager;

public class EntryColumn extends Column
{
    private Bar bar;

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


}
