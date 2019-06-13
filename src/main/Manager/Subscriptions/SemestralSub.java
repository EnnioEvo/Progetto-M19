package main.Manager.Subscriptions;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SemestralSub extends Subscription {
    public SemestralSub(Double cost)
    {
        this.cost = cost;
    }
    @Override
    public GregorianCalendar makesub() {
        GregorianCalendar d = new GregorianCalendar();
        d.add(Calendar.MONTH, 6);
        return d;

    }
}
