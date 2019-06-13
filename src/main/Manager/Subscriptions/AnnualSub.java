package main.Manager.Subscriptions;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnnualSub extends Subscription {
    public AnnualSub(Double cost){ this.cost = cost;}
    @Override
    public GregorianCalendar makesub() {
        GregorianCalendar d = new GregorianCalendar();
        d.add(Calendar.MONTH, 12);
        return d;
    }
}
