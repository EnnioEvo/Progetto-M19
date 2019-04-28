package GUIs;

import main.Floor;
import main.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.ArrayList;

public class ManagerGUI implements ItemListener
{
    private Manager man;
    JPanel cards;  //Pannello che usa CardLayout
    final static String MAKEFLOORS = "Crea piani";
    final static String TARRIFF = "Scegli tariffa";
    final static String REMOVEFLOOR = "Rimuovi piano";

    public ManagerGUI(Manager man)
    {
        this.man = man;
        JFrame f = new JFrame();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        f.setSize(screenWidth / 2, screenHeight / 2);
        f.setLocation(screenWidth / 4, screenHeight / 4);
        f.setTitle("Manager");

        initComponents(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void initComponents(JFrame f)
    {
        //Metto JComboBox in un JPanel per un look migliore
        JPanel comboBoxPane = new JPanel();
        comboBoxPane.setLayout(new GridLayout(1,2));
        String comboBoxItems[] = {MAKEFLOORS, REMOVEFLOOR, TARRIFF};
        JComboBox<String> cb = new JComboBox<>(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        JTextField jf = new JTextField("Cosa vuoi fare?");
        jf.setEditable(false);
        comboBoxPane.add(jf);
        comboBoxPane.add(cb);
        comboBoxPane.setPreferredSize(new Dimension(500, 100));
        setFont(comboBoxPane, new Font("Helvetica", Font.PLAIN, 30));

        //Schermata 1: crea i piani
        JPanel card1 = makeFloorsCard();
        //Schermata 2: elimina piano
        JPanel card2 = removeFloorCard();
        //Schermata 3: tariffa
        JPanel card3 = chooseTariffCard();

        //Creo il pannello che contiene le "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, MAKEFLOORS);
        cards.add(card2, REMOVEFLOOR);
        cards.add(card3, TARRIFF);

        f.add(comboBoxPane, BorderLayout.NORTH);
        f.add(cards, BorderLayout.CENTER);
    }

    //***********Creo le schermate del manager*********************

    public JPanel makeFloorsCard()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        JTextField jf1 = new JTextField("Numero piani: ");
        JTextField jf2 = new JTextField("Numero posti: ");
        jf1.setEditable(false);
        jf2.setEditable(false);
        JTextField floors = new JTextField();
        JTextField spaces = new JTextField();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        topPanel.add(jf1); topPanel.add(floors); topPanel.add(jf2); topPanel.add(spaces);
        topPanel.setPreferredSize(new Dimension(500,100));
        JButton create = new JButton("Crea");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    man.makeFloors(Integer.parseInt(floors.getText()), Integer.parseInt(spaces.getText()));
                    ArrayList<Floor> fl = man.getFloorsList();
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<fl.size();i++)
                    {
                        sb.append("Piano ");
                        sb.append(fl.get(i).getId());
                        sb.append(", posti ");
                        sb.append(fl.get(i).getFreeSpace());
                        sb.append("\n");
                    }
                    info.setText(sb.toString());
                }
                catch(NumberFormatException ex)
                {
                    info.setText("Numeri inseriti errati");
                }
            }
        });
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(create); bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 40));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    public JPanel removeFloorCard()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JTextField jf1 = new JTextField("Piano da rimuovere: ");
        jf1.setEditable(false);
        JTextField floor = new JTextField();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        topPanel.add(jf1); topPanel.add(floor);
        topPanel.setPreferredSize(new Dimension(500,50));
        JButton remove = new JButton("Rimuovi");
        remove.setPreferredSize(new Dimension(200,100));
        remove.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    man.removeFloor(Integer.parseInt(floor.getText()));
                    ArrayList<Floor> fl = man.getFloorsList();
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<fl.size();i++)
                    {
                        sb.append("Piano ");
                        sb.append(fl.get(i).getId());
                        sb.append(", posti ");
                        sb.append(fl.get(i).getFreeSpace());
                        sb.append("\n");
                    }
                    info.setText(sb.toString());
                }
                catch(NumberFormatException ex)
                {
                    info.setText("Numeri inseriti errati");
                }
            }
        });
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(remove); bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 40));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    public JPanel chooseTariffCard()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JTextField jf1 = new JTextField("Tariffa: ");
        jf1.setEditable(false);
        JTextField tariff = new JTextField();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        topPanel.add(jf1); topPanel.add(tariff);
        topPanel.setPreferredSize(new Dimension(500,50));
        JButton create = new JButton("Scegli");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    man.setTariff(Integer.parseInt(tariff.getText()));
                    String st = "Tariffa attuale: " + man.getTariff();
                    info.setText(st);
                }
                catch(NumberFormatException ex)
                {
                    info.setText("Numeri inseriti errati");
                }
            }
        });
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(create); bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 40));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    //******************************************************

    public void itemStateChanged(ItemEvent evt)
    {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
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
