-- 06-users.sql
-- Crea la tabla de usuarios y añade usuarios predeterminados.

 -- Crear BD si no existe. --
CREATE DATABASE IF NOT EXISTS project3;
USE project3;

-- Crear tabla users --
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Insertar usuarios predeterminados --
-- USER: admin  | PASSWORD: admin
INSERT IGNORE INTO users (username, password_hash, role) 
VALUES ('admin', '$2b$10$TAxhf.ziwuv7ynXlSQYjMeftWo47ft8M4JfdXjLoBPdERwuo7zD06', 'ADMIN');

-- USER: cashier | PASSWORD: cashier123
INSERT IGNORE INTO users (username, password_hash, role) 
VALUES ('cashier', '$2b$10$HLeNcr00JL6qpGJUd.klqud4PfSRci2qvuKr7fnklGjtMOO6OhwLC', 'CASHIER');
