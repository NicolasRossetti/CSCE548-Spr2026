package com.bazaar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BazaarServiceApplication {

    /*
     * ========================================
     * RAILWAY PLATFORM DEPLOYMENT INSTRUCTIONS
     * ========================================
     *
     * Step 1: Push to GitHub
     *    - Ensure all code is committed and pushed to GitHub
     *    - Repository should be at: https://github.com/YOUR_USERNAME/CSCE548-Spr2026
     *
     * Step 2: Connect to Railway
     *    - Go to https://railway.app
     *    - Click "New Project" → "Deploy from GitHub repo"
     *    - Select CSCE548-Spr2026 repository
     *    - Railway auto-detects Maven/Java project and starts build
     *
     * Step 3: Add MySQL Database Service
     *    - In Railway project, click "+ Create" → "MySQL"
     *    - Railway generates MYSQL_HOST, MYSQL_PORT, MYSQL_USER, MYSQL_PASSWORD
     *
     * Step 4: Configure Environment Variables (Java Service)
     *    - In Java service, go to "Variables" tab
     *    - Add these variables:
     *      BAZAAR_DB_URL = jdbc:mysql://mysql.railway.internal:3306/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
     *      BAZAAR_DB_USERNAME = root
     *      BAZAAR_DB_PASSWORD = [copy from MySQL service's MYSQL_PASSWORD]
     *    - IMPORTANT: Use mysql.railway.internal (internal host) for Java app
     *                 This only works within Railway services
     *
     * Step 5: Initialize Database Schema
     *    - From local machine, connect to Railway MySQL using public host:
     *      mysql -h [MYSQL_HOST] -P [MYSQL_PORT] -u root -p
     *    - Run: source sql/schema.sql;
     *    - Verify: SHOW TABLES;
     *      (Should see: items, price_snapshots, orders, trades, notes)
     *
     * Step 6: Monitor Build & Deployment
     *    - Watch "Deployments" tab for build progress
     *    - Wait for "Started BazaarServiceApplication" in logs
     *    - Copy public URL from Java service (top right)
     *
     * Step 7: Test the Service
     *    - Local: mvn -q exec:java -Dexec.mainClass=com.bazaar.ServiceConsoleClient
     *    - Remote: Set $env:BAZAAR_API_URL="https://YOUR-RAILWAY-URL/api"
     *            Then run console test above
     *    - Expected: All 5 entity CRUD operations succeed (200 responses)
     *
     * Step 8: API Endpoints (Available after deployment)
     *    - Items:          https://YOUR-RAILWAY-URL/api/items
     *    - PriceSnapshots: https://YOUR-RAILWAY-URL/api/price-snapshots
     *    - Orders:         https://YOUR-RAILWAY-URL/api/orders
     *    - Trades:         https://YOUR-RAILWAY-URL/api/trades
     *    - Notes:          https://YOUR-RAILWAY-URL/api/notes
     *    - Each endpoint supports: GET (all), POST (create), GET/:id, PUT (update), DELETE
     *
     * Troubleshooting:
     *    - 500 Error: Check Railway logs for SQL syntax errors
     *    - Connection refused: Verify BAZAAR_DB_URL uses mysql.railway.internal
     *    - Table doesn't exist: Run sql/schema.sql against Railway MySQL
     */
    public static void main(String[] args) {
        SpringApplication.run(BazaarServiceApplication.class, args);
    }
}
