package GUIs;

import main.Peripherals.Cash.BancomatAdapter;
import main.Peripherals.Cash.Cash;
import main.Peripherals.Columns.EntryColumn;
import main.Peripherals.Columns.ExitColumn;

public class TestGuiCash {
    public static void main(String[] args) {
        CashGUI cashGUI = new CashGUI(new Cash("127.0.0.1", 501, "1", new BancomatAdapter()));
        //EntryColumnGUI2 entryColumnGUI2 = new EntryColumnGUI2(new EntryColumn("127.0.0.1",121));
        //ExitColumnGUI2 exitColumnGUI2 = new ExitColumnGUI2(new ExitColumn("127.0.0.1",121));

    }
}
