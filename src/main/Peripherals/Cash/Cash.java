package main.Peripherals.Cash;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import GUIs.CashGUI2;
import main.Manager.Manager;
import main.Manager.Driver;
import main.Manager.Subscriptions.Subscription;
import main.Peripherals.ClientCommand;
import main.Peripherals.Observer;
import main.Peripherals.Peripheral;
import main.Utilities.DriverParser;
import net.Client;

public class Cash implements Peripheral
{
    private String id;
    private double tariffaOraria; // in Euro
    private double fund = 0;
    private PaymentAdapter paymentAdapter;
    private Manager manager;
    Payment currentPayment;
    Driver currentDriver;
    private double extraCost;
    private double resto;
    private Observer obs;
    private final LinkedBlockingQueue<String> messages;
    private String infoBox;
    private HashMap<String, ClientCommand> commands;

    public Cash(String hostName, int port)
    {
        Cash cash = this;
        paymentAdapter = new VisaAdapter();
        createCommands();
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                CashGUI2 g = new CashGUI2(cash);
                cash.setObs(g);
                getIdFromMan();
            }
        });
        this.messages = new LinkedBlockingQueue<>();
        new Client(hostName, port, messages, cash);
    }

    private void createCommands()
    {
        commands = new HashMap<>();
        commands.put("id", (String[] args) ->
        {
            System.out.println("id");
            id = args[1];
        });
        commands.put("helpComing", (String[] args) ->
        {
            System.out.println("helpComing");
            infoBox = args[1];
            notifyObs();
        });
        commands.put("info", (String[] args) ->
        {
            System.out.println("driverInfo" + args[1]);
            if(args[1].equals("0"))
            {
                infoBox = "Tessera non riconosciuta";
            }
            else
            {
                // Per parsare devo inviare tutta la stringa non divisa
                StringJoiner sj = new StringJoiner("--");
                for(String s : args)
                {
                    sj.add(s);
                }
                System.out.println(sj.toString());
                currentDriver = DriverParser.parseDriver(sj.toString());
                System.out.println("ck" + currentDriver.getDriverInfo());
                // Recupero eventuale costo aggiuntivo per abbonamneto
                getExtra();
                generatePayment();
                notifyPayOk();
            }
            notifyObs();
        });
        commands.put("extra", (String[] args) ->
        {
            System.out.println("extraCost");
            extraCost = Double.parseDouble(args[1]);
            //infoBox = "Abbonamento scaduto, devi pagare un sovrapprezzo";
            notifyObs();
        });
        commands.put("logError", (String[] args) ->
        {
            System.out.println("logError");
            infoBox = "C'é stato un errore con la registrazione del pagamento, chiedi aiuto ai nostri addetti.";
            notifyObs();
        });
        commands.put("logOk", (String[] args) ->
        {
            System.out.println("logOk");
            infoBox = "Pagamento effettuato, grazie e a presto!\nResto: " + resto;
            notifyObs();
        });

    }

    public void askDriver(String carId)
    {
        System.out.println("driverInfo--" + carId);
        if(carId.equals(""))
        {
            messages.add("driverInfo--XX");
        }
        else
        {
            messages.add("driverInfo--" + carId);
        }
    }

    public void generatePayment(){
        Driver driver = currentDriver;
        Payment payment = new Payment(0d, driver.getCarId(),false);
        currentPayment = payment;

        if (driver.getSub() != null){
            // il cliente possiede un abbonamento

            if (driver.getSub().getDateFinish().compareTo(GregorianCalendar.getInstance())==-1)
            {
                // l'abbonamento è scaduto

                payment.setAmount(getServiceHours(driver.getDateFinishOfSub())*extraCost);
                System.out.println("sub exp" + currentPayment.getAmount());
            }
            else
                {
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
                System.out.println("exp tick" + currentPayment.getAmount());

            }
            else {
                if (!driver.isPaid()){
                    //se il ticket non è stato pagato

                    payment.setAmount(getServiceHours(driver.getTimeIn())*driver.getTariff());
                    System.out.println("payement tick" + currentPayment.getAmount() );
                }
                else {
                    //se il ticket è stato pagato
                    payment.setCheck(Boolean.TRUE);
                }
            }
        }
        currentPayment = payment;
    }

    public Double receiveCashMoney(Double money)
    {
        if(!currentPayment.getCheck())
        {
            currentPayment.setAmountPaid(currentPayment.getAmountPaid() + money);
            checkPaid();
        }
        else
        {
            deliverMoney(money);
            infoBox = "Hai gia pagato! Resto: " + resto;
            notifyObs();
        }

        System.out.println("payement tick" + currentPayment.getAmountPaid());
        if(currentPayment.getDovuto() < 0)
        {
            return 0d;
        }
        return currentPayment.getDovuto();
    }

    public Double receiveElectronicPayment()
    {
        if(!currentPayment.getCheck())
        {
            if (paymentAdapter.pay(currentPayment.getAmount()))
            {
                currentPayment.setAmountPaid(currentPayment.getAmount());
            }
            else
            {
                infoBox = "Pagamento con carta fallito!";
                notifyObs();
            }
            checkPaid();
        }
        else
        {
            infoBox = "Hai gia pagato!";
            notifyObs();
        }
        if(currentPayment.getDovuto() < 0)
        {
            return 0d;
        }
        return currentPayment.dovuto;
    }

    public void checkPaid(){
        if (currentPayment.getAmountPaid()>=currentPayment.getAmount())
        {
            //Se ho pagato il dovuto cambio lo stato di Payment
            currentPayment.setCheck(Boolean.TRUE);
            if (currentPayment.getAmountPaid() > currentPayment.getAmount())
            {
                //Se ho pagato più del dovuto erogo il resto
                deliverMoney(currentPayment.getAmountPaid() - currentPayment.getAmount());
            }
            //Notifico al manager l'avvenuto pagamento
            notifyManager(currentDriver.getCarId());
            //Cancello la sessione del pagamento
            //forgetSession();  Lo sposto in notify manager
        }

    }

    public void deliverMoney(double amount){
        System.out.println("Erogati " + amount + "€.");
        /*infoBox = "Erogati " + amount + "€.";
        notifyObs();*/
        resto = amount;
    }

    public void notifyManager(String carId)
    {
        if (currentDriver.getSub() == null)
        {
            messages.add("setTicketPaid--" + carId);
        }
        else
        {
            messages.add("setSubPaid--" + currentDriver.getCarId());
        }
        //forgetSession();
    }

    public void setTariffaOraria(double tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    public int getServiceHours (GregorianCalendar lastPaid)
    {
        GregorianCalendar nowCalendar = new GregorianCalendar();
        int hours = (int) Math.ceil((nowCalendar.getTimeInMillis() - lastPaid.getTimeInMillis()) / (1000 )); //Metto secondi
        System.out.println("hours"+hours);
        return hours;
    }

    public void forgetSession()
    {
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

    public String getAdapterName() {
        return paymentAdapter.getName();
    }

    public String getId() {
        return id;
    }

    @Override
    public void notifyObs()
    {
        obs.update();
    }

    public void notifyPayOk()
    {
        ((CashGUI2)obs).payOk();
    }

    public void setObs(Observer obs)
    {
        this.obs = obs;
    }

    public void getIdFromMan()
    {
        messages.add("getId--XX");
    }

    public void help()
    {
        messages.add("help--" + id);
    }

    public void getExtra()
    {
        messages.add("extra--XX");
    }

    @Override
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        commands.get(split[0]).execute(split);
    }

    public String getInfoBox()
    {
        return infoBox;
    }

    public double getResto() {
        return resto;
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Argomenti errati");
            return;
        }

        new Cash(args[0], Integer.parseInt(args[1]));
    }
}