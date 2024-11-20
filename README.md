# Note 20.11.2024

**In order to use the local Docker setup:**

1. Uncomment all lines in the file `/backend/src/main/resousrces/application.yml`
2. Run the docker container using:

```bash
docker compose up --force-recreate --build --detach
```

Now you habe a local postgresql server, which will persist in `/database/data` folder.

**In order to use the CloudSQL database:**

1. Comment all lines in the file `/backend/src/main/resousrces/application.yml`
2. Connect to the uni VPN
3. Make sure you have the `/backend/.env` with CloudSQL credentials
4. If you want to query CloudSQL database:

```bash
psql "sslmode=require hostaddr=34.107.78.43 port=5432  user=postgres dbname=test"
# Enter the pass and use postgres syntax to query
```

## Build

Make sure you have the correct versions of the prerequisites if one is specified.

**Prerequisites**

- JDK 21
- Node.js v20.x(LTS)
- npm
- Docker

**Recommended IDE**

- backend: IntelliJ IDEA Ultimate (available with the uni account) or Community Edition
- frontend: Visual Studio Code

## Steps

1. Clone GitHub repository (be sure you set up SSH connection keys):

```bash
git clone git@github.com:ddarnold/Online_Tutoring_Platform.git
```

2. Go into directory:

```bash
cd Online_Tutoring_Platform
```

3. Configure the `.env` File:

```bash
cd backend # go into backend root directory
# step2: Paste .env content into the file (Ask Arnold for it)
```

4. Connect to THU VPN (database access is allowed only from THU IP):
   Might not be needed in the future

```bash
sudo openconnect vpn.thu.de # or any other way you connect to VPN
```

5. Set up Application Default Credentials for Cloud SQL
   [Configure ADC with your Google Account](https://cloud.google.com/docs/authentication/provide-credentials-adc#google-idp)

6. Send an email to [arnodo01@thu.de](arnodo01@thu.de) from Google account you used in the previous step so I can verify you

7. Wait for the verification confirmation from Arnold

8. Run backend:

```bash
mvn spring-boot:run # run using maven

# Or if you have IntelliJ you can open the backend project and run it using Shift + F10
```

9. Run frontend:

```bash
cd ../frontend # go into backend root directory
npm install # Install dependencies
npm start # Start the development server
```

10. Voila!

Spring is on localhost:8080, try <http://localhost:8080/actuator/health>

React is on localhost:3000
