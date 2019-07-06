package main.Parking;

import Exceptions.NotEmptyFloorException;
import Exceptions.SubdivisionException;
import GUIs.ManagerGUI;
import main.Parking.DataBase.DataBaseAdapter;
import main.Utilities.Observer;
import main.Utilities.ServiceFactory;
import net.Server;

import java.awt.*;
import java.util.*;


public class Parking
{
    private double monthlyCost=1, semestralCost, annualCost, extraCost;

    private int peripheralId = 0;

    private Server server;

    private DataBaseAdapter db;

    private AnalyticsEngine analyticsEngine;

    private Entrance entryMan;
    private Exit exitMan;

    private ArrayList<Floor> floorsList;
    private int freeSpacesTot, freeSpacesSubTot, freeSpacesTicketTot;
    private int freeSpacesSubNow, freeSpacesTicketNow;
    private double tariff;
    private ArrayList<Driver> drivers, subDrivers;
    //PaymantAnalytics variables
    private int entryToT;
    private double DAYS=365, MONTH=12;
    private HashMap<String, Command> commands;
    private Observer obs;
    private String infoBox;


    //Aggiungo deltaTime
    private int deltaTimePaid;  //In minuti

    //Costruttore parte server, gli passiamo il numero di porta
    public Parking(int port)

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

        System.setProperty("db.class.name", "main.Parking.DataBase.TextDataBaseAdapter");
        ServiceFactory sf = ServiceFactory.getInstance();
        this.db = sf.getDataBaseAdapter("./db.txt");

        this.analyticsEngine = new AnalyticsEngine(db);

        this.entryMan = new Entrance(this);
        this.exitMan = new Exit(this);

        createCommands();
        getDriversFromDb();

