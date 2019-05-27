package main;

import Exceptions.SubdivisionException;
import main.Peripherals.Column;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

//CHANGED METHODS: FIRST TEST: positive
public class Manager
{
    public static double monthlyCost;


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
    //private ArrayList<Subscription> sublist;  Ora sono in subDrivers

    //aggiungo deltaTime
    private final static int deltaTimePaid = 10;  //In minuti


    public Manager()
    {
        this.floorsList = new ArrayList<>();
        this.freeSpacesTot = 0;
        this.freeSpacesSubTot = 0;
        this.freeSpacesTicketTot = 10;
        this.freeSpacesSubNow = 0;
        this.freeSpacesTicketNow = 0;
        this.drivers = new ArrayList<>();
        this.subDrivers = new ArrayList<>();
        this.entryToT = 0;

        //arraylist abbonamenti
        //this.sublist = new ArrayList<>();
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

    public void removeFloor(int rm)
    {
        Floor toBeRemoved = new Floor(-1, -1);
        for (Floor f : floorsList)
        {
            if(f.getId() == rm)
            {
                //NB mai rimuovere oggetti in un foreach
                toBeRemoved = f;
            }
        }
        floorsList.remove(toBeRemoved);
        changeFloorId();
        freeSpacesTot = setFreeSpacesTot();
    }

//******************* metodi d'ingresso********************

    public boolean entryTicket(String carId)
    {
        boolean entry = false;
        if(freeSpacesTicketNow + 1 > freeSpacesTicketTot)
        {
            System.out.println("Posti ticket finiti");
        } else if (checkTicket(carId)){
            entry = false;
            System.out.println("ERROR: targa: "+ carId + "  già presente all'interno del parcheggio");
        } else
        {
            freeSpacesTicketNow++;
            entryToT++;   //Perche non viene incrementata all'ingresso degli abbonati?

            drivers.add(new Driver(carId));

            //stampa fittizia della tessera
            System.out.println(printTickt(carId));
            entry = true;
        }
        return entry;
    }

    public boolean entrySub(String carId)
    {
        boolean entry = false;
        if(freeSpacesSubNow + 1 > freeSpacesSubTot)
        {
            System.out.println("Abbonamenti  finiti");
        }
        else if(checkSub(carId) == false)
        {
            // aggiungo qui l'acquisto dell'abbonamento che va impletato nella gui
            Driver d = new Driver(carId);
            d.makeSub();
            System.out.println("Abbonamento acquistato");
            System.out.println(d.printSub());
            freeSpacesSubNow++; //NB: secondo me potremmo anche decrementarlo , e quando arriva a Zero il metodo non va piu,
            //ovviamente è la stessa cosa, dimmi cosa secondo te è più corretto
            subDrivers.add(d);
            d.setInPark(true);
            entry = true;
        }
        else
        {
            //controllo sulla validità dell'abbonamento per effettuare l'ingresso
            if(checkDateSub(carId) == false)
            {
                System.out.println("Abbonamento scaduto");
            }
            else if (checkInPark(carId)){

                entry = false;
                System.out.println("ERROR: targa: " +carId + "  già all'interno del parcheggio");
            }else
            {

                System.out.println("Ingresso abbonato avvenuto con successo");
                entry = true;
            }

        }
        return entry;
    }

//********************** fine metodi d'ingresso****************************

//*********************************metodi d'uscita***************************************

    public boolean exit(String carID)   //messo boolean per recuperare il check
    {
        boolean check = false;
        boolean exit = false;
        Driver toBeRemoved = new Driver("");
        //Da fare: thread che ogni ora elimina abbonamneti scaduti NON presenti in quel momento nel parcheggio
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
                if(GregorianCalendar.getInstance().after(d.getDateFinishOfSub()) || !d.getPaySub())
                {
                    //Controlla se ha pagato la tariffa extra dopo la scadenza dell'abbonamneto
                    if(checkDeltaTime(d.getDatePaidExtraOfSub()) && d.getPaySub())
                    {
                        exit = true;
                        System.out.println("Uscita abbonamento avvenuta con successo        " + d.getCarId());
                    }
                    else
                    {
                        System.out.println("ERROR: uscita abbonamento negata");
                    }
                }
                else
                {
                    exit = true;
                    System.out.println("Uscita abbonamento avvenuta con successo        " + d.getCarId());
                }
            }
        }
        for(Driver d : drivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
                if((!checkDeltaTime(d.getTimePaid())) || !d.isPaid())
                {
                    System.out.println("ERROR: uscita negata");
                }
                else
                {
                    exit = true;
                    //NB mai rimuovere oggetti in un foreach
                    toBeRemoved = d;
                    freeSpacesTicketNow--;
                    System.out.println("Uscita avvenuta con successo        " + d.getCarId());

                }
            }
        }
        drivers.remove(toBeRemoved);
        //Caso in cui la tessera non è riconosciuta per un qualsiasi motivo
        if(!check)
        {
            System.out.println("Tessera non riconosciuta");
        }
        return exit;   //con il check vedo se aprire la sbarra o tenerla chiusa
    }

    private boolean checkDeltaTime(GregorianCalendar dataDriverPaid)
    {
        GregorianCalendar dataNow = new GregorianCalendar();
        if(dataDriverPaid != null)
        {
            dataDriverPaid.add(Calendar.MINUTE, deltaTimePaid);
        }
        return dataNow.before(dataDriverPaid);

        /*double DeltaTime = dataNow.getTimeInMillis() - dataDriver.getTimeInMillis();
        DeltaTime = DeltaTime/(1000*60*60); //risalgo ai minuti
        return DeltaTime;*/ //VISTO: POSSIAMO ANCHE ELIMINARE IL DeltaTime se sei d'accordo
    }



    //********************************* fine metodi d'uscita*********************************

    // ho cambiato il metodo da ''private'' a ''public'' perchè non potevo settare dal main il numero dei posti per gli abbonati
    public void setSpacesSubdivision(int sub)
    {
        if(sub <= freeSpacesTot)
        {
            freeSpacesSubTot = sub;
            freeSpacesTicketTot = freeSpacesTot - sub;
        }
        else
        {
            throw new SubdivisionException("Non ci sono abbastanza posti");
        }
    }

    private int setFreeSpacesTot()  //Modificare non dovrebbe restituire nulla
    {
        int i = 0;
        for(Floor f : floorsList)
        {
            i += f.getFreeSpace();
        }
        return i;
    }

    private void changeFloorId()
    {
        for(int i=0;i<floorsList.size();i++)
        {
            floorsList.get(i).setId(i);
        }
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
        s += "IDTicket:   " + carId + "\n";
        for(Driver d : drivers)
        {
            if(d.getCarId().equals(carId)){
                s+= "Ora Ingresso:  " + d.getTimeIn().toZonedDateTime().toString(); // toZonedDateTime converte nel nuovo formato di tempo di java 1.8
            }
        }
        return s;
    }

    //*********************************** metodi 'check' per abbonamento****************************
    private boolean checkDateSub(String carID)
    {
        GregorianCalendar dataNow = new GregorianCalendar();
        boolean check = false;
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                if(dataNow.after(d.getDateFinishOfSub()))  //Pattern protected variations
                {
                    check = false;
                }
                else
                {
                    check = true;
                }
            }
        }
        return  check;
    }

    private boolean checkSub(String carID)
    {
        boolean check = false;
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
            }
        }
        return check;
    }

    private boolean checkInPark(String cardID)
    {
        boolean check = false;
        for (Driver d : subDrivers){
            if(d.getCarId().equals(cardID)){
                if(d.getInPark()){
                    check = true;
                }
            }
        }
        return check;
    }
