package main.Peripherals.Columns;

import main.Peripherals.Peripheral;

public abstract class Column implements Peripheral
{
    protected String id;

    public Column()
    {
    }

    //Permette il passaggio di informazioni tra il Manager e la colonnina, in modo da
    //aggironare la GUI
    public abstract void receiveInfo(String info);

    //Viene utilizzato per eseguire il metodo update() nell'observer, ogni volta che
    //il contenuto di variabili presenti nella GUI cambia
    public abstract void notifyObs();

    public String getId()
    {
        return id;
    }
}
