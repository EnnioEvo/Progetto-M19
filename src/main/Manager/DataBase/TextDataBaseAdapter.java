package main.Manager.DataBase;

import main.Manager.Driver;
import main.Manager.Subscriptions.Subscription;

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
        catch (IOException ex)
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
                Driver d = parseDriver(line);
                drivers.put(d.getCarId(), d);
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
            oldDrivers.remove(driver.getCarId());
            newDrivers.addAll(oldDrivers.values());
        }
        newDrivers.add(driver);

        try
        {
            StringBuilder sb = new StringBuilder();
            FileWriter f = new FileWriter(filePath, true);
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

    private Driver parseDriver(String line)
    {
        Driver d;
        ArrayList<String> tmp = new ArrayList<>();

        // Suddivisione di primo livello
        String[] split1 = line.split("--");
        // Salto il comando  che è nella prima stringa
        for(int i=1;i<split1.length;i++)
        {
            // Suddivido in variabili il driver e controllo se esiste sub
            if(!split1[i].equals("0"))
            {
                String[] split2 = split1[i].split("\\$");
                for (int j=1;j<split2.length;j++)
                {
                    String[] split3 = split2[j].split("=");
                    tmp.add(split3[1]);
                }
            }

            // Controllo se sub è nullo
                    /*if(!split1[2].equals("0"))
                    {
                        // Suddivido in variabili
                        String[] split4 = split1[2].split("$");
                        for(int j=0;j<split4.length;j++)
                        {
                            String[] split5 = split4[j].split("=");
                            tmp.add(split5[1]);
                        }
                    }*/
            else
            {
                tmp.add(split1[i]);
            }
        }

        // Creo il driver rispettando l'ordine delle info ricevute;
        d = new Driver(tmp.get(0));
        // Parso timeIn
        d.setTimeIn(parseDate(tmp.get(1)));
        // Parso timePaid
        if(!tmp.get(2).equals("0"))
        {
            d.setTimePaid(parseDate(tmp.get(2)));
        }
        // Parso la tariffa
        d.setTimeIn(parseDate(tmp.get(3)));
        // Parso i boolean
        d.setPaid(Boolean.parseBoolean(tmp.get(4)));
        d.setTicketPayementExpired(Boolean.parseBoolean(tmp.get(5)));
        // Se esiste parso l'abbonamento
        if(!tmp.get(6).equals("0"))
        {
            try
            {
                Constructor c = Class.forName(tmp.get(6)).getConstructor(Double.class);
                Subscription sub = (Subscription) c.newInstance( Double.parseDouble(tmp.get(10)));
                d.setSub(sub);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                System.out.println("Tipo di abbonamento errato nel database");
            }
            // Parso la scadenza
            d.setDateFinishOfSub(parseDate(tmp.get(7)));
            // Parso i boolean
            d.setPaidSub(Boolean.parseBoolean(tmp.get(8)));
            d.setSubPayementExpiredOfSub(Boolean.parseBoolean(tmp.get(9)));
        }

        return d;
    }

    private GregorianCalendar parseDate(String s)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date();
        try
        {
            date = sdf.parse(s);
        }
        catch (ParseException ex)
        {
            System.out.println("Errore nel parsing di una data nel database");
        }
        GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