        Parking m = this;
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                ManagerGUI gui = new ManagerGUI(m);
                m.setObs(gui);
            }
        });

        this.server = new Server(port, this);
        this.server.startServer();

    }

    //Creo un secondo costruttore che serve solo nei test, lo creo così posso "aggirare" il lato server del manager (tolgo anche il caricamento da db)
    public Parking(){
        this.floorsList = new ArrayList<>();
        this.freeSpacesTot = 0;
        this.freeSpacesSubTot = 0;
        this.freeSpacesTicketTot = 0;
        this.freeSpacesSubNow = 0;
        this.freeSpacesTicketNow = 0;
        this.drivers = new ArrayList<>();
        this.subDrivers = new ArrayList<>();
        this.entryToT = 0;

        System.setProperty("db.class.name", "main.Parking.DataBase.TextDataBaseAdapter");
        ServiceFactory sf = ServiceFactory.getInstance();
        this.db = sf.getDataBaseAdapter("./db.txt");

        this.analyticsEngine = new AnalyticsEngine(db);

        this.entryMan = new Entrance(this);
        this.exitMan = new Exit(this);

        createCommands();
    }

    public static void main(String[] args)
    {
        if (args.length < 1) return;
        new Parking(Integer.parseInt(args[0]));
    }

    //Creo l'Hashmap e aggiungo al suo intero i comandi principali che posso ricevere dalle varie periferiche
    public void createCommands()
    {
        commands = new HashMap<>();
        commands.put("getId", (String[] args) -> "id--" + peripheralId());
        commands.put("help", (String[] args) -> {
            infoBox = "Richiesta assistenza alla periferica " + args[1];
            notifyObs();
            return "helpComing--Assitenza in arrivo, attendi.";
        });
        commands.put("entry", (String[] args) -> entryMan.entryTicket(args[1]));
        commands.put("entrySub", (String[] args) -> entryMan.entrySub(args[1], args[2]));
        commands.put("getTariff", (String[] args) -> "tariff--" + getTariff());
        commands.put("getSubTariffs", (String[] args) -> "subTariffs--" + getSubTariffs());
        commands.put("exit", (String[] args) -> exitMan.exit(args[1]));
        commands.put("driverInfo", (String[] args) -> {
            System.out.println("drinfo");
            return getDriverClientInfo(args[1]);
        });
        commands.put("setTicketPaid", (String[] args) -> setTicketPaid(args[1]));
        commands.put("setSubPaid", (String[] args) -> setSubPaid(args[1]));
        commands.put("extra", (String[] args) -> "extra--" + extraCost);
    }

    //Metodo che mi esegue il comando ricevuto da una delle periferiche
    public String executeCommand(String[] args)
    {
        String s = "";
        System.out.println(args[0]);
        try
        {
            s = commands.get(args[0]).execute(args);
        }
        catch(NullPointerException ex)
        {
            System.out.println("Comando errato");
        }
        return s;
    }

    //Riprendo utenti dal db
    private void getDriversFromDb()
    {
        ArrayList<Driver> totDrivers = new ArrayList<>();
        totDrivers.addAll(db.getData().values());
        ArrayList<Driver> toKeep = new ArrayList<>();
        for(Driver d : totDrivers)
        {
            if((d.getSub() == null && d.getTimePaid() == null) || (d.getSub() != null && (d.getDateFinishOfSub().after(GregorianCalendar.getInstance()) || d.getInPark())))
            {
                toKeep.add(d);
            }
        }

        int nSub = 0;
        for(Driver d : toKeep)
        {
            if(d.getSub() != null)
            {
                nSub++;
                subDrivers.add(d);
                freeSpacesSubNow++;
            }
            else
            {
                drivers.add(d);
                freeSpacesTicketNow++;
            }

        }

        //Creo piano fittizio.
        if(toKeep.size() > 0)
        {
            makeFloors(1, toKeep.size());
            setSpacesSubdivision(nSub);
            for(Driver d : toKeep)
            {
                //Conto come ingressi solo gli abbonati che sono dentro e i ticket
                if(d.getSub() == null || (d.getSub() != null && d.getInPark()))
                {
                    randomEntry();
                }
            }
        }
    }

    //Creo i piani: posso creare il parcheggio scegliendo il numero di  piani (numFloors) e la loro capienza (numSpaces)
    public void makeFloors(int numFloors, int numSpaces)
    {
        for(int i=0; i<numFloors; i++)
        {
            Floor floor = new Floor(floorsList.size(), numSpaces);
            floorsList.add(floor);
        }
        setFreeSpacesTot();
    }

    //Rimuove un piano: per eventuali modifiche del parcheggio , posso scegliere di eliminare un piano dal software che controlla la struttura
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
                toBeRemoved = f;
            }
        }
        floorsList.remove(toBeRemoved);
        changeFloorId();
        setFreeSpacesTot();
    }

    //Controlla il deltaTime, cioè il minutaggio massimo che può trascorrere dal momento che si paga il ticket all'uscita,
    //se il delta è rispettato l'uscita avviene senza problemi, in caso contrario il driver deve tornare alla cassa
    boolean checkDeltaTime(GregorianCalendar dataDriverPaid)
    {
        GregorianCalendar dataNow = new GregorianCalendar();
        if(dataDriverPaid != null)
        {
            dataDriverPaid.add(Calendar.MINUTE, deltaTimePaid);
        }
        return dataNow.before(dataDriverPaid);

    }

    //Determina il numero di  posti riservati agli abbonamenti
    public void setSpacesSubdivision(int sub)
    {
        if(sub <= freeSpacesTot)
        {
            if(freeSpacesTicketNow <= freeSpacesTot - sub && freeSpacesSubNow <= sub)
            {
                freeSpacesSubTot = sub;
                freeSpacesTicketTot = freeSpacesTot - sub;
            }
            else
            {
                throw new SubdivisionException("Non ci sono abbastanza posti");
            }
        }
        else
        {
            throw new SubdivisionException("Non ci sono abbastanza posti");
        }
    }
    //Determina il numero di posti riservati ai Ticket
    private void setFreeSpacesTot()
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

    //Dopo aver eliminato uno o più piani, con questo metodo sistemo gli indici restanti
    private void changeFloorId()
    {
        for(int i=0;i<floorsList.size();i++)
        {
            floorsList.get(i).setId(i);
        }
    }

    //Analisi ingressi e incassi
    public double analyticsMean(String from, String to)
    {
        return analyticsEngine.meanTicketTimeIn(from, to);
    }

    public double analyticsTotPaid(String from, String to)
    {
        return analyticsEngine.meanPaid(from, to);
    }

    public int[] analyticsTotTicketandSub(String from, String to)
    {
        return analyticsEngine.totTicketAndSub(from, to);
    }


    //Stampa le informazioni del driver in ingresso
    String printTicket(String carId)
    {
        String s = "";
        s += "IDTicket:   " + carId;
        for(Driver d : drivers)
        {
            if(d.getCarId().equals(carId)){
                s+= ", ora Ingresso:  " + d.getTimeIn().toZonedDateTime().toString(); // toZonedDateTime converte nel nuovo formato di tempo di java 1.8
            }
        }
        return s;
    }

    //*********************************** metodi 'check' per abbonamento****************************
   //Controlla se l'abbonamento è scaduto
    boolean checkDateSub(String carID)
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
    //Controlla se la targa passata è presente all'interno dell'arraylist dei driver o dei sub
    boolean checkSubOrTicket(String carID)
    {
        boolean check = checkTicket(carID);
        for(Driver d : subDrivers)
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
            }
        }
        return check;
    }
    //Controlla se il driver è già all'interno del parcheggio
    boolean checkInPark(String cardID)
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
    //Controlla se la sintassi della targa sia corretta
    boolean checkCarId(String carId)
    {
        if(carId.length() == 7)
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

    //Controlla se il driver che sta tentando di far l'ingresso è già in possesso di un'altro ticket
    boolean checkTicket(String carID)
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

    //Questo metodo mi restituisce le informaizoni relative al driver o al sub
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
    //Rimuove dall'arraylist degl'abbonati, il Driver a cui è scaduto l'abbonamento
    void removeSub(String carID)
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

    String setTicketPaid(String carId)
    {
        Driver d = getDriver(carId);
        if(d == null)
        {
            return "logError--XX";
        }
        d.setTimePaid(new GregorianCalendar());
        d.setPaid(Boolean.TRUE);
        db.writeData(d, true);
        return "logOk--XX";
    }

    String setSubPaid(String carId)
    {
        Driver d = getDriver(carId);
        if(d == null)
        {
            return "logError--XX";
        }
        //Se l'abbonamneto era scaduto aggiorno la sua validità
        if(d.getDateFinishOfSub().before(new GregorianCalendar()))
        {
            d.setDateFinishOfSub(new GregorianCalendar());
        }
        d.setPaidSub(Boolean.TRUE);
        db.writeData(d, true);
        return "logOk--XX";
    }
    //Simula l'ingresso di un driver in un piano
    void randomEntry()
    {
        Random r = new Random();
        int i;
        //Impedisco che si superi il numero massimo di utenti per piano
        do
        {
            i = r.nextInt(floorsList.size());
        }while(floorsList.get(i).getCountCarIn() >= floorsList.get(i).getFreeSpace());
        floorsList.get(i).addCar();
    }
    //Simula un uscita di un driver da un piano
    void randomExit()
    {
        Random r = new Random();
        int i;
        //Impedisco che i posti occupati vadano in negativo
        do
        {
            i = r.nextInt(floorsList.size());
        }while(floorsList.get(i).getCountCarIn() <= 0);
        floorsList.get(i).deleteCar();
    }

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

    public DataBaseAdapter getDb() {
        return db;
    }

    public int getFreeSpacesTot()
    {
        return freeSpacesTot;
    }

    public int getFreeSpacesSubTot()
    {
        return freeSpacesSubTot;
    }

    public int getFreeSpacesSubNow() {
        return freeSpacesSubNow;
    }

    public void setFreeSpacesSubNow(int freeSpacesSubNow) {
        this.freeSpacesSubNow = freeSpacesSubNow;
    }

    public int getFreeSpacesTicketTot()
    {
        return freeSpacesTicketTot;
    }

    public int getFreeSpacesTicketNow()
    {
        return freeSpacesTicketNow;
    }

    public void setFreeSpacesTicketNow(int freeSpacesTicketNow) {
        this.freeSpacesTicketNow = freeSpacesTicketNow;
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

    public int getEntryToT() {
        return entryToT;
    }

    public void setEntryToT(int entryToT) {
        this.entryToT = entryToT;
    }

    public void setObs(Observer obs) {
        this.obs = obs;
    }

    public void notifyObs()
    {
        obs.update();
    }

    public String getInfoBox() {
        return infoBox;
    }
}