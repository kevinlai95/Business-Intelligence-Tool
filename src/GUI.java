package Olap;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends JFrame {

	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTable table;
	
	private ArrayList<String> dimensions; //dimensions to be operated on 
	private HashMap<String,String> map;

	private DAO dao;
	private JPanel panel_1;
	private JButton centralCube;
	private JLabel status;
	private JLabel status2;

	private DefaultComboBoxModel<String> operationMenu = new DefaultComboBoxModel<String>();
	private JComboBox<String> operationBox;
	private JScrollPane operationPane;
	private DefaultComboBoxModel<String> sliceMenu = new DefaultComboBoxModel<String>();
	private JComboBox<String> sliceBox;
	private JScrollPane slicePane;
	private DefaultComboBoxModel<String> diceMenu = new DefaultComboBoxModel<String>();
	private JComboBox<String> diceBox;
	private JScrollPane dicePane;
	private DefaultComboBoxModel<String> sliceCmpMenu = new DefaultComboBoxModel<String>();
	private JComboBox<String> sliceCmpBox;
	private JScrollPane sliceCmpPane;
	private DefaultComboBoxModel<String> diceCmpMenu = new DefaultComboBoxModel<String>();
	private JComboBox<String> diceCmpBox;
	private JScrollPane diceCmpPane;
	
	private JTextField slice;
	private JTextField dice;
	private JButton submit;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {

		// create the DAO
		try {
			dao = new DAO();
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
		}
		operationMenu.addElement("None");
		operationMenu.addElement("Roll Up");
		operationMenu.addElement("Drill Down");
		operationMenu.addElement("Add Dimension");
		operationMenu.addElement("Remove Dimension");
		operationMenu.addElement("Slice");
		operationMenu.addElement("Dice");
		sliceMenu.addElement("product.brand");
		sliceMenu.addElement("product.category");
		sliceMenu.addElement("product.department");
		sliceMenu.addElement("destination.city");
		sliceMenu.addElement("destination.state");
		sliceMenu.addElement("destination.country");
		sliceMenu.addElement("date.day");
		sliceMenu.addElement("date.month");
		sliceMenu.addElement("date.year");
		sliceCmpMenu.addElement("<");
		sliceCmpMenu.addElement(">");
		sliceCmpMenu.addElement("<=");
		sliceCmpMenu.addElement(">=");
		sliceCmpMenu.addElement("=");
		diceMenu.addElement("product.brand");
		diceMenu.addElement("product.category");
		diceMenu.addElement("product.department");
		diceMenu.addElement("destination.city");
		diceMenu.addElement("destination.state");
		diceMenu.addElement("destination.country");
		diceMenu.addElement("date.day");
		diceMenu.addElement("date.month");
		diceMenu.addElement("date.year");
		diceCmpMenu.addElement("<");
		diceCmpMenu.addElement(">");
		diceCmpMenu.addElement("<=");
		diceCmpMenu.addElement(">=");
		diceCmpMenu.addElement("=");
		
		
		slice = new JTextField();
		dice = new JTextField();
		
		JLabel dimensionLabel = new JLabel("Dimension: ");
		
		final JCheckBox location = new JCheckBox("Location");
		JCheckBox item = new JCheckBox("Item");
		JCheckBox time = new JCheckBox("Time");
		
		dimensions = new ArrayList<String>();

		setTitle("Business Intelligence Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		status = new JLabel("");
		status2 = new JLabel("");

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);

		JLabel operationLabel = new JLabel("Operation: ");
		panel.add(operationLabel);
		operationBox = new JComboBox<String>(operationMenu);
		operationPane = new JScrollPane(operationBox);
		panel.add(operationPane);
		
		operationBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submit.setText(operationBox.getItemAt(operationBox.getSelectedIndex()));
		
				diceCmpPane.setVisible(false);
				sliceCmpPane.setVisible(false);
				dicePane.setVisible(false);
				slicePane.setVisible(false);
				slice.setVisible(false);
				dice.setVisible(false);
				dimensionLabel.setVisible(true);
				location.setVisible(true);
				item.setVisible(true);
				time.setVisible(true);
				
				if (operationBox.getSelectedIndex() == 5) {
					sliceCmpPane.setVisible(true);
					slicePane.setVisible(true);
					slice.setVisible(true);
					dimensionLabel.setVisible(false);
					location.setVisible(false);
					item.setVisible(false);
					time.setVisible(false);
					panel.repaint();
				}
				if (operationBox.getSelectedIndex() == 6) {
					diceCmpPane.setVisible(true);
					sliceCmpPane.setVisible(true);
					dicePane.setVisible(true);
					slicePane.setVisible(true);
					slice.setVisible(true);
					dice.setVisible(true);
					dimensionLabel.setVisible(false);
					location.setVisible(false);
					item.setVisible(false);
					time.setVisible(false);
					panel.repaint();
				}

			}
		});

		panel.add(dimensionLabel);


		location.setMnemonic(KeyEvent.VK_C);
		item.setMnemonic(KeyEvent.VK_M);
		time.setMnemonic(KeyEvent.VK_P);
		
		location.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	        	 if(e.getStateChange() == 1){
	        		 dimensions.add("location");
	        		 System.out.println("added");
	        	 }else{
	        		 dimensions.remove("location");
	        		 System.out.println("removed");
	        	 }
	         }           
	      });
		
		item.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	        	 if(e.getStateChange() == 1){
	        		 dimensions.add("item");
	        		 System.out.println("added");
	        	 } else{
	        		 dimensions.remove("item");
	        		 System.out.println("removed");
	        	 }
	         }           
	      });
		
		time.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	        	 if(e.getStateChange() == 1){
	        		 dimensions.add("time");
	        		 System.out.println("added");
	        	 }else{
	        		 dimensions.remove("time");
	        		 System.out.println("removed");
	        	 }
	         }           
	      });
		
		panel.add(location);
		panel.add(item);
		panel.add(time);
		
		sliceBox = new JComboBox<String>(sliceMenu);
		slicePane = new JScrollPane(sliceBox);
		sliceCmpBox = new JComboBox<String>(sliceCmpMenu);
		sliceCmpPane = new JScrollPane(sliceCmpBox);
		panel.add(slicePane);
		panel.add(sliceCmpPane);
		panel.add(slice);
		slice.setColumns(10);
		

		diceBox = new JComboBox<String>(diceMenu);
		dicePane = new JScrollPane(diceBox);
		diceCmpBox = new JComboBox<String>(diceCmpMenu);
		diceCmpPane = new JScrollPane(diceCmpBox);
		panel.add(dicePane);
		panel.add(diceCmpPane);
		panel.add(dice);
		dice.setColumns(10);
		
		map = new HashMap<String,String>();
		
		submit = new JButton("Analyze");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					status.setText("");
					int opcode = operationBox.getSelectedIndex(); //get the opcode
					if(opcode == 0){ //if opcode is none
						status.setText("Operation cannot be: None");
					}else{
						String sliceText = slice.getText();//get text at "value 1"
						String diceText = dice.getText();//get text at "value 2"
						String sliceColumn = sliceBox.getItemAt(sliceBox.getSelectedIndex());
						String diceColumn = diceBox.getItemAt(diceBox.getSelectedIndex());
						String sliceCmp = sliceCmpBox.getItemAt(sliceCmpBox.getSelectedIndex());
						String diceCmp = diceCmpBox.getItemAt(diceCmpBox.getSelectedIndex());
						if (sliceColumn.matches(diceColumn)){
							status.setText("You cannot dice on the same column");
						}
						if(sliceText != null && sliceText.trim().length() > 0){ //if not empty
							if(opcode == 5){
								dao.slice(sliceColumn, sliceCmp, sliceText);
							}
							if(diceText!= null && diceText.trim().length() > 0){//if not empty
								dao.dice(sliceColumn,sliceCmp,sliceText,diceColumn,diceCmp,diceText);
							}
						}else{
							if(opcode == 5 || opcode == 6){
								status.setText("You must enter values to slice or dice");
							}else{
								dao.operations(opcode, dimensions);
							}
					
						}
						
					}
					dimensions.clear();
					map.clear();
					status.setText("");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(GUI.this, "Error: " + exc, "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		panel.add(submit);

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		centralCube = new JButton("Reset Central Cube");
		centralCube.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					ArrayList<ArrayList<String>> results = null;
					results = dao.centralCube();

					// create the model and update the "table"
					ModelTable model = new ModelTable(results);

					table.setModel(model);

				} catch (Exception a) {
					a.printStackTrace();
				}
			}
		});
		panel_1.add(centralCube);
		panel_1.add(status);
		panel_1.add(status2);
		
		diceCmpPane.setVisible(false);
		sliceCmpPane.setVisible(false);
		dicePane.setVisible(false);
		slicePane.setVisible(false);
		slice.setVisible(false);
		dice.setVisible(false);
		dimensionLabel.setVisible(true);
		location.setVisible(true);
		item.setVisible(true);
		time.setVisible(true);
		try {

			ArrayList<ArrayList<String>> results = null;
			results = dao.centralCube();

			// create the model and update the "table"
			ModelTable model = new ModelTable(results);

			table.setModel(model);

		} catch (Exception a) {
			a.printStackTrace();
		}
	}

}
