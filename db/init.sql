/**
 * init.sql
 * Script de inicialización de la base de datos project3 para el contenedor Docker MySQL.
 */

-- Desactivar restricciones de claves foráneas.
SET FOREIGN_KEY_CHECKS = 0;

-- Crear BD si no existe.
CREATE DATABASE IF NOT EXISTS project3;
USE project3;

----- CREACIÓN DE TABLAS -----
-- Tabla 1. RestaurantManagement.
CREATE TABLE IF NOT EXISTS RestaurantManagement (
    Restaurant_id      NUMERIC(10) PRIMARY KEY,
    Restaurant_name    VARCHAR(30),
    Restaurant_address VARCHAR(30),
    Manager_id         NUMERIC(10)
        REFERENCES Manager (Manager_id)
);

-- Tabla 2. Manager.
CREATE TABLE IF NOT EXISTS Manager (
    Manager_id     NUMERIC(10) PRIMARY KEY,
    Manager_Fname  VARCHAR(30),
    Manager_Mname  VARCHAR(30),
    Manager_Lname  VARCHAR(30),
    Manager_number NUMERIC(10),
    Manager_salary NUMERIC(5)
);

-- Tabla 3. Item.
CREATE TABLE IF NOT EXISTS Item (
    Item_food         VARCHAR(30),
    food_price        NUMERIC(4),
    food_id           NUMERIC(5),
    
    Item_appetizers   VARCHAR(30),
    appetizers_price  NUMERIC(4),
    appetizers_id     NUMERIC(5),
    
    Item_drinks       VARCHAR(30),
    drinks_price      NUMERIC(4),
    drinks_id         NUMERIC(5),

    Receipt_id NUMERIC(10) REFERENCES Receipt (Receipt_id),

    PRIMARY KEY (food_id, appetizers_id, drinks_id)
);

-- Tabla 4. Chef.
CREATE TABLE IF NOT EXISTS Chef (
    Chef_id      NUMERIC(10) PRIMARY KEY,
    Chef_name    VARCHAR(30),
    Chef_manager VARCHAR(30),
    Chef_salary  NUMERIC(5),
    Manager_id   NUMERIC(10) REFERENCES Manager (Manager_id)
);

-- Tabla 5. Cashier.  
CREATE TABLE IF NOT EXISTS Cashier (
    Cashier_id     NUMERIC(10) PRIMARY KEY,
    Cashier_name   VARCHAR(30),
    Cashier_salary NUMERIC(5)
);

-- Tabla 6. Receipt.
CREATE TABLE IF NOT EXISTS Receipt (
    Receipt_id    NUMERIC(10) PRIMARY KEY,
    Receipt_time  TIME,
    Receipt_date  DATE,
    Receipt_total NUMERIC(10)
);

-- Tabla 7. Dependent_Manager.
CREATE TABLE IF NOT EXISTS Dependent_Manager (
    Dependent_name     VARCHAR(30) PRIMARY KEY,
    Dependent_relation VARCHAR(30),
    Dependent_sex      ENUM('M','F'),
    Manager_id         NUMERIC(10) REFERENCES Manager (Manager_id)
);

-- Tabla 8. Dependent_chef.
CREATE TABLE IF NOT EXISTS Dependent_chef (
    Dependent_name     VARCHAR(30) PRIMARY KEY,
    Dependent_relation VARCHAR(30),
    Dependent_sex      ENUM('M','F'),
    Chef_id            NUMERIC(10) REFERENCES Chef (Chef_id)
);

-- Tabla 9. Dependent_Cashier.
CREATE TABLE IF NOT EXISTS Dependent_Cashier (
    Dependent_name     VARCHAR(30) PRIMARY KEY,
    Dependent_relation VARCHAR(30),
    Dependent_sex      ENUM('M','F'),
    Cashier_id         NUMERIC(10) REFERENCES Cashier (Cashier_id)
);

-- Tabla 10. Restaurant_address.
CREATE TABLE IF NOT EXISTS Restaurant_address (
    Restaurant_address VARCHAR(30) PRIMARY KEY,
    Restaurant_id      NUMERIC(10) REFERENCES RestaurantManagement (Restaurant_id)
);

-- Tabla 11. Chef_Prepare_item.
CREATE TABLE IF NOT EXISTS Chef_Prepare_item (
    Chef_id   NUMERIC(10) REFERENCES Chef (Chef_id),
    food_id   NUMERIC(5) REFERENCES Item (food_id),
    sweet_id  NUMERIC(5),
    drinks_id NUMERIC(5)
);

-- Tabla 12. Receipt_takenBy_Cashier.
CREATE TABLE IF NOT EXISTS Receipt_takenBy_Cashier (
    Receipt_id NUMERIC(10) REFERENCES Receipt (Receipt_id),
    Cashier_id NUMERIC(10) REFERENCES Cashier (Cashier_id)
);

----- INSERCIÓN DE DATOS -----
-- Restaurant --
INSERT INTO RestaurantManagement VALUES (56471,'project_resturant','khobar',3214);

-- Manager --
INSERT INTO Manager VALUES
(3214,'ahmed','khalid','alfahad',053276488,7000);

-- Chefs --
INSERT INTO Chef VALUES
(2561,'abduallah','ahmed',6000,3214),
(2562,'saleh','ahmed',6000,3214),
(2563,'mohammad','ahmed',6000,3214),
(2564,'khalid','ahmed',6000,3214);

-- Cashiers --
INSERT INTO Cashier VALUES
(4231,'abdualmajeed',7000),
(4232,'abdualrahman',7000);

