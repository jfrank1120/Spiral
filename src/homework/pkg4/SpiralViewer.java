package homework.pkg4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;

/**
 * The viewer component of the Spiral program. Allows the user to customize
 * their spiral in different ways such as: line length, radius, spiral color,
 * background color, and much more! Also allows for the user to saved their
 * created spiral for later user.
 *
 * @author Jared
 * @date 5/1/2017
 */
public class SpiralViewer extends JFrame {

    static Timer timer;
    static int rateChange = 1;
    final static int INIT_LINE_LENGTH = 1;
    final static int INIT_RADIUS = 10;
    static int radius = 10;
    private final JSlider lineLengthSlider = new JSlider(JSlider.VERTICAL, 1, 8, 1);
    private final JSlider radiusSlider = new JSlider(JSlider.VERTICAL, 1, 20, INIT_RADIUS);
    private final JPopupMenu popupMenu = new JPopupMenu();
    private final Spiral SPIRAL = new Spiral(INIT_RADIUS, Color.PINK, Color.BLACK, INIT_LINE_LENGTH);

    public SpiralViewer() {
        initTimer();
        JMenuBar mBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("OPTIONS");
        setJMenuBar(mBar);
        mBar.add(getMenu());
        // create SPIRAL and place in center of frame
        SPIRAL.setPreferredSize(new Dimension(800, 400));
        setPanels();
    }

