package Olap;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.math.BigDecimal;

public class DAO {

	private Connection connection;

	String productDimension;
	String destinationDimension;
	String dateDimension;
	String quantityDimension;

	public DAO() throws Exception {

		productDimension = "";
		destinationDimension = "";
		dateDimension = "";
		quantityDimension = "";

		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ass2", "root", "");
	}

	// parses user information to rollup, drilldown, add dimension, remove
	// dimensino
	public ArrayList<ArrayList<String>> operations(int opcode, ArrayList<String> dimensions) throws Exception {

		if (opcode == 1) {// rollup
			System.out.println("Rollup");
		} else if (opcode == 2) {// drill down
			System.out.println("Drill Down");
		} else if (opcode == 3) {// add dimension
			System.out.println("Add Dimension");
		} else if (opcode == 4) {// remove dimension
			System.out.println("Remove Dimension");

		}

		return queryDatabase();
	}

	// parses user infromation to slice and dice.
	public ArrayList<ArrayList<String>> slice(String column, String operation, String value) throws Exception {
		System.out.println(column);
		System.out.println(operation);
		System.out.println(value);
		return queryDatabase();
	}

	public ArrayList<ArrayList<String>> dice(String column1, String operation1, String value1, String column2,
			String operation2, String value2) throws Exception {
		System.out.println(column1);
		System.out.println(operation1);
		System.out.println(value1);
		System.out.println(column2);
		System.out.println(operation2);
		System.out.println(value2);
		return queryDatabase();
	}

	// default central cube
	public ArrayList<ArrayList<String>> centralCube() throws Exception {

		productDimension = "product.category";
		destinationDimension = "destination.state";
		dateDimension = "date.month";
		quantityDimension = "sum(sale_facts.qty_sold) AS 'qty_sold'";
		return queryDatabase();

	}

	// runs the query based on variables: productDimension,
	// destinationDimension, dateDimension, quantityDimension
	// each variable will store the granularity of the corresponding dimension
	public ArrayList<ArrayList<String>> queryDatabase() throws Exception {
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		ArrayList<String> row;

		Statement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.createStatement();

			/*
			 * "SELECT product.category,destination.state, date.year, sum(sale_facts.qty_sold) AS 'qty_sold' "
			 * + "FROM date, destination, product, sale_facts " +
			 * "WHERE destination.destination_key = sale_facts.destination_id AND product.product_key = sale_facts.product_id AND date.date_key = sale_facts.date_id "
			 * + "GROUP BY destination.state, product.category, date.year"
			 */

			resultSet = statement.executeQuery("SELECT "
					+ parseDimensions(productDimension, destinationDimension, dateDimension, quantityDimension)
					+ " FROM " + parseFrom(productDimension, destinationDimension, dateDimension) + " WHERE "
					+ parseWhere(productDimension, destinationDimension, dateDimension) + " GROUP BY "
					+ parseDimensions(productDimension, destinationDimension, dateDimension));

			while (resultSet.next()) {
				row = new ArrayList<String>();
				row.add(resultSet.getString("category"));
				row.add(resultSet.getString("state"));
				row.add(resultSet.getString("month"));
				row.add("" + resultSet.getInt("qty_sold"));
				results.add(row);
			}

			return results;
		} finally {
			statement.close();
		}
	}

	public String parseFrom(String productDimension, String destinationDimension, String dateDimension) {
		String retString = "";
		if (!productDimension.equals("")) {
			retString = "product";
		}
		if (!destinationDimension.equals("")) {
			if (retString.equals("")) {
				retString = "destination";
			} else {
				retString = retString.concat(", ".concat("destination"));
			}
		}
		if (!dateDimension.equals("")) {
			if (retString.equals("")) {
				retString = "date";
			} else {
				retString = retString.concat(", ".concat("date"));
			}
		}
		retString = retString.concat(", ".concat("sale_facts"));
		return retString;
	}

	public String parseWhere(String productDimension, String destinationDimension, String dateDimension) {
		String retString = "";
		if (!productDimension.equals("")) {
			retString = "destination.destination_key = sale_facts.destination_id";
		}
		if (!destinationDimension.equals("")) {
			if (retString.equals("")) {
				retString = "product.product_key = sale_facts.product_id";
			} else {
				retString = retString.concat(" AND product.product_key = sale_facts.product_id");
			}
		}
		if (!dateDimension.equals("")) {
			if (retString.equals("")) {
				retString = "date.date_key = sale_facts.date_id";
			} else {
				retString = retString.concat(" AND date.date_key = sale_facts.date_id");
			}
		}
		return retString;
	}

	public String parseDimensions(String productDimension, String destinationDimension, String dateDimension,
			String quantityDimension) {
		String retString = "";
		if (!productDimension.equals("")) {
			retString = productDimension;
		}
		if (!destinationDimension.equals("")) {
			if (retString.equals("")) {
				retString = destinationDimension;
			} else {
				retString = retString.concat(", ".concat(destinationDimension));
			}
		}
		if (!dateDimension.equals("")) {
			if (retString.equals("")) {
				retString = dateDimension;
			} else {
				retString = retString.concat(", ".concat(dateDimension));
			}
		}
		retString = retString.concat(",".concat(quantityDimension));
		return retString;
	}

	public String parseDimensions(String productDimension, String destinationDimension, String dateDimension) {
		String retString = "";
		if (!productDimension.equals("")) {
			retString = productDimension;
		}
		if (!destinationDimension.equals("")) {
			if (retString.equals("")) {
				retString = destinationDimension;
			} else {
				retString = retString.concat(", ".concat(destinationDimension));
			}
		}
		if (!dateDimension.equals("")) {
			if (retString.equals("")) {
				retString = dateDimension;
			} else {
				retString = retString.concat(", ".concat(dateDimension));
			}
		}
		return retString;
	}

}
