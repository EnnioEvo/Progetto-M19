package main.Peripherals.Cash;

import java.awt.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import GUIs.CashGUI2;
import main.Manager.Driver;
import main.Peripherals.ClientCommand;
import main.Utilities.Observer;
import main.Peripherals.Peripheral;
import main.Utilities.DriverParser;
import net.Client;

/*CASSA DEL PARCHEGGIO M-19
Una volta che il cliente è pronto per uscire, affinchè la colonnina di uscita si apra, è necessario che risulti nel
manager che il cliente non abbia pagamenti in sospeso, cosa che potrebbe verificarsi nei seguenti casi:
1. Il cliente ha scelto la modalità di ingresso "Ticket" e non lo ha ancora pagato.
2. Il cliente ha scelto la modalità di ingresso "Ticket", lo ha pagato ma ha fatto passare troppo tempo dal pagamento.
3. Il cliente ha scelto la modalità di ingresso "Abbonamento" e non lo ha ancora pagato.
4. Il cliente ha scelto la modalità di intresso "Abbonamento", lo ha pagato, ma l'abbonamento è scaduto durante
    la sosta.
In tutti questi casi, è necessario che il cliente si rechi alla cassa per pagare l'intero prezzo della modalità
di sosta scelta, oppure soltanto l'extra dovuto. L'extra viene pagato sempre secondo la tariffa oraria.

Affinchè la cassa funzioni è necessario che il Manager sia attivo e connesso alla rete.
Una volta avviata, la cassa stabilisce una connesione col manager all'indirizzo hostName e port specificati.

Una volta arrivato alla cassa, il cliente inserisce nella schermata prncipale della cassa il numero della sua targa,
la cassa chiede al manager i dati del cliente corrispondenti alla targa. Se la targa è presente, il manager invia tali dati sotto forma di stringa alla
cassa, che li riassembla in un oggetto Driver e li salva nell'attributo currentDriver.
Poi attraverso il metodo generatePayment(Driver), viene calcolato il costo del servizio usufruito non ancora
pagato, e viene salvato in un oggetto currentPayment di tipo Payment.

L'importo è mostrato nella seconda schermata dell'interfaccia. Qui il cliente può scegliere due modalità di pagamento:
    1. Contanti: il cliente inserisce iterativamente un numero sufficiente di monete e banconote finchè l'importo
    non è stato pagato totalmente. In questa simulazione software, gli ingressi per i contanti sono rappresentati
    da una casella di testo e da un bottone "Inserisci contanti".
    2. Pagamento elettronico: il cliente inserisce la carta relativa al servizio di pagamento implementato
    in questa cassa. La cassa interagisce con un tipo astratto PaymentAdapter, che può essere implementato in
    maniera concreta in maniera differente a seconda dell'azienda che eroga il servizio di pagamento. In questa
    cassa viene implementato a titolo di esempio un servizio di pagamento VISA, che riesce 3 volte su 4.
    Il cliente può decidere di pagare una parte in contanti e una parte con pagamento elettronico.

Se il cliente cambia idea prima del pagamento, può tornare alla schiermata principale e riprendere l'importo
già inserito.
Se il cliente inserisce una moneta o una banconota non riconosciuta questa viene restituita.
Se il cliente inserisce una quantità di contante maggiore o uguale all'importo, viene erogato il resto e viene
notificato al manager che il cliente corrispondente alla targa ha pagato l'importo totale del servizio usufruito.
La stessa cosa viene notificata al manager nel caso di corretta ricezione del pagamento elettronico, in caso di
transazione fallita invece, il cliente è invitato a riprovare.

*/

public class Cash implements Peripheral
{
    private String id;
    private PaymentAdapter paymentAdapter; //Tipo astratto per la gestione del pagamento elettronica
    private Driver currentDriver; //Cliente che sta tentando di pagare
    private Payment currentPayment; //Dati del tentativo di pagamento in corso
    private double extraCost;
    private double resto; //Eventuale resto da erogare
    private Observer obs; //L'observer è l'interfaccia grafica
    private final LinkedBlockingQueue<String> messages; //I messaggi da inviare al server
    private String infoBox; //L'interfaccia mostra sempre il messaggio contenuto in infoBox
    private HashMap<String, ClientCommand> commands; //I comandi da ricevere dal server

