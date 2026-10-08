# Version Tree (VT)

Version Tree is a mini-GitHub-style collaboration and version-management platform for an Advanced Java Programming project. Users can create repositories, upload files, view file versions, comment, star repositories, follow developers, search projects, and manage users through an admin panel.

## Technology Stack

- Java 17+
- Spring Boot 2.7.18
- Spring MVC and Java Servlets
- JSP, JSTL, HTML, CSS, and JavaScript
- Hibernate/JPA
- PostgreSQL
- Maven

## Database Setup

The application expects a PostgreSQL database named `MiniGithub`.

1. Install and start PostgreSQL.
2. Ensure the PostgreSQL user is `postgres` with password `postgres`.
3. Create the database if it does not already exist:

```sql
CREATE DATABASE "MiniGithub";
```

4. Apply the schema and seed data from the project directory:

```powershell
& 'C:\Program Files\PostgreSQL\16\bin\psql.exe' -U postgres -h localhost -p 5432 -d MiniGithub -f schema.sql
```

The script creates all tables and inserts sample users, repositories, files, versions, stars, follows, comments, and activities. It recreates the tables, so use it only when a database reset is intended.

The database configuration is in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/MiniGithub
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Running the Application

### First-time Maven setup on Windows

If Maven is not already installed, download the binary ZIP from the Apache Maven website. The following example assumes the ZIP is saved at `C:\Users\student\Downloads\apache-maven-3.10.0-bin.zip`:

```powershell
Expand-Archive -Path "$env:USERPROFILE\Downloads\apache-maven-3.10.0-bin.zip" -DestinationPath "$env:USERPROFILE" -Force
$env:MAVEN_HOME = "$env:USERPROFILE\apache-maven-3.10.0"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"
mvn -version
```

To make Maven available in new terminals, add `$env:MAVEN_HOME\bin` to the Windows user `Path`, or reopen a terminal after configuring it in System Properties.

### Build and run with Maven

From the project directory:

```powershell
cd "E:\College Codes\Mini Project\MiniGithub"
mvn clean package -DskipTests
java -jar target\mini-github-1.0.0.jar
```

The application starts on port `8082`. Open http://localhost:8082/ after startup.

If port `8082` is already in use, choose another port:

```powershell
java -jar target\mini-github-1.0.0.jar --server.port=8084
```

Then open http://localhost:8084/.

### Using the Existing JAR

If Maven is not installed, run the packaged artifact directly:

```powershell
java -jar target\mini-github-1.0.0.jar
```

Open the application at http://localhost:8082/ or the login page at http://localhost:8082/login.

The application uses `spring.jpa.hibernate.ddl-auto=validate`, so the PostgreSQL schema must exist before startup.

## Test Accounts

| Role | Username | Password |
|---|---|---|
| Administrator | `admin` | `admin123` |
| Developer | `alex_dev` | `password123` |
| Developer | `sarah_coder` | `password123` |

These accounts are provided by `schema.sql` for local development and testing only.

## Seeded Content

The seed includes:

- 3 users
- 3 repositories
- 2 files
- 3 file versions
- Stars, follows, comments, and activity records

Uploaded sample files are stored in `vt_storage/`.

## Uploading a Project Folder

Repository owners upload a project as a ZIP file from the repository page. The application preserves paths such as `src/main/java/App.java` and creates or updates each file version automatically.

- ZIP upload limit: 50 MB
- Individual file limit: 10 MB
- Only the repository owner can upload files
- Unsafe archive paths such as `../file.txt` are rejected

## Project Structure

```text
src/main/java/com/versiontree/
  controller/   Spring MVC controllers
  dao/          Database access objects
  model/        JPA entity models
  service/      Application services
  servlet/      Servlet upload and legacy demo components

src/main/resources/
  application.properties

src/main/webapp/WEB-INF/jsp/
  JSP views

schema.sql      PostgreSQL schema and seed data
vt_storage/     Local uploaded file storage
target/         Maven build output
```

## Troubleshooting

- `Connection refused` on port `5432`: start PostgreSQL and confirm it is listening on port `5432`.
- `database "MiniGithub" does not exist`: create the database, then apply `schema.sql`.
- Schema validation errors: apply `schema.sql` to the same database configured in `application.properties`.
- Port `8082` already in use: start with `--server.port=8083`, for example:

```powershell
java -jar target\mini-github-1.0.0.jar --server.port=8083
```
