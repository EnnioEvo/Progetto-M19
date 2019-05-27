package main;

import java.util.GregorianCalendar;

public class Driver
{
    private String carId;
    private GregorianCalendar timeIn;
    private GregorianCalendar timePaid;
    private boolean Paid;
    private Subscription sub;


    public Driver(String carId)
    {
        this.carId = carId;
        this.timeIn = new GregorianCalendar();
        this.Paid = false;
    }

    public void setTimePaid(GregorianCalendar timePaid)
    {
        this.timePaid = timePaid;
    }

    public void makeSub()
    {
        sub = new MonthlySubscription();
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
        Paid = paid;
    }

    public void setPaidSub(boolean paid)
    {
        sub.setPaySub(paid);
    }


    public String getCarId() { return carId; }

    public GregorianCalendar getTimeIn() { return timeIn; }

    public GregorianCalendar getTimePaid() { return timePaid; }

    public boolean isPaid() { return Paid;  }

    // get and set ''inpark''
    public boolean getInPark(){return sub.getInPark();}

    public void setInPark(boolean inPark) {sub.setInPark(inPark);}
}
