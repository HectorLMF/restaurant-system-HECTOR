-- 02-procedures.sql
-- Define procedimientos almacenados de la base de datos.

USE project3;

DROP PROCEDURE IF EXISTS chefname;

DELIMITER $$

CREATE PROCEDURE chefname (IN chef_name_param VARCHAR(10))
BEGIN
    SELECT *
    FROM chef
    WHERE chef_name = chef_name_param;
END $$

DELIMITER ;

