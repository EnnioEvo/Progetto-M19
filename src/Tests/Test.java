package Tests;

import main.Parking.Driver;
import main.Parking.Subscriptions.Subscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test
{

    public static void main(String[] args)
    {

        /*int posti[] = new int[10];
        Parking m = new Parking();
        m.makeFloors(7, 50);
        m.setTariff(5);
        m.setSpacesSubdivision(50);
        m.entryTicket("IT4560JV");
        m.entrySub("IT3456GT");


        Driver d = m.getDriver("IT4560JV");
        d.setPaid(true);
        GregorianCalendar time = new GregorianCalendar();
        time.add(Calendar.MINUTE, -9);
        d.setTimePaid(time);
        //m.exit("IT4560JV");

        Driver d2 = m.getDriver("IT3456GT");
        d2.setPaidSub(true);
        //Per testare tariffa extra mettere mese abbonamneto a -1
        d2.setDatePaidExtraOfSub(time);
        m.exit("IT3456GT");
       // m.Analytics();

        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run()
            {
                new ManagerGUI(m);
            }
        });

        /*EntryColumn entry = m.createEntryColumn();
        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run() {
                EntryColumnGUI g = new EntryColumnGUI(entry);
                entry.setObs(g);
            }
        });*/

        /*GregorianCalendar c = new GregorianCalendar();
        String t = c.toZonedDateTime().toString();
        System.out.println(c.toZonedDateTime());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm XXX");
        Date d = new Date();
        try {
            d = sdf.parse(t);
        }
        catch (ParseException ex)
        {}
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        System.out.println(d);*/

        /*String s = "info--$carId=aaaaaaaa$timeIn=2019-06-20T23:14:38.495+02:00[Europe/Rome]$timepaid=0$paid=false$ticketPayementExpired=false--0";
        Test.parseDriver(s);*/

        /*try {
            Constructor c = Class.forName("main.Parking.Subscriptions.MonthlySubscription").getDeclaredConstructor(Double.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        ArrayList<Double> d = new ArrayList<>();
        d.add(2.0);
        d.add(3.1);
        System.out.println(d.toString());

        File f = new File("./ooo");
        try {
            f.createNewFile();
            System.out.println("sds");
        }
        catch(Exception ex){ex.printStackTrace();}*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try
        {
            date = sdf.parse("1711-11-32");
        }
        catch (ParseException ex)
        {
            System.out.println("Errore nel parsing di una data nel database");
        }
        GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
        cal.setTime(date);
        System.out.println(cal.toZonedDateTime().toString());
    }

    private static Driver parseDriver(String line)
    {
        Driver d;
        ArrayList<String> tmp = new ArrayList<>();
        System.out.println(line);
        //Suddivisione di primo livello
        String[] split1 = line.split("--");
        // Salto il comando  che è nella prima stringa
        for(int i=1;i<split1.length;i++)
        {
            //Suddivido in variabili il driver e controllo se esiste sub
            if(!split1[i].equals("0"))
            {
                System.out.println("1" +split1[i]);
                String[] split2 = split1[i].split("\\$");
                for (int j=1;j<split2.length;j++)
                {
                    System.out.println("2" + split2[j]);
                    String[] split3 = split2[j].split("=");
                    tmp.add(split3[1]);
                }
            }

            //Controllo se sub è nullo
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

        for (String  s: tmp
             ) {System.out.println(tmp);

        }

        //Creo il driver rispettando l'ordine delle info ricevute;
        d = new Driver(tmp.get(0));
        //Parso timeIn
        d.setTimeIn(parseDate(tmp.get(1)));
        //Parso timePaid
        if(!tmp.get(2).equals("0"))
        {
            System.out.println("time in");
            d.setTimeIn(parseDate(tmp.get(2)));
        }
        //Parso i boolean
        d.setPaid(Boolean.parseBoolean(tmp.get(3)));
        d.setTicketPayementExpired(Boolean.parseBoolean(tmp.get(4)));
        //Se esiste parso l'abbonamento
        if(!tmp.get(5).equals("0"))
        {
            try
            {
                System.out.println("sub");
                Subscription sub = (Subscription)Class.forName(tmp.get(5)).getConstructor().newInstance(80);
                d.setSub(sub);
            }
            catch(Exception ex)
            {
                System.out.println("Tipo di abbonamento errato nel database");
            }
            //Parso la scadenza
            d.setDateFinishOfSub(parseDate(tmp.get(6)));
            //Parso i boolean
            d.setPaidSub(Boolean.parseBoolean(tmp.get(7)));
            d.setSubPayementExpiredOfSub(Boolean.parseBoolean(tmp.get(8)));
            //Parso il costo
            d.setCostOfSub(Double.parseDouble(tmp.get(9)));
        }

        return d;
    }

    private static GregorianCalendar parseDate(String s)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date();
        try
        {
            date = sdf.parse(s);
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
            System.out.println("Errore nel parsing di una data nel database");
        }
        GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

