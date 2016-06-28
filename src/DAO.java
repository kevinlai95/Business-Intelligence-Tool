package Olap;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.math.BigDecimal;

public class DAO {

	private Connection connection;
	
	private String[] productDimensions = { "brand", "category", "department" };
	private String[] dateDimensions = { "day", "month", "year" };
	private String[] destinationDimensions = { "city", "state", "country" };
	
	private int productDimensionCounter;
	private int dateDimensionCounter;
	private int destinationDimensionCounter;
	
	private static final int productDimensionCentralIndex = 1;
	private static final int dateDimensionCentralIndex = 1;
	private static final int destinationDimensionCentralIndex = 1;

	public String productDimension;
	public String destinationDimension;
	public String dateDimension;
	public String quantityDimension;

	public DAO() throws Exception {

		productDimension = "";
		destinationDimension = "";
		dateDimension = "";
		quantityDimension = "";
		
		productDimensionCounter =  1;
		dateDimensionCounter =  1;
		destinationDimensionCounter = 1;
		

		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ass2", "root", "zeratal1");
	}

	// parses user information to rollup, drilldown, add dimension, remove
	// dimensino
	public ArrayList<ArrayList<String>> operations(int opcode, ArrayList<String> dimensions) throws Exception {

		if (opcode == 1) {// rollup
			System.out.println("Rollup");
			if (dimensions.contains("location")&& !destinationDimension.equals(""))
				rollUpDestination();
			if (dimensions.contains("item")&& !productDimension.equals(""))
				rollUpProduct();
			if (dimensions.contains("time")&& !dateDimension.equals(""))
				rollUpDate();
		} else if (opcode == 2) {// drill down
			System.out.println("Drill Down");
			if (dimensions.contains("location") && !destinationDimension.equals(""))
				drillDownDestination();
			if (dimensions.contains("item")&& !productDimension.equals(""))
				drillDownProduct();
			if (dimensions.contains("time")&& !dateDimension.equals(""))
				drillDownDate();
		} else if (opcode == 3) {// add dimension
			System.out.println("Add Dimension");
			return addDimension(dimensions);
		} else if (opcode == 4) {// remove dimension
			System.out.println("Remove Dimension");
			return removeDimension(dimensions);
		}

		return queryDatabase();
	}

