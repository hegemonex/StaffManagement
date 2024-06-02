-- Create user table
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create department table
CREATE TABLE IF NOT EXISTS department
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Insert sample data into department table
INSERT INTO department (name)
VALUES ('IT Department'),
       ('Human Resources'),
       ('Finance'),
       ('Marketing'),
       ('Operations');



-- Create staff table
CREATE TABLE IF NOT EXISTS staff
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255) NOT NULL,
    department_id BIGINT,
    FOREIGN KEY(department_id) REFERENCES department(id)
);


-- Create image table
CREATE TABLE IF NOT EXISTS image
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    data LONGBLOB,
    staff_id BIGINT UNIQUE,
    FOREIGN KEY(staff_id) REFERENCES staff(id)
);
