-- 02-data.sql
-- Inserción de datos de ejemplo / iniciales.

USE project3;

-- (Opcional pero útil) Desactivar FKs mientras insertamos datos iniciales
SET FOREIGN_KEY_CHECKS = 0;

-- Managers (primero, porque otros datos dependen de ellos)
INSERT INTO Manager (Manager_id, Manager_Fname, Manager_Mname, Manager_Lname, Manager_number, Manager_salary) VALUES
(3214, 'ahmed', 'khalid', 'alfahad', 053276488, 7000);

-- Restaurant (referencia a Manager_id = 3214, ahora ya existe)
INSERT INTO RestaurantManagement (Restaurant_id, Restaurant_name, Restaurant_address, Manager_id) VALUES
(56471, 'project_resturant', 'khobar', 3214);

-- Chefs
INSERT INTO Chef (Chef_id, Chef_name, Chef_manager, Chef_salary, Manager_id) VALUES
(2561, 'abduallah', 'ahmed', 6000, 3214),
(2562, 'saleh',     'ahmed', 6000, 3214),
(2563, 'mohammad',  'ahmed', 6000, 3214),
(2564, 'khalid',    'ahmed', 6000, 3214);

-- Cashiers
INSERT INTO Cashier (Cashier_id, Cashier_name, Cashier_salary) VALUES
(4231, 'abdualmajeed', 7000),
(4232, 'abdualrahman', 7000);

-- Receipts
INSERT INTO Receipt (Receipt_id, Receipt_time, Receipt_date, Receipt_total) VALUES
(1, '12:45:56', '2022-01-23', 300),
(2, '12:44:32', '2022-01-23', 200),
(3, '01:30:50', '2022-01-24', 300),
(4, '03:30:55', '2022-01-24', 360),
(5, '03:00:50', '2022-01-25', 400),
(6, '02:00:00', '2022-01-25', 200),
(7, '03:00:00', '2022-01-26', 150),
(10,'13:46:51', '2022-02-23', 650),
(8, '12:45:51', '2022-01-23', 300);

-- Dependents (Manager)
INSERT INTO Dependent_Manager (Dependent_name, Dependent_relation, Dependent_sex, Manager_id) VALUES
('maram', 'wife',  'F', 3214),
('marwa', 'child', 'F', 3214);

-- Dependents (Chef)
INSERT INTO Dependent_chef (Dependent_name, Dependent_relation, Dependent_sex, Chef_id) VALUES
('saad', 'son',  'M', 2562),
('sara', 'wife', 'F', 2563),
('norah','wife', 'F', 2561);

-- Dependents (Cashier)
INSERT INTO Dependent_Cashier (Dependent_name, Dependent_relation, Dependent_sex, Cashier_id) VALUES
('sara', 'daughter', 'F', 4231),
('lama', 'daughter', 'F', 4232);

-- Items
INSERT INTO Item (Item_food, food_price, food_id, Item_appetizers, appetizers_price, appetizers_id, Item_drinks, drinks_price, drinks_id, Receipt_id) VALUES
('buratta pizza',   56.0, 1, 'Dynamite shrimp',  39.0, 1, 'cola',        5.0, 1, 1),
('pink pasta',      37.0, 2, 'mac&cheese balls', 45.0, 2, '7up',         5.0, 2, 2),
('spaghetti',       40.0, 3, 'tiramisu',         42.0, 3, 'orang juice',15.0, 3, 3),
('rosemary salmon', 87.0, 4, 'molten chocolate',19.0, 4, 'mojito',      25.0, 4, 4);

-- Chef_Prepare_item
INSERT INTO Chef_Prepare_item (Chef_id, food_id, sweet_id, drinks_id) VALUES
(2561, 1, 1, 1),
(2562, 2, 2, 2),
(2563, 3, 3, 3),
(2564, 4, 4, 4);

-- Receipt_takenBy_Cashier
INSERT INTO Receipt_takenBy_Cashier (Receipt_id, Cashier_id) VALUES
(1,1),(2,1),(3,1),(4,1),
(5,2),(6,2),(7,2);

-- Tabla Receipt_copy (pruebas)
CREATE TABLE IF NOT EXISTS Receipt_copy (
    Receipt_id    NUMERIC(10) PRIMARY KEY,
    Receipt_time  TIME,
    Receipt_date  DATE,
    Receipt_total NUMERIC(10)
);

INSERT INTO Receipt_copy (Receipt_id, Receipt_time, Receipt_date, Receipt_total) VALUES
(1, '12:45:51', '2022-01-23', 300);

DELETE FROM Receipt_copy WHERE Receipt_id = 1;

-- Productos para la API
INSERT INTO appetizers (id, name, price) VALUES
(1, 'Truffel Fries',      23),
(2, 'Molten Chocolate',   12),
(3, 'Mac&Cheese Balls',   12),
(4, 'Dynamite Shrimp',    32),
(5, 'Kheera',             10);

INSERT INTO drinks (id, name, price) VALUES
(1, 'cola',          6),
(2, '7up',           6),
(3, 'orange juice', 10),
(4, 'mojito',       14),
(5, 'Red Bull',      8);

INSERT INTO maincourse (id, name, price) VALUES
(1, 'Buratta Pizza',   54),
(2, 'Pink Pasta',      12),
(3, 'Rosemary Salmon', 30),
(4, 'Spaghetti',        8),
(5, 'Crown Pizza',     50);

-- Volver a activar FKs
SET FOREIGN_KEY_CHECKS = 1;
