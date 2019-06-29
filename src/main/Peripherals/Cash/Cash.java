package main.Peripherals.Cash;

import java.awt.*;
import java.util.GregorianCalendar;
import java.util.Locale;

import GUIs.CashGUI;
import main.Manager.Manager;
import main.Manager.Driver;
import main.Peripherals.ClientCommand;
import main.Peripherals.Observer;
import main.Peripherals.Peripheral;
import main.Utilities.DriverParser;
import net.Client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Cash implements Peripheral {

    private Observer obs;
    private final ConcurrentLinkedQueue<String> messages;
    private String infoBox;
    private HashMap<String, ClientCommand> commands;

    private String id;
    private double fund = 0;
    private PaymentAdapter paymentAdapter;
    private Manager manager;
    private Payment currentPayment;
    private Driver currentDriver;

    public Cash(String hostName, int port, String id, PaymentAdapter paymentAdapter){

        Cash cash = this;
        createCommands();
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                CashGUI g = new CashGUI(cash);
                cash.setObs(g);
                getIdFromMan();
            }
        });
        this.messages = new ConcurrentLinkedQueue<>();
        new Client(hostName, port, messages, cash);

        this.id = id;
        this.paymentAdapter = paymentAdapter;
    }

    public void askDriver(String carId){
        // da eliminare con client server
        currentDriver = manager.getDriver(carId);
        notifyObs();
    }

    public void generatePayment(){
        Driver driver = currentDriver;
        Payment payment = new Payment(0d,driver.getCarId(),false);

        if (driver.getSub()!=null){
            // il cliente possiede un abbonamento

            if (driver.getSub().getDateFinish().compareTo(GregorianCalendar.getInstance())==-1){
                // l'abbonamento è scaduto

                payment.setAmount(getServiceHours(driver.getDateFinishOfSub())*driver.getTariff());
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
                payment.setAmount(getServiceHours(driver.getTimePaid())*driver.getTariff());

            }
            else {
                if (!driver.isPaid()){
                    //se il ticket non è stato pagato
                    payment.setAmount(getServiceHours(driver.getTimeIn())*driver.getTariff());
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
        notifyObs();
    }

    public Boolean receiveElectronicPayment(){
        if(paymentAdapter.pay(currentPayment.getAmount())){
            currentPayment.amountPaid = currentPayment.amount;
        };
        checkPaid();
        notifyObs();
        return currentPayment.getCheck();
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

    public double getCurrentPaid() {
        return currentPayment.amountPaid;
    }

    public double getCurrentTotPay(){
        return currentPayment.amount;
    }

    public Driver getCurrentDriver() {
        return currentDriver;
    }

    public String getPaymentInfo(){
        return "Targa: " + currentDriver.getCarId() +
                "\nTotale da pagare: " + currentPayment.getAmount() +
                "\nInseriti: " + currentPayment.getAmountPaid();
    }

    public String getAdapterName() {
        return paymentAdapter.getName();
    }

    public String getId() {
        return id;
    }

    public void setObs(Observer obs)
    {
        this.obs = obs;
    }
    public void notifyObs()
    {
        obs.update();
    }
    public void getIdFromMan()
    {
        messages.add("getId--XX");
    }

    public void notifyManager(){
        //cambiare per client server
    }

    @Override
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        commands.get(split[0]).execute(split);
    }



    private void createCommands()
    {
        commands = new HashMap<>();
        commands.put("id", (String[] args) ->
        {
            System.out.println("id");
            id = args[1];
        });
        commands.put("driver",(String[] args) ->
        {
            System.out.println("driver");
            currentDriver = DriverParser.parseDriver(args[1]);

        });
        commands.put("getTariff", (String[] args) -> System.out.println("getTariff"));
    }
}