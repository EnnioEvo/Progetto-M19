package main;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Subscription {

    private String carId;
    private GregorianCalendar dateStart,dateFinish;
    private boolean paysub;
    public Subscription(String carId) {
        this.carId = carId;
        this.paysub = false;
        this.dateStart = new GregorianCalendar();
        this.dateFinish = makesub();
    }

    private GregorianCalendar makesub(){

        //creo solo un caso: abbonamento mensile

        GregorianCalendar d = new GregorianCalendar();
        d.add(Calendar.MONTH, 1); // funzione corretta, mi aggiunge un mese alla data odierna, ma stesso problema
        // della classe manager, non riesco a stampare il formato Gregorian
        return d;
    }



    //get and set

    public void setPaysub(boolean paysub) {
        this.paysub = paysub;
    }

    public String getCarId() {
        return carId;
    }

    public GregorianCalendar getDateFinish() {
        return dateFinish;
    }
}
