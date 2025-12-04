-- 04-triggers.sql
-- Añade triggers a la base de datos.

USE project3;

DROP TRIGGER IF EXISTS uppercase;

CREATE TRIGGER uppercase
BEFORE INSERT ON Manager
FOR EACH ROW
SET NEW.Manager_Fname = UPPER(NEW.Manager_Fname);

-- Ejemplo de uso, NO necesario en producción.
-- INSERT INTO Manager (Manager_id, Manager_Fname, Manager_Mname, Manager_Lname, Manager_number, Manager_salary)
-- VALUES (3217,'Yazeed','fahad','alahmad',053276488,7000);
