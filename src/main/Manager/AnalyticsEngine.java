package main.Manager;

import main.Manager.DataBase.DataBaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AnalyticsEngine
{
    private DataBaseAdapter db;

    public AnalyticsEngine(DataBaseAdapter db)
    {
        this.db = db;
    }

    public double meanTicketTimeIn(String from, String to)
    {
        ArrayList<Driver> drivers = new ArrayList<>();
        removeFromTo(drivers, from, to);

        double hours = 0;
        for (Driver d : drivers)
        {
            if(d.getSub() == null)
            {
                GregorianCalendar exitTime = new GregorianCalendar();
                if(d.getTimePaid() != null)
                {
                    exitTime = d.getTimePaid();
                }
                hours += ChronoUnit.SECONDS.between(d.getTimeIn().toZonedDateTime(), exitTime.toZonedDateTime());
            }
        }

        if(drivers.size() > 0)
        {
            hours = (double) hours / drivers.size();
        }
        return hours;
    }

    public int[] totTicketAndSub(String from, String to)
    {
        ArrayList<Driver> drivers = new ArrayList<>();
        removeFromTo(drivers, from, to);

        int totT = 0;
        int totS = 0;
        for (Driver d : drivers)
        {
            if(d.getSub() == null)
            {
                totT++;
            }
            else
            {
                totS++;
            }
        }
        int[] ret = {totT, totS};
        return ret;
    }

    public double meanPaid(String from, String to)
    {
        ArrayList<Driver> drivers = new ArrayList<>();
        removeFromTo(drivers, from, to);

        double paid = 0;
        for (Driver d : drivers)
        {
            if(d.getSub() == null)
            {
                GregorianCalendar exitTime = new GregorianCalendar();
                if(d.getTimePaid() != null)
                {
                    exitTime = d.getTimePaid();
                }
                // Uso secondi per il testing
                paid += ChronoUnit.SECONDS.between(d.getTimeIn().toZonedDateTime(), exitTime.toZonedDateTime()) * d.getTariff();
            }
            else
            {
                paid += d.getSubCost();
            }
        }

        if(drivers.size() > 0)
        {
            paid = (double) paid / drivers.size();
        }
        return paid;
    }


    private GregorianCalendar[] parseDates(String from, String to)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = new Date();
        Date dateTo = new Date();
        try
        {
            dateFrom = sdf.parse(from);
            dateTo = sdf.parse(to);
        }
        catch (ParseException ex)
        {
            throw new RuntimeException("Errore nel parsing della data");
        }
        GregorianCalendar[] ret = new GregorianCalendar[2];
        ret[0] = (GregorianCalendar) Calendar.getInstance();
        ret[0].setTime(dateFrom);
        ret[1] = (GregorianCalendar) Calendar.getInstance();
        ret[1].setTime(dateTo);
        return ret;
    }

    private Boolean checkBetween(GregorianCalendar[] fromTo, GregorianCalendar toCheck)
    {
        if(fromTo[0].before(toCheck) && fromTo[1].after(toCheck))
        {
            return true;
        }
        return false;
    }

    private void removeFromTo(ArrayList<Driver> drivers,String from, String to)
    {
        ArrayList<Driver> toBeRemoved = new ArrayList<>();
        drivers.addAll(db.getData().values());
        GregorianCalendar[] fromTo = parseDates(from, to);
        for (Driver d : drivers)
        {
            if(!checkBetween(fromTo, d.getTimeIn()))
            {
                toBeRemoved.add(d);
            }
        }
        drivers.removeAll(toBeRemoved);
    }
}
