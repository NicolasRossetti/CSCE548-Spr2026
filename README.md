# Bazaar Tracker

A full-stack Hypixel Skyblock Bazaar tracking application. The backend is a Spring Boot REST service backed by MySQL. The frontend is a React + Vite single-page application. Both can be run locally or deployed to a cloud platform.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5, Maven |
| Database | MySQL 8.x, JDBC |
| Frontend | React 18, Vite 5, Node.js 18+ |
| Cloud hosting | Railway (backend + DB), Netlify / Vercel (frontend) |

## Architecture

```
React SPA (client/)
    └─▶ REST API  /api/*  (Spring Boot)
            └─▶ Business layer  (BazaarBusinessServiceImpl)
                    └─▶ Data layer  (BazaarDAO → MySQL)
```

## Prerequisites

Install these before you start:

- **Java 17** JDK
- **Maven 3.9+**
- **Node.js 18+** and **npm 9+**
- **MySQL Server 8.x** and a MySQL client (e.g. MySQL Workbench, DBeaver, or the `mysql` CLI)
- **PowerShell 7+** (`pwsh`) — only needed for the automated system test script

## Quick Start (Local)

### 1. Clone the repository

```bash
git clone https://github.com/NicolasRossetti/CSCE548-Spr2026.git
cd CSCE548-Spr2026
```

### 2. Create the database and load sample data

```powershell
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/test_data.sql
```

This creates the `bazaar_tracker` database with five tables (`items`, `price_snapshots`, `orders`, `trades`, `notes`) and loads starter records.

### 3. Set backend environment variables

```powershell
$env:BAZAAR_DB_URL      = "jdbc:mysql://localhost:3306/bazaar_tracker"
$env:BAZAAR_DB_USERNAME = "root"
$env:BAZAAR_DB_PASSWORD = "your_mysql_password"
```

If your MySQL installation requires extra JDBC options:

```powershell
$env:BAZAAR_DB_URL = "jdbc:mysql://localhost:3306/bazaar_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
```

### 4. Build and run the backend

```powershell
mvn clean package
mvn spring-boot:run
```

The service starts at `http://localhost:8080`. Verify it with:

```powershell
Invoke-RestMethod http://localhost:8080/api/items
```

### 5. Run the frontend

Open a second terminal:

```powershell
cd client
npm install
npm run dev
```

The React client starts at `http://localhost:5173`.

Open that URL in your browser, then click **Run All Required GET Calls** to confirm the frontend can reach all five entity endpoints.

## Configuration Reference

### Backend (`application.properties` / environment variables)

| Variable | Default | Description |
|---|---|---|
| `BAZAAR_DB_URL` | `jdbc:mysql://localhost:3306/bazaar_tracker` | JDBC connection string |
| `BAZAAR_DB_USERNAME` | `root` | Database username |
| `BAZAAR_DB_PASSWORD` | `changeme` | Database password |
| `BAZAAR_CORS_ALLOWED_ORIGINS` | `http://localhost:5173` | Comma-separated allowed frontend origins |
| `PORT` | `8080` | HTTP port (auto-set by Railway) |

### Frontend (optional `.env.local` in `client/`)

Copy `client/.env.example` to `client/.env.local` to pre-set the backend target at build time:

```text
VITE_API_BASE_URL=http://localhost:8080
```

The connection panel in the UI also lets you change the target at runtime — enter a port number (e.g. `8080`) for local use or a full URL for a hosted backend.

## API Endpoints

All five entities share the same CRUD pattern:

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/{entity}` | Create a record |
| `GET` | `/api/{entity}` | Get all records |
| `GET` | `/api/{entity}?limit={n}` | Get a subset |
| `GET` | `/api/{entity}/{id}` | Get one record |
| `PUT` | `/api/{entity}/{id}` | Update a record |
| `DELETE` | `/api/{entity}/{id}` | Delete a record |

Entities: `items`, `price-snapshots`, `orders`, `trades`, `notes`

## Verification

### Console CRUD client

Runs a full create → read → update → read → delete flow for every entity and asserts `404` after each delete:

```powershell
mvn exec:java -Dexec.mainClass=com.bazaar.ServiceConsoleClient
```

Point it at a hosted backend with:

```powershell
$env:BAZAAR_API_URL = "https://your-backend.up.railway.app/api"
mvn exec:java -Dexec.mainClass=com.bazaar.ServiceConsoleClient
```

### Automated PowerShell system test

Requires PowerShell 7 (`pwsh`):

```powershell
pwsh ./scripts/project4_system_test.ps1
```

Writes a JSON results artifact to `artifacts/`. Use `-SkipDelete` to leave created rows in the database for manual inspection via `sql/project4_verification_queries.sql`.

## Cloud Deployment

### Backend — Railway

1. Push the repository to GitHub and connect it to a new Railway project.
2. Add a **MySQL** service plugin to the Railway project.
3. Set these environment variables on the Java service:

   | Variable | Value |
   |---|---|
   | `BAZAAR_DB_URL` | `jdbc:mysql://mysql.railway.internal:3306/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
   | `BAZAAR_DB_USERNAME` | *(from Railway MySQL credentials)* |
   | `BAZAAR_DB_PASSWORD` | *(from Railway MySQL credentials)* |
   | `BAZAAR_CORS_ALLOWED_ORIGINS` | your hosted frontend URL + `,http://localhost:5173` |

4. Build command: `mvn clean package`
5. Start command: `java -jar target/bazaar-tracker-1.0.0.jar`
6. After the first deploy, connect to the Railway MySQL service and run `sql/schema.sql` and `sql/test_data.sql` to initialize the database.

### Frontend — Netlify / Vercel

| Setting | Value |
|---|---|
| Root / base directory | `client` |
| Build command | `npm run build` |
| Publish / output directory | `dist` |
| Environment variable | `VITE_API_BASE_URL=https://your-backend.up.railway.app` |

## Project Structure

```
CSCE548-Spr2026/
├── client/                      # React + Vite frontend
│   ├── src/App.jsx              # Single-page application
│   └── .env.example             # Frontend env template
├── sql/
│   ├── schema.sql               # Database + table definitions
│   ├── test_data.sql            # Starter records
│   └── project4_verification_queries.sql
├── scripts/
│   └── project4_system_test.ps1 # End-to-end PowerShell test
├── src/main/java/com/bazaar/
│   ├── api/                     # REST controllers
│   ├── business/                # Business service layer
│   ├── dao/                     # JDBC data access layer
│   └── model/                   # Entity model classes
└── pom.xml                      # Maven build file
```
