CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);




-- USER: admin  | PASSWORT: admin
INSERT IGNORE INTO users (username, password_hash, role) 
VALUES ('admin', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDNYApwElpZnlJGmaxXux2', 'ADMIN');

-- USER: cashier | PASSWORT: cashier123
INSERT IGNORE INTO users (username, password_hash, role) 
VALUES ('cashier', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'CASHIER');