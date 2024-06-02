# StaffManagement
Staff Management Application
Overview
This repository contains a web application built for managing staff members. It provides functionalities for user registration, authentication, and access to a private dashboard. The application incorporates robust security measures, data validation, and database management.

Public Pages
Register: Allows new users to create an account.
URL: http://localhost:8090/register.html
Login: Provides access for registered users.
URL: http://localhost:8090/login.html
Note: http://localhost:8090/ redirects to the login page.
Private Page
Dashboard: Grants access to authenticated users to view staff-related information.
URL: http://localhost:8090/dashboard.html
Functionalities
Spring Security and JWT Token Authorization/Authentication: Implements robust security measures to ensure secure access to private pages using JSON Web Tokens (JWT).
Validations (Front and Back): Enforces data validation both on the client-side (front-end) and server-side (back-end) to maintain data integrity and enhance security.
CRUD for Staff: Provides functionalities for creating, reading, updating, and deleting staff members' information.
Flyway Migration: Utilizes Flyway as a database migration tool to manage database schema changes efficiently over time.
PostgreSQL and H2: Utilizes PostgreSQL as the primary database for the actual application and H2 for testing purposes.
JUnit Tests: Includes a suite of automated tests using JUnit to ensure the reliability and correctness of the application.
HTML, JavaScript, CSS: Utilizes HTML for structuring web pages, JavaScript for dynamic client-side interactions, and CSS for styling and formatting to enhance the user interface and experience.
Additional Details
CSS Description: Cascading Style Sheets (CSS) play a crucial role in defining the visual presentation of web pages. This includes layout design, color schemes, typography, and responsive behavior for various devices. CSS ensures consistency and aesthetics across the application, contributing to an engaging user experience.