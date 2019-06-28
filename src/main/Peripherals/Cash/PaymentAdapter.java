package main.Peripherals.Cash;

public interface PaymentAdapter {
    public Boolean pay(Double amount);
    public String getName();
}
