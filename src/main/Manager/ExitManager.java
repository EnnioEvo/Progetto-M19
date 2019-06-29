package main.Manager;

import java.util.GregorianCalendar;

public class ExitManager
{
    private Manager man;

    public ExitManager(Manager man)
    {
        this.man = man;
    }
    // metodo che permette ad un driver di uscire, se quest'ultimo è munito di ticket per poter uscire deve rispettare il deltaTime
    // e l'aver pagato il ticket, se invece il driver è munito di abbonamento deve aver effettuato il pagamento di quest'ultimo
    // e sopratutto l'abbonamento deve essere valido
    public String exit(String carID)   //messo boolean per recuperare il check
    {
        boolean check = false;
        boolean exit = false;
        String info = "";
        Driver toBeRemoved = new Driver("");
        //Da fare: thread che ogni ora elimina abbonamneti scaduti NON presenti in quel momento nel parcheggio
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
                    }
                    else
                    {
                        // Se è pagato, allora è scaduto
                        if(d.getPaySub())
                        {
                            info = "L'abbonamento è scaduto, si prega di tornare alle casse.";
                            d.setSubPayementExpiredOfSub(true);
                            // Aggiorno l'entry dell'utente nel db
                            man.getDb().writeData(d, true);
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
        for(Driver d : man.getDrivers())
        {
            if(d.getCarId().equals(carID))
            {
                check = true;
                if((!man.checkDeltaTime(d.getTimePaid())) || !d.isPaid())
                {
                    // Se è pagato, vuol dire che è scaduto
                    if(d.isPaid())
                    {
                        info = "E' passato troppo tempo dal pagamento, si prega di tornare alle casse.";
                        d.setTicketPayementExpired(true);
                        // Aggiorno l'entry dell'utente nel db
                        man.getDb().writeData(d, true);
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
            info = "Tessera non riconosciuta";
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
