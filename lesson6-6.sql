DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE IF NOT EXISTS products
	(id IDENTITY UNIQUE NOT NULL, PRIMARY KEY (id),
	 title VARCHAR(255) NOT NULL,
	 price DECIMAL(12,2));

CREATE TABLE IF NOT EXISTS customers
	(id IDENTITY UNIQUE NOT NULL, PRIMARY KEY (id),
	 customername VARCHAR(255) NOT NULL);

--	Тип IDENTITY можно использовать только один раз, иначе хибер-т скажет, что у вас несколько
--	первичных ключей, … даже если вы ему явно укажите, что именно нужно считать первичным ключом.

CREATE TABLE IF NOT EXISTS orders_data
	(id IDENTITY UNIQUE NOT NULL, PRIMARY KEY (id),
	 customer_id BIGINT NOT NULL,
	 prouct_id BIGINT NOT NULL,
	 FOREIGN KEY (customer_id) REFERENCES customers(id),
	 FOREIGN KEY (prouct_id) REFERENCES products(id),
	 buy_price DECIMAL(12,2));

--	Таблицы заполнять нужно в том порядке, в каком в них появляются данные. Например, если
--	ссылочную таблицу заполнить раньше, чем таблицы, на которые она ссылается, то будет выдано
--	сообщение от ошибке, …смысл которого вам никогда не расшифровать.

INSERT INTO customers (customername) VALUES
	('CustomerA'),
	('CustomerO'),
	('CustomerW'),
	('CustomerT');

INSERT INTO products (title, price) VALUES
	('Product01',  11.0),
	('Product02',  21.0),
	('Product03',  31.0),
	('Product04',  41.0),
	('Product05',  51.0),
	('Product06',  61.0),
	('Product07',  71.0),
	('Product08',  81.0),
	('Product09',  91.0),
	('Product10', 101.0);

INSERT INTO orders_data (customer_id, prouct_id, buy_price) VALUES
	(1,  1,  10.0),
	(1,  2,  20.0),
	(1,  3,  30.0),
	(2,  4,  40.0),
	(2,  5,  50.0),
	(2,  6,  60.0),
	(3,  7,  70.0),
	(3,  8,  80.0),
	(3,  9,  90.0),
	(4, 10, 100.0),
	(4,  5,  10.0);
