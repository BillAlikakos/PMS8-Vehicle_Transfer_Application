# PMS8-Vehicle_Transfer_Application
A simple application that processes forms regarding the transferral of the ownership of privately owned vehicles.<br>
It can be deployed to a microk8s cluster, or run on a local machine with the usage of docker compose.

## Architecture

The application consists of the following components:

- Front-end application.
- Back-end application, implementing the main functionality.
- Database for the main application.
- Keycloak, handling the authentication and authorization of the users.
- Keycloak Database.
- User management application, essentially a keycloak wrapper application that exposes the required functionality to the front end application via REST services.
- Notification service that sends the corresponding e-mail notification upon the completion of the flow.

Sample configuration & swagger files can be found under the correspoding directories.
