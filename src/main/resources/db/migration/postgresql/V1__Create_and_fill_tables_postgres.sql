-- Create user table
CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT unique_user_name UNIQUE (username)
);

-- Create department table
CREATE TABLE IF NOT EXISTS department
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT unique_department_name UNIQUE (name)
);

-- Create staff table
CREATE TABLE IF NOT EXISTS staff
(
    id BIGSERIAL PRIMARY KEY,
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
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    data BYTEA,
    staff_id BIGINT UNIQUE,
    FOREIGN KEY(staff_id) REFERENCES staff(id)
);

-- Insert sample data into department table only if it does not exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM department WHERE name = 'IT Department') THEN
            INSERT INTO department (name)
            VALUES ('IT Department'),
                   ('Human Resources'),
                   ('Finance'),
                   ('Marketing'),
                   ('Operations');
        END IF;
    END
$$;


-- Insert sample data into staff table only if it does not exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM staff WHERE email = 'john.doe@example.com') THEN
            INSERT INTO staff (first_name, last_name, email, contact_number, department_id)
            VALUES ('John', 'Doe', 'john.doe@example.com', '555-1234', 1),
                   ('Jane', 'Smith', 'jane.smith@example.com', '555-2345', 2),
                   ('Michael', 'Brown', 'michael.brown@example.com', '555-3456', 3),
                   ('Emily', 'Davis', 'emily.davis@example.com', '555-4567', 4),
                   ('Chris', 'Wilson', 'chris.wilson@example.com', '555-5678', 5),
                   ('Sarah', 'Moore', 'sarah.moore@example.com', '555-6789', 1),
                   ('David', 'Taylor', 'david.taylor@example.com', '555-7890', 2),
                   ('Laura', 'Anderson', 'laura.anderson@example.com', '555-8901', 3),
                   ('James', 'Thomas', 'james.thomas@example.com', '555-9012', 4),
                   ('Linda', 'Jackson', 'linda.jackson@example.com', '555-0123', 5),
                   ('Robert', 'White', 'robert.white@example.com', '555-1235', 1),
                   ('Patricia', 'Harris', 'patricia.harris@example.com', '555-2346', 2),
                   ('Charles', 'Martin', 'charles.martin@example.com', '555-3457', 3),
                   ('Jennifer', 'Garcia', 'jennifer.garcia@example.com', '555-4568', 4),
                   ('Daniel', 'Martinez', 'daniel.martinez@example.com', '555-5679', 5),
                   ('Susan', 'Rodriguez', 'susan.rodriguez@example.com', '555-6780', 1),
                   ('Matthew', 'Lee', 'matthew.lee@example.com', '555-7891', 2),
                   ('Karen', 'Walker', 'karen.walker@example.com', '555-8902', 3),
                   ('Joseph', 'Hall', 'joseph.hall@example.com', '555-9013', 4),
                   ('Nancy', 'Allen', 'nancy.allen@example.com', '555-0124', 5);
        END IF;
    END
$$;


-- Insert sample data into image table only if it does not exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM image WHERE staff_id = 1) THEN
            INSERT INTO image (name, data, staff_id)
            VALUES (NULL, NULL, 1),
                   (NULL, NULL, 2),
                   (NULL, NULL, 3),
                   (NULL, NULL, 4),
                   (NULL, NULL, 5),
                   (NULL, NULL, 6),
                   (NULL, NULL, 7),
                   (NULL, NULL, 8),
                   (NULL, NULL, 9),
                   (NULL, NULL, 10),
                   (NULL, NULL, 11),
                   (NULL, NULL, 12),
                   (NULL, NULL, 13),
                   (NULL, NULL, 14),
                   (NULL, NULL, 15),
                   (NULL, NULL, 16),
                   (NULL, NULL, 17),
                   (NULL, NULL, 18),
                   (NULL, NULL, 19),
                   (NULL, NULL, 20);
        END IF;
    END
$$;
