package main.Peripherals.Columns;

import main.Manager.Manager;

public abstract class Column {
    protected String id;
    protected Manager man;

    public Column(String id, Manager man)
    {
        this.id = id;
        this.man = man;
    }

    public double getTariffofMan()
    {
        return man.getTariff();
    }

    public abstract void notifyObs();
}