    public Cash(String hostName, int port)
    {
        Cash cash = this;
        paymentAdapter = new VisaAdapter(); //In questa cassa è implementato il pagamento con carta Visa
        createCommands();
        //La GUI va chiamata prima del client se no non compare
        EventQueue.invokeLater(new Runnable()
        {
            /*
            L'interfaccia viene eseguita in un thread diverso dalle operazioni effettive.
            L'interfaccia è un Observer della cassa, eventuali cambiamenti nella GUI vengono introdotti
            tramite il metodo notifyObs().
            */
            @Override
            public void run()
            {
                CashGUI2 g = new CashGUI2(cash);
                cash.setObs(g);
                //La cassa è istanziata senza attributi, l'id le viene assegnato inizialmente dal manager
                getIdFromMan();
            }
        });
        this.messages = new LinkedBlockingQueue<>();

        /*
        Viene creato un client che resta in ascolto di nuovi elementi nella lista messages da inviare al server,
        e di comandi da ricevere dal server.
        */
        new Client(hostName, port, messages, cash);
    }

    private void createCommands()
    {
        /*
        Inizializza l'attributo commands come una mappa le cui chiavi sono i nomi dei comandi
        e i cui valori sono istanze anonime di classi che implementano ClientCommand
        */
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
                infoBox = "Tessera non riconosciuta.";
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

    /*Chiedo al manager l'oggetto Driver corrispondente alla targa carId
    Se la targa è presente nel parcheggio, il manager invierà alla cassa una stringa da parsare
    per costruire un oggetto Driver
     */
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

    /*Genero il pagamento corrente relativo al driver corrente che ho appena ricevuto dal manager:
    distinguo i casi
        abbonamento - ticket
        pagato - non pagato
        scaduto - non scaduto
    e genero il pagamento giusto.
     */
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

    /*
    Incrementa il totale già pagato in currentPayment di money
    Se non era necessario lo segnalo
    */
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

    //Interagisco con il paymentAdapter per effettuare la transazione elettronica
    //Restituisce l'importo ancora dovuto
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

    // Erogo il resto
    public void deliverMoney(double amount){
        System.out.println("Erogati " + amount + "€.");
        /*infoBox = "Erogati " + amount + "€.";
        notifyObs();*/
        resto = amount;
    }

    /* La cassa notifica al manager che il cliente corrispondente alla targa carId ha pagato il ticket
    o l'abbonamento e non è più in debito*/
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
    }

    //Restituisce il numero di ore trascorse da lastPaid, arrotondate per eccesso
    public int getServiceHours (GregorianCalendar lastPaid)
    {
        GregorianCalendar nowCalendar = new GregorianCalendar();
        int hours = (int) Math.ceil((nowCalendar.getTimeInMillis() - lastPaid.getTimeInMillis()) / (1000)); //Metto secondi per test
        System.out.println("hours"+hours);
        return hours;
    }

    // Metodi per restituire attributi
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
    public String getInfoBox()
    {
        return infoBox;
    }
    public double getResto() {
        return resto;
    }
    //

    // Notifico all'interfaccia che deve aggiornarsi per mostrare le informazioni più recenti
    @Override
    public void notifyObs()
    {
        obs.update();
    }

    //Notifico all'interfaccia la corretta ricezione del pagamento
    public void notifyPayOk()
    {
        ((CashGUI2)obs).payOk();
    }

    public void setObs(Observer obs)
    {
        this.obs = obs;
    }

    //Richieste per il manager
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

    //info = id--arg
    //Eseguo il comando "id" con argomento "arg"
    @Override
    public void receiveInfo(String info)
    {
        String split[] = info.split("--");
        commands.get(split[0]).execute(split);
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Argomenti errati");
            return;
        }

        //args[0] = hostName, args[1] = port
        //Esempio: java main.Peripherals.Cash 127.0.0.1 1030
        new Cash(args[0], Integer.parseInt(args[1]));
    }
}