package main.Manager;

import main.Manager.DataBase.DataBaseAdapter;
import main.Utilities.ServiceFactory;

@SuppressWarnings("Duplicates")
public class EntryManager
{
    private Manager man;
    private DataBaseAdapter db;

    public EntryManager(Manager man)
    {
        this.man = man;
        ServiceFactory sf = ServiceFactory.getInstance();
        this.db = sf.getDataBaseAdapter("./db.txt");
    }

    //Metodo che prende come attributo una targa e permette al driver di entrare nel parcheggio tramite un ticket,
    //il driver può entrare se e solo se ci sono posti disponibili e se la targa è sintatticamente giusta
   public String entryTicket(String carId)
    {
        boolean entry = false;
        String info;
        if (!man.checkCarId(carId))
        {
            info = "Targa non valida.";
            System.out.println(info);
            return "entryNo--" + info;
        }

        if (man.getFreeSpacesTicketNow() + 1 > man.getFreeSpacesTicketTot())
        {
            info = "Posti ticket finiti.";
            System.out.println(info);
        }
        else if (man.checkSubOrTicket(carId))
        {
            info = "Ingresso fallito: targa: " + carId + " già presente all'interno del parcheggio.";
            System.out.println(info);
        }
        else
        {
            man.setFreeSpacesTicketNow(man.getFreeSpacesTicketNow() + 1);
            man.setEntryToT(man.getEntryToT() + 1);
            Driver d = new Driver(carId);
            d.setTariff(man.getTariff());
            man.getDrivers().add(d);
            //Nuovo ingresso, non rimuovo dal db
            db.writeData(d, false);

            //Stampa fittizia della tessera
            info = "Ingresso riuscito, " + man.printTicket(carId);
            System.out.println(info);
            entry = true;
        }
        if (entry)
        {
            man.randomEntry();
            return "entryOk--" + info;
        }
        else
        {
            return "entryNo--" + info;
        }
    }
    //Metodo che permette sia di acquistare un abbonamento sia di entrare nel parcheggio se si possiede già un sub
   public  String entrySub(String carId, String typeSub)
    //Codice abbonamento: MM = mensile, SM = semestrale, AN = annuale.
    {
        String info;
        if(!man.checkCarId(carId))
        {
            info = "Targa non valida.";
            System.out.println(info);
            return "entryNo--" + info;
        }

        boolean entry = false;
        if(!man.checkSubOrTicket(carId))
        {
            if(typeSub.equals("XX"))
            {
                info = "Non hai ancora l'abbonamento.";
                return "entryNo--" + info;
            }
            if(man.getFreeSpacesSubNow() + 1 > man.getFreeSpacesSubTot())
            {
                info = "Abbonamenti finiti.";
                System.out.println(info);

            }
            else
            {
                //Aggiungo qui l'acquisto dell'abbonamento che va impletato nella GUI
                Driver d = new Driver(carId);
                switch (typeSub){
                    case "Mensile":
                        d.makeMonthlySub(man.getMonthlyCost());
                        break;
                    case "Semestrale":
                        d.makeSemestralSub(man.getSemestralCost());
                        break;
                    case "Annuale":
                        d.makeAnnualMonthly(man.getAnnualCost());
                        break;
                }

                info = "Abbonamento acquistato, " + d.printSub();
                System.out.println(info);
                man.setFreeSpacesSubNow(man.getFreeSpacesSubNow() + 1);
                man.getSubDrivers().add(d);
                d.setInPark(true);
                //Nuovo ingresso, non rimuovo dal db
                db.writeData(d, false);
                entry = true;
            }
        }
        else
        {
            //Controllo sulla validità dell'abbonamento per effettuare l'ingresso
            if (man.checkTicket(carId))
            {
                info = "Ingresso non riuscito, la targa risulta già all'interno con un ticket.";
                System.out.println(info);
            }
            else if (!man.checkDateSub(carId))
            {
                info = "Abbonamento scaduto, ora è possibile riacquistarlo.";
                System.out.println(info);
                man.removeSub(carId);
                man.setFreeSpacesSubNow(man.getFreeSpacesSubNow() - 1);
            }
            else if (man.checkInPark(carId))
            {
                info = "Ingresso non riuscito, targa: " + carId + " già all'interno del parcheggio.";
                System.out.println(info);
            }
            else
            {
                if(typeSub.equals("XX"))
                {
                    info = "Ingresso abbonato avvenuto con successo.";
                }
                else
                {
                    info = "Hai già un abbonamento, puoi entrare.";
                }
                System.out.println(info);
                Driver d = man.getDriver(carId);
                d.setInPark(true);
                db.writeData(d, true);
                entry = true;
            }
        }
        if (entry)
        {
            man.randomEntry();
            return "entryOk--" + info;
        }
        else
        {
            return "entryNo--" + info;
        }
    }
}
