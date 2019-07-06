package main.Utilities;

import main.Parking.Driver;
import main.Parking.Subscriptions.Subscription;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DriverParser
{
    public static Driver parseDriver(String line)
    {
        Driver d;
        ArrayList<String> tmp = new ArrayList<>();

        //Suddivisione di primo livello
        String[] split1 = line.split("--");
        //Salto il comando  che è nella prima stringa
        for (int i = 1; i < split1.length; i++) {
            //Suddivido in variabili il driver e controllo se esiste sub
            if (!split1[i].equals("0")) {
                String[] split2 = split1[i].split("\\$");
                for (int j = 1; j < split2.length; j++) {
                    String[] split3 = split2[j].split("=");
                    tmp.add(split3[1]);
                }
            } else {
                tmp.add(split1[i]);
            }
        }

        //Creo il driver rispettando l'ordine delle info ricevute;
        d = new Driver(tmp.get(0));
        //Parso timeIn
        d.setTimeIn(parseDate(tmp.get(1)));
        //Parso timePaid
        if (!tmp.get(2).equals("0"))
        {
            d.setTimePaid(parseDate(tmp.get(2)));
        }
        //Parso la tariffa
        d.setTariff(Double.parseDouble(tmp.get(3)));
        //Parso i boolean
        d.setPaid(Boolean.parseBoolean(tmp.get(4)));
        d.setTicketPayementExpired(Boolean.parseBoolean(tmp.get(5)));
        //Se esiste parso l'abbonamento
        if (!tmp.get(6).equals("0")) {
            try {
                Constructor c = Class.forName(tmp.get(6)).getConstructor(Double.class);
                Subscription sub = (Subscription) c.newInstance(Double.parseDouble(tmp.get(10)));
                d.setSub(sub);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Tipo di abbonamento errato nel database");
            }
            //Parso la scadenza
            d.setDateFinishOfSub(parseDate(tmp.get(7)));
            //Parso i boolean
            d.setPaidSub(Boolean.parseBoolean(tmp.get(8)));
            d.setSubPayementExpiredOfSub(Boolean.parseBoolean(tmp.get(9)));
            d.setInPark(Boolean.parseBoolean(tmp.get(11)));
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
            System.out.println("Errore nel parsing di una data nel database");
        }
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

}

