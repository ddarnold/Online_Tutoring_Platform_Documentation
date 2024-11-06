-- Step 1: Create the Users table
CREATE TABLE Users (
    UserID INT PRIMARY KEY,
    UserName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    RegistrationDate DATE
);

-- Step 2: Create the Courses table
CREATE TABLE Courses (
    CourseID INT PRIMARY KEY,
    CourseName VARCHAR(100) NOT NULL,
    Instructor VARCHAR(50),
    Credits INT
);

-- Step 3: Populate the Users table with sample data
INSERT INTO Users (UserID, UserName, Email, RegistrationDate)
VALUES
    (1, 'Alice Smith', 'alice@example.com', '2024-10-01'),
    (2, 'Bob Jones', 'bob@example.com', '2024-10-02'),
    (3, 'Charlie Brown', 'charlie@example.com', '2024-10-03');

-- Step 4: Populate the Courses table with sample data
INSERT INTO Courses (CourseID, CourseName, Instructor, Credits)
VALUES
    (101, 'Introduction to Computer Science', 'Dr. James', 3),
    (102, 'Data Structures', 'Dr. Allen', 4),
    (103, 'Algorithms', 'Dr. Clara', 4);

-- Step 5: Populate the actors spring-boot sample table with sample data
INSERT INTO Courses (id, first_name, last_name)
VALUES
    (0, 'Marko', 'Markovic'),
    (1, 'Luka', 'Lukic')
