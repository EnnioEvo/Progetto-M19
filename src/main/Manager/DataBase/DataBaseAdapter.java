package main.Manager.DataBase;

import main.Manager.Driver;

import java.util.HashMap;

public interface DataBaseAdapter
{
    public HashMap<String, Driver> getData();
    public void writeData(Driver driver, Boolean remove);
}

