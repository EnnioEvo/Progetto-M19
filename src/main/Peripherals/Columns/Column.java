package main.Peripherals.Columns;

import main.Manager.Manager;

public abstract class Column {
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
}
