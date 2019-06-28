package main.Manager;

import main.Manager.Subscriptions.AnnualSub;
import main.Manager.Subscriptions.MonthlySubscription;
import main.Manager.Subscriptions.SemestralSub;
import main.Manager.Subscriptions.Subscription;

import java.util.GregorianCalendar;

public class Driver
{
    private String carId;
    private GregorianCalendar timeIn;
    private GregorianCalendar timePaid;
    private double tariff;
    private boolean paid;
    private boolean ticketPayementExpired;
    private Subscription sub;


    public Driver(String carId)
    {
        this.carId = carId;
        this.timeIn = new GregorianCalendar();
        this.paid = false;
        this.ticketPayementExpired = false;
        //Per testing
        if (carId.equals("test0001"))
        {
            timePaid = new GregorianCalendar();
            paid = true;
        }
    }

    public void makeSub()
    {
        // DA ELIMINARE ALLA FINE
        sub = new MonthlySubscription(80.0);
        if (carId.equals("test0002"))
        {
            setPaidSub(true);
        }
    }

    // Aggiunto per il database
    public void setSub(Subscription s)
    {
        sub = s;
    }

    public void makeMonthlySub(double cost)
    {
        sub = new MonthlySubscription(cost);
        if (carId.equals("test0002"))
        {
            setPaidSub(true);
        }
    }

    public void makeSemestralSub(double cost)
    {
        sub = new SemestralSub(cost);
    }

    public void makeAnnualMonthly(double cost)
    {
        sub = new AnnualSub(cost);
    }



    public String getDriverInfo()
    {
        return "Cliente: " + getCarId() + ", ingresso: " + getTimeIn().toZonedDateTime().toString() + ", pagato: " + (getTimePaid()==null?"No":getTimePaid().toZonedDateTime().toString()) + ", abbonamento: " + (printSub()==null?"No":printSub());
    }

    public String infoClient()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("info--$carId=" + carId);
        sb.append("$timeIn=" + timeIn.toZonedDateTime().toString());
        if(timePaid == null)
        {
            sb.append("$timepaid=0");
        }
        else
        {
            sb.append("$timepaid=" + timePaid.toZonedDateTime().toString());
        }
        sb.append("$tariff=" + tariff + "$paid=" + paid + "$ticketPayementExpired=" + ticketPayementExpired);
        if(sub == null)
        {
            sb.append("--0");
        }
        else
        {
            sb.append(sub.infoClient());
        }

        return sb.toString();
    }

    public String printSub()
    {
        return (sub==null?null:sub.toString());
    }

    public GregorianCalendar getDateFinishOfSub()
    {
        return sub.getDateFinish();
    }

    public void setDateFinishOfSub(GregorianCalendar c)
    {
        sub.setDateFinish(c);
    }

    public Subscription getSub() {
        return sub;
    }

    public boolean getPaySub()
    {
        return sub.getPaySub();
    }

    public Double getSubCost(){ return sub.getCost();}

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

    public void setTimeIn(GregorianCalendar timeIn)
    {
        this.timeIn = timeIn;
    }

    public GregorianCalendar getTimePaid() { return timePaid; }

    public void setTimePaid(GregorianCalendar timePaid)
    {
        this.timePaid = timePaid;
    }

    public boolean isPaid() { return paid;  }

    // get and set ''inpark''
    public boolean getInPark(){return sub.getInPark();}

    public void setInPark(boolean inPark) {sub.setInPark(inPark);}

    public boolean isTicketPayementExpired() {
        return ticketPayementExpired;
    }

    public void setTicketPayementExpired(boolean ticketpayementExpired) {
        this.ticketPayementExpired = ticketpayementExpired;
    }

    public boolean getSubPayementExpiredOfSub()
    {
        return sub.getSubPayementExpired();
    }

    public void setSubPayementExpiredOfSub(boolean b)
    {
        sub.setSubPayementExpired(b);
    }

    public void setCostOfSub(double d)
    {
        sub.setCost(d);
    }

    public double getTariff()
    {
        return tariff;
    }

    public void setTariff(double tariff)
    {
        this.tariff = tariff;
    }
}