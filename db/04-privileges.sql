-- 04-privileges.sql
-- Asigna permisos al usuario usado por Spring Boot.

USE project3;

GRANT ALL PRIVILEGES ON project3.* TO 'restaurant'@'%';
FLUSH PRIVILEGES;