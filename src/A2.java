/*  Spencer Hamish Voorend svoo432 897480273
A2.java, the main file for the assignment. This assignment takes in IP data from the Auckland Satellite Simulator,
wrangles it and graphs it with multiple categorical variables.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.math.*;

/**
 * A2 extends JFrame to create a GUI. It contains the methods that initiate the DataProcess and IPSort classes.
 */

public class A2 extends JFrame {
    private GraphPanelDefault defaultpanel;
    private GraphPanel panel;
    private JPanel radioButtonPanel;
    private ButtonGroup radioButtons;
    private JRadioButton radioSourceHosts;
    private JRadioButton radioDestinationHosts;
    private Font font;
    private JComboBox<String> IPComboBox;
    private DataProcess d;
    private ArrayList<Double> bytestotal;

    /**
     * The constructor for A2.
     */
    public A2() {
        super("Flow volume viewer");
        setLayout(new FlowLayout());
        setSize(1100, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        font = new Font("Sans-serif", Font.PLAIN, 20);
        setupRadioButtons();
        setupComboBox();
        setupMenu();
        setupDefaultGraph();
        setVisible(true);

    }

    private void setupMenu() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        menubar.add(fileMenu);
        JMenuItem fileOpentrace = new JMenuItem("Open trace file");
        JMenuItem filequit = new JMenuItem("Quit");
        fileMenu.add(fileOpentrace);
        fileOpentrace.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                JFileChooser fileChooser = new JFileChooser(".");
                                                int retval = fileChooser.showOpenDialog(A2.this);
                                                if (retval == JFileChooser.APPROVE_OPTION) {
                                                    String f = fileChooser.getSelectedFile().getPath();
                                                    d = new DataProcess(f);
                                                    if (radioDestinationHosts.isSelected() == true) {
                                                        updateComboBox(d.destreturn());
                                                    } else {
                                                        updateComboBox(d.sourcereturn());
                                                    }

                                                }
                                            }
                                        }
        );
        fileMenu.add(filequit);
        filequit.addActionListener(new ActionListener() {
                                       public void actionPerformed(ActionEvent e) {
                                           System.exit(0);
                                       }
                                   }
        );
    }

    private void setupRadioButtons() {
        radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        radioButtons = new ButtonGroup();
        buttonhandler h = new buttonhandler();
        radioSourceHosts = new JRadioButton("Source hosts");
        radioSourceHosts.setFont(font);
        radioSourceHosts.setSelected(true);
        radioSourceHosts.addActionListener(h);
        radioButtons.add(radioSourceHosts);
        radioButtonPanel.add(radioSourceHosts, c);
        radioDestinationHosts = new JRadioButton("Destination hosts");
        radioDestinationHosts.setFont(font);
        radioDestinationHosts.addActionListener(h);
        radioButtons.add(radioDestinationHosts);
        radioButtonPanel.add(radioDestinationHosts, c);
        add(radioButtonPanel);
    }

    private void setupDefaultGraph() {
        defaultpanel = new GraphPanelDefault();
        defaultpanel.setLayout(null);
        Dimension panelsize = new Dimension(1100, 325);
        defaultpanel.setPreferredSize(panelsize);
        add(defaultpanel);

    }

    private void setupGraph() {
        defaultpanel.setVisible(false);
        panel = new GraphPanel();
        panel.setLayout(null);
        Dimension panelsize = new Dimension(1100, 325);
        panel.setPreferredSize(panelsize);
        repaint();
        add(panel);

    }

    private void setupComboBox() {
        IPComboBox = new JComboBox<>();
        IPComboBox.setMaximumRowCount(8);
        IPComboBox.setFont(font);
        IPComboBox.setMinimumSize(new Dimension(300, 25));
        combohandler c = new combohandler();
        IPComboBox.addActionListener(c);
        IPComboBox.setVisible(false);
        add(IPComboBox);
    }

    private void updateComboBox(String[] Sorted) {
        IPComboBox.removeAllItems();
        for (String i : Sorted) {
            IPComboBox.addItem(i);
            IPComboBox.setVisible(true);
        }

    }

    /**
     * Buttonhandler implements the ActionListener interface to check which button is selected. It then updates the
     * combobox with the selected IP type.
     */
    class buttonhandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (radioDestinationHosts.isSelected() == true) {
                updateComboBox(d.destreturn());
            } else {
                updateComboBox(d.sourcereturn());
            }
        }
    }

    /**
     * combohandler implements the ActionListener interface to check which IP is selected in the combobox. This is then
     * passed as a parameter to the DataProcess method "process2" to return the packet sums for that IP. It then
     * calls the setupGraph method to graph the data. It catches the NullPointerException to prevent this error when
     * nothing has been "selected" when the GUI is first launched.
     */
    class combohandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                bytestotal = d.process2((IPComboBox.getSelectedItem().toString()));
                setupGraph();
            } catch (Exception NullPointerException) {
            }

        }
    }

    /**
     * Creates the default graph on startup of the GUI/Application. In future I would extend the GraphPanel from the default
     * to avoid replication.
     */
    class GraphPanelDefault extends JPanel {

        public GraphPanelDefault() {
            super();
            setVisible(true);
            setBackground(Color.white);
        }

        /**
         * Overrides the paintGraphic to generate the default panel and axis.
         * @param g
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawLine(50, 270, 50, 30); //y-axis
            g.drawLine(40, 260, 1080, 260); //x-axis
            g.drawString("Volume [bytes]", 10, 15);
            g.drawString("Time [s]", 550, 310);

            g.drawString(Integer.toString(0), 28, 266); //y-zero label

            //x-axis notches + labels
            int x = 50;
            for (int i = 0; i < 14; i++) {
                g.drawLine(x, 260, x, 270);
                x += 75;
            }
            g.drawString(Integer.toString(0), 47, 285); // x-zero label
            g.drawString(Integer.toString(50), 118, 285); //x-50 label
            int xlabel = 100;
            int x2 = 188;
            for (int i = 0; i < 12; i++) { //x 100-650 labels
                g.drawString(Integer.toString(xlabel), x2, 285);
                x2 += 75;
                xlabel += 50;
            }
        }
    }
    /**
     * Creates the graph based on what IP is selected and what IP type. It creates and scales the x and y axises and
     * labels. In future I would extend the GraphPanel from the defaul to avoid replication.
     */
    class GraphPanel extends JPanel {
        public GraphPanel() {
            super();
            setVisible(true);
            setBackground(Color.white);
        }

        /**
         * Overrides the paintComponent to draw the graph using manual graphic generation.
         * @param g
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int xspacer = 0;
            double ymax = Collections.max(bytestotal);
            double ycoef = 230 / ymax;
            for (Double item : bytestotal) {
                //System.out.println("paint " + item);
                int yheight = (int) (ycoef * item);
                //System.out.println("y height " + (int) (ycoef*item));
                g.setColor(Color.red);
                g.drawRect(50 + xspacer, 260 - yheight, 3, yheight);
                g.setColor(Color.black);
                xspacer += 3;
            }

            g.drawLine(50, 270, 50, 30); //y-axis
            g.drawLine(40, 260, 1080, 260); //x-axis
            g.drawString("Volume [bytes]", 10, 15);
            g.drawString("Time [s]", 550, 310);


            //y-axis labels//
            MathContext mc = new MathContext(3, RoundingMode.CEILING);
            BigDecimal bd = new BigDecimal(ymax, mc);
            int ylabelincrement = bd.intValue() / 6;
            int ylabel = ylabelincrement;
            int ylabelposition = 225;
            String numberstring = "";
            for (int i = 0; i < 6; i++) { //formatting the y labels
                if (ylabel > 100000 && ylabel < 10000000) {
                    numberstring = String.format("%,d", ylabel).substring(0, 3) + "M";
                    numberstring = numberstring.replaceAll(",", ".");
                }
                if (ylabel >= 100000 && ylabel < 1000000) {
                    numberstring = Integer.toString(ylabel).substring(0, 3) + "K";
                }
                if (ylabel >= 10000 && ylabel < 100000) {
                    numberstring = String.format("%,d", ylabel).substring(0, 2) + "K";
                }
                if (ylabel < 10000) {
                    numberstring = Integer.toString(ylabel);
                }


                g.drawString(numberstring, 5, ylabelposition);
                ylabelposition -= 38;
                ylabel += ylabelincrement;
            }
            //y-notches//
            int y = 220;
            for (int i = 0; i < 6; i++) {
                g.drawLine(40, y, 50, y);
                y -= 38;
            }
            g.drawString(Integer.toString(0), 28, 266); //y-zero label

            //x-axis notches//
            int x = 50;
            for (int i = 0; i < 14; i++) {
                g.drawLine(x, 260, x, 270);
                x += 75;
            }
            //x-axis labels//
            g.drawString(Integer.toString(0), 47, 285); // x-zero label
            g.drawString(Integer.toString(50), 118, 285); //x-50 label
            int xlabel = 100;
            int x2 = 188;
            for (int i = 0; i < 12; i++) { //x 100-650 labels
                g.drawString(Integer.toString(xlabel), x2, 285);
                x2 += 75;
                xlabel += 50;
            }
        }
    }

}

