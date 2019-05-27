import GUIs.ManagerGUI;
import main.Driver;
import main.Manager;
import main.Peripherals.EntryColumn;
import main.Peripherals.EntryColumnGUI;
import main.Subscription;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test2 {

    public static void main(String[] args) {
        Manager m = new Manager();
        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                new ManagerGUI(m);
            }
        });

        EntryColumn entry = new EntryColumn("IT1224LK", m);
        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run() {
                new EntryColumnGUI(entry);
            }
        });




    }
}
