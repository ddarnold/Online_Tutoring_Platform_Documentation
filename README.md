### Prerequisites
- Docker
- Docker Compose
- JDK 21
- Node.js v22.10.0(LTS)
- npm
- psql

### Recommended IDE
- BE: IntelliJ IDEA Ultimate (available with uni account) or Community Edition
- FE: vs Code

## Build the project step-by-step
Make sure you have the correct versions of the prerequisites if one is specified.
Soon, we will have a docker image which will avoid the struggle with versions.

1. Clone GitHub repo (be sure you set up SSH connection keys):
```bash
git clone git@github.com:ddarnold/Online_Tutoring_Platform.git
```
2. Go into irectory:
```bash
cd Online_Tutoring_Platform
```
3. Configure the `.env` File:
```bash
cp .env.example .env 			# step1: Rename it

# step2: Paste .env content into the file (Ask Arnold for it)
```
4. Connect to THU VPN (database access is allowed only from THU IP):
```bash
sudo openconnect vpn.thu.de 	# or any other way you connect to VPN
```

5. Run backend:
```bash
cd backend 						# go into backend root directory
mvn spring-boot:run 			# run using maven

# Or if you have IntelliJ you can open the backend project and run it using Shift + F10
```

6. Run frontend:
```bash
cd ../frontend 					# go into backend root directory
npm install						# Install dependencies
npm start						# Start the development server		

# Or if you have IntelliJ you can open the backend project and run it using Shift + F10
```
5. Voila!
spring is on localhost:8080, try http://localhost:8080/sample for example
react is on localhost:3000

6. If you want to query database directly:
```bash
psql "sslmode=require hostaddr=34.107.78.43 port=5432  user=postgres dbname=test"\
# Enter the pass and use postgres syntax to query
```