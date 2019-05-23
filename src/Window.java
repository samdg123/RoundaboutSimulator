import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Window {

	private final int maxCarsSliderMax = 30;
	private final int genSliderMax = 50;
	
	private String testCaseBtnText;
	
	private JFrame frame;
	private JPanel controllerPanel;
	
	private JPanel testCasePanel;
	private OutputPanel outputPanel = OutputPanel.outputPanel();
	private JScrollBar scrollBar;
	private JButton test1Button;
	private JButton test2Button;
	private JButton test3Button;
	private JButton test4Button;
	
	SimulatorController simCont;
	JButton startStopButton;
	boolean started = false;
	private JCheckBox cbAIn;
	private JCheckBox cbBIn;
	private JCheckBox cbCIn;
	private JCheckBox cbDIn;
	private JButton customTestButton;
	private JLabel startingRoadsLbl;
	private Component verticalStrut1;
	private Component verticalStrut2;
	private Component verticalStrut3;
	private JLabel endingRoadsLbl;
	private JCheckBox cbAOut;
	private JCheckBox cbBOut;
	private JCheckBox cbCOut;
	private JCheckBox cbDOut;
	private Component verticalStrut4;
	private JLabel genLbl;
	private JSlider genSlider;
	private Component verticalStrut5;
	private JLabel maxCarsLbl;
	private JSlider maxCarsSlider;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
		simCont.setGenSliderMax(genSliderMax+1);
	}
	
	private void initializeControllerPanel() {
		outputPanel.getDocument().addDocumentListener(new TextAreaListener());
		simCont.setOutputPanel(outputPanel);
		JScrollPane scrollPane = new JScrollPane(outputPanel);
		scrollBar = scrollPane.getVerticalScrollBar();
		
		controllerPanel = new JPanel();
		controllerPanel.setBackground(Color.gray);
		
		startStopButton = new JButton("Start");
		startStopButton.setHorizontalAlignment(SwingConstants.LEFT);
		startStopButton.setEnabled(false);
		
		startStopButton.addActionListener(new StartStopListener());
		
		controllerPanel.add(startStopButton);
		controllerPanel.add(scrollPane);
		
		controllerPanel.setSize(300, 210);
		
	}
	
	private void initializeTestCasePanel() {
		testCasePanel = new JPanel();
		testCasePanel.setLayout(new BoxLayout(testCasePanel, BoxLayout.PAGE_AXIS));
		test1Button = new JButton("Test Case 1");
		test2Button = new JButton("Test Case 2");
		test3Button = new JButton("Test Case 3");
		test4Button = new JButton("Test Case 4");
		customTestButton = new JButton("Custom Test");
		
		cbAIn = new JCheckBox("A");
		cbBIn = new JCheckBox("B");
		cbCIn = new JCheckBox("C");
		cbDIn = new JCheckBox("D");
		cbAOut = new JCheckBox("A");
		cbBOut = new JCheckBox("B");
		cbCOut = new JCheckBox("C");
		cbDOut = new JCheckBox("D");
		
		cbAIn.setSelected(true);
		cbBIn.setSelected(true);
		cbCIn.setSelected(true);
		cbDIn.setSelected(true);
		cbAOut.setSelected(true);
		cbBOut.setSelected(true);
		cbCOut.setSelected(true);
		cbDOut.setSelected(true);
		
		startingRoadsLbl = new JLabel("Starting Road");
		startingRoadsLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		startingRoadsLbl.setForeground(Color.WHITE);
		
		endingRoadsLbl = new JLabel("Ending Roads");
		endingRoadsLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		endingRoadsLbl.setForeground(Color.WHITE);
		
		genLbl = new JLabel("Generation Speed");
		genLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		genLbl.setForeground(Color.WHITE);

		maxCarsLbl = new JLabel("Max Cars");
		maxCarsLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		maxCarsLbl.setForeground(Color.WHITE);
		
		
		verticalStrut1 = Box.createVerticalStrut(20);
		verticalStrut2 = Box.createVerticalStrut(10);
		verticalStrut3 = Box.createVerticalStrut(10);
		verticalStrut4 = Box.createVerticalStrut(10);
		verticalStrut5 = Box.createVerticalStrut(10);
		
		genSlider = new JSlider();
		genSlider.setMajorTickSpacing(20);
		genSlider.setPaintTicks(true);
		genSlider.setPaintLabels(true);
		genSlider.setMinimum(1);
		genSlider.setMaximum(genSliderMax-1);
		genSlider.setValue(genSliderMax/2);
		genSlider.setLabelTable(genSlider.createStandardLabels(10, 10));
		
		maxCarsSlider = new JSlider();
		maxCarsSlider.setMajorTickSpacing(5);
		maxCarsSlider.setPaintTicks(true);
		maxCarsSlider.setPaintLabels(true);
		maxCarsSlider.setMinimum(1);
		maxCarsSlider.setMaximum(maxCarsSliderMax);
		maxCarsSlider.setValue(maxCarsSliderMax/2);
		maxCarsSlider.setLabelTable(maxCarsSlider.createStandardLabels(10, 10));
		
		
		test1Button.addActionListener(new testCaseListener());
		test2Button.addActionListener(new testCaseListener());
		test3Button.addActionListener(new testCaseListener());
		test4Button.addActionListener(new testCaseListener());
		customTestButton.addActionListener(new testCaseListener());
		
		testCasePanel.setBackground(Color.gray);
		
		testCasePanel.add(test1Button);
		testCasePanel.add(test2Button);
		testCasePanel.add(test3Button);
		testCasePanel.add(test4Button);
		testCasePanel.add(verticalStrut1);
		testCasePanel.add(customTestButton);
		testCasePanel.add(verticalStrut2);
		testCasePanel.add(startingRoadsLbl);
		testCasePanel.add(cbAIn);
		testCasePanel.add(cbBIn);
		testCasePanel.add(cbCIn);
		testCasePanel.add(cbDIn);
		testCasePanel.add(verticalStrut3);
		testCasePanel.add(endingRoadsLbl);
		testCasePanel.add(cbAOut);
		testCasePanel.add(cbBOut);
		testCasePanel.add(cbCOut);
		testCasePanel.add(cbDOut);
		testCasePanel.add(verticalStrut4);
		testCasePanel.add(genLbl);
		testCasePanel.add(genSlider);
		testCasePanel.add(verticalStrut5);
		testCasePanel.add(maxCarsLbl);
		testCasePanel.add(maxCarsSlider);
		
		testCasePanel.setSize(239, 600);
		
		setIfCustomControlsEnabled(false);
	}

	
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.setTitle("Roundabout Simulator - Sami Aljumily");

		simCont = SimulatorController.simCont();
		
		initializeControllerPanel();
		initializeTestCasePanel();
		
		frame.getContentPane().add( simCont.getView(), BorderLayout.WEST);
	    frame.getContentPane().add( controllerPanel, BorderLayout.SOUTH );
		frame.getContentPane().add( testCasePanel, BorderLayout.EAST);
		
		System.out.println(simCont.getView().getWidth());
		
		frame.setSize((simCont.getView().getWidth() + testCasePanel.getWidth()), simCont.getView().getHeight() + controllerPanel.getHeight());

	    
	}
	
	public void appendOutputPanel(String text) {
		outputPanel.append("\n" + text);
		scrollBar.setValue(scrollBar.getMaximum());		
	}
	
	private void setIfCustomControlsEnabled(boolean enabled) {
		cbAIn.setEnabled(enabled);
		cbBIn.setEnabled(enabled);
		cbCIn.setEnabled(enabled);
		cbDIn.setEnabled(enabled);
		cbAOut.setEnabled(enabled);
		cbBOut.setEnabled(enabled);
		cbCOut.setEnabled(enabled);
		cbDOut.setEnabled(enabled);
		
		genSlider.setEnabled(enabled);
		maxCarsSlider.setEnabled(enabled);	
	}
	
		
	public class StartStopListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (started){
				appendOutputPanel("Simulation stopped\n");
				simCont.stopSim();
				startStopButton.setText("Start");
			} else {
				
				if (testCaseBtnText == "Custom Test") {
					if (getStartingRoads().isEmpty()) {
						appendOutputPanel("Please select at least 1 starting road!");
						return;
					}
					
					if (getEndingRoads().isEmpty()) {
						appendOutputPanel("Please select at least 1 ending road!");
						return;
					}

					simCont.setStartingRoads(getStartingRoads());
					simCont.setEndingRoads(getEndingRoads());
					simCont.setGenSpeed(genSlider.getValue());
					simCont.setMaxCars(maxCarsSlider.getValue());
					
					System.out.println();
				}
				appendOutputPanel("Simulation started:");
				simCont.startSim();
				startStopButton.setText("Stop");
			}
			
			started=!started;
		
		}
	
	}
	
	public class testCaseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			testCaseBtnText = ((JButton)e.getSource()).getText();
			
			switch (testCaseBtnText) {
			case "Test Case 1":
				simCont.selectTestCase(1);
				break;
			case "Test Case 2":
				simCont.selectTestCase(2);
				break;
			case "Test Case 3":
				simCont.selectTestCase(3);
				break;
			case "Test Case 4":
				simCont.selectTestCase(4);
				break;
			case "Custom Test":								
				simCont.selectTestCase(5);
				break;
			}
			
			if (testCaseBtnText == "Custom Test") {
				setIfCustomControlsEnabled(true);
			} else {
				setIfCustomControlsEnabled(false);
			}

			appendOutputPanel(testCaseBtnText + " Selected:");
			
			if (started) {
				simCont.stopSim();
			started = false;
			}
			
			startStopButton.setText("Start");
			
			startStopButton.setEnabled(true);
		}
		
	}
	
	public class TextAreaListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			outputPanel.setCaretPosition(outputPanel.getDocument().getLength());
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			
		}


		
	}
	
	public OutputPanel getOutputPanel() {return outputPanel;}
	
	public ArrayList<Car.RoadLbl> getStartingRoads() {
		ArrayList<Car.RoadLbl> roads = new ArrayList<Car.RoadLbl>();
		
		if (cbAIn.isSelected()) {
			roads.add(Car.RoadLbl.a);
		}
		if (cbBIn.isSelected()) {
			roads.add(Car.RoadLbl.b);
		}
		if (cbCIn.isSelected()) {
			roads.add(Car.RoadLbl.c);
		}
		if (cbDIn.isSelected()) {
			roads.add(Car.RoadLbl.d);
		}
		
		return roads;
	}
	
	public ArrayList<Car.RoadLbl> getEndingRoads() {
		ArrayList<Car.RoadLbl> roads = new ArrayList<Car.RoadLbl>();
		
		if (cbAOut.isSelected()) {
			roads.add(Car.RoadLbl.a);
		}
		if (cbBOut.isSelected()) {
			roads.add(Car.RoadLbl.b);
		}
		if (cbCOut.isSelected()) {
			roads.add(Car.RoadLbl.c);
		}
		if (cbDOut.isSelected()) {
			roads.add(Car.RoadLbl.d);
		}
		
		return roads;
	}

}