-- The central cube 
-- We are a supplier that ships products to destinations. 
-- We keep track of what we sold, when we sold it, and where we shipped it.
-- The fact we care about is how much we have sold.  

-- This is the central cube. 
USE ass2;

SELECT 
	product.category, 
	destination.state, 
	date.month,
	sum(sale_facts.qty_sold) AS 'qty_sold' 
FROM
	date, destination, product, sale_facts
WHERE
	date.year = 2016 AND 
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id AND
	date.date_key = sale_facts.date_id
GROUP BY 
	destination.state, 
	product.category, 
	date.month
	
-- Roll up the central cube by climbing up a concept hierarchy
-- We will need two different years in the table for this. 
Use ass2; 

SELECT 
	product.category, 
	destination.state, 
	date.year,
	sum(sale_facts.qty_sold) AS 'qty_sold' 
FROM
	date, destination, product, sale_facts
WHERE
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id AND
	date.date_key = sale_facts.date_id
GROUP BY 
	destination.state, 
	product.category, 
	date.year

	
-- Roll up the central cube by removing a dimension
Use ass2; 

SELECT 
	product.category, 
	destination.state,
	sum(sale_facts.qty_sold) AS 'qty_sold' 
FROM
	destination, product, sale_facts
WHERE
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id
GROUP BY 
	destination.state,
	product.category
	

-- Drill down the central cube by climbing down a concept hierarchy by one level.
Use ass2; 

SELECT
	product.brand, 
	destination.state, 
	sum(sale_facts.qty_sold) AS 'qty_sold' 
FROM
	destination, product, sale_facts
WHERE
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id
GROUP BY 
	destination.state, 
	product.brand	

-- Drill down the central cube by adding a dimension
Use ass2; 

SELECT
	product.brand, 
	destination.state,
	date.year,
	sum(sale_facts.qty_sold) AS 'qty_sold'
FROM
	destination, product, date, sale_facts
WHERE
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id AND
	date.date_key = sale_facts.date_id
GROUP BY
	destination.state,
	product.brand, 
	date.year 

-- Slice
Use ass2; 

SELECT
	product.brand, 
	destination.state, 
	date.year, 
	sum(sale_facts.qty_sold) AS 'qty_sold' 
FROM
	destination, product, date, sale_facts
WHERE
	date.year = 2016 AND
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id AND
	date.date_key = sale_facts.date_id
GROUP BY 
	destination.state, 
	product.brand,
	date.year

-- Dice
-- 2016 candy 
Use ass2; 

SELECT
	product.brand, 
	destination.state, 
	date.year, 
	sum(sale_facts.qty_sold) AS 'qty_sold'
FROM
	destination, product, date, sale_facts
WHERE
	product.brand = "Candy" AND
	date.year = 2016 AND
	destination.destination_key = sale_facts.destination_id AND
	product.product_key = sale_facts.product_id AND
	date.date_key = sale_facts.date_id
GROUP BY 
	destination.state, 
	product.brand,
	date.year