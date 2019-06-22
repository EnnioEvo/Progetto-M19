package JUnitTest;

import Exceptions.NotEmptyFloorException;
import Exceptions.SubdivisionException;
import GUIs.ManagerGUI;
import main.Manager.DataBase.DataBaseAdapter;
import main.Manager.DataBase.TextDataBaseAdapter;
import main.Manager.Driver;
import main.Manager.Floor;
import main.Peripherals.Cash.Cash;
import main.Peripherals.Columns.Column;
import main.Peripherals.Observer;
import net.Server;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

@SuppressWarnings("Duplicates")
//ho copiato la classe manager per poter evitare il lancio della GUI ad ogni tets,
// ho tolto solo la parte ''server'' del manager il resto è invariato
public class TestManager
{
    private double monthlyCost=1, semestralCost, annualCost,extraCost;

    private int peripheralId = 0;

    private Server server;

    private DataBaseAdapter db;

    private ArrayList<Floor> floorsList;
    private int freeSpacesTot, freeSpacesSubTot, freeSpacesTicketTot;
    private int freeSpacesSubNow, freeSpacesTicketNow;
    private double tariff;
    private ArrayList<Driver> drivers, subDrivers;
    private ArrayList<Cash> cashList;
    private ArrayList<Column> columnList;
    // paymantAnalytics variables
    private int entryToT;
    private double DAYS=365, MONTH=12;

    //aggiungo l'arraylist degli abbonamenti
    //private ArrayList<Subscription> sublist;  Ora sono in subDrivers

    //aggiungo deltaTime
    private int deltaTimePaid;  //In minuti


    public TestManager() {
        this.floorsList = new ArrayList<>();
        this.freeSpacesTot = 0;
        this.freeSpacesSubTot = 0;
        this.freeSpacesTicketTot = 0;
        this.freeSpacesSubNow = 0;
        this.freeSpacesTicketNow = 0;
        this.drivers = new ArrayList<>();
        this.subDrivers = new ArrayList<>();
        this.entryToT = 0;
        this.columnList = new ArrayList<>();

        this.db = new TextDataBaseAdapter("./db");
    }

    // ho cambiato il metodo perchè non settava il numero di posti liberi dei piani
    public void makeFloors(int numFloors, int numSpaces)
    {
        for(int i=0; i<numFloors; i++)
        {
            Floor floor = new Floor(floorsList.size(), numSpaces);
            floorsList.add(floor);
        }
        setFreeSpacesTot();
    }

    public void removeFloor(int rm)
    {
        Floor toBeRemoved = new Floor(-1, -1);
        for (Floor f : floorsList)
        {
            if(f.getId() == rm)
            {
                if (f.getCountCarIn() != 0)
                {
                    throw new NotEmptyFloorException("Non puoi rimuovere un piano non vuoto.");
                }
                //NB mai rimuovere oggetti in un foreach
                toBeRemoved = f;
            }
        }
        floorsList.remove(toBeRemoved);
        changeFloorId();
        setFreeSpacesTot();
    }

//******************* metodi d'ingresso********************

    public String entryTicket(String carId)
    {
        boolean entry = false;
        String info;
        if (!checkCarId(carId))
        {
            info = "Targa non valida.";
            System.out.println(info);
            return "entryNo--" + info;
        }

        if (freeSpacesTicketNow + 1 > freeSpacesTicketTot)
        {
            info = "Posti ticket finiti.";
            System.out.println(info);
        }
        else if (checkSubOrTicket(carId))
        {
            info = "Ingresso fallito: targa: " + carId + " già presente all'interno del parcheggio.";
            System.out.println(info);
        }
        else
        {
            freeSpacesTicketNow++;
            entryToT++;   //Perche non viene incrementata all'ingresso degli abbonati?
            Driver d = new Driver(carId);
            drivers.add(d);
            // Nuovo ingresso, non rimuovo dal db
            db.writeData(d, false);

            //stampa fittizia della tessera
            info = "Ingresso riuscito, " + printTickt(carId);
            System.out.println(info);
            entry = true;
        }
        if (entry)
        {
            randomEntry();
            return "entryOk--" + info;
        }
        else
        {
            return "entryNo--" + info;
        }
    }

