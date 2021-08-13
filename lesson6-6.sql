-- http://www.h2database.com/html/datatypes.html

--	Может показаться странным, что мы поручаем назначать идентификаторы какому-то механизму, а потом доверчиво пользуемся сгенерированными им значенями. Дело здесь в том, что мы «играем» в то, что все эти значения были помещены в БД не нами, а приложением (как бы в прошлом), и каждой строке в orders_data были присвоены товар или покупатель в строгом соответствии с логикой «настоящего» приложения. Мы просто аккуратно сымитировали результат такой работы — «настоящую» базу данных.
--	В реальных условиях наша БД не будет храниться в памяти ПК и не будет считываться из файла на стадии инициализации приложения; все строки во всех её таблицах будут созданы нашим пиложением.

DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS customer_products CASCADE;

CREATE TABLE IF NOT EXISTS products
	(id IDENTITY UNIQUE NOT NULL, PRIMARY KEY (id),
	 title VARCHAR(255) NOT NULL,
	 price DECIMAL(12,2));

CREATE TABLE IF NOT EXISTS customers
	(id IDENTITY UNIQUE NOT NULL, PRIMARY KEY (id),
	 customername VARCHAR(255) NOT NULL);

--	Тип IDENTITY можно использовать только один раз, иначе хибер-т скажет, что у вас несколько первичных ключей, … даже если вы ему явно укажите, что именно нужно считать первичным ключом.

CREATE TABLE IF NOT EXISTS orders_data
	(id IDENTITY UNIQUE NOT NULL, PRIMARY KEY (id),
	 customer_id BIGINT NOT NULL REFERENCES customers(id),	-- в H2 диапазоны BIGINT и IDENTITY равны
	 product_id BIGINT NOT NULL REFERENCES products(id),
	 buy_price DECIMAL(12,2),
	 time_stamp TIMESTAMP (0) WITH TIME ZONE	-- здесь аналогично TIMESTAMP (в H2 нет TIMESTAMP WITH LOCAL TIME ZONE; настройка количества символов после запятой не работает)
	 );

CREATE TABLE IF NOT EXISTS customer_products
	(customer_id BIGINT NOT NULL REFERENCES customers(id),
	 product_id BIGINT NOT NULL REFERENCES products(id));

--	Таблицы заполнять нужно в том порядке, в каком в них появляются данные. Например, если ссылочную таблицу заполнить раньше, чем таблицы, на которые она ссылается, то будет выдано сообщение об ошибке, …смысл которого вам никогда не расшифровать.

INSERT INTO customers (customername) VALUES
	('CustomerA'),	-- id = 1
	('CustomerO'),	-- id = 2
	('CustomerW'),	-- id = 3
	('CustomerT');	-- id = 4

INSERT INTO products (title, price) VALUES
	('Product01',  11.0),	-- id = 1
	('Product02',  21.0),	-- id = 2
	('Product03',  31.0),	-- id = 3
	('Product04',  41.0),	-- id = 4
	('Product05',  51.0),	-- id = 5
	('Product06',  61.0),	-- id = 6
	('Product07',  71.0),	-- id = 7
	('Product08',  81.0),	-- id = 8
	('Product09',  91.0),	-- id = 9
	('Product10', 101.0);	-- id = 10

INSERT INTO orders_data (customer_id, product_id, buy_price) VALUES
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

INSERT INTO customer_products (customer_id, product_id) VALUES
	(1,  1),
	(1,  2),
	(1,  3),
	(2,  4),
	(2,  5),
	(2,  6),
	(3,  7),
	(3,  8),
	(3,  9),
	(4, 10),
	(4,  5);
