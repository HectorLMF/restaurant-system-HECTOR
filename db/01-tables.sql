-- 01-tables.sql
-- Define todas las tablas de la base de datos.

USE project3;

-- ======================================
-- USUARIOS DEL SISTEMA (para la API)
-- ======================================
CREATE TABLE IF NOT EXISTS users (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(50)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- MANAGER
-- ======================================
CREATE TABLE IF NOT EXISTS manager (
    manager_id     BIGINT UNSIGNED PRIMARY KEY,
    manager_fname  VARCHAR(30),
    manager_mname  VARCHAR(30),
    manager_lname  VARCHAR(30),
    manager_number BIGINT,
    manager_salary INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- CASHIER (vinculado a users.id)
-- ======================================
CREATE TABLE IF NOT EXISTS cashier (
    cashier_id     BIGINT UNSIGNED PRIMARY KEY,
    cashier_name   VARCHAR(30),
    cashier_salary INT,
    CONSTRAINT fk_cashier_user
      FOREIGN KEY (cashier_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- CHEF
-- ======================================
CREATE TABLE IF NOT EXISTS chef (
    chef_id      BIGINT UNSIGNED PRIMARY KEY,
    chef_name    VARCHAR(30),
    chef_manager VARCHAR(30),
    chef_salary  INT,
    manager_id   BIGINT UNSIGNED,
    CONSTRAINT fk_chef_manager
      FOREIGN KEY (manager_id) REFERENCES manager (manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- RECEIPT
-- ======================================
CREATE TABLE IF NOT EXISTS receipt (
    receipt_id    BIGINT UNSIGNED PRIMARY KEY,
    receipt_time  TIME,
    receipt_date  DATE,
    receipt_total INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- RESTAURANT MANAGEMENT
-- ======================================
CREATE TABLE IF NOT EXISTS restaurant_management (
    restaurant_id      BIGINT UNSIGNED PRIMARY KEY,
    restaurant_name    VARCHAR(50),
    restaurant_address VARCHAR(100),
    manager_id         BIGINT UNSIGNED,
    CONSTRAINT fk_restaurant_manager
      FOREIGN KEY (manager_id) REFERENCES manager (manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- DEPENDENTS (MANAGER)
-- ======================================
CREATE TABLE IF NOT EXISTS dependent_manager (
    dependent_name     VARCHAR(30) PRIMARY KEY,
    dependent_relation VARCHAR(30),
    dependent_sex      ENUM('M','F'),
    manager_id         BIGINT UNSIGNED,
    CONSTRAINT fk_dep_manager
      FOREIGN KEY (manager_id) REFERENCES manager (manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- DEPENDENTS (CHEF)
-- ======================================
CREATE TABLE IF NOT EXISTS dependent_chef (
    dependent_name     VARCHAR(30) PRIMARY KEY,
    dependent_relation VARCHAR(30),
    dependent_sex      ENUM('M','F'),
    chef_id            BIGINT UNSIGNED,
    CONSTRAINT fk_dep_chef
      FOREIGN KEY (chef_id) REFERENCES chef (chef_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- DEPENDENTS (CASHIER)
-- ======================================
CREATE TABLE IF NOT EXISTS dependent_cashier (
    dependent_name     VARCHAR(30) PRIMARY KEY,
    dependent_relation VARCHAR(30),
    dependent_sex      ENUM('M','F'),
    cashier_id         BIGINT UNSIGNED,
    CONSTRAINT fk_dep_cashier
      FOREIGN KEY (cashier_id) REFERENCES cashier (cashier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- ITEM (productos combinados en un ticket)
-- ======================================
CREATE TABLE IF NOT EXISTS item (
    item_food         VARCHAR(30),
    food_price        INT,
    food_id           INT,

    item_appetizers   VARCHAR(30),
    appetizers_price  INT,
    appetizers_id     INT,

    item_drinks       VARCHAR(30),
    drinks_price      INT,
    drinks_id         INT,

    receipt_id        BIGINT UNSIGNED,

    PRIMARY KEY (food_id, appetizers_id, drinks_id),
    CONSTRAINT fk_item_receipt
      FOREIGN KEY (receipt_id) REFERENCES receipt (receipt_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX item_index ON item (item_food);

-- ======================================
-- CHEF_PREPARE_ITEM
-- ======================================
CREATE TABLE IF NOT EXISTS chef_prepare_item (
    chef_id   BIGINT UNSIGNED,
    food_id   INT,
    sweet_id  INT,
    drinks_id INT,
    CONSTRAINT fk_cpi_chef
      FOREIGN KEY (chef_id) REFERENCES chef (chef_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- RECEIPT_TAKEN_BY_CASHIER
-- ======================================
CREATE TABLE IF NOT EXISTS receipt_taken_by_cashier (
    receipt_id BIGINT UNSIGNED,
    cashier_id BIGINT UNSIGNED,
    CONSTRAINT fk_rtc_receipt
      FOREIGN KEY (receipt_id) REFERENCES receipt (receipt_id),
    CONSTRAINT fk_rtc_cashier
      FOREIGN KEY (cashier_id) REFERENCES cashier (cashier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- RESTAURANT_ADDRESS
-- ======================================
CREATE TABLE IF NOT EXISTS restaurant_address (
    restaurant_address VARCHAR(100) PRIMARY KEY,
    restaurant_id      BIGINT UNSIGNED,
    CONSTRAINT fk_restaurant_addr
      FOREIGN KEY (restaurant_id) REFERENCES restaurant_management (restaurant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- TABLAS DE PRODUCTOS PARA LA API
-- ======================================
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

-- ======================================
-- TABLA DE PRUEBAS: receipt_copy
-- ======================================
CREATE TABLE IF NOT EXISTS receipt_copy (
    receipt_id    BIGINT UNSIGNED PRIMARY KEY,
    receipt_time  TIME,
    receipt_date  DATE,
    receipt_total INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
