package Olap;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

class ModelTable extends AbstractTableModel {

	private static final int CATEGORY = 0;
	private static final int STATE = 1;
	private static final int YEAR = 2;
	private static final int QUANTITY = 3;

	private String[] columnNames = { "Item", "Location", "Time", "Quantity" }; //names of column headers
	private ArrayList<ArrayList<String>> table;

	public ModelTable(ArrayList<ArrayList<String>> table) {
		this.table = table;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return table.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {

		ArrayList<String> temp= table.get(row);

		switch (col) {
		case CATEGORY:
			return temp.get(0);
		case STATE:
			return temp.get(1);
		case YEAR:
			return temp.get(2);
		case QUANTITY:
			return temp.get(3);
		default:
			return temp.get(0);
		}
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
