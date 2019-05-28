package main.Peripherals.Cash;

public class Cash{
    private int id;
    private double tariffaOraria; // in Euro
    private double fund = 0;
    private PaymentAdapter paymentAdapter;

    public Cash(int id, double tariffaOraria, PaymentAdapter paymentAdapter){
        this.id = id;
        this.tariffaOraria = tariffaOraria;
    }

    //client server

    public Payment pay(String carId){
        int nOreServizio = (int)(Math.random()*29) + 1; // verrà sostituito dalle ore effettive
        Payment payment = new Payment(nOreServizio * tariffaOraria, carId, Boolean.FALSE);
        paymentAdapter.pay(payment);
        if (!payment.getCheck()){
            System.out.println("Transazione fallita.");
        }
        return payment;
    }

    public double getMoney(double moneyPut, double notYetPaid){
        fund += moneyPut;
        return notYetPaid - moneyPut;
    }

    public void setTariffaOraria(double tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }
}