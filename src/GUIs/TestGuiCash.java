package GUIs;

import main.BancomatAdapter;
import main.Cash;
import main.Payment;
import main.PaymentAdapter;

public class TestGuiCash {
    public static void main(String[] args) {
        CashGUI cashGUI = new CashGUI(new Cash(1, 20, new BancomatAdapter()));
    }
}
