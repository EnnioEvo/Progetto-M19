package main;

import main.Peripherals.Column;

import java.util.ArrayList;

public class Manager
{
    private ArrayList<Floor> floorsList;
    private int freeSpacesTot, freeSpacesSubTot, freeSpacesTicketTot;
    private int freeSpacesSubNow, freeSpacesTicketNow;
    private int tariff;
    private ArrayList<Driver> drivers, subDrivers;
    private ArrayList<Cash> cashList;
    private ArrayList<Column> columnList;


    public Manager()
    {
        this.floorsList = new ArrayList<>();
        this.freeSpacesTot = 0;
        this.freeSpacesSubTot = 0;
        this.freeSpacesTicketTot = 0;
        this.drivers = new ArrayList<>();
        this.subDrivers = new ArrayList<>();
    }


    public void makeFloors(int[] freeSpaces)
    {
        for(int i=0; i<freeSpaces.length; i++)
        {
            Floor floor = new Floor(floorsList.size(), freeSpaces[i]);
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
            drivers.add(new Driver(carId));
        }
    }

    public void entrySub(String carId)
    {
        if (freeSpacesSubNow + 1 > freeSpacesSubTot)
        {
            throw new RuntimeException("posti ticket finiti");
        }
        else
        {
            freeSpacesSubNow++;
            subDrivers.add(new Driver(carId));
        }
    }


    



    private void setSpacesSubdivision(int sub)
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

    public void setTariff(int tariff)
    {
        this.tariff = tariff;
    }


}
