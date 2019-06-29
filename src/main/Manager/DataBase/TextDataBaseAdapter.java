package main.Manager.DataBase;

import main.Manager.Driver;
import main.Manager.Subscriptions.Subscription;
import main.Utilities.DriverParser;

import java.io.*;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TextDataBaseAdapter implements DataBaseAdapter
{
    private String filePath;
    private BufferedWriter bw;
    private BufferedReader br;

    public TextDataBaseAdapter(String filePath)
    {
        this.filePath = filePath;
        createFile();
    }

    private void createFile()
    {
        File f = new File(filePath);
        try
        {
            f.createNewFile();
        }
        catch (Exception ex)
        {
            System.out.println("Impossibile creare file");
        }
    }

    @Override
    public HashMap<String, Driver> getData()
    {
        HashMap<String, Driver> drivers = new HashMap<>();
        try
        {
            FileReader f = new FileReader(filePath);
            br = new BufferedReader(f);
            String line;

            while((line = br.readLine()) != null)
            {
                Driver d = DriverParser.parseDriver(line);
                // Uso targa e data di ingresso come chiave
                drivers.put(d.getCarId() + "-" +d.getTimeIn().toZonedDateTime().toString(), d);
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return drivers;
    }

    @Override
    public void writeData(Driver driver, Boolean remove)
    {
        HashMap<String, Driver> oldDrivers = getData();
        ArrayList<Driver> newDrivers = new ArrayList<>();
        // Elimino record precedente relativo ai nuovi driver
        if(remove)
        {
            oldDrivers.remove(driver.getCarId() + "-" + driver.getTimeIn().toZonedDateTime().toString());
        }
        newDrivers.addAll(oldDrivers.values());
        newDrivers.add(driver);

        try
        {
            StringBuilder sb = new StringBuilder();
            FileWriter f = new FileWriter(filePath, false);
            bw = new BufferedWriter(f);

            for (Driver d : newDrivers)
            {
                sb.append(d.infoClient());
                sb.append("\n");
            }

            bw.write(sb.toString());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(bw != null)
            {
                try
                {
                    bw.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
