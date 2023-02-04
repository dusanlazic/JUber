
# Ubre — Taxi service that will make you go "Ubre!"

**Ubre** is an academic project made for "Advanced Web Technologies" and "Software construction and testing" courses at Faculty of Technical Sciences, University of Novi Sad.
## Authors

- [@pavleglusac](https://github.com/pavleglusac) SW 19/2019
- [@KatarinaKomad](https://github.com/KatarinaKomad) SW 28/2019
- [@dusanlazic](https://github.com/dusanlazic) SW 4/2019


## Installation

Before running the application, **environment variables** must be set!

### Cloning 📦

```bash
$ git clone https://github.com/dusanlazic/JUber.git
$ cd JUber
```

### Running frontend 🆖

```bash
$ cd frontend
$ npm run start
```

Frontend will be served at <http://localhost:4200>

### Running backend 🍃

```
$ cd backend
$ mvn spring-boot:run
```

Backend will be live at <http://localhost:8080>

- h2-console: <http://localhost:8080/h2-console>
  - `jdbc:h2:./data/juber-db`
  - `admin:admin`
- Swagger API docs: <http://localhost:8080/swagger-ui.html>

Environment variables list 🔒

```
TOKEN_SECRET
OAUTH_GOOGLE_CLIENT_ID
OAUTH_GOOGLE_CLIENT_SECRET
OAUTH_FACEBOOK_CLIENT_ID
OAUTH_FACEBOOK_CLIENT_SECRET
ETHERSCAN_API_KEY
CRYPTOCOMPARE_API_KEY
EMAIL_SENDER_ADDRESS
EMAIL_SENDER_PASSWORD
```

## Screenshots (with components highlighted)

Unauthorized user components
![Screenshot](components_images/unauhtorized-user.png)

Passenger home page (read the note!)
![Screenshot](components_images/passenger.png)

Passenger ride

Here, a finished ride is shown. The same page and components are used for the current/future one so there was no need to add two identical images.
![Screenshot](components_images/passenger-finished.png)

Login
![Screenshot](components_images/login.png)

Register, step 1
![Screenshot](components_images/registration-step-1.png)

Register, step 2
![Screenshot](components_images/register-step-2.png)

Register, OAuth
![Screenshot](components_images/register-oauth.png)

Profile balance
(feel free to send some eth)
![Screenshot](components_images/profile-balance.png)

Profile details
![Screenshot](components_images/profile-details.png)

Profile reports
![Screenshot](components_images/profile-reports.png)

Profile password change 
![Screenshot](components_images/profile-password-change.png)

Profile past rides
![Screenshot](components_images/profile-past-rides.png)

Passenger support
![Screenshot](components_images/passenger-support.png)

Admin support
![Screenshot](components_images/admin-support.png)

Admin support
![Screenshot](components_images/admin-driver-registration.png)

The rest of admin's components are just some tables that were already shown before.

