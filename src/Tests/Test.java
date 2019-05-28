package Tests;

import GUIs.ManagerGUI;
import main.Manager.Driver;
import main.Manager.Manager;
import main.Peripherals.Columns.EntryColumn;
import main.Peripherals.Columns.EntryColumnGUI;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test
{

    public static void main(String[] args)
    {

        int posti[] = new int[10];
        Manager m = new Manager();
        m.makeFloors(7, 50);
        m.setTariff(5);
        m.setSpacesSubdivision(50);
        m.entryTicket("IT4560JV");
        m.entrySub("IT3456GT");


        Driver d = m.getDriver("IT4560JV");
        d.setPaid(true);
        GregorianCalendar time = new GregorianCalendar();
        time.add(Calendar.MINUTE, -9);
        d.setTimePaid(time);
        //m.exit("IT4560JV");

        Driver d2 = m.getDriver("IT3456GT");
        d2.setPaidSub(true);
        //Per testare tariffa extra mettere mese abbonamneto a -1
        d2.setDatePaidExtraOfSub(time);
        m.exit("IT3456GT");
       // m.Analytics();

        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                new ManagerGUI(m);
            }
        });

        EntryColumn entry = m.createEntryColumn();
        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run() {
                EntryColumnGUI g = new EntryColumnGUI(entry);
                entry.setObs(g);
            }
        });

    }
}

