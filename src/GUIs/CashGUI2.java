package GUIs;


import main.Peripherals.Cash.Cash;
import main.Peripherals.Columns.EntryColumn;
import main.Peripherals.Columns.ExitColumn;
import main.Peripherals.Observer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("Duplicates")
public class CashGUI2 implements ItemListener, Observer
{
    private Cash cash;
    private JPanel cards;  //Pannello che usa CardLayout
    final private static String MAIN = "Main";
    final private static String PAY = "Pagamento";
    private JTextArea info;
    private JTextArea infoP;
    private JTextField toPay;

    public CashGUI2(Cash cash)
    {
        this.cash = cash;
        JFrame f = new JFrame();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int height = 900;
        int width = 700;
        f.setSize(width, height);
        f.setLocation((screenWidth - width) / 2, (screenHeight - height) / 2);
        f.setTitle("Cash");

        initComponents(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void initComponents(JFrame f)
    {
        //Schermata 0: benvenuto
        JPanel card0 = selection();
        //Schermata 1: paga
        JPanel card1 = pay();


        //Creo il pannello che contiene le "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card0, MAIN);
        cards.add(card1, PAY);

        f.add(cards, BorderLayout.CENTER);
    }

    private JPanel pay()
    {
        JPanel card = new JPanel();
        card.setBackground(Color.decode("#778ca3"));
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#778ca3"));
        topPanel.add(Box.createRigidArea(new Dimension(70,20)), BorderLayout.NORTH);
        topPanel.add(Box.createRigidArea(new Dimension(70,20)), BorderLayout.SOUTH);
        topPanel.add(Box.createRigidArea(new Dimension(40,50)), BorderLayout.EAST);
        topPanel.add(Box.createRigidArea(new Dimension(40,50)), BorderLayout.WEST);
        JPanel topCenterPanel = new JPanel(new GridLayout(1,3));
        topCenterPanel.setBackground(Color.decode("#778ca3"));
        /*LayoutManager layout = new BoxLayout(topCenterPanel, BoxLayout.X_AXIS);*/
        JButton back = createSimpleButton("Indietro");
        topCenterPanel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, MAIN);
            }
        });
        topCenterPanel.add(Box.createRigidArea(new Dimension(400, 50)));
        topCenterPanel.add(createHelpButton("Aiuto"));
        topPanel.add(topCenterPanel, BorderLayout.CENTER);

        JPanel displayContainer = new JPanel(new BorderLayout());
        displayContainer.setBackground(Color.decode("#778ca3"));
        JPanel display = new JPanel(new GridLayout(2, 1));
        display.setBackground(Color.decode("#778ca3"));
        JTextField toPayText = createSimpleTextField("Da pagare: ", false, 5, 5, 5, 5);
        toPay = createSimpleTextField("", false, 5, 0, 5, 5);
        JTextField jf1 = createSimpleTextField("Inserire contanti:", false, 0, 5, 5, 5);
        JTextField money = createSimpleTextField("", true, 0, 0, 5, 5);
        display.add(toPayText);
        display.add(toPay);
        display.add(jf1);
        display.add(money);
        displayContainer.add(Box.createRigidArea(new Dimension(100, 15)), BorderLayout.NORTH);
        displayContainer.add(display, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(Box.createRigidArea(new Dimension(200,15)), BorderLayout.NORTH);
        actionPanel.add(Box.createRigidArea(new Dimension(200,15)), BorderLayout.SOUTH);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.EAST);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.WEST);
        JButton action = createSimpleButton("Paga in contanti");
        action.setPreferredSize(new Dimension(200,100));
        action.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    toPay.setText("" + cash.receiveCashMoney(Double.parseDouble(money.getText())));
                }
                catch(NumberFormatException ex)
                {
                    infoP.setText("Numeri inseriti errati");
                }
            }
        });
        JButton action2 = createSimpleButton("Paga con carta");
        action2.setPreferredSize(new Dimension(200,100));
        action2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                toPay.setText(""+cash.receiveElectronicPayment());
            }
        });
        JPanel centerPanel = new JPanel(new GridLayout(1,3));
        centerPanel.setBackground(Color.decode("#778ca3"));
        centerPanel.add(action);
        centerPanel.add(Box.createRigidArea(new Dimension(300, 100)));
        centerPanel.add(action2);
        actionPanel.add(centerPanel, BorderLayout.CENTER);
        actionPanel.setBackground(Color.decode("#778ca3"));
        displayContainer.add(actionPanel, BorderLayout.SOUTH);

        infoP = new JTextArea();
        infoP.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        infoP.setEditable(false);
        infoP.setLineWrap(true);
        infoP.setPreferredSize(new Dimension(300, 300));
        setFont(topCenterPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(display, new Font("Helvetica", Font.PLAIN, 30));
        setFont(infoP, new Font("Helvetica", Font.PLAIN, 30));
        setFont(actionPanel, new Font("Helvetica", Font.PLAIN, 30));

        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.EAST);
        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(displayContainer, BorderLayout.CENTER);
        card.add(infoP, BorderLayout.SOUTH);

        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                infoP.setText("");
                toPay.setText("" + cash.getCurrentTotPay());
            }
        });
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

    private JButton createHelpButton(String text)
    {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.decode("#fc5c65"));
        Border line =  BorderFactory.createMatteBorder(5, 5, 5, 5, Color.decode("#eb3b5a"));
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#eb3b5a"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#fc5c65"));
            }
        });
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cash.help();
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


    //- - - - - - - - - - - - - - -Schermata della Colonnina- - - - - - - - - - - - -


    private JPanel selection()
    {
        JPanel card = new JPanel();
        card.setBackground(Color.decode("#778ca3"));
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#778ca3"));
        topPanel.add(Box.createRigidArea(new Dimension(70,20)), BorderLayout.NORTH);
        topPanel.add(Box.createRigidArea(new Dimension(70,20)), BorderLayout.SOUTH);
        topPanel.add(Box.createRigidArea(new Dimension(40,50)), BorderLayout.EAST);
        topPanel.add(Box.createRigidArea(new Dimension(40,50)), BorderLayout.WEST);
        JPanel topCenterPanel = new JPanel(new GridLayout(1,3));
        topCenterPanel.setBackground(Color.decode("#778ca3"));
        /*LayoutManager layout = new BoxLayout(topCenterPanel, BoxLayout.X_AXIS);*/
        JButton back = createSimpleButton("Indietro");
        topCenterPanel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, MAIN);
            }
        });
        topCenterPanel.add(Box.createRigidArea(new Dimension(400, 50)));
        topCenterPanel.add(createHelpButton("Aiuto"));
        topPanel.add(topCenterPanel, BorderLayout.CENTER);

        JPanel displayContainer = new JPanel(new BorderLayout());
        displayContainer.setBackground(Color.decode("#778ca3"));
        JPanel display = new JPanel(new GridLayout(1, 2));
        display.setBackground(Color.decode("#778ca3"));
        JTextField jf1 = createSimpleTextField("Inserire Targa:", false, 5, 5, 5, 5);
        JTextField targa = createSimpleTextField("", true, 5, 0, 5, 5);
        display.add(jf1);
        display.add(targa);
        displayContainer.add(Box.createRigidArea(new Dimension(100, 100)), BorderLayout.NORTH);
        displayContainer.add(display, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(Box.createRigidArea(new Dimension(200,100)), BorderLayout.NORTH);
        actionPanel.add(Box.createRigidArea(new Dimension(200,50)), BorderLayout.SOUTH);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.EAST);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.WEST);
        JButton action = createSimpleButton("Paga");
        action.setPreferredSize(new Dimension(200,100));
        action.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cash.askDriver(targa.getText());
                /*if(!info.toString().equals("Tessera non riconosciuta"))
                {
                    CardLayout cl = (CardLayout) (cards.getLayout());
                    cl.show(cards, PAY);
                }*/
            }
        });
        actionPanel.add(action, BorderLayout.CENTER);
        actionPanel.setBackground(Color.decode("#778ca3"));
        displayContainer.add(actionPanel, BorderLayout.SOUTH);

        info = new JTextArea();
        info.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setPreferredSize(new Dimension(300, 300));
        setFont(topCenterPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(display, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 30));
        setFont(actionPanel, new Font("Helvetica", Font.PLAIN, 30));

        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.EAST);
        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(displayContainer, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                info.setText("");
                targa.setText("");
            }
        });
        return card;
    }


    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


    public void itemStateChanged(ItemEvent evt)
    {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }

    @Override
    public void update()
    {
        System.out.println("update");
        info.setText(cash.getInfoBox());
        infoP.setText(cash.getInfoBox());
    }

    public void payOk()
    {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, PAY);
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