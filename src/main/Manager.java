package main;

import main.Peripherals.Column;
import java.lang.Math.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

//CHANGED METHODS: FIRST TEST: positive
public class Manager
{
    private ArrayList<Floor> floorsList;
    private int freeSpacesTot, freeSpacesSubTot, freeSpacesTicketTot;
    private int freeSpacesSubNow, freeSpacesTicketNow;
    private int tariff;
    private ArrayList<Driver> drivers, subDrivers;
    private ArrayList<Cash> cashList;
    private ArrayList<Column> columnList;
    // paymantAnalytics variables
    private int entryToT;
    private double DAYS=365, MONTH=12;

    //aggiungo l'arraylist degli abbonamenti
    private ArrayList<Subscription> sublist;


    public Manager()
    {
        this.floorsList = new ArrayList<>();
        this.freeSpacesTot = 0;
        this.freeSpacesSubTot = 0;
        this.freeSpacesTicketTot = 0;
        this.freeSpacesSubNow = 0;
        this.freeSpacesTicketNow = 0;
        this.drivers = new ArrayList<>();
        this.subDrivers = new ArrayList<>();
        this.entryToT = 0;

        //arraylist abbonamenti
        this.sublist = new ArrayList<>();
    }

    // ho cambiato il metodo perchè non settava il numero di posti liberi dei piani
    public void makeFloors(int numFloors, int numSpaces)
    {
        for(int i=0; i<numFloors; i++)
        {
            Floor floor = new Floor(floorsList.size(), numSpaces);
            floorsList.add(floor);
        }
        freeSpacesTot = setFreeSpacesTot();
    }


    public void entryTicket(String carId)
    {
        if (freeSpacesTicketNow + 1 > freeSpacesTicketTot)
        {

            throw new RuntimeException("posti ticket finiti");
        }
        else
        {
            freeSpacesTicketNow++;
            entryToT++;
            drivers.add(new Driver(carId));

            //stampa fittizia della tessera
            System.out.println(printTickt(carId));
        }
    }

    public void entrySub(String carId) {
        if (freeSpacesSubNow + 1 > freeSpacesSubTot) {
            throw new RuntimeException("abbonamenti  finiti");
        } else if (checkSub(carId) == false) {
            // aggiungo qui l'acquisto dell'abbonamento che va impletato nella gui
            Subscription s = new Subscription(carId);
            System.out.println("abbonamento acquistato");
            sublist.add(s);
            System.out.println(s);
            freeSpacesSubNow++;
            subDrivers.add(new Driver(carId));
        } else {
            //controllo sulla validità dell'abbonamento per effettuare l'ingresso
            if(checkdateSub(carId) == false) {
                throw new RuntimeException("abbonamento scaduto");
            }else
                {
                    System.out.println("ingresso abbonato avvenuto con successo");

            }

        }
    }
    
    // ho cambiato il metodo da ''private'' a ''public'' perchè non potevo settare dal main il numero dei posti per gli abbonati
    public void setSpacesSubdivision(int sub)
    {
        if (sub <= freeSpacesTot)
        {
            freeSpacesSubTot = sub;
            freeSpacesTicketTot = freeSpacesTot - sub;
        }
        else
        {
            throw new RuntimeException("Troppi sub");
        }
    }

    private int setFreeSpacesTot()
    {
        int i = 0;
        for (Floor f : floorsList)
        {
            i += f.getFreeSpace();
        }
        return i;
    }



    // analisi ingressi e incassi
    public void Analytics()
    {

        // NumberFormat arrotonda un double per eccesso alle ultime due cifre decimali  0.41666666 --> 0.417
        NumberFormat nf = new DecimalFormat("0.000");
        double meanDay = (double)entryToT / DAYS;
        double meanMonth = (double)entryToT / MONTH;
        double meanPayDay = meanDay*tariff;
        double meanPayMth = meanMonth*tariff;

        System.out.println("MEDIA INGRESSI: \nGioralieri:  " + nf.format(meanDay) + "\t" + "Mensili:  "+nf.format(meanMonth));
        System.out.println("**********************************");
        System.out.println("MEDIA INCASSI: \nGioralieri:  " + nf.format(meanPayDay) + "\t" + "Mensili:  "+nf.format(meanPayMth));
    }

    private String printTickt(String carId)
    {
        String s = "";
        s += "IDTicket:   " + carId + "   ";
        for(Driver d : drivers)
        {
            if (d.getCarId().equals(carId)){
                s+= "Ora Ingresso:  " + d.getTimeIn(); // non riesco a stampare data e ora d'ingresso
            }
        }
        return s;
    }

    //*********************************** metodi 'check' per abbonamento****************************
    private boolean checkdateSub(String carID)
    {
        GregorianCalendar dataNow = new GregorianCalendar();
        boolean check = false;
        for(Subscription s : sublist){
            if(s.getCarId().equals(carID)){
                if(dataNow.after(s.getDateFinish())){
                    check = false;
                } else {
                    check = true;
                }
            }
        }
        return  check;
    }
    private boolean checkSub(String carID)
    {
        boolean check = false;
        for(Subscription s : sublist){
            if(s.getCarId().equals(carID)){
                check = true;
            }
        }
        return check;
    }
// ************** fine metodi check abbonamento ************************************

    // get and set
    public void setTariff(int tariff)
    {
        this.tariff = tariff;
    }


}
