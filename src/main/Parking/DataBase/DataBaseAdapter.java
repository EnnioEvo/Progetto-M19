package main.Parking.DataBase;


import main.Parking.Driver;


import java.util.HashMap;

public interface DataBaseAdapter
{
    public HashMap<String, Driver> getData();
    public void writeData(Driver driver, Boolean remove);
}

