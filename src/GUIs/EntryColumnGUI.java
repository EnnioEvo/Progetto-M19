package GUIs;

import main.Peripherals.Columns.EntryColumn;
import main.Peripherals.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class EntryColumnGUI implements ItemListener, Observer
{
    private EntryColumn entry;
    private JPanel cards;  //Pannello che usa CardLayout
    final private static String TICKET = "Ticket";
    final private static String SUB = "Abbonamento";
    private JFrame f;
    private JTextField tariff;

    public EntryColumnGUI(EntryColumn entry)
    {
        this.entry = entry;
        f = new JFrame();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        f.setSize(screenWidth / 2, screenHeight / 2);
        f.setLocation(screenWidth / 4, screenHeight / 4);
        f.setTitle("Entry Column");

        initComponents(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void initComponents(JFrame f)
    {
        //Metto JComboBox in un JPanel per un look migliore
        JPanel comboBoxPane = new JPanel();
        comboBoxPane.setLayout(new GridLayout(1,2));
        String comboBoxItems[] = {TICKET, SUB};
        JComboBox<String> cb = new JComboBox<>(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        JTextField jf = new JTextField("Cosa vuoi fare?");
        jf.setEditable(false);
        comboBoxPane.add(jf);
        comboBoxPane.add(cb);
        comboBoxPane.setPreferredSize(new Dimension(500, 100));
        setFont(comboBoxPane, new Font("Helvetica", Font.PLAIN, 30));

        //Schermata 1: crea il ticket
        JPanel card1 = ticket();
        //Schermata 2: crea nuovo abbonamento
        JPanel card2 = Sub();


        //Creo il pannello che contiene le "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, TICKET);
        cards.add(card2, SUB);

        f.add(comboBoxPane, BorderLayout.NORTH);
        f.add(cards, BorderLayout.CENTER);
    }


    //- - - - - - - - - - - - - - -Schermata della Colonnina- - - - - - - - - - - - -

    private JPanel ticket()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 1));
        tariff = new JTextField("Tariffa : " + entry.getTariffofMan());
        JTextField jf2 = new JTextField("Inserire Targa: ");
        tariff.setEditable(false);
        jf2.setEditable(false);
        JTextField targa = new JTextField();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        //Reinizializzo JTextArea quando cambio card
        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentHidden(ComponentEvent e)
            {
                super.componentHidden(e);
                info.setText("");
            }
        });
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        topPanel.add(tariff);
        topPanel.add(jf2);
        topPanel.add(targa);
        topPanel.setPreferredSize(new Dimension(500,100));
        JButton create = new JButton("Acquista");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                entry.entryTicket(targa.getText());
            }
        });
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel());
        bottomPanel.add(create);
        bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 40));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }


    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


    private JPanel Sub()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 1));
        JTextField jf1 = new JTextField("Tariffa : "  + entry.getTariffofMan());
        JTextField jf2 = new JTextField("Inserire Targa: ");
        jf1.setEditable(false);
        jf2.setEditable(false);
        JTextField targa = new JTextField();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        //Reinizializzo JTextArea quando cambio card
        card.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentHidden(ComponentEvent e)
            {
                super.componentHidden(e);
                info.setText("");
            }
        });
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        topPanel.add(jf1);
        topPanel.add(jf2);
        topPanel.add(targa);
        topPanel.setPreferredSize(new Dimension(500,100));
        JButton create = new JButton("Acquista");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                entry.entrySub(targa.getText());
            }
        });
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel());
        bottomPanel.add(create);
        bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 40));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

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
        tariff.setText("Tariffa : "  + entry.getTariffofMan());
        System.out.println("update");
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
