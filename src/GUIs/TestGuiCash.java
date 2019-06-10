package GUIs;

import main.Peripherals.Cash.BancomatAdapter;
import main.Peripherals.Cash.Cash;

public class TestGuiCash {
    public static void main(String[] args) {
        CashGUI cashGUI = new CashGUI(new Cash(1, 20, new BancomatAdapter()));
    }
}
