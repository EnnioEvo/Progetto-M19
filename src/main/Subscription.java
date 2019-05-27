package main;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Subscription
{

    private GregorianCalendar dateStart, dateFinish, datePaidExtra;
    private boolean paySub, inPark;
    public Subscription()
    {
        this.paySub = false;
        this.dateStart = new GregorianCalendar();
        this.dateFinish = makesub();
        this.inPark = false;
    }

    private GregorianCalendar makesub()
    {

        //creo solo un caso: abbonamento mensile

        GregorianCalendar d = new GregorianCalendar();
        d.add(Calendar.MONTH, 1); // funzione corretta, mi aggiunge un mese alla data odierna, ma stesso problema
        // della classe manager, non riesco a stampare il formato Gregorian
        return d;
    }

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

    public boolean isInPark() {
        return inPark;
    }

    public void setInPark(boolean inPark) {
        this.inPark = inPark;
    }
}
