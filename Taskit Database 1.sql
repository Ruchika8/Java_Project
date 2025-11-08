CREATE DATABASE IF NOT EXISTS taskitdb;
USE taskitdb;

CREATE TABLE users (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(100),
  role ENUM('admin','customer')
);

CREATE TABLE professionals (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  service_type VARCHAR(100),
  price_per_hour DECIMAL(10,2),
  area VARCHAR(100),
  available BOOLEAN DEFAULT TRUE
);

CREATE TABLE service_requests (
  srvic_req_ID INT AUTO_INCREMENT PRIMARY KEY,
  Customer_ID INT,
  professionals_ID INT,
  date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status ENUM('Pending','Completed') DEFAULT 'Pending',
  FOREIGN KEY (Customer_ID) REFERENCES users(ID),
  FOREIGN KEY (professionals_ID) REFERENCES professionals(ID)
);


INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@taskit.com', 'admin123', 'admin'),
       ('Ishita', 'user@gmail.com', '1234', 'customer');

INSERT INTO professionals (name, service_type, price_per_hour, area, available)
VALUES ('Rohit Kumar', 'Electrician', 250, 'Noida', TRUE),
       ('Sahil Sharma', 'Plumber', 300, 'Greater Noida', TRUE);
       
select * from professionals;
select * from users;
select * from service_requests;


