-- 01-tables.sql
-- Define las tablas de la base de datos.

USE project3;

-- MANAGER
CREATE TABLE IF NOT EXISTS Manager (
    Manager_id     NUMERIC(10) PRIMARY KEY,
    Manager_Fname  VARCHAR(30),
    Manager_Mname  VARCHAR(30),
    Manager_Lname  VARCHAR(30),
    Manager_number NUMERIC(10),
    Manager_salary NUMERIC(5)
);

-- CASHIER
CREATE TABLE IF NOT EXISTS Cashier (
    Cashier_id     NUMERIC(10) PRIMARY KEY,
    Cashier_name   VARCHAR(30),
    Cashier_salary NUMERIC(5)
);

-- CHEF
CREATE TABLE IF NOT EXISTS Chef (
    Chef_id      NUMERIC(10) PRIMARY KEY,
    Chef_name    VARCHAR(30),
    Chef_manager VARCHAR(30),
    Chef_salary  NUMERIC(5),
    Manager_id   NUMERIC(10),
    CONSTRAINT fk_chef_manager
      FOREIGN KEY (Manager_id) REFERENCES Manager (Manager_id)
);

-- RECEIPT
CREATE TABLE IF NOT EXISTS Receipt (
    Receipt_id    NUMERIC(10) PRIMARY KEY,
    Receipt_time  TIME,
    Receipt_date  DATE,
    Receipt_total NUMERIC(10)
);

-- RESTAURANT MANAGEMENT
CREATE TABLE IF NOT EXISTS RestaurantManagement (
    Restaurant_id      NUMERIC(10) PRIMARY KEY,
    Restaurant_name    VARCHAR(30),
    Restaurant_address VARCHAR(30),
    Manager_id         NUMERIC(10),
    CONSTRAINT fk_restaurant_manager
      FOREIGN KEY (Manager_id) REFERENCES Manager (Manager_id)
);

-- DEPENDENTS
CREATE TABLE IF NOT EXISTS Dependent_Manager (
    Dependent_name     VARCHAR(30) PRIMARY KEY,
    Dependent_relation VARCHAR(30),
    Dependent_sex      ENUM('M','F'),
    Manager_id         NUMERIC(10),
    CONSTRAINT fk_dep_manager
      FOREIGN KEY (Manager_id) REFERENCES Manager (Manager_id)
);

CREATE TABLE IF NOT EXISTS Dependent_chef (
    Dependent_name     VARCHAR(30) PRIMARY KEY,
    Dependent_relation VARCHAR(30),
    Dependent_sex      ENUM('M','F'),
    Chef_id            NUMERIC(10),
    CONSTRAINT fk_dep_chef
      FOREIGN KEY (Chef_id) REFERENCES Chef (Chef_id)
);

CREATE TABLE IF NOT EXISTS Dependent_Cashier (
    Dependent_name     VARCHAR(30) PRIMARY KEY,
    Dependent_relation VARCHAR(30),
    Dependent_sex      ENUM('M','F'),
    Cashier_id         NUMERIC(10),
    CONSTRAINT fk_dep_cashier
      FOREIGN KEY (Cashier_id) REFERENCES Cashier (Cashier_id)
);

-- ITEM (productos combinados en un ticket)
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

    Receipt_id        NUMERIC(10),

    PRIMARY KEY (food_id, appetizers_id, drinks_id),
    CONSTRAINT fk_item_receipt
      FOREIGN KEY (Receipt_id) REFERENCES Receipt (Receipt_id)
);

-- CHEF_PREPARE_ITEM
CREATE TABLE IF NOT EXISTS Chef_Prepare_item (
    Chef_id   NUMERIC(10),
    food_id   NUMERIC(5),
    sweet_id  NUMERIC(5),
    drinks_id NUMERIC(5),
    CONSTRAINT fk_cpi_chef
      FOREIGN KEY (Chef_id) REFERENCES Chef (Chef_id)
    -- NOTA: no hay columnas sweet_id/drinks_id en Item, por eso no ponemos FKs aquí
);

-- RECEIPT_TAKENBY_CASHIER
CREATE TABLE IF NOT EXISTS Receipt_takenBy_Cashier (
    Receipt_id NUMERIC(10),
    Cashier_id NUMERIC(10),
    CONSTRAINT fk_rtc_receipt
      FOREIGN KEY (Receipt_id) REFERENCES Receipt (Receipt_id),
    CONSTRAINT fk_rtc_cashier
      FOREIGN KEY (Cashier_id) REFERENCES Cashier (Cashier_id)
);

-- RESTAURANT_ADDRESS
CREATE TABLE IF NOT EXISTS Restaurant_address (
    Restaurant_address VARCHAR(30) PRIMARY KEY,
    Restaurant_id      NUMERIC(10),
    CONSTRAINT fk_restaurant_addr
      FOREIGN KEY (Restaurant_id) REFERENCES RestaurantManagement (Restaurant_id)
);

-- TABLAS DE PRODUCTOS PARA LA API (Appetizers, Drinks, MainCourse)
CREATE TABLE IF NOT EXISTS appetizers (
  id    INT NOT NULL AUTO_INCREMENT,
  name  VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS drinks (
  id    INT NOT NULL AUTO_INCREMENT,
  name  VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS maincourse (
  id    INT NOT NULL AUTO_INCREMENT,
  name  VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices
CREATE INDEX Itemindex ON Item (Item_food);
