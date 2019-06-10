package main.Peripherals.Cash;

import java.util.GregorianCalendar;
import java.util.Locale;
import main.Manager.Manager;
import main.Manager.Driver;

public class Cash{
    private int id;
    private double tariffaOraria; // in Euro
    private double fund = 0;
    private PaymentAdapter paymentAdapter;
    private Manager manager;

    public Cash(int id, double tariffaOraria, PaymentAdapter paymentAdapter){
        this.id = id;
        this.tariffaOraria = tariffaOraria;
        this.paymentAdapter = paymentAdapter;
        this.manager = manager;
    }

    //client server
    //acquistare abbonamento
    //GUI

    public Payment pay(String carId){
        Driver driver = manager.getDriver(carId);
        double serviceHours = getServiceHours(driver);
        Payment payment = new Payment(serviceHours * tariffaOraria, carId, Boolean.FALSE);
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

    public double getServiceHours (Driver driver){
        //int nOreServizio = (int)(Math.random()*29) + 1; // verrà sostituito dalle ore effettive
        GregorianCalendar c1 = driver.getTimeIn();
        GregorianCalendar c2 = new GregorianCalendar(new Locale("en", "IT"));
        double hours = Math.ceil((c2.getTimeInMillis()-c1.getTimeInMillis())/( 1000*60*60));
        return hours;
    }
}