-- Receipts --
INSERT INTO Receipt VALUES
(1,'12:45:56','2022-01-23',300),
(2,'12:44:32','2022-01-23',200),
(3,'01:30:50','2022-01-24',300),
(4,'03:30:55','2022-01-24',360),
(5,'03:00:50','2022-01-25',400),
(6,'02:00:00','2022-01-25',200),
(7,'03:00:00','2022-01-26',150);

-- Dependents (Manager) --
INSERT INTO Dependent_Manager VALUES
('maram','wife','F',3214),
('marwa','child','F',3214);

-- Dependents (Chef) --
INSERT INTO Dependent_chef VALUES
('saad','son','M',2562),
('sara','wife','F',2563),
('norah','wife','F',2561);

-- Dependents (Cashier) --
INSERT INTO Dependent_Cashier VALUES
('sara','daughter','F',4231),
('lama','daughter','F',4232);

-- Items --
INSERT INTO Item VALUES
('buratta pizza',56,1,'Dynamite shrimp',39,1,'cola',5,1,1),
('pink pasta',37,2,'mac&cheese balls',45,2,'7up',5,2,2),
('spaghetti',40,3,'tiramisu',42,3,'orang juice',15,3,3),
('rosemary salmon',87,4,'molten chocolate',19,4,'mojito',25,4,4);

-- Chef Prepare Item --
INSERT INTO Chef_Prepare_item VALUES
(2561,1,1,1),
(2562,2,2,2),
(2563,3,3,3),
(2564,4,4,4);

-- Receipt taken by cashier --
INSERT INTO Receipt_takenBy_Cashier VALUES
(1,1),(2,1),(3,1),(4,1),(5,2),(6,2),(7,2);

-- CONSULTAS, PRUEBAS Y PROCEDIMIENTOS --
-- SELECTs de prueba --
SELECT DISTINCT Receipt_total FROM Receipt;

SELECT AVG(Receipt_total) AS avg_total FROM Receipt;
SELECT MAX(Receipt_total) AS max_total FROM Receipt;
SELECT MIN(Receipt_total) AS min_total FROM Receipt;

SELECT * FROM Chef WHERE NOT Chef_name='khalid';
SELECT * FROM Chef WHERE Chef_name LIKE 'k%';

SELECT * FROM Item ORDER BY Item_food DESC;
SELECT * FROM Item ORDER BY Item_food ASC;

SELECT COUNT(Chef_id), Chef_name FROM Chef GROUP BY Chef_name HAVING COUNT(Chef_id) > 2561;

SELECT * FROM Receipt WHERE Receipt_date IN ('2022-01-24','2022-01-25');
SELECT * FROM Receipt WHERE Receipt_total BETWEEN 300 AND 400;

-- CASE example --
INSERT INTO Receipt VALUES (10,'13:46:51','2022-02-23',650);

SELECT 
    Receipt_id,
    Receipt_time,
    Receipt_date,
    Receipt_total,
    CASE 
        WHEN Receipt_total BETWEEN 600 AND 700 THEN 'salary increasing'
        ELSE 'normal'
    END AS salary
FROM Receipt;

-- Receipt copy table --
CREATE TABLE IF NOT EXISTS Receipt_copy (
    Receipt_id    NUMERIC(10) PRIMARY KEY,
    Receipt_time  TIME,
    Receipt_date  DATE,
    Receipt_total NUMERIC(10)
);

INSERT INTO Receipt_copy VALUES (1,'12:45:51','2022-01-23',300);
DELETE FROM Receipt_copy WHERE Receipt_id = 1;

-- Index --
CREATE INDEX Itemindex ON Item (Item_food);

-- PROCEDURE: Chefname --
DELIMITER $$
CREATE PROCEDURE Chefname (IN Cheffname VARCHAR(10))
BEGIN
    SELECT * FROM Chef WHERE Chef_name = Cheffname;
END $$
DELIMITER ;

CALL Chefname('mohammad');
CALL Chefname('abduallah');

-- TRIGGER: uppercase names in Manager --
CREATE TRIGGER uppercase BEFORE INSERT ON Manager
FOR EACH ROW
SET NEW.Manager_Fname = UPPER(NEW.Manager_Fname);

INSERT INTO Manager VALUES (3217,'Yazeed','fahad','alahmad',053276488,7000);

SELECT * FROM Manager;

-- CREACIÓN E INSERCIÓN DE DATOS EN TABLAS ADICIONALES --
-- Tabla Appetizers --
CREATE TABLE IF NOT EXISTS appetizers (
  id    INT NOT NULL AUTO_INCREMENT,
  name  VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO appetizers (id, name, price) VALUES
(1,'Truffel Fries',23),
(2,'Molten Chocolate',12),
(3,'Mac&Cheese Balls',12),
(4,'Dynamite Shrimp',32),
(5,'Kheera',10);

-- Tabla Drinks --
CREATE TABLE IF NOT EXISTS drinks (
  id    INT NOT NULL AUTO_INCREMENT,
  name  VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO drinks (id, name, price) VALUES
(1,'cola',6),
(2,'7up',6),
(3,'orange juice',10),
(4,'mojito',14),
(5,'Red Bull',8);

-- Tabla MainCourse --
CREATE TABLE IF NOT EXISTS maincourse (
  id    INT NOT NULL AUTO_INCREMENT,
  name  VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO maincourse (id, name, price) VALUES
(1,'Buratta Pizza',54),
(2,'Pink Pasta',12),
(3,'Rosemary Salmon',30),
(4,'Spaghetti',8),
(5,'Crown Pizza',50);

COMMIT;

-- CONCESIÓN DE PRIVILEGIOS AL USUARIO RESTAURANT --
GRANT ALL PRIVILEGES ON project3.* TO 'restaurant'@'%';
FLUSH PRIVILEGES;

-- Reactivar restricciones de claves foráneas. --
SET FOREIGN_KEY_CHECKS = 1;
