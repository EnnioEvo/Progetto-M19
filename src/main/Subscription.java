package main;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Subscription
{
    private GregorianCalendar dateStart, dateFinish, datePaidExtra;
    private boolean paySub, inPark;
    protected double cost;
    public Subscription()
    {
        this.paySub = false;
        this.dateStart = new GregorianCalendar();

        this.dateFinish = makesub();
        this.inPark = false;
    }

    public abstract GregorianCalendar makesub();


    @Override
    public String toString()
    {
        return "Abbonamento scade:" + dateFinish.toZonedDateTime().toString();
    }

    //get and set

    public void setPaySub(boolean paySub)
    {
        this.paySub = paySub;
    }

    public boolean getPaySub()
    {
        return paySub;
    }

    public GregorianCalendar getDateFinish()
    {
        return dateFinish;
    }

    public GregorianCalendar getDatePaidExtra()
    {
        return datePaidExtra;
    }

    public void setDatePaidExtra(GregorianCalendar datePaidExtra)
    {

        this.datePaidExtra = datePaidExtra;
    }

    public boolean getInPark()
    {
        return inPark;
    }

    public void setInPark(boolean inPark)
    {
        this.inPark = inPark;
    }
}
