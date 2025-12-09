-- 03-triggers.sql
-- Añade triggers a la base de datos.

USE project3;

-- ======================================
-- Trigger: nombres de manager en mayúsculas
-- ======================================
DROP TRIGGER IF EXISTS trg_uppercase_manager_fname;

DELIMITER $$

CREATE TRIGGER trg_uppercase_manager_fname
BEFORE INSERT ON manager
FOR EACH ROW
BEGIN
  SET NEW.manager_fname = UPPER(NEW.manager_fname);
END $$

DELIMITER ;

-- ======================================
-- Trigger: crear cashier automáticamente al insertar un usuario CASHIER
-- Asigna salario base = 1500 €
-- ======================================
DROP TRIGGER IF EXISTS trg_create_cashier_from_user;

DELIMITER $$

CREATE TRIGGER trg_create_cashier_from_user
AFTER INSERT ON users
FOR EACH ROW
BEGIN
  IF NEW.role = 'CASHIER' THEN
    INSERT INTO cashier (cashier_id, cashier_name, cashier_salary)
    VALUES (NEW.id, NEW.username, 1500);
  END IF;
END $$

DELIMITER ;

