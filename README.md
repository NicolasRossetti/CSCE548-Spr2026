# Bazaar Tracker

This project now includes:
- A **data layer** (`BazaarDAO`) from Project 1
- A **business layer** (`BazaarBusinessService`, `BazaarBusinessServiceImpl`)
- A **service layer** (REST endpoints for all entities)
- A **console front end** (`ServiceConsoleClient`) that invokes the services

All CRUD operations in the data layer are exposed through the business layer and then through the REST service layer.

## Architecture

1. **Data layer**: direct JDBC CRUD in `BazaarDAO`
2. **Business layer**: wraps DAO calls and centralizes exception handling
3. **Service layer**: REST APIs under `/api/*`
4. **Console client**: performs create → read → update → read → delete flow via HTTP for all entities

## Service Endpoints

Each entity has full CRUD endpoints:

- `items`
   - `POST /api/items`
   - `GET /api/items/{id}`
   - `GET /api/items`
   - `GET /api/items?limit={n}` (subset)
   - `PUT /api/items/{id}`
   - `DELETE /api/items/{id}`

- `price-snapshots`
   - `POST /api/price-snapshots`
   - `GET /api/price-snapshots/{id}`
   - `GET /api/price-snapshots`
   - `GET /api/price-snapshots?limit={n}` (subset)
   - `PUT /api/price-snapshots/{id}`
   - `DELETE /api/price-snapshots/{id}`

- `orders`
   - `POST /api/orders`
   - `GET /api/orders/{id}`
   - `GET /api/orders`
   - `GET /api/orders?limit={n}` (subset)
   - `PUT /api/orders/{id}`
   - `DELETE /api/orders/{id}`

- `trades`
   - `POST /api/trades`
   - `GET /api/trades/{id}`
   - `GET /api/trades`
   - `GET /api/trades?limit={n}` (subset)
   - `PUT /api/trades/{id}`
   - `DELETE /api/trades/{id}`

- `notes`
   - `POST /api/notes`
   - `GET /api/notes/{id}`
   - `GET /api/notes`
   - `GET /api/notes?limit={n}` (subset)
   - `PUT /api/notes/{id}`
   - `DELETE /api/notes/{id}`

## Local Setup

### 1) Database

```powershell
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/test_data.sql
```

### 2) Configure environment variables

```powershell
$env:BAZAAR_DB_URL="jdbc:mysql://localhost:3306/bazaar_tracker"
$env:BAZAAR_DB_USERNAME="root"
$env:BAZAAR_DB_PASSWORD="your_password"
```

### 3) Build and run service

```powershell
mvn clean package
mvn spring-boot:run
```

Service base URL (local): `http://localhost:8080`

### 4) Run the web client (React)

From a second terminal:

```powershell
cd client
npm install
npm run dev
```

Web client URL (local): `http://localhost:5173`

The page includes 5 sections/tabs (Items, Price Snapshots, Orders, Trades, Notes) and supports:
- `GET all`
- `GET by id`
- `GET subset` via `?limit=`

Use **Run All Required GET Calls** in the UI to automatically execute all GET calls for all tables.

## Console Front End Test

Run the console client while the service is running:

```powershell
mvn exec:java -Dexec.mainClass=com.bazaar.ServiceConsoleClient
```

This executes complete CRUD workflows against the API for:
1. Item
2. Price Snapshot
3. Order
4. Trade
5. Note

For each entity, it performs create, get, update, get, delete, and then verifies `404` after delete.

## Console-Based Front End

The console front end is implemented in:
- `src/main/java/com/bazaar/ServiceConsoleClient.java`

This program invokes the service layer over HTTP and performs a complete verification flow across all services:
1. Insert object
2. Get object
3. Update object
4. Get updated object
5. Delete object
6. Get after delete to verify `404`

Run it with:

```powershell
mvn exec:java -Dexec.mainClass=com.bazaar.ServiceConsoleClient
```

If your service is hosted on Railway, set:

```powershell
$env:BAZAAR_API_URL="https://your-railway-url"
```

## Railway Hosting

Platform selected: **Railway**

1. Push project to GitHub.
2. Create a new Railway project from that repo.
3. Set environment variables in Railway:
    - `BAZAAR_DB_URL`
    - `BAZAAR_DB_USERNAME`
    - `BAZAAR_DB_PASSWORD`
4. Build command: `mvn clean package`
5. Start command: `java -jar target/bazaar-tracker-1.0.0.jar`
6. Use generated Railway URL as service host.

Hosting comments are also embedded in:
- `BazaarServiceApplication.java`
- `ServiceConsoleClient.java`

## Testing and Screenshot Checklist

Capture screenshots for the following:

1. Spring service startup logs (`mvn spring-boot:run`)
2. React client running at `http://localhost:5173`
3. Click on **Run All Required GET Calls** and capture the **Run-All Execution Log** showing:
   - Items: `GET /api/items`, `GET /api/items/{id}`, `GET /api/items?limit=2`
   - Price Snapshots: `GET /api/price-snapshots`, `GET /api/price-snapshots/{id}`, `GET /api/price-snapshots?limit=2`
   - Orders: `GET /api/orders`, `GET /api/orders/{id}`, `GET /api/orders?limit=2`
   - Trades: `GET /api/trades`, `GET /api/trades/{id}`, `GET /api/trades?limit=2`
   - Notes: `GET /api/notes`, `GET /api/notes/{id}`, `GET /api/notes?limit=2`
4. At least one screenshot per tab showing returned JSON data in the output panel.

## Notes

- Existing Project 1 DAO and model classes were reused.
- The older console app (`BazaarConsoleApp`) remains in the project.
- The React website client in `client/` provides a simple interface for validating service-layer GET functionality.