// ************** fine metodi check abbonamento ************************************

//****************** metodo check in park per tickets *******************************

    private boolean checkTicket(String cardID)
    {
        boolean check = false;
        for (Driver d : drivers){
            if(d.getCarId().equals(cardID)){
                check = true;
            }
        }
        return  check;

    }

    //****************** fine metodo check in park per tickets *******************************

    //Get and set
    public void setTariff(int tariff)
    {
        this.tariff = tariff;
    }

    public Driver getDriver(String carId)
    {
        for(Driver d : drivers)
        {
            if(d.getCarId().equals(carId))
            {
                return d;
            }
        }
        for (Driver d : subDrivers)
        {
            if(d.getCarId().equals(carId))
            {
                return d;
            }
        }
        return null;
    }

    public ArrayList<Floor> getFloorsList()
    {
        return floorsList;
    }

    public int getTariff()
    {
        return tariff;
    }

    public int getFreeSpacesTot()
    {
        return freeSpacesTot;
    }

    public int getFreeSpacesSubTot()
    {
        return freeSpacesSubTot;
    }

    public int getFreeSpacesTicketTot()
    {
        return freeSpacesTicketTot;
    }

    public ArrayList<Driver> getDrivers()
    {
        return drivers;
    }

    public ArrayList<Driver> getSubDrivers()
    {
        return subDrivers;
    }
}
