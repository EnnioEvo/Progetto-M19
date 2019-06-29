package GUIs;

import Exceptions.NotEmptyFloorException;
import Exceptions.SubdivisionException;
import main.Manager.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("Duplicates")
public class ManagerGUI implements ItemListener
{
    private Manager man;
    private JPanel cards;  //Pannello che usa CardLayout
    final private static String MAKEFLOORS = "Crea piani";
    final private static String TARRIFF = "Scegli tariffa";
    final private static String REMOVEFLOOR = "Rimuovi piano";
    final private static String INFOFLOOR = "Informazioni piani";
    final private static String SUBDIVISION = "Scegli suddivisione posti";
    final private static String DRIVERINFO = "Visualizza informazioni clienti";
    final private static String DELTATIME = "Scegli durata validità pagamento";
    final private static String SUBCOST = "Scegli costi abbonamenti";
    final private static String ANALYTICS = "Statistiche";



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


        String iconPath = "icons\\manager.png";
        f.setIconImage(new ImageIcon(iconPath).getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

    private void initComponents(JFrame f)
    {
        //Metto JComboBox in un JPanel per un look migliore
        JPanel comboBoxPane = new JPanel();
        comboBoxPane.setLayout(new GridLayout(1,2));
        String comboBoxItems[] = {MAKEFLOORS, REMOVEFLOOR, INFOFLOOR, TARRIFF, SUBDIVISION, DRIVERINFO, DELTATIME, SUBCOST, ANALYTICS};
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
        JPanel makeFloors = makeFloorsCard();
        //Schermata 2: elimina piano
        JPanel removeFloor = removeFloorCard();
        //Schermata 3: elimina piano
        JPanel infoFloor = infoFloorCard();
        //Schermata 4: tariffa
        JPanel setTariff = chooseTariffCard();
        //Schermata 5: divisione posti standard/in abbonamento
        JPanel subDivision = chooseSubdivisionCard();
        //Schermata 6: visualizza informazioni sui clienti nel parcheggio
        JPanel driverInfo = showDriversInfo();
        //Schermata 7: scelta deltatime
        JPanel deltaTime = chooseDeltaTime();
        //Schermata 8: scelta costo abbonamento
        JPanel subCost = chooseCostSub();
        //Schermata 9: statistiche
        JPanel analytics = analytics();


        //Creo il pannello che contiene le "cards".
        cards = new JPanel(new CardLayout());
        cards.add(makeFloors, MAKEFLOORS);
        cards.add(removeFloor, REMOVEFLOOR);
        cards.add(infoFloor, INFOFLOOR);
        cards.add(setTariff, TARRIFF);
        cards.add(subDivision, SUBDIVISION);
        cards.add(driverInfo, DRIVERINFO);
        cards.add(deltaTime, DELTATIME);
        cards.add(subCost, SUBCOST);
        cards.add(analytics, ANALYTICS);


        f.add(comboBoxPane, BorderLayout.NORTH);
        f.add(cards, BorderLayout.CENTER);
    }

    //***********Creo le schermate del manager*********************

    private JPanel makeFloorsCard()
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
                    info.setText(man.getFloorsInfo());
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

    private JPanel removeFloorCard()
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
                    info.setText(man.getFloorsInfo());
                }
                catch(NumberFormatException ex)
                {
                    info.setText("Numeri inseriti errati");
                }
                catch(NotEmptyFloorException ex)
                {
                    info.setText(ex.getMessage());
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

    private JPanel infoFloorCard()
    {

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
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

        JButton action = new JButton("Visualizza");
        action.setPreferredSize(new Dimension(200,100));
        action.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                info.setText(man.getFloorsInfo());
            }
        });
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(action); bottomPanel.add(new JPanel());
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 40));
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel chooseTariffCard()
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
    private JPanel chooseDeltaTime()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JTextField jf1 = new JTextField("Tempo d'uscita: ");
        jf1.setEditable(false);
        JTextField tempo = new JTextField();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        //Reinizializzo JTextArea quando cambio card
        card.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentHidden(ComponentEvent e)
            {
                super.componentHidden(e);
                info.setText("");
            }
        });
        topPanel.add(jf1); topPanel.add(tempo);
        topPanel.setPreferredSize(new Dimension(500,50));
        JButton create = new JButton("Scegli");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    man.setDeltaTimePaid(Integer.parseInt(tempo.getText()));
                    String st = "DeltaTime attuale: " + man.getDeltaTimePaid();
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

    private JPanel chooseSubdivisionCard()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JTextField jf1 = new JTextField("Posti riservati abbonamento: ");
        jf1.setEditable(false);
        JTextField sub = new JTextField();
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
        topPanel.add(jf1); topPanel.add(sub);
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
                    man.setSpacesSubdivision(Integer.parseInt(sub.getText()));
                    String st = "Posti totali: " + man.getFreeSpacesTot() + "\n" + "Posti abbonati: " + man.getFreeSpacesSubTot() + ", posti ticket: " + man.getFreeSpacesTicketTot();
                    info.setText(st);
                }
                catch(NumberFormatException ex)
                {
                    info.setText("Numeri inseriti errati");
                }
                catch(SubdivisionException ex)
                {
                    info.setText(ex.getMessage());
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

    private JPanel chooseCostSub()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        JTextField jf2 = new JTextField("Extra Costo: ");
        jf2.setEditable(false);
        String comboBoxItems[] = {"Mensile", "Semestrale", "Annuale"};
        JComboBox<String> cb = new JComboBox<>(comboBoxItems);
        cb.setEditable(false);
        JTextField costo = new JTextField();
        JTextField extra = new JTextField();
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
        topPanel.add(cb); topPanel.add(costo);topPanel.add(jf2); topPanel.add(extra);
        topPanel.setPreferredSize(new Dimension(500,100));
        JButton create = new JButton("Scegli");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    String sw = (String) cb.getSelectedItem();
                    switch (sw)
                    {
                        case "Mensile":
                            man.setMonthlyCost(Double.parseDouble(costo.getText()));
                            break;
                        case "Semestrale":
                            man.setSemestralCost(Double.parseDouble(costo.getText()));

                            break;
                        case "Annuale":
                            man.setAnnualCost(Double.parseDouble(costo.getText()));

                            break;
                    }

                    man.setExtraCost(Double.parseDouble(extra.getText()));

                    info.setText("Costo mensile:" + man.getMonthlyCost() + "\n" +
                            "Costo semestrale:" + man.getSemestralCost() + "\n" +
                            "Costo annuale:" + man.getAnnualCost() + "\n" +
                            "Costro extra:  " + man.getExtraCost());

                } catch (NumberFormatException ex ){
                    info.setText("numeri inserito errato");
                }
            }
        });
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(create); bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 20));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel showDriversInfo()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JTextField jf1 = new JTextField("Scegli categoria clienti: ");
        jf1.setEditable(false);
        String comboBoxItems[] = {"Solo clienti normali", "Solo abbonati", "Tutti"};
        JComboBox<String> cb = new JComboBox<>(comboBoxItems);
        cb.setEditable(false);
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
        topPanel.add(jf1); topPanel.add(cb);
        topPanel.setPreferredSize(new Dimension(500,50));
        JButton create = new JButton("Scegli");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String sw = (String)cb.getSelectedItem();
                switch(sw)
                {
                    case "Solo clienti normali":
                        info.setText(man.getDriversInfo());
                        break;
                    case "Solo abbonati":
                        info.setText(man.getSubDriversInfo());
                        break;
                    case "Tutti":
                        info.setText(man.getDriversInfo() + man.getSubDriversInfo());
                        break;
                }
            }
        });
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(create); bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 20));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel analytics()
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 3));
        JTextField dayStartTxt = new JTextField("Giorno inizio ");
        dayStartTxt.setEditable(false);
        JTextField dayStart = new JTextField();
        JTextField monthStartTxt = new JTextField("Mese inizio ");
        monthStartTxt.setEditable(false);
        JTextField monthStart = new JTextField();
        JTextField yearStartTxt = new JTextField("Anno inizio ");
        yearStartTxt.setEditable(false);
        JTextField yearStart = new JTextField();
        JTextField dayEndTxt = new JTextField("Giorno fine ");
        dayEndTxt.setEditable(false);
        JTextField dayEnd = new JTextField();
        JTextField monthEndTxt = new JTextField("Mese fine ");
        monthEndTxt.setEditable(false);
        JTextField monthEnd = new JTextField();
        JTextField yearEndTxt = new JTextField("Anno fine ");
        yearEndTxt.setEditable(false);
        JTextField yearEnd = new JTextField();

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
        topPanel.add(dayStartTxt); topPanel.add(dayStart);topPanel.add(monthStartTxt); topPanel.add(monthStart); topPanel.add(yearStartTxt); topPanel.add(yearStart);
        topPanel.add(dayEndTxt); topPanel.add(dayEnd);topPanel.add(monthEndTxt); topPanel.add(monthEnd); topPanel.add(yearEndTxt); topPanel.add(yearEnd);
        topPanel.setPreferredSize(new Dimension(500,100));
        JButton create = new JButton("Scegli");
        create.setPreferredSize(new Dimension(200,100));
        create.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    String from = yearStart.getText() + "-" + monthStart.getText() + "-" + dayStart.getText();
                    String to = yearEnd.getText() + "-" + monthEnd.getText() + "-" + dayEnd.getText();
                    double mean = man.analyticsMean(from, to);
                    info.setText("Secondi medi permanenza ticket: " + mean);

                } catch (RuntimeException ex)
                {
                    info.setText(ex.getMessage());
                }

            }
        });
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel bottomPanel = new JPanel(new GridLayout(1,3));
        bottomPanel.add(new JPanel()); bottomPanel.add(create); bottomPanel.add(new JPanel());
        setFont(topPanel, new Font("Helvetica", Font.PLAIN, 30));
        setFont(info, new Font("Helvetica", Font.PLAIN, 30));
        setFont(bottomPanel, new Font("Helvetica", Font.PLAIN, 30));
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
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