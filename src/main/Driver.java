package main;

import java.util.GregorianCalendar;

public class Driver
{
    private String carId;
    private GregorianCalendar timeIn;
    private GregorianCalendar timePaid;
    private boolean Paid;

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

    public void setPaid(boolean paid) { Paid = paid; }

    public String getCarId() { return carId; }

    public GregorianCalendar getTimeIn() { return timeIn; }

    public GregorianCalendar getTimePaid() { return timePaid; }

    public boolean isPaid() { return Paid;  }
}
