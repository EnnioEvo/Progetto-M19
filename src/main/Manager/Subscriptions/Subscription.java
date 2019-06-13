package main.Manager.Subscriptions;

import java.util.GregorianCalendar;

public abstract class Subscription
{
    private GregorianCalendar dateStart, dateFinish;
    private boolean paySub, inPark, subPayementExpired;
    protected double cost;
    public Subscription()
    {
        this.paySub = false;
        this.dateStart = new GregorianCalendar();

        this.dateFinish = makesub();
        this.inPark = false;
        this.subPayementExpired = false;

    }

    public abstract GregorianCalendar makesub();


    @Override
    public String toString()
    {
        return "Abbonamento scade:" + dateFinish.toZonedDateTime().toString();
    }

    public String infoClient()
    {
        return "--$dateFinish=" + dateFinish + "$paySub=" + paySub + "$subPayementExpired=" + subPayementExpired + "$cost=" + cost;
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


    public boolean getInPark()
    {
        return inPark;
    }

    public void setInPark(boolean inPark)
    {
        this.inPark = inPark;
    }

    public boolean getSubPayementExpired()
    {
        return subPayementExpired;
    }

    public void setSubPayementExpired(boolean subPayementExpired)
    {
        this.subPayementExpired = subPayementExpired;
    }
}