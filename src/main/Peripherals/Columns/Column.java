package main.Peripherals.Columns;

import main.Manager.Manager;
import main.Peripherals.Peripheral;

public abstract class Column implements Peripheral
{
    protected String id;
    protected Manager man;

    public Column()
    {
    }

    /*public double getTariffofMan()
    {
        return man.getTariff();
    }*/

    public abstract void receiveInfo(String info);

    public abstract void notifyObs();

    public String getId()
    {
        return id;
    }
}
