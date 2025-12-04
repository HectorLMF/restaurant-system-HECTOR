-- 05-privileges.sql
-- Asigna permisos al usuario usado por Spring Boot.

GRANT ALL PRIVILEGES ON project3.* TO 'restaurant'@'%';
FLUSH PRIVILEGES;
