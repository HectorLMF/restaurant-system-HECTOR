-- 03-procedures.sql
-- Define procedimientos almacenados de la base de datos.

USE project3;

DELIMITER $$
CREATE PROCEDURE Chefname (IN Cheffname VARCHAR(10))
BEGIN
    SELECT *
    FROM Chef
    WHERE Chef_name = Cheffname;
END $$
DELIMITER ;

-- Opcional: Llamadas de prueba, NO necesarias en producción.
-- CALL Chefname('mohammad');
-- CALL Chefname('abduallah');
