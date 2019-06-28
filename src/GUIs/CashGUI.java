package GUIs;
import main.Peripherals.Cash.Cash;
import main.Peripherals.Cash.Payment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CashGUI{

    private Cash cash;
    String electronicMethod;
    private JPanel cards;  //Pannello che usa CardLayout
    final private static String START = "Inserisci la targa";
    final private static String PAY = "Inserisci contanti";
    private JTextArea info;

    public CashGUI(Cash cash){
        this.cash = cash;
        this.electronicMethod = cash.getPaymentAdapter().getName();
        JFrame f = new JFrame();
        setUIFont (new javax.swing.plaf.FontUIResource("Dialog",Font.PLAIN,32));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        f.setSize((int)(screenWidth / 4), (int) (screenHeight / 1.5));
        f.setLocation(screenWidth / 2, screenHeight / 2);
        f.setTitle("Cassa");

        initComponents(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void initComponents(Frame f){
        //Schermata 1: inserisci la targa
        JPanel card1 = startCard();
        //Schermata 2: paga
        JPanel card2 = payCard();

        //Creo il pannello che contiene le "cards".
        CardLayout cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(card1, START);
        cards.add(card2, PAY);


        f.add(cards, BorderLayout.CENTER);
        cardLayout.show(cards,START);
    }

    public JPanel startCard(){
        JPanel card = new JPanel();
        info = new JTextArea("Grazie di aver sostato al parcheggio M-19!");
        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                info.setText("Grazie di aver sostato al parcheggio M-19!");
            }
        });
        card.setBackground(Color.decode("#778ca3"));
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#778ca3"));
        //topPanel.add(Box.createRigidArea(new Dimension(70,20)), BorderLayout.NORTH);
        //topPanel.add(Box.createRigidArea(new Dimension(500,20)), BorderLayout.WEST);
        //topPanel.add(Box.createRigidArea(new Dimension(20,20)), BorderLayout.EAST);

        //Metto JComboBox in un JPanel per un look migliore
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.setBackground(Color.decode("#778ca3"));
        String starterTextInJTarga = "ES: AA000AA";
        JTextField JTarga = createSimpleTextField(starterTextInJTarga,true,5,5,5,5);
        JTarga.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if (JTarga.getText().equals(starterTextInJTarga)){
                    JTarga.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {

            }
        });
        mainPanel.add(JTarga);
        JButton t = createSimpleButton(START);
        t.setPreferredSize(new Dimension(200, 200));
        t.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, START);
            }
        });
        mainPanel.add(t);
        //mainPanel.add(Box.createRigidArea(new Dimension(10,20)));
        info.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setPreferredSize(new Dimension(300, 300));
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(mainPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 30));

        //card.add(Box.createRigidArea(new Dimension(130,10)), BorderLayout.EAST);
        //card.add(Box.createRigidArea(new Dimension(130,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(mainPanel, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);
        return card;

    }

    public JPanel payCard(){
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new GridLayout(1,2));

        return card;
    }

    private JButton createSimpleButton(String text)
    {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.decode("#45aaf2"));
        Border line =  BorderFactory.createMatteBorder(5, 5, 5, 5, Color.decode("#4b6584"));
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#9AECDB"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#45aaf2"));
            }
        });
        return button;
    }

    private JTextField createSimpleTextField(String text, Boolean editable, int top, int left, int bottom, int right)
    {
        JTextField tf = new JTextField(text);
        tf.setEditable(editable);
        tf.setForeground(Color.BLACK);
        tf.setBackground(Color.decode("#d1d8e0"));
        Border line =  BorderFactory.createMatteBorder(top, left, bottom, right, Color.decode("#4b6584"));
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        tf.setBorder(compound);
        if(editable)
        {
            tf.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    tf.setBackground(Color.decode("#9AECDB"));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    tf.setBackground(Color.decode("#d1d8e0"));
                }
            });
        }
        return tf;
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

    private void setFont(Component comp, Font font)
    {
        comp.setFont(font);
        for (Component child : ((Container)comp).getComponents())
        {
            child.setFont(font);
        }
    }

}
