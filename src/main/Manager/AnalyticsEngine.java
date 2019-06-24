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

        hours = (double) hours / drivers.size();
        return hours;
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
}
