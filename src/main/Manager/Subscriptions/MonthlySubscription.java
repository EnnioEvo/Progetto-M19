package main.Manager.Subscriptions;

import main.Manager.Manager;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthlySubscription extends Subscription
{
    public MonthlySubscription()
    {
        this.cost = Manager.monthlyCost;
    }

    public GregorianCalendar makesub()
    {


        GregorianCalendar d = new GregorianCalendar();
        d.add(Calendar.MONTH, 1); // funzione corretta, mi aggiunge un mese alla data odierna, ma stesso problema
        // della classe manager, non riesco a stampare il formato Gregorian
        return d;
    }
}
