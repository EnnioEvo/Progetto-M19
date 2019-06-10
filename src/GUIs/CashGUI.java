package GUIs;
import main.Cash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CashGUI{

    private Cash cash;
    private JPanel cards;  //Pannello che usa CardLayout

    final private static String START = "Inserisci la targa";
    final private static String CHOOSEMETHOD = "Scegli il metodo";
    final private static String PAYWITHCASH = "Paga in contanti";
    final private static String ELECTRONICPAYMENT = "Pagamento elettronico";
    final private static String RESULT = "Esito transazione";

    public CashGUI(Cash cash){
        this.cash = cash;
        JFrame f = new JFrame();
        setUIFont (new javax.swing.plaf.FontUIResource("Dialog",Font.PLAIN,32));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        f.setSize(screenWidth / 2, screenHeight / 2);
        f.setLocation(screenWidth / 4, screenHeight / 4);
        f.setTitle("Cassa");

        initComponents(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void initComponents(Frame f){
        //Schermata 0: inserisci la targa
        JPanel card0 = startCard();
        //Schermata 1: scegli il metodo di pagamento
        JPanel card1 = choosePaymentCard();
        //Schermata 2: paga in contanti
        JPanel card2 = payWithCashCard();
        //Schermata 3: pagamento elettronico
        JPanel card3 = electronicPaymentCard();
        //Schermata 4: esito della transazione
        JPanel card4 = resultCard();

        //Creo il pannello che contiene le "cards".
        CardLayout cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(card0, START);
        cards.add(card1, CHOOSEMETHOD);
        cards.add(card2, PAYWITHCASH);
        cards.add(card3, ELECTRONICPAYMENT);
        cards.add(card4, RESULT);



        f.add(cards, BorderLayout.CENTER);
        cardLayout.show(cards,START);
        cardLayout.show(cards,ELECTRONICPAYMENT);
    }

    public JPanel startCard(){
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new GridLayout(2,1));
        JLabel plateLabel = new JLabel("Inserisci la targa");
        plateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField plateField = new JTextField();
        plateField.setHorizontalAlignment(SwingConstants.CENTER);
        JButton plateButton = new JButton("Invia");
        bottomPanel.add(plateField);
        bottomPanel.add(plateButton);
        card.add(plateLabel,BorderLayout.CENTER);
        card.add(bottomPanel,BorderLayout.SOUTH);

        plateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((CardLayout)cards.getLayout()).show(cards,CHOOSEMETHOD);
            }
        });

        return card;
    }

    public JPanel choosePaymentCard(){
        JPanel card = new JPanel();
        card.setLayout(new GridLayout(2,1));
        JButton cashButton = new JButton("Paga in contanti");
        JButton bancomatButton = new JButton("Paga col bancomat");
        card.add(cashButton);
        card.add(bancomatButton);

        cashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((CardLayout)cards.getLayout()).show(cards,PAYWITHCASH);
            }
        });
        bancomatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((CardLayout)cards.getLayout()).show(cards,ELECTRONICPAYMENT);
            }
        });

        return card;
    }

    public JPanel payWithCashCard(){
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        JLabel remainingLabel = new JLabel("Inserisci xx €");
        remainingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton coinButton = new JButton("Inserisci moneta");
        JButton banknoteButton = new JButton("Inserisci banconota");
        bottomPanel.add(coinButton);
        bottomPanel.add(banknoteButton);
        card.add(remainingLabel,BorderLayout.CENTER);
        card.add(bottomPanel,BorderLayout.SOUTH);

        ArrayList<Integer> coinTypes = new ArrayList<Integer>(Arrays.asList(10,20,50,100,200));
        ArrayList<Integer> banknoteTypes = new ArrayList<Integer>(Arrays.asList(5,10,20,50,100));

        coinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                coinTypes.get(new Random().nextInt(coinTypes.size()));
                ((CardLayout)cards.getLayout()).show(cards,RESULT);
            }
        });
        banknoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                banknoteTypes.get(new Random().nextInt(coinTypes.size()));
                ((CardLayout)cards.getLayout()).show(cards,RESULT);
            }
        });

        return card;
    }

    public JPanel electronicPaymentCard(){
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel remainingLabel = new JLabel("Inserire bancomat");
        remainingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton bancomatButton = new JButton("Inserisci bancomat");
        bottomPanel.add(bancomatButton,BorderLayout.CENTER);
        card.add(remainingLabel,BorderLayout.CENTER);
        card.add(bottomPanel,BorderLayout.SOUTH);

        bancomatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((CardLayout)cards.getLayout()).show(cards,RESULT);
            }
        });

        return card;
    }

    public JPanel resultCard(){
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Transazione riuscita");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton restartButton = new JButton("Riprova");
        bottomPanel.add(restartButton,BorderLayout.CENTER);
        card.add(resultLabel,BorderLayout.CENTER);
        card.add(bottomPanel,BorderLayout.SOUTH);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((CardLayout)cards.getLayout()).show(cards,CHOOSEMETHOD);
            }
        });

        return card;
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

}
