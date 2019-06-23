package GUIs;

import com.sun.corba.se.spi.activation.Server;
import main.Peripherals.Columns.EntryColumn;
import main.Peripherals.Observer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("Duplicates")
public class EntryColumnGUI2 implements ItemListener, Observer
{
    private EntryColumn entry;
    private JPanel cards;  //Pannello che usa CardLayout
    final private static String TICKET = "Ticket";
    final private static String SUB = "Acquista Abbonamento";
    final private static String INSUB = "Ingresso Abbonamento";
    final private static String MAIN = "Main";
    private JFrame f;
    private JTextField tariffT;
    private JTextField tariffS;
    private JTextArea info;
    private JTextArea infoT;
    private JTextArea infoS;
    private JTextArea infoIS;
    private JComboBox<String> comboSub;

    public EntryColumnGUI2(EntryColumn entry)
    {
        this.entry = entry;
        f = new JFrame();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        f.setSize(screenHeight / 2, screenWidth / 3);
        f.setLocation(screenWidth / 4, screenHeight / 4);
        f.setTitle("Entry Column");

        initComponents(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void initComponents(JFrame f)
    {
        //Schermata 0: benvenuto
        JPanel card0 = selection();
        //Schermata 1: crea il ticket
        JPanel card1 = ticket();
        //Schermata 2: crea nuovo abbonamento
        JPanel card2 = sub();
        //Schermata 3: crea nuovo abbonamento
        JPanel card3 = inSub();


        //Creo il pannello che contiene le "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card0, MAIN);
        cards.add(card1, TICKET);
        cards.add(card2, SUB);
        cards.add(card3, INSUB);

        f.add(cards, BorderLayout.CENTER);
    }

    private JPanel selection()
    {
        JPanel card = new JPanel();
        info = new JTextArea("Benvenuto al parcheggio M-19!");
        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                info.setText("Benvenuto al parcheggio M-19!");
            }
        });
        card.setBackground(Color.decode("#778ca3"));
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#778ca3"));
        topPanel.add(Box.createRigidArea(new Dimension(70,20)), BorderLayout.NORTH);
        topPanel.add(Box.createRigidArea(new Dimension(500,20)), BorderLayout.WEST);
        topPanel.add(Box.createRigidArea(new Dimension(20,20)), BorderLayout.EAST);
        topPanel.add(createHelpButton("Aiuto"), BorderLayout.CENTER);
        //Metto JComboBox in un JPanel per un look migliore
        JPanel buttons = new JPanel(new GridLayout(0, 1));
        buttons.setBackground(Color.decode("#778ca3"));
        buttons.add(Box.createRigidArea(new Dimension(10,20)));
        JButton t = createSimpleButton(TICKET);
        t.setPreferredSize(new Dimension(200, 200));
        t.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, TICKET);
            }
        });
        buttons.add(t);
        buttons.add(Box.createRigidArea(new Dimension(10,20)));
        JButton s = createSimpleButton(SUB);
        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, SUB);
            }
        });
        buttons.add(s);
        buttons.add(Box.createRigidArea(new Dimension(10,20)));
        JButton is = createSimpleButton(INSUB);
        is.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, INSUB);
            }
        });
        buttons.add(is);
        buttons.add(Box.createRigidArea(new Dimension(10,20)));
        info.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setPreferredSize(new Dimension(300, 300));
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(buttons, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 30));

        card.add(Box.createRigidArea(new Dimension(130,10)), BorderLayout.EAST);
        card.add(Box.createRigidArea(new Dimension(130,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(buttons, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);
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
                entry.help();
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

    private JPanel ticket()
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
        JPanel display = new JPanel(new GridLayout(2, 2));
        display.setBackground(Color.decode("#778ca3"));
        JTextField tariffText = createSimpleTextField("Tariffa: ", false, 5, 5, 5, 5);
        tariffT = createSimpleTextField("" + entry.getTariff(), false, 5, 0, 5, 5);
        JTextField jf1 = createSimpleTextField("Inserire Targa:", false, 0, 5, 5, 5);
        JTextField targa = createSimpleTextField("", true, 0, 0, 5, 5);
        display.add(tariffText);
        display.add(tariffT);
        display.add(jf1);
        display.add(targa);
        displayContainer.add(Box.createRigidArea(new Dimension(100, 50)), BorderLayout.NORTH);
        displayContainer.add(display, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(Box.createRigidArea(new Dimension(200,50)), BorderLayout.NORTH);
        actionPanel.add(Box.createRigidArea(new Dimension(200,50)), BorderLayout.SOUTH);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.EAST);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.WEST);
        JButton action = createSimpleButton("Acquista");
        action.setPreferredSize(new Dimension(200,100));
        action.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                entry.entryTicket(targa.getText());
            }
        });
        actionPanel.add(action, BorderLayout.CENTER);
        actionPanel.setBackground(Color.decode("#778ca3"));
        displayContainer.add(actionPanel, BorderLayout.SOUTH);

        infoT = new JTextArea();
        infoT.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        infoT.setEditable(false);
        infoT.setLineWrap(true);
        infoT.setPreferredSize(new Dimension(300, 300));
        setFont(topCenterPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(display, new Font("Helvetica", Font.PLAIN, 30));
        setFont(infoT, new Font("Helvetica", Font.PLAIN, 30));
        setFont(actionPanel, new Font("Helvetica", Font.PLAIN, 30));

        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.EAST);
        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(displayContainer, BorderLayout.CENTER);
        card.add(infoT, BorderLayout.SOUTH);

        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                infoT.setText("");
                targa.setText("");
            }
        });
        return card;
    }

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    private JPanel sub()
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
        JPanel display = new JPanel(new GridLayout(3, 1));
        display.setBackground(Color.decode("#778ca3"));
        JTextField abbTxt = createSimpleTextField("Abbonamento: ", false, 5, 5, 5, 5);
        String comboBoxItems[] = {"Mensile", "Semestrale", "Annuale"};
        comboSub = new JComboBox<>(comboBoxItems);
        comboSub.setEditable(false);
        comboSub.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateTariff();
            }
        });
        comboSub.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboSub.setBackground(Color.decode("#9AECDB"));
            }

            @Override
            public void focusLost(FocusEvent e) {
                comboSub.setBackground(Color.decode("#d1d8e0"));
            }
        });
        comboSub.setBackground(Color.decode("#d1d8e0"));
        Border line =  BorderFactory.createMatteBorder(5, 0, 5, 5, Color.decode("#4b6584"));
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        comboSub.setBorder(compound);
        JTextField tariffText = createSimpleTextField("Tariffa: ", false, 0, 5, 5, 5);
        tariffS = createSimpleTextField("" + entry.getTariff(), false, 0, 0, 5, 5);
        JTextField jf1 = createSimpleTextField("Inserire Targa:", false, 0, 5, 5, 5);
        JTextField targa = createSimpleTextField("", true, 0, 0, 5, 5);
        display.add(abbTxt);
        display.add(comboSub);
        display.add(tariffText);
        display.add(tariffS);
        display.add(jf1);
        display.add(targa);
        displayContainer.add(Box.createRigidArea(new Dimension(100, 15)), BorderLayout.NORTH);
        displayContainer.add(display, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(Box.createRigidArea(new Dimension(200,15)), BorderLayout.NORTH);
        actionPanel.add(Box.createRigidArea(new Dimension(200,15)), BorderLayout.SOUTH);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.EAST);
        actionPanel.add(Box.createRigidArea(new Dimension(150,50)), BorderLayout.WEST);
        JButton action = createSimpleButton("Acquista");
        action.setPreferredSize(new Dimension(200,100));
        action.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                entry.entrySub(targa.getText(), (String) comboSub.getSelectedItem());
            }
        });
        actionPanel.add(action, BorderLayout.CENTER);
        actionPanel.setBackground(Color.decode("#778ca3"));
        displayContainer.add(actionPanel, BorderLayout.SOUTH);

        infoS = new JTextArea();
        infoS.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        infoS.setEditable(false);
        infoS.setLineWrap(true);
        infoS.setPreferredSize(new Dimension(300, 300));
        setFont(topCenterPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(display, new Font("Helvetica", Font.PLAIN, 30));
        setFont(infoS, new Font("Helvetica", Font.PLAIN, 30));
        setFont(actionPanel, new Font("Helvetica", Font.PLAIN, 30));

        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.EAST);
        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(displayContainer, BorderLayout.CENTER);
        card.add(infoS, BorderLayout.SOUTH);

        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                infoS.setText("");
                targa.setText("");
                comboSub.setSelectedIndex(0);
            }
        });
        return card;
    }

    private JPanel inSub()
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
        JButton action = createSimpleButton("Acquista");
        action.setPreferredSize(new Dimension(200,100));
        action.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                entry.entrySub(targa.getText(), "XX");
            }
        });
        actionPanel.add(action, BorderLayout.CENTER);
        actionPanel.setBackground(Color.decode("#778ca3"));
        displayContainer.add(actionPanel, BorderLayout.SOUTH);

        infoIS = new JTextArea();
        infoIS.setBorder(BorderFactory.createMatteBorder(
                10, 10, 10, 10, Color.decode("#4b6584")));
        infoIS.setEditable(false);
        infoIS.setLineWrap(true);
        infoIS.setPreferredSize(new Dimension(300, 300));
        setFont(topCenterPanel, new Font("Helvetica", Font.PLAIN, 20));
        setFont(display, new Font("Helvetica", Font.PLAIN, 30));
        setFont(infoIS, new Font("Helvetica", Font.PLAIN, 30));
        setFont(actionPanel, new Font("Helvetica", Font.PLAIN, 30));

        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.EAST);
        card.add(Box.createRigidArea(new Dimension(40,10)), BorderLayout.WEST);
        card.add(topPanel, BorderLayout.NORTH);
        card.add(displayContainer, BorderLayout.CENTER);
        card.add(infoIS, BorderLayout.SOUTH);

        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                infoIS.setText("");
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
        tariffT.setText("" + entry.getTariff());
        updateTariff();
        // If per far comparire stringa di benvenuto che altrimenti verrebbe sovrascritta
        if(!entry.getInfoBox().equals(""))
        {
            info.setText(entry.getInfoBox());
        }
        infoT.setText(entry.getInfoBox());
        infoS.setText(entry.getInfoBox());
        infoIS.setText(entry.getInfoBox());
    }

    public void updateTariff()
    {
        String sw = (String) comboSub.getSelectedItem();
        switch (sw)
        {
            case "Mensile":
                tariffS.setText("Tariffa : " + entry.getMonthlySubTariff());
                break;
            case "Semestrale":
                tariffS.setText("Tariffa : " + entry.getSemestralSubTariff());
                break;
            case "Annuale":
                tariffS.setText("Tariffa : " + entry.getAnnualSubTariff());
                break;
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
