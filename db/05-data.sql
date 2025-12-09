-- 05-data.sql
-- Inserción de datos realistas para el restaurante "Black Plate"

USE project3;

-- =====================================================================
-- MANAGER
-- =====================================================================
INSERT INTO manager (manager_id, manager_fname, manager_mname, manager_lname, manager_number, manager_salary)
VALUES
(1001, 'marta', 'elena', 'rodriguez', 659123456, 3200);

-- =====================================================================
-- RESTAURANT
-- =====================================================================
INSERT INTO restaurant_management (restaurant_id, restaurant_name, restaurant_address, manager_id)
VALUES
(2001, 'black plate', 'Calle Luna 45, Madrid', 1001);

-- =====================================================================
-- CHEFS DE BLACK PLATE
-- =====================================================================
INSERT INTO chef (chef_id, chef_name, chef_manager, chef_salary, manager_id)
VALUES
(3001, 'Luis Gómez', 'Marta', 1800, 1001),
(3002, 'Carmen Ruiz', 'Marta', 1750, 1001),
(3003, 'Javier Soto', 'Marta', 1700, 1001);

-- =====================================================================
-- RECEIPTS (Tickets reales)
-- =====================================================================
INSERT INTO receipt (receipt_id, receipt_time, receipt_date, receipt_total)
VALUES
(1, '13:15:20', '2024-10-01', 42),
(2, '14:22:10', '2024-10-01', 27),
(3, '20:45:50', '2024-10-02', 58),
(4, '21:10:35', '2024-10-03', 73),
(5, '12:30:10', '2024-10-04', 19);

-- =====================================================================
-- DEPENDENTS DEL MANAGER
-- =====================================================================
INSERT INTO dependent_manager (dependent_name, dependent_relation, dependent_sex, manager_id)
VALUES
('lucia', 'daughter', 'F', 1001),
('pablo', 'son',      'M', 1001);

-- =====================================================================
-- DEPENDENTS DE CHEF
-- =====================================================================
INSERT INTO dependent_chef (dependent_name, dependent_relation, dependent_sex, chef_id)
VALUES
('marcos', 'son',  'M', 3001),
('irene',  'wife', 'F', 3002);

-- =====================================================================
-- USUARIOS DEL SISTEMA
-- El trigger creará automáticamente los CASHIERS en la tabla cashier
-- =====================================================================

-- ADMIN (NO genera cashier)
INSERT INTO users (id, username, password_hash, role)
VALUES (1, 'admin', '$2b$10$TAxhf.ziwuv7ynXlSQYjMeftWo47ft8M4JfdXjLoBPdERwuo7zD06', 'ADMIN');

-- CAJEROS (generan automáticamente filas en cashier gracias al trigger)
-- password: cashier123 (hash reutilizado)
INSERT INTO users (id, username, password_hash, role)
VALUES
(2, 'laura.mendez', '$2b$10$HLeNcr00JL6qpGJUd.klqud4PfSRci2qvuKr7fnklGjtMOO6OhwLC', 'CASHIER'),
(3, 'diego.santos', '$2b$10$HLeNcr00JL6qpGJUd.klqud4PfSRci2qvuKr7fnklGjtMOO6OhwLC', 'CASHIER');

-- En este punto, gracias al trigger:
--   cashier tendrá:
--   (2, 'laura.mendez', 1500) y (3, 'diego.santos', 1500)

-- =====================================================================
-- DEPENDENTS DE LOS CASHIERS
-- =====================================================================
INSERT INTO dependent_cashier (dependent_name, dependent_relation, dependent_sex, cashier_id)
VALUES
('emma', 'daughter', 'F', 2),
('alex', 'son',      'M', 3);

-- =====================================================================
-- ITEMS DEL TICKET (combinaciones reales del menú Black Plate)
-- =====================================================================
INSERT INTO item (item_food, food_price, food_id,
                  item_appetizers, appetizers_price, appetizers_id,
                  item_drinks, drinks_price, drinks_id,
                  receipt_id)
VALUES
('Grilled Salmon',      18, 1, 'Truffle Fries',      6, 1, 'Coca Cola',       2, 1, 1),
('Black Angus Burger',  16, 2, 'Garlic Bread',       4, 2, 'Sparkling Water', 2, 2, 2),
('Chicken Teriyaki',    14, 3, 'Hummus Bowl',        5, 3, 'Iced Tea',        3, 3, 3),
('Vegan Buddha Bowl',   13, 4, 'Sweet Potato Chips', 4, 4, 'Lemonade',        3, 4, 4),
('Pasta Alfredo',       11, 5, 'Bruschetta',         4, 5, 'Coca Cola',       2, 1, 5);

-- =====================================================================
-- CHEF PREPARA PLATOS
-- =====================================================================
INSERT INTO chef_prepare_item (chef_id, food_id, sweet_id, drinks_id)
VALUES
(3001, 1, 1, 1),
(3002, 2, 2, 2),
(3003, 3, 3, 3);

-- =====================================================================
-- RECEIPTS TOMADOS POR CASHIERS (usa IDs 2 y 3)
-- =====================================================================
INSERT INTO receipt_taken_by_cashier (receipt_id, cashier_id)
VALUES
(1, 2),
(2, 2),
(3, 3),
(4, 3),
(5, 2);

-- =====================================================================
-- Tabla receipt_copy (pruebas)
-- =====================================================================
INSERT INTO receipt_copy (receipt_id, receipt_time, receipt_date, receipt_total)
VALUES
(1, '12:45:51', '2024-10-01', 42);

DELETE FROM receipt_copy WHERE receipt_id = 1;

-- =====================================================================
-- Productos para la API (Black Plate)
-- =====================================================================
INSERT INTO appetizers (name, price) VALUES
('Truffle Fries',      6),
('Garlic Bread',       4),
('Hummus Bowl',        5),
('Sweet Potato Chips', 4),
('Bruschetta',         4);

INSERT INTO drinks (name, price) VALUES
('Coca Cola',       2),
('Sparkling Water', 2),
('Iced Tea',        3),
('Lemonade',        3),
('Red Fruit Juice', 4);

INSERT INTO maincourse (name, price) VALUES
('Grilled Salmon',        18),
('Black Angus Burger',    16),
('Chicken Teriyaki',      14),
('Vegan Buddha Bowl',     13),
('Pasta Alfredo',         11);