	// parses user infromation to slice and dice.
	public ArrayList<ArrayList<String>> slice(String column, String operation, String value) throws Exception {
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		ArrayList<String> row;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.createStatement();
			StringBuilder query = new StringBuilder();
			query.append("SELECT ");
			query.append(productDimensions[productDimensionCounter]);
			query.append(", " + destinationDimensions[destinationDimensionCounter]);
			query.append(", " + dateDimensions[dateDimensionCounter]);
			query.append(", " + quantityDimension);
			query.append(" FROM " + parseFrom(productDimension, destinationDimension, dateDimension));
			query.append(" WHERE " + parseWhere(productDimension, destinationDimension, dateDimension));
			query.append(" AND " + column + " " + operation + " " + "'" + value + "'");
			query.append(" GROUP BY ");
			query.append(productDimensions[productDimensionCounter]);
			query.append(", " + destinationDimensions[destinationDimensionCounter]);
			query.append(", " + dateDimensions[dateDimensionCounter]);
			query.append(";");
			System.out.println(query.toString());
			resultSet = statement.executeQuery(query.toString());

			while (resultSet.next()) {
				row = new ArrayList<String>();
				row.add(resultSet.getString(productDimensions[productDimensionCounter]));
				row.add(resultSet.getString(destinationDimensions[destinationDimensionCounter]));
				row.add(resultSet.getString(dateDimensions[dateDimensionCounter]));
				row.add("" + resultSet.getInt("qty_sold"));
				results.add(row);
			}

			return results;
		} finally {
			statement.close();
		}
	}

	public ArrayList<ArrayList<String>> dice(String column1, String operation1, String value1, String column2,
			String operation2, String value2) throws Exception {
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		ArrayList<String> row;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.createStatement();
			StringBuilder query = new StringBuilder();
			query.append("SELECT ");
			query.append(productDimensions[productDimensionCounter]);
			query.append(", " + destinationDimensions[destinationDimensionCounter]);
			query.append(", " + dateDimensions[dateDimensionCounter]);
			query.append(", " + quantityDimension);
			query.append(" FROM " + parseFrom(productDimension, destinationDimension, dateDimension));
			query.append(" WHERE " + parseWhere(productDimension, destinationDimension, dateDimension));
			query.append(" AND " + column1 + " " + operation1 + " " + "'" + value1 + "'");
			query.append(" AND " + column2 + " " + operation2 + " " + "'" + value2 + "'");
			query.append(" GROUP BY ");
			query.append(productDimensions[productDimensionCounter]);
			query.append(", " + destinationDimensions[destinationDimensionCounter]);
			query.append(", " + dateDimensions[dateDimensionCounter]);
			query.append(";");
			System.out.println(query.toString());
			resultSet = statement.executeQuery(query.toString());

			while (resultSet.next()) {
				row = new ArrayList<String>();
				row.add(resultSet.getString(productDimensions[productDimensionCounter]));
				row.add(resultSet.getString(destinationDimensions[destinationDimensionCounter]));
				row.add(resultSet.getString(dateDimensions[dateDimensionCounter]));
				row.add("" + resultSet.getInt("qty_sold"));
				results.add(row);
			}

			return results;
		} finally {
			statement.close();
		}
	}

	// default central cube
	public ArrayList<ArrayList<String>> centralCube() throws Exception {

		productDimensionCounter = productDimensionCentralIndex;
		dateDimensionCounter = dateDimensionCentralIndex;
		destinationDimensionCounter = destinationDimensionCentralIndex;
		
		productDimension = productDimensions[productDimensionCounter];
		destinationDimension = destinationDimensions[destinationDimensionCounter];
		dateDimension = dateDimensions[dateDimensionCounter];
		quantityDimension = "sum(sale_facts.qty_sold) AS 'qty_sold'";
		return queryDatabase();

	}
	
	public ArrayList<ArrayList<String>> addDimension(ArrayList<String> dimensions) throws Exception{
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		ArrayList<String> row;
		for (String dimension : dimensions){
			if (dimension.equals("location") && destinationDimension.equals("")){
				destinationDimension = destinationDimensions[destinationDimensionCounter];
			}else if (dimension.equals("item") && productDimension.equals("")){
				productDimension = productDimensions[productDimensionCounter];
			}
			else if (dimension.equals("time") && dateDimension.equals("")){
				dateDimension = dateDimensions[dateDimensionCounter];
			}
		}
		
		Statement statement = null;
		ResultSet resultSet = null;

		String query =  "SELECT "
				+ parseDimensions(productDimension, destinationDimension, dateDimension, quantityDimension)
				+ " FROM " + parseFrom(productDimension, destinationDimension, dateDimension) + " WHERE "
				+ parseWhere(productDimension, destinationDimension, dateDimension) + " GROUP BY "
				+ parseDimensions(productDimension, destinationDimension, dateDimension);
		System.out.println(query);
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				row = new ArrayList<String>();
				if (!productDimension.equals(""))
					row.add(resultSet.getString(productDimensions[productDimensionCounter]));
				else
					row.add("");
				if (!destinationDimension.equals(""))
					row.add(resultSet.getString(destinationDimensions[destinationDimensionCounter]));
				else
					row.add("");
				if (!dateDimension.equals(""))
					row.add(resultSet.getString(dateDimensions[dateDimensionCounter]));
				else 
					row.add("");
				row.add("" + resultSet.getInt("qty_sold"));
				results.add(row);
			}

			return results;
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			statement.close();
		}
		return results;
		
	}
	
	
	public ArrayList<ArrayList<String>> removeDimension(ArrayList<String> dimensions) throws Exception{
		productDimension = productDimensions[productDimensionCounter];
		destinationDimension = destinationDimensions[destinationDimensionCounter];
		dateDimension = dateDimensions[dateDimensionCounter];
		for (String dimension : dimensions){
			if (dimension.equals("location"))
				destinationDimension = "";
			else if (dimension.equals("item"))
				productDimension = "";
			else if (dimension.equals("time"))
				dateDimension = "";
		}
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		ArrayList<String> row;

		Statement statement = null;
		ResultSet resultSet = null;

		String query =  "SELECT "
				+ parseDimensions(productDimension, destinationDimension, dateDimension, quantityDimension)
				+ " FROM " + parseFrom(productDimension, destinationDimension, dateDimension) + " WHERE "
				+ parseWhere(productDimension, destinationDimension, dateDimension) + " GROUP BY "
				+ parseDimensions(productDimension, destinationDimension, dateDimension);
		System.out.println(query);
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				row = new ArrayList<String>();
				if (!productDimension.equals(""))
					row.add(resultSet.getString(productDimensions[productDimensionCounter]));
				else
					row.add("");
				if (!destinationDimension.equals(""))
					row.add(resultSet.getString(destinationDimensions[destinationDimensionCounter]));
				else
					row.add("");
				if (!dateDimension.equals(""))
					row.add(resultSet.getString(dateDimensions[dateDimensionCounter]));
				else 
					row.add("");
				row.add("" + resultSet.getInt("qty_sold"));
				results.add(row);
			}

			return results;
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			statement.close();
		}
		
		return queryDatabase();
	}
	
	public void rollUpDate(){
		if(dateDimensionCounter < dateDimensions.length - 1){
			dateDimensionCounter++;
		}
		dateDimension = dateDimensions[dateDimensionCounter];
	}

	public void drillDownDate(){
		if (dateDimensionCounter > 0){
			dateDimensionCounter--;
		}
		dateDimension = dateDimensions[dateDimensionCounter];
	}
	
	public void rollUpProduct(){
		if (productDimensionCounter < productDimensions.length - 1){
			productDimensionCounter++;
		}
		productDimension = productDimensions[productDimensionCounter];
	}
	
	public void drillDownProduct(){
		if (productDimensionCounter > 0){
			productDimensionCounter--;
		}
		productDimension = productDimensions[productDimensionCounter];
	}
	
	public void rollUpDestination(){
		if (destinationDimensionCounter < destinationDimensions.length - 1){
			destinationDimensionCounter++;
		}
		destinationDimension = destinationDimensions[destinationDimensionCounter];
	}
	
	public void drillDownDestination(){
		if (destinationDimensionCounter > 0){
			destinationDimensionCounter--;
		}
		destinationDimension = destinationDimensions[destinationDimensionCounter];
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

			String query =  "SELECT "
					+ parseDimensions(productDimension, destinationDimension, dateDimension, quantityDimension)
					+ " FROM " + parseFrom(productDimension, destinationDimension, dateDimension) + " WHERE "
					+ parseWhere(productDimension, destinationDimension, dateDimension) + " GROUP BY "
					+ parseDimensions(productDimension, destinationDimension, dateDimension);
			System.out.println(query.toString());
			resultSet = statement.executeQuery(query.toString());

			while (resultSet.next()) {
				row = new ArrayList<String>();
				if (!productDimension.equals(""))
					row.add(resultSet.getString(productDimensions[productDimensionCounter]));
				else
					row.add("");
				if (!destinationDimension.equals(""))
					row.add(resultSet.getString(destinationDimensions[destinationDimensionCounter]));
				else
					row.add("");
				if (!dateDimension.equals(""))
					row.add(resultSet.getString(dateDimensions[dateDimensionCounter]));
				else 
					row.add("");
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
		if (!destinationDimension.equals("")) {
			retString = "destination.destination_key = sale_facts.destination_id";
		}
		if (!productDimension.equals("")) {
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
		System.out.println(retString);
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

	public String getProductDimension() {
		return productDimensions[productDimensionCounter];
	}

	public String getDestinationDimension() {
		return destinationDimensions[destinationDimensionCounter];
	}

	public String getDateDimension() {
		return dateDimensions[dateDimensionCounter];
	}

	

}
