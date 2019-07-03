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
        return "Abbonamento scade:" + dateFinish.toZonedDateTime().toString() + ", abbonamento pagato: " + (paySub?"Si":"No");
    }

    public String infoClient()
    {
        return "--$subType=" + getClass().getName() + "$dateFinish=" + dateFinish.toZonedDateTime() + "$paySub=" + paySub + "$subPayementExpired=" + subPayementExpired + "$cost=" + cost + "$inPark=" + inPark;
    }

    //Get and set

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

    public void setDateFinish(GregorianCalendar c)
    {
        dateFinish = c;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost)
    {
        this.cost = cost;
    }
}