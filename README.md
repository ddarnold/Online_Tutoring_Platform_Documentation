### Prerequisites
- Docker
- Docker Compose
Backend:
1. JDK 21 from https://www.oracle.com/java/technologies/downloads/  (current LTS)
2. IntelliJ IDEA Ultimate (available with uni account) or Community Edition {my recommendation}
Frontend:
1. Node.js v22.10.0(LTS) https://nodejs.org/en/download/package-manager
2. npm
3. vsCode

#### Dependencies included - spring:
- Lombok (quite handy)
- Spring Data JPA
- Spring Boot Dev Tools
- Spring Web

#### Dependencies react:
- axios (not included, my recommendation)
- bootstrap (not included, my recommendation)


## Build the project step-by-step
Disclaimer: Database will persist only locally. Dump policy is still up to come.
1. Clone GitHub repo (be sure you set up SSH connection keys):
```bash
git clone git@github.com:ddarnold/Online_Tutoring_Platform.git
```
2. Change directory:
```bash
cd Online_Tutoring_Platform
```
3. Configure the `.env` File:
```bash
cp .env.example .env # Rename it
# step2: Populate the .env file with our values
```
4. Build a docker container:
```bash
# step by step
docker-compose rm -f # Remove any existing services
docker-compose pull # Pull any updates from available images
docker-compose up --build -d # Build and Start Services

# or as a single liner
docker compose up --build -d --force-recreate --remove-orphans
```
5. Voila!
	postgres is on localhost:5432
	pgadmin is on localhost:5050
	sprint is on localhost:8080
	react is on localhost:3000


7. Optional: At the end create database dump:
```bash
docker exec -t <your-postgres-container-id> pg_dumpall -c -U developer > dump_`date +%d-%m-%Y"_"%H_%M_%S`.sql
```
7. if you wanna gracefully stop services:
```bash
docker-compose stop -t 1
```
