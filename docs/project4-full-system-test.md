# Project 4 Full System Test Guide

This guide executes and documents the Project 4 full-system-test requirement.

## What this proves

- Data layer CRUD is reachable through business and service layers.
- Service layer is reachable on the hosted platform (Railway).
- Client-layer CRUD checks can be shown using the console client test runner script.
- Front-end hosting can be shown separately with the React client screenshots.

## Prerequisites

1. Railway service is deployed and running.
2. Railway service environment variables are configured:
   - BAZAAR_DB_URL
   - BAZAAR_DB_USERNAME
   - BAZAAR_DB_PASSWORD
3. Railway database is initialized with schema:
   - sql/schema.sql
4. Local machine has:
   - PowerShell 7+ (pwsh)
   - Java 17+
   - Maven
   - MySQL client or Railway SQL query access

## Step 1: Point tests to Railway

```powershell
$env:BAZAAR_API_URL = "https://<your-railway-service>.up.railway.app/api"
```

If your URL does not include /api, the script auto-appends it.

## Step 2: Run the full CRUD system test

```powershell
pwsh ./scripts/project4_system_test.ps1
```

Optional: keep test rows for manual review (skip delete phase):

```powershell
pwsh ./scripts/project4_system_test.ps1 -SkipDelete
```

The script prints and saves:

- pass/fail per CRUD step across all entities
- created IDs (item, snapshot, order, trade, note)
- JSON artifact in artifacts/project4-system-test-<timestamp>.json

## Step 3: Capture database-proof screenshots

1. Open sql/project4_verification_queries.sql.
2. Replace the @..._id values with script output IDs.
3. Run each query in your database tool.
4. Capture screenshots for:
   - GET by ID proof
   - GET all proof
   - delete proof (optional but recommended)

## Step 4: Capture client and host screenshots

Capture these for grading evidence:

1. Railway deployment/service status page (service online).
2. Script output terminal showing full CRUD pass.
3. React client running and connected to your service.
4. SQL query outputs showing matching records.

## Suggested screenshot set

- Service running on Railway.
- Create item (script output) + matching DB row.
- Update item (script output) + updated DB row.
- Get all items (client or query output).
- Get item by ID (client or query output).
- Optional delete verification (404 in script + zero-row SQL count).

## Troubleshooting

- 503 with database message: check Railway DB env vars on the service.
- 404 for all endpoints: confirm your base URL includes /api or let script append it.
- Network errors: confirm Railway service is deployed and not sleeping.
