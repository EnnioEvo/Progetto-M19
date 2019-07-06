package main.Parking;

import main.Parking.DataBase.DataBaseAdapter;
import main.Utilities.ServiceFactory;

import java.util.GregorianCalendar;

public class Exit
{
    private Parking man;
    private DataBaseAdapter db;

    public Exit(Parking man)
    {
        this.man = man;
        ServiceFactory sf = ServiceFactory.getInstance();
        this.db = sf.getDataBaseAdapter("./db.txt");
    }
    //Metodo che permette ad un driver di uscire, se quest'ultimo è munito di ticket per poter uscire deve rispettare il deltaTime
    //e l'aver pagato il ticket, se invece il driver è munito di abbonamento deve aver effettuato il pagamento di quest'ultimo
    //e sopratutto l'abbonamento deve essere valido
    public String exit(String carID)
    {
        boolean check = false;
        boolean exit = false;
        String info = "";
        Driver toBeRemoved = new Driver("");
        for(Driver d : man.getSubDrivers())
        {
            if(d.getCarId().equals(carID) && d.getInPark())
            {
                check = true;
                if(GregorianCalendar.getInstance().after(d.getDateFinishOfSub()) || !d.getPaySub())
                {
                    //Controlla se ha pagato la tariffa extra dopo la scadenza dell'abbonamneto
                    if(man.checkDeltaTime(d.getDateFinishOfSub()) && d.getPaySub())
                    {
                        exit = true;
                        info = "Uscita abbonamento avvenuta con successo " + d.getCarId();
                        System.out.println(info);
                        d.setInPark(false);
                        db.writeData(d, true);
                    }
                    else
                    {
                        //Se è pagato, allora è scaduto
                        if(d.getPaySub())
                        {
                            info = "L'abbonamento è scaduto, si prega di tornare alle casse.";
                            d.setSubPayementExpiredOfSub(true);
                            //Aggiorno l'entry dell'utente nel db
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
                    db.writeData(d, true);
                }
            }
            else if (d.getCarId().equals(carID))
            {
                check = true;
                info = "Uscita fallita, l'abbonato non è nel parcheggio " + d.getCarId();
                System.out.println(info);
            }
        }
        for(Driver d : man.getDrivers())
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
                if((!man.checkDeltaTime(d.getTimePaid())) || !d.isPaid())
                {
                    //Se è pagato, vuol dire che è scaduto
                    if(d.isPaid())
                    {
                        info = "E' passato troppo tempo dal pagamento, si prega di tornare alle casse.";
                        d.setTicketPayementExpired(true);
                        //Aggiorno l'entry dell'utente nel db
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
                    toBeRemoved = d;
                    man.setFreeSpacesTicketNow(man.getFreeSpacesTicketNow() - 1);
                    info = "Uscita avvenuta con successo " + d.getCarId();
                    System.out.println(info);
                }
            }
        }
        man.getDrivers().remove(toBeRemoved);
        //Caso in cui la tessera non è riconosciuta per un qualsiasi motivo
        if(!check)
        {
            info = "Tessera non riconosciuta.";
            System.out.println(info);
        }
        if (exit)
        {
            man.randomExit();
            return "exitOk--" + info;
        }
        else
        {
            return "exitNo--" + info;
        }
    }
}
