package main.Peripherals.Cash;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import main.Manager.Manager;
import main.Manager.Driver;
import main.Manager.Subscriptions.Subscription;

public class Cash{
    private int id;
    private double tariffaOraria; // in Euro
    private double fund = 0;
    private PaymentAdapter paymentAdapter;
    private Manager manager;
    Payment currentPayment;
    Driver currentDriver;

    public Cash(int id, double tariffaOraria, PaymentAdapter paymentAdapter){
        this.id = id;
        this.tariffaOraria = tariffaOraria;
        this.paymentAdapter = paymentAdapter;
    }

    //client server
    //acquistare abbonamento

    public Payment generatePayment(Driver driver){
        Payment payment = new Payment(0d,driver.getCarId(),Boolean.FALSE);

        if (driver.getSub()!=null){
            // il cliente possiede un abbonamento

            if (driver.getSub().getDateFinish().compareTo(GregorianCalendar.getInstance())==-1){
                // l'abbonamento è scaduto

                payment.setAmount(getServiceHours(driver.getDateFinishOfSub())*tariffaOraria);
            }
            else {
                // l'abbonamento non è scaduto

                if (driver.getPaySub()){
                    // l'abbonamento è stato già pagato
                    payment.setCheck(Boolean.TRUE);
                }
                else {
                    // l'abbonamento non è stato pagato
                    payment.setAmount(driver.getSubCost());
                }

            }
        }
        else{
            // il cliente non possiede un abbonamento

            if (driver.isTicketPayementExpired()){
                // il ticket pagato è scaduto
                payment.setAmount(getServiceHours(driver.getTimePaid())*tariffaOraria);

            }
            else {
                if (!driver.isPaid()){
                    //se il ticket non è stato pagato
                    payment.setAmount(getServiceHours(driver.getTimeIn())*tariffaOraria);
                }
                else {
                    //se il ticket è stato pagato
                    payment.setCheck(Boolean.TRUE);
                }
            }
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

    public int getServiceHours (GregorianCalendar lastPaid) {
        GregorianCalendar nowCalendar = new GregorianCalendar(new Locale("en", "IT"));
        int hours = (int) Math.ceil((nowCalendar.getTimeInMillis() - lastPaid.getTimeInMillis()) / (1000 * 60 * 60));
        return hours;
    }

    public Driver askDriver(String carId){
        return manager.getDriver(carId);
    }

    public void forgetSession(){
        currentPayment = null;
        currentDriver = null;
    }

    public PaymentAdapter getPaymentAdapter() {
        return paymentAdapter;
    }

    public int getId() {
        return id;
    }
}