    public String entrySub(String carId, String typeSub)
            // codice sub MM = mensile, SM= semestrale, AN=annuale.
    {
        String info;
        if(!checkCarId(carId))
        {
            info = "Targa non valida";
            System.out.println(info);
            return "entryNo--" + info;
        }

        boolean entry = false;
        if(checkSubOrTicket(carId) == false)
        {
            if(typeSub.equals("XX"))
            {
                info = "Non hai ancora l'abbonamento";
                return "entryNo--" + info;
            }
            if(freeSpacesSubNow + 1 > freeSpacesSubTot)
            {
                info = "Abbonamenti  finiti";
                System.out.println(info);
            }
            else
            {
                // aggiungo qui l'acquisto dell'abbonamento che va impletato nella gui
                Driver d = new Driver(carId);
                 switch (typeSub){
                     case "Mensile":
                         //d.makeSub();  // DA ELIMINARE, ABBONAMETO DI TEST
                         d.makeMonthlySub(monthlyCost);
                         break;
                     case "Semestrale":
                         d.makeSemestralSub(semestralCost);
                         break;
                     case "Annuale":
                         d.makeAnnualMonthly(annualCost);
                         break;
                 }

                info = "Abbonamento acquistato, " + d.printSub();
                System.out.println(info);
                freeSpacesSubNow++; //NB: secondo me potremmo anche decrementarlo , e quando arriva a Zero il metodo non va piu,
                //ovviamente è la stessa cosa, dimmi cosa secondo te è più corretto
                subDrivers.add(d);
                // Nuovo ingresso, non rimuovo dal db
                db.writeData(d, false);
                d.setInPark(true);
                entry = true;
            }
        }
        else
        {
            //controllo sulla validità dell'abbonamento per effettuare l'ingresso
            if (checkTicket(carId))
            {
                info = "Ingresso non riuscito, la targa risulta già all'interno con un ticket.";
                System.out.println(info);
            }
            else if (checkDateSub(carId) == false)
            {
                info = "Abbonamento scaduto, ora è possibile riacquistarlo.";
                System.out.println(info);
                removeSub(carId);
                freeSpacesSubNow--;
            }
            else if (checkInPark(carId))
            {
                info = "Ingresso non riuscito, targa: " + carId + " già all'interno del parcheggio";
                System.out.println(info);
            }
            else
            {
                info = "Ingresso abbonato avvenuto con successo";
                System.out.println(info);
                Driver d = getDriver(carId);
                d.setInPark(true);
                entry = true;
            }
        }
        if (entry)
        {
            randomEntry();
            return "entryOk--" + info;
        }
        else
        {
            return "entryNo--" + info;
        }
    }

//********************** fine metodi d'ingresso****************************

//*********************************metodi d'uscita***************************************

