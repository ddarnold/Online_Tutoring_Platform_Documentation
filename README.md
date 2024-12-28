
# THUtorium
An online tutoring platform exclusively for THU students from THU students.

## Installing the development project

### Prerequisites
- Software development Kit:
   - JAVA 17 or higher
- Build Tool:
   - MAVEN 3.9 or higher
- Integrated Development Environment:
  - IntelliJ IDEA (Recommended)
- Optional:
   - API development and testing tool:
      - POSTMAN

### Steps:
1. Delete any existing versions of the project
2. Clone the Online_Tutoring_platform from the [github repository](https://github.com/ddarnold/Online_Tutoring_Platform.git).
3. Add the .env file at the root folder with **at least(!!)** the following parameters:
- DB_NAME
- DB_PASSWORD
- DB_URL
- DB_USER
- JWT_SECRET_KEY
- JWT_DURATION_IN_MILLISECONDS

Contact [Nikolai Ivanov](mailto:ivanni01@thu.de) or [Jossin Antony](antojo01@thu.de) for the values of the parameters.
4. Pass the correct environment variables to the IDE (IntelliJ IDEA) - ***Edit configurations -> User Environment variables***.


***Attention***:
- The **.env** database url contains ***'database'*** and the port address of the docker database container;
- whereas the **IDE** environment variables contain ***'localhost'*** and the respective port number in the database URL.

5. ***Install dependencies:***
From the root folder, access:
   - ./backend -> MVN install
   - ./frontend -> NPM install
   - ./webrtc -> NPM install

6. Proceed with docker configurations.

## Docker

### Prerequisites

Before running the application, ensure you have the following installed:

- **Docker Engine**: Make sure Docker is installed and running on your machine.
- **Docker Compose**: Docker Compose is also required. It typically comes with Docker Desktop, but you can install it separately if needed.

### Running the Application with Docker Compose

To run both the backend and frontend applications using Docker Compose, follow these steps:

1. **Navigate to the root directory of the project**:
   ```bash
   cd ~/path/to/Online_Tutoring_Platform
   ```

2. **Run the Compose command**:
   ```bash
   docker-compose up --build --detach
   ```

### Running Backend, Frontend, or Database Individually

- **Running only the Backend** (will start the database and webrtc server as dependencies):
   ```bash
   docker-compose up --build --detach backend
   ```

- **Running only the Frontend**:
   ```bash
   docker-compose up --build --detach frontend
   ```

- **Running only the Database**:
   ```bash
   docker-compose up --build --detach database
   ```

- **Running only the webrtc server**:
   ```bash
   docker-compose up --build --detach webrtc-server
   ```

### Stopping Containers

- **Stopping all containers**:
   ```bash
   docker-compose down
   ```

- **Stopping only the Backend**:
   ```bash
   docker-compose stop backend
   ```

- **Stopping only the Frontend**:
   ```bash
   docker-compose stop frontend
   ```

- **Stopping only the Database**:
   ```bash
   docker-compose stop database
   ```
  
- **Stopping only the webrtc server**:
   ```bash
   docker-compose stop webrtc-server
   ```

### Running the code

- **`.env` file**: An `.env` file, required for creating the database and backend container, is included in the repository as an example.
- **Running Backend Locally**: If you run the backend from an IDE, ensure the database is started first (**!!IMPORTANT!!**), ideally from the docker container. 
Identical environment variables should be set in the IDE to match those in Docker. In the intellij IDE, these can be set in the 'Edit Configurations' section.
- **Local Database URL**: If the backend runs outside a Docker container, make sure the `databaseUrl` points to your local database (e.g., `localhost`).
- **JWT_SECRET_KEY**:The secret key must be an HMAC hash string of 256 bits; otherwise, the token generation will throw an error.

### Database Data Persistence

- The database used in this application is configured to be persistent by utilizing Docker volumes.
- In the `docker-compose.yml` file, a named volume (`postgres_data`) is created and mapped to the PostgreSQL container’s data directory (`/var/lib/postgresql/data`)
- Additionally, the data is stored in a folder named `database` in the repository.
- This setup ensures that all database data remains intact even when the container is stopped or removed, allowing for seamless data management across container lifecycles.

### Ports Used

- **Backend**: Available at [http://localhost:8080](http://localhost:8080)
- **Frontend**: Available at [http://localhost](http://localhost) on port 80 (served by Nginx, not React's development server).
- **Database**: Available at [http://localhost:5431](http://localhost:5431) (Ensure match with parameters in .env file)
- **Webrtc server**: Available at [http://localhost:5000](http://localhost:5000)


### Connecting with the local database from the IDE:
- From the addon database section/icon in the IntelliJ IDE:
   - create a new datasource
   - select postgresql
   - Pass the database credentials for the localhost deployment- Database name, username, password, URL (localhost URL) amnd the respective port number.
   - Test connection
   - Save connection.

