## NOTE 10.11.2024
In order to fix the connection for the local development, please uncomment the lines for hikari connection in backend/src/main/resources/application.yml


### Prerequisites
- JDK 21
- Node.js v20.x(LTS)
- npm
- psql (handy tool)

### Recommended IDE
- backend: IntelliJ IDEA Ultimate (available with the uni account) or Community Edition
- frontend: vsCode

## Build the project step-by-step
Make sure you have the correct versions of the prerequisites if one is specified.
Soon, we will have a docker image which will avoid the struggle with versions.

1. Clone GitHub repo (be sure you set up SSH connection keys):
```bash
git clone git@github.com:ddarnold/Online_Tutoring_Platform.git
```
2. Go into directory:
```bash
cd Online_Tutoring_Platform
```
3. Configure the `.env` File:
```bash
cd backend 						# go into backend root directory
# step2: Paste .env content into the file (Ask Arnold for it)
```
4. Connect to THU VPN (database access is allowed only from THU IP):
Might not be needed in the future
```bash
sudo openconnect vpn.thu.de 	# or any other way you connect to VPN
```

5. Set up Application Default Credentials for Cloud SQL
https://cloud.google.com/docs/authentication/provide-credentials-adc#google-idp

6. Send an email to arnodo01@thu.de from Google account you used in the previous step so I can verify you

7. Wait for the verification confirmation from Arnold

8. Run backend:
```bash
mvn spring-boot:run 			# run using maven

# Or if you have IntelliJ you can open the backend project and run it using Shift + F10
```
9. Run frontend:
```bash
cd ../frontend 					# go into backend root directory
npm install						# Install dependencies
npm start						# Start the development server		
```
10. Voila!
spring is on localhost:8080, try http://localhost:8080/sample for example
react is on localhost:3000

11. If you want to query database directly:
```bash
psql "sslmode=require hostaddr=34.107.78.43 port=5432  user=postgres dbname=test"\
# Enter the pass and use postgres syntax to query
```