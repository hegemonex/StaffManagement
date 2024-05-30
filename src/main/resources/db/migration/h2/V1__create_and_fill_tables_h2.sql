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

INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('John', 'Doe', 'john.doe@example.com', '555-1234', 1);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Jane', 'Smith', 'jane.smith@example.com', '555-2345', 2);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Michael', 'Brown', 'michael.brown@example.com', '555-3456', 3);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Emily', 'Davis', 'emily.davis@example.com', '555-4567', 4);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Chris', 'Wilson', 'chris.wilson@example.com', '555-5678', 5);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Sarah', 'Moore', 'sarah.moore@example.com', '555-6789', 1);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('David', 'Taylor', 'david.taylor@example.com', '555-7890', 2);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Laura', 'Anderson', 'laura.anderson@example.com', '555-8901', 3);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('James', 'Thomas', 'james.thomas@example.com', '555-9012', 4);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Linda', 'Jackson', 'linda.jackson@example.com', '555-0123', 5);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Robert', 'White', 'robert.white@example.com', '555-1235', 1);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Patricia', 'Harris', 'patricia.harris@example.com', '555-2346', 2);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Charles', 'Martin', 'charles.martin@example.com', '555-3457', 3);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Jennifer', 'Garcia', 'jennifer.garcia@example.com', '555-4568', 4);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Daniel', 'Martinez', 'daniel.martinez@example.com', '555-5679', 5);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Susan', 'Rodriguez', 'susan.rodriguez@example.com', '555-6780', 1);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Matthew', 'Lee', 'matthew.lee@example.com', '555-7891', 2);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Karen', 'Walker', 'karen.walker@example.com', '555-8902', 3);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Joseph', 'Hall', 'joseph.hall@example.com', '555-9013', 4);
INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
VALUES ('Nancy', 'Allen', 'nancy.allen@example.com', '555-0124', 5);

-- Create image table
CREATE TABLE IF NOT EXISTS image
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    data LONGBLOB,
    staff_id BIGINT UNIQUE,
    FOREIGN KEY(staff_id) REFERENCES staff(id)
);

INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 1);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 2);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 3);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 4);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 5);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 6);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 7);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 8);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 9);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 10);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 11);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 12);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 13);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 14);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 15);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 16);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 17);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 18);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 19);
INSERT INTO image (name, data, staff_id) VALUES (NULL, NULL, 20);