    /**
     * Creates the options menu along with its menuitems
     */
    private JMenu getMenu() {
        JMenu menu = new JMenu("OPTIONS");
        JMenuItem[] items = {
            new JMenuItem("Save"),
            new JMenuItem("New Shape"),
            new JMenuItem("Information")
        };
        for (JMenuItem menuItem : items) {
            menu.add(menuItem);
        }
        items[0].addActionListener(e -> {
            saveSpiral();
        });
        items[1].addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Not supported yet.")
        );
        items[2].addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Not supported yet.")
        );
        return menu;
    }

    /**
     * Creates a JLabel with the input message and color of writing
     *
     * @param msg - Message to be displayed
     * @param writingColor - Color of the text
     * @return - JLabel
     */
    public static JLabel createLabel(String msg, Color writingColor) {
        JLabel label = new JLabel(msg);
        label.setForeground(writingColor);
        return label;
    }

    /**
     * Creates a new checkBox
     */
    private JCheckBox createCheckBox() {
        // Checkbox for toggling nautilus shell
        JCheckBox checkbox = new JCheckBox("Nautilus Shell");
        checkbox.setBackground(Color.BLACK);
        checkbox.setForeground(Color.CYAN);
        checkbox.setFocusable(false);
        checkbox.addActionListener(ActionEvent -> {
            SPIRAL.setNautilusShell(checkbox.isSelected());
        });
        return checkbox;
    }

    /**
     * Saves the spiral that the user currently has created and puts it into
     * another frame
     */
    private void saveSpiral() {
        String nameOfSave = "";
        try {
            nameOfSave = (String) JOptionPane.showInputDialog(rootPane, "Save Spiral As");
        } catch (NullPointerException e) {

        }
        if ((nameOfSave != null) && (nameOfSave.length() > 0)) {
            JFrame savedSpiralFrame = new JFrame(nameOfSave);
            savedSpiralFrame.setPreferredSize(new Dimension(500, 500));
            savedSpiralFrame.add(new Spiral(SPIRAL.getRadius(), SPIRAL.getSpiralColor(), SPIRAL.getBackgroundColor(), SPIRAL.getLineLength()));
            savedSpiralFrame.setVisible(true);
            savedSpiralFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            savedSpiralFrame.pack();
        }
    }

    private void setPanels() {
        add(SPIRAL, BorderLayout.CENTER);
        add(controlPanel(sliderPanel(), middlePanel(), bottomPanel()), BorderLayout.WEST);
        //add(getMenu());
    }

    /**
     * Creates a panel that contains two sliders and their labels
     *
     * @return
     */
    private JPanel sliderPanel() {
        // Top panel containing two sliders that control line length and radius length
        JPanel sliderPanel = new JPanel();
        sliderPanel.setBackground(Color.BLACK);
        sliderPanel.setBorder(new EtchedBorder(Color.lightGray, Color.WHITE));
        // slider for controling radius
        radiusSlider.setBackground(Color.BLACK);
        radiusSlider.setToolTipText("Change the Radius");
        radiusSlider.addChangeListener((ChangeEvent e) -> {
            SPIRAL.setRadius(radiusSlider.getValue());
        });
        sliderPanel.add(radiusSlider);
        sliderPanel.add(createLabel("Set Radius Length", Color.CYAN));
        lineLengthSlider.setBackground(Color.BLACK);
        lineLengthSlider.setToolTipText("Change the length of the lines creating the SPIRAL");
        lineLengthSlider.addChangeListener((ChangeEvent e) -> {
            SPIRAL.setLineLength(lineLengthSlider.getValue());
        });
        sliderPanel.add(lineLengthSlider);
        sliderPanel.add(createLabel("Set Line Length", Color.CYAN));
        return sliderPanel;
    }

    // Middle panel that contains: checkbox, resetbutton, ocelation checkbox,
    // and occelation speed spinner
    private JPanel middlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setBackground(Color.BLACK);
        middlePanel.setBorder(new EtchedBorder(Color.lightGray, Color.WHITE));
        middlePanel.setLayout(new GridLayout(2, 2, 5, 20));
        // Spinner for change of radius while timer is on
        SpinnerModel model = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener((ChangeEvent e) -> {
            int currentVal = (Integer) spinner.getValue();
            if (currentVal > 10) {
                currentVal = 10;
                model.setValue(10);
            }
            if (currentVal < 1) {
                currentVal = 1;
                model.setValue(1);
            }
            rateChange = currentVal;
        });
        // Panel that contains the spinner that controls the speed of oscellation
        JPanel spinnerPanel = new JPanel();
        spinnerPanel.add(spinner);
        spinnerPanel.add(createLabel("Set Speed", Color.CYAN));
        spinnerPanel.setBackground(Color.BLACK);
        JCheckBox checkbox2 = new JCheckBox("Oscillate Spiral");
        checkbox2.setBackground(Color.BLACK);
        checkbox2.setForeground(Color.CYAN);
        checkbox2.setFocusable(false);
        checkbox2.addActionListener((ActionEvent e) -> {
            if (checkbox2.isSelected()) {
                timer.start();
            } else {
                timer.stop();
            }
        });
        middlePanel.add(createCheckBox());
        JButton resetButton = new JButton("Reset Spiral");
        resetButton.setBackground(Color.WHITE);
        resetButton.addActionListener((ActionEvent e) -> {
            SPIRAL.setRadius(10);
            radiusSlider.setValue(10);
            SPIRAL.setLineLength(1.0);
            lineLengthSlider.setValue(1);
        });
        middlePanel.add(resetButton);
        middlePanel.add(checkbox2);
        middlePanel.add(spinnerPanel);
        return middlePanel;
    }

    // Bottom panel that contains color selector for both the spiral, the
    // background, and the nautilius as well
    private JPanel bottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(new EtchedBorder(Color.lightGray, Color.WHITE));
        JButton selectBackgroundColor = new JButton("Background Color");
        selectBackgroundColor.setBackground(Color.WHITE);
        selectBackgroundColor.addActionListener((ActionEvent e) -> {
            SPIRAL.setBackgroundColor(JColorChooser.showDialog(popupMenu, "Background Color", SPIRAL.getBackgroundColor()));
        });
        bottomPanel.add(selectBackgroundColor);
        JButton selectSpiralColor = new JButton("Color of Spiral");
        selectSpiralColor.setBackground(Color.WHITE);
        selectSpiralColor.addActionListener((ActionEvent e) -> {
            SPIRAL.setSpiralColor(JColorChooser.showDialog(popupMenu, "Sprial Color", SPIRAL.getSpiralColor()));
        });
        bottomPanel.add(selectSpiralColor);
        return bottomPanel;
    }

    // Control Panel that contains all subpanels with components
    private JPanel controlPanel(JPanel slider, JPanel middle, JPanel bottom) {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(slider);
        controlPanel.add(middle);
        controlPanel.add(bottom);
        return controlPanel;
    }

    /**
     * Initializes a timer for the oscillation of the spiral
     */
    private void initTimer() {
        class TimerListener implements ActionListener {

            private final int MAX_RADIUS = 100;
            private final int MIN_RADIUS = 10;
            private boolean radiusIncreasing;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (radiusIncreasing) {
                    radius = radius + rateChange;
                } else {
                    radius = radius - rateChange;
                }
                if (radius <= MIN_RADIUS) {
                    radiusIncreasing = true;
                    radius = MIN_RADIUS;
                } else if ((radius >= MAX_RADIUS)) {
                    radiusIncreasing = false;
                    radius = MAX_RADIUS;
                }
                SPIRAL.setRadius(radius);
            }
        }
        timer = new Timer(50, new TimerListener());
    }

    public static void main(String[] args) {
        SpiralViewer viewer = new SpiralViewer();
        viewer.setTitle("Spiraling Out Of Control");
        viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewer.setVisible(true);
        viewer.setSize(900, 500);
    }
}
