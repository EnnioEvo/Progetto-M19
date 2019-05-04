package main.Peripherals;

import main.Manager;

public abstract class Column {
    protected String id;
    protected Manager man;

    public Column(String id, Manager man) {
        this.id = id;
        this.man = man;
    }
}
