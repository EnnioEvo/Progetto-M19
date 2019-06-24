package main.Manager;

@SuppressWarnings("Duplicates")
public class EntryManager
{
    private Manager man;

    EntryManager(Manager man)
    {
        this.man = man;
    }

    String entryTicket(String carId)
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
            info = "Ingreso fallito: targa: " + carId + " già presente all'interno del parcheggio.";
            System.out.println(info);
        }
        else
        {
            man.setFreeSpacesTicketNow(man.getFreeSpacesTicketNow() + 1);
            man.setEntryToT(man.getEntryToT() + 1);   //Perche non viene incrementata all'ingresso degli abbonati?
            Driver d = new Driver(carId);
            d.setTariff(man.getTariff());
            man.getDrivers().add(d);
            // Nuovo ingresso, non rimuovo dal db
            man.getDb().writeData(d, false);

            //stampa fittizia della tessera
            info = "Ingresso riuscito, " + man.printTickt(carId);
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

    String entrySub(String carId, String typeSub)
    // codice sub MM = mensile, SM= semestrale, AN=annuale.
    {
        String info;
        if(!man.checkCarId(carId))
        {
            info = "Targa non valida";
            System.out.println(info);
            return "entryNo--" + info;
        }

        boolean entry = false;
        if(!man.checkSubOrTicket(carId))
        {
            if(typeSub.equals("XX"))
            {
                info = "Non hai ancora l'abbonamento";
                return "entryNo--" + info;
            }
            if(man.getFreeSpacesSubNow() + 1 > man.getFreeSpacesSubTot())
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
                man.setFreeSpacesSubNow(man.getFreeSpacesSubNow() + 1); //NB: secondo me potremmo anche decrementarlo , e quando arriva a Zero il metodo non va piu,
                //ovviamente è la stessa cosa, dimmi cosa secondo te è più corretto
                man.getSubDrivers().add(d);
                // Nuovo ingresso, non rimuovo dal db
                man.getDb().writeData(d, false);
                d.setInPark(true);
                entry = true;
            }
        }
        else
        {
            //controllo sulla validità dell'abbonamento per effettuare l'ingresso
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
                info = "Ingresso non riuscito, targa: " + carId + " già all'interno del parcheggio";
                System.out.println(info);
            }
            else
            {
                info = "Ingresso abbonato avvenuto con successo";
                System.out.println(info);
                Driver d = man.getDriver(carId);
                d.setInPark(true);
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
