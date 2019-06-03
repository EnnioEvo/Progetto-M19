package main.Manager;

import main.Manager.Subscriptions.MonthlySubscription;
import main.Manager.Subscriptions.Subscription;

import java.util.GregorianCalendar;

public class Driver
{
    private String carId;
    private GregorianCalendar timeIn;
    private GregorianCalendar timePaid;
    private boolean paid;
    private Subscription sub;


    public Driver(String carId)
    {
        this.carId = carId;
        this.timeIn = new GregorianCalendar();
        this.paid = false;
        //Per testing
        if (carId.equals("test0001"))
        {
            timePaid = new GregorianCalendar();
            paid = true;
        }
    }

    public void setTimePaid(GregorianCalendar timePaid)
    {
        this.timePaid = timePaid;
    }

    public void makeSub()
    {
        sub = new MonthlySubscription();
        if (carId.equals("test0002"))
        {
            setPaidSub(true);
        }
    }

    public String getDriverInfo()
    {
        return "Cliente: " + getCarId() + ", ingresso: " + getTimeIn().toZonedDateTime().toString() + ", pagato: " + (getTimePaid()==null?"No":getTimePaid().toZonedDateTime().toString()) + ", abbonamento: " + (printSub()==null?"No":printSub());
    }

    public String printSub()
    {
        return (sub==null?null:sub.toString());
    }

    public GregorianCalendar getDateFinishOfSub()
    {
        return sub.getDateFinish();
    }

    public boolean getPaySub()
    {
        return sub.getPaySub();
    }

    public GregorianCalendar getDatePaidExtraOfSub()
    {
        return sub.getDatePaidExtra();
    }

    public void setDatePaidExtraOfSub(GregorianCalendar c)
    {
        sub.setDatePaidExtra(c);
    }

    public void setPaid(boolean paid)
    {
        this.paid = paid;
    }

    public void setPaidSub(boolean paid)
    {
        sub.setPaySub(paid);
    }


    public String getCarId() { return carId; }

    public GregorianCalendar getTimeIn() { return timeIn; }

    public GregorianCalendar getTimePaid() { return timePaid; }

    public boolean isPaid() { return paid;  }

    // get and set ''inpark''
    public boolean getInPark(){return sub.getInPark();}

    public void setInPark(boolean inPark) {sub.setInPark(inPark);}
}
