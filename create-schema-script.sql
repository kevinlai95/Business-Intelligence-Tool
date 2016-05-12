-- SQL script 
CREATE DATABASE ass2;

-- Create destination dimension 
CREATE TABLE ass2.destination (
	destination_key INT AUTO_INCREMENT,
	country VARCHAR(45),
	state VARCHAR(45),
	city VARCHAR(45),
	PRIMARY KEY (destination_key)
);
  
-- Create product dimension 
CREATE TABLE ass2.product (
	product_key INT AUTO_INCREMENT,
	department VARCHAR(45), 
	category VARCHAR(45), 
	brand VARCHAR(45),
	PRIMARY KEY (product_key)
);

-- Create date dimension 
CREATE TABLE ass2.date (
	date_key INT AUTO_INCREMENT,
	year INT, 
	month VARCHAR(45), 
	day INT, 
	PRIMARY KEY (date_key)
); 

-- Create Fact Table 
CREATE TABLE ass2.sale_facts (
	sale_key INT AUTO_INCREMENT,
	destination_id INT,
	product_id INT,
	date_id INT, 
	qty_sold INT,
	PRIMARY KEY (sale_key),
	FOREIGN KEY (destination_id) REFERENCES ass2.destination(destination_key),
	FOREIGN KEY (product_id) REFERENCES ass2.product(product_key), 
	FOREIGN KEY (date_id) REFERENCES ass2.date(date_key)
); 

-- Populate Destination Table 
INSERT INTO ass2.destination (country, state, city)
VALUES 
	("USA", "CA", "San Francisco"), 
	("USA", "FL", "Orlando"), 
	("USA", "OR", "Portland"),
	("USA", "CA", "San Jose"),
	("USA", "FL", "Miami"),
	("USA", "OR", "Salen"),
	("USA", "CA", "Sunnyvale"); -- 7
	
-- Populate Product Table 
INSERT INTO ass2.product (department, category, brand) 
VALUES
	("Grocery", "Food", "Frozen Foods"), 
	("Grocery", "Food", "Candy"), 
	("Grocery", "Food", "Candy"),
	("Grocery", "Drink", "Soft Drinks"),
	("Grocery", "Drink", "Soft Drinks"),
	("Grocery", "Drink", "Juice"), 
	("Grocery", "Food", "Salty Snacks");
	
-- Populate Date Table 
INSERT INTO ass2.date (year, month, day) 
VALUES
	(2016, "April", 6),
	(2016, "April", 6),
	(2016, "April", 1),
	(2016, "May", 2),
	(2015, "May", 4),
	(2016, "May", 3),
	(2016, "May", 2),  -- 7
	(2015, "April", 9),
	(2015, "April", 3),
	(2015, "April", 17),
	(2015, "May", 22),
	(2016, "May", 7),
	(2015, "May", 8),
	(2015, "May", 6);  -- 7

  
-- Populate Fact Table 
INSERT INTO ass2.sale_facts (destination_id, product_id, date_id, qty_sold)
VALUES 
	(5, 3, 2, 1),
	(3, 1, 7, 3),
	(3, 3, 3, 4),
	(2, 4, 2, 3), 
	(6, 4, 3, 5), 
	(3, 5, 6, 4), 
	(4, 3, 7, 5), -- 7 
	(5, 3, 8, 1),
	(3, 1, 9, 3),
	(3, 3, 10, 7),
	(2, 4, 11, 6), 
	(6, 4, 12, 2), 
	(3, 5, 13, 7), 
	(4, 3, 14, 8); -- 7 