    public String exit(String carID)   //messo boolean per recuperare il check
    {
        boolean check = false;
        boolean exit = false;
        String info = "";
        Driver toBeRemoved = new Driver("");
        //Da fare: thread che ogni ora elimina abbonamneti scaduti NON presenti in quel momento nel parcheggio
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID) && d.getInPark())
            {
                check = true;
                if(GregorianCalendar.getInstance().after(d.getDateFinishOfSub()) || !d.getPaySub())
                {
                    //Controlla se ha pagato la tariffa extra dopo la scadenza dell'abbonamneto
                    if(checkDeltaTime(d.getDateFinishOfSub()) && d.getPaySub())
                    {
                        exit = true;
                        info = "Uscita abbonamento avvenuta con successo " + d.getCarId();
                        System.out.println(info);
                        d.setInPark(false);
                    }
                    else
                    {
                        // Se è pagato, allora è scaduto
                        if(!d.getPaySub())
                        {
                            info = "L'abbonamento è scaduto, si prega di tornare alle casse.";
                            d.setSubPayementExpiredOfSub(true);
                            // Aggiorno l'entry dell'utente nel db
                            db.writeData(d, true);
                        }
                        else
                        {
                            info = "L'abbonamento non è pagato, si prega di tornare alle casse.";
                        }
                        System.out.println(info);
                    }
                }
                else
                {
                    exit = true;
                    info = "Uscita abbonamento avvenuta con successo " + d.getCarId();
                    System.out.println(info);
                    d.setInPark(false);
                }
            }
            else if (d.getCarId().equals(carID))
            {
                check = true;
                info = "Uscita fallita, l'abbonato non è nel parcheggio " + d.getCarId();
                System.out.println(info);
            }
        }
        for(Driver d : drivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
                if((!checkDeltaTime(d.getTimePaid())) || !d.isPaid())
                {
                    // Se è pagato, vuol dire che è scaduto
                    if(d.isPaid())
                    {
                        info = "E' passato troppo tempo dal pagamento, si prega di tornare alle casse.";
                        d.setTicketPayementExpired(true);
                        // Aggiorno l'entry dell'utente nel db
                        db.writeData(d, true);
                    }
                    else
                    {
                        info = "Ticket non pagato, torna in cassa!";
                    }
                    System.out.println(info);
                }
                else
                {
                    exit = true;
                    //NB mai rimuovere oggetti in un foreach
                    toBeRemoved = d;
                    freeSpacesTicketNow--;
                    info = "Uscita avvenuta con successo " + d.getCarId();
                    System.out.println(info);

                }
            }
        }
        drivers.remove(toBeRemoved);
        //Caso in cui la tessera non è riconosciuta per un qualsiasi motivo
        if(!check)
        {
            info = "Tessera non riconosciuta";
            System.out.println(info);
        }
        if (exit)
        {
            randomExit();
            return "exitOk--" + info;
        }
        else
        {
            return "exitNo--" + info;
        }
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

    private void setFreeSpacesTot()  //Modificare non dovrebbe restituire nulla
    {
        int i = 0;
        for(Floor f : floorsList)
        {
            i += f.getFreeSpace();
        }
        freeSpacesTot = i;
        freeSpacesTicketTot = freeSpacesTot - freeSpacesSubTot;
        //Gestico caso in cui eliminando i piani ho piu posti in abbonamneto che posti liberi
        if (freeSpacesTicketTot < 0)
        {
            freeSpacesSubTot = freeSpacesTicketTot;
            freeSpacesTicketTot = 0;
        }
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

    // nel metodo originale in questa stringa abbiamo l'ora in cui il drive inizia,per motivi di test
    // SOLO PER QUANTO RIGUARDA LA STAMPA, ho sostituito l'ora reale con una fittizia
    private String printTickt(String carId)
    {
        String s = "";
        s += "IDTicket:   " + carId;
        for(Driver d : drivers)
        {
            if(d.getCarId().equals(carId)){
                s+= ", ora Ingresso:    00:00:00:000" ; // toZonedDateTime converte nel nuovo formato di tempo di java 1.8
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

    private boolean checkSubOrTicket(String carID)
    {
        boolean check = false;
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
            }
        }
        for (Driver d : drivers)
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
            if(d.getCarId().equals(cardID))
            {
                if(d.getInPark())
                {
                    check = true;
                }
            }
        }
        return check;
    }

    private boolean checkCarId(String carId)
    {
        if(carId.length() == 8)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
// ************** fine metodi check abbonamento ************************************

//****************** metodo check in park per tickets *******************************

    private boolean checkTicket(String carID)
    {
        boolean check = false;
        for (Driver d : drivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
            }
        }

        return  check;
    }

    //*******************************************

    public String getDriverClientInfo(String carID)
    {
        StringBuilder sb = new StringBuilder();
        boolean check = false;
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                sb.append(d.infoClient());
                check = true;
            }
        }
        for (Driver d : drivers)
        {
            if(d.getCarId().equals(carID))
            {
                sb.append(d.infoClient());
                check = true;
            }
        }
        if(check)
        {
            return sb.toString();
        }
        else
        {
            return "info--0";
        }
    }



    //****************** fine metodo check in park per tickets *******************************

    private void removeSub(String carID)
    {
        Driver toBeRemoved = new Driver("");
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                toBeRemoved = d;
            }
        }
        subDrivers.remove(toBeRemoved);
    }

    private void randomEntry()
    {
        Random r = new Random();
        int i = r.nextInt(floorsList.size());
        floorsList.get(i).addCar();
    }

    private void randomExit()
    {
        Random r = new Random();
        int i = r.nextInt(floorsList.size());
        floorsList.get(i).deleteCar();
    }

    private void addObserver(List<Observer> list, Observer obs)
    {
        list.add(obs);
    }

    private void notifyColumns()
    {
        for (Column c : columnList)
        {
            c.notifyObs();
        }
    }

    /*public EntryColumn createEntryColumn()
    {
        EntryColumn col = new EntryColumn(peripheralId(), this);
        columnList.add(col);
        return col;
    }*/

    /*public ExitColumn createExitColumn()
    {
        ExitColumn col = new ExitColumn(peripheralId(), this);
        columnList.add(col);
        return col;
    }*/

    private String peripheralId()
    {
        peripheralId++;
        return "ID_" + peripheralId;
    }


    //Get and set
    public void setTariff(int tariff)
    {
        this.tariff = tariff;
        server.updatePeripherals("getTariff");
    }

    public void setDeltaTimePaid(int deltaTimePaid)
    {
        this.deltaTimePaid = deltaTimePaid;
        server.updatePeripherals("getTariff");
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

    public String getFloorsInfo()
    {
        StringBuilder sb = new StringBuilder();
        for (Floor f : floorsList)
        {
            sb.append(f.getFloorInfo());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getDriversInfo()
    {
        StringBuilder sb = new StringBuilder();
        for (Driver d : drivers)
        {
            sb.append(d.getDriverInfo());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getSubDriversInfo()
    {
        StringBuilder sb = new StringBuilder();
        for (Driver d : subDrivers)
        {
            sb.append(d.getDriverInfo());
            sb.append("\n");
        }
        return sb.toString();
    }



    public ArrayList<Floor> getFloorsList()
    {
        return floorsList;
    }

    public double getTariff()
    {
        return tariff;
    }

    public ArrayList<Double> getSubTariffs()
    {
        ArrayList<Double> t = new ArrayList<>();
        t.add(monthlyCost);
        t.add(semestralCost);
        t.add(annualCost);
        return t;
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

    public int getDeltaTimePaid() {
        return deltaTimePaid;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(double monthlyCost)
    {
        this.monthlyCost = monthlyCost;
        server.updatePeripherals("getSubTariffs");
    }

    public double getSemestralCost() {
        return semestralCost;
    }

    public void setSemestralCost(double semestralCost)
    {
        this.semestralCost = semestralCost;
        server.updatePeripherals("getSubTariffs");
    }

    public double getAnnualCost() {
        return annualCost;
    }

    public void setAnnualCost(double annualCost)
    {
        this.annualCost = annualCost;
        server.updatePeripherals("getSubTariffs");
    }

    public double getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(double extraCost) {
        this.extraCost = extraCost;
    }
}