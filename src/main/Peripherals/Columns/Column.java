package main.Peripherals.Columns;

import main.Peripherals.Peripheral;

public abstract class Column implements Peripheral
{
    protected String id;

    public Column()
    {
    }

    public abstract void receiveInfo(String info);

    public abstract void notifyObs();

    public String getId()
    {
        return id;
    }
}
