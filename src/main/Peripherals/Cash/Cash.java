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

    public void askDriver(String carId){
        // da cambiare con client server
        currentDriver = manager.getDriver(carId);
    }

    public void generatePayment(){
        Driver driver = currentDriver;
        Payment payment = new Payment(0d,driver.getCarId(),false);

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
        currentPayment = payment;
    }

    public void receiveCashMoney(Double money){
        currentPayment.setAmountPaid(currentPayment.getAmountPaid()+money);
        checkPaid();
    }

    public void receiveElectronicPayment(){
        if(paymentAdapter.pay(currentPayment.getAmount())){
            currentPayment.amountPaid = currentPayment.amount;
        };
        checkPaid();
    }

    public void checkPaid(){
        if (currentPayment.getAmountPaid()>=currentPayment.getAmount()) {
            //Se ho pagato il dovuto cambio lo stato di Payment
            currentPayment.setCheck(Boolean.TRUE);
            if (currentPayment.getAmountPaid() > currentPayment.getAmount()) {
                //Se ho pagato più del dovuto erogo il resto
                deliverMoney(currentPayment.getAmountPaid() - currentPayment.getAmount());
            }
            //Notifico al manager l'avvenuto pagamento
            notifyManager();
            //Cancello la sessione del pagamento
            forgetSession();
        }

    }

    public void deliverMoney(double amount){
        System.out.println("Erogati " + amount + "€.");
    }

    public void notifyManager(){
        //cambiare per client server
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

    public void forgetSession(){
        deliverMoney(currentPayment.amountPaid);
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