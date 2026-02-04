# Hypixel Skyblock Bazaar Tracker

A Java-based database application for tracking bazaar investments in Hypixel Skyblock, including items, price snapshots, orders, trades, and notes.

## Project Structure

```
CSCE548-Spr2026/
├── sql/
│   ├── schema.sql          # Database schema with 5 tables
│   └── test_data.sql       # Test data (50+ rows)
├── src/main/java/com/bazaar/
│   ├── model/              # Data model classes
│   │   ├── Item.java
│   │   ├── PriceSnapshot.java
│   │   ├── Order.java
│   │   ├── Trade.java
│   │   └── Note.java
│   ├── dao/
│   │   └── BazaarDAO.java  # Data Access Object with CRUD operations
│   └── BazaarConsoleApp.java  # Console application
├── pom.xml                 # Maven build configuration
└── README.md
```

## Database Schema

The application uses 5 MySQL tables:

1. **items** - Bazaar items (name, category, rarity, npc_sell_price)
2. **price_snapshots** - Price history (buy/sell prices, volumes, timestamp)
3. **orders** - Buy/sell orders (type, quantity, target price, status)
4. **trades** - Executed trades (filled quantity, price, fees, profit)
5. **notes** - Item notes and strategies

### Foreign Key Relationships
- `price_snapshots.item_id` → `items.item_id`
- `orders.item_id` → `items.item_id`
- `trades.order_id` → `orders.order_id`
- `notes.item_id` → `items.item_id`

## Prerequisites

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **MySQL Server 8.0 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - OR install via package manager

## Setup Instructions

### 1. Install and Configure MySQL

**Windows:**
```powershell
# Download MySQL installer from https://dev.mysql.com/downloads/installer/
# Run installer and set root password during setup
# Start MySQL service
net start MySQL80
```

**Alternative - Using Chocolatey:**
```powershell
choco install mysql
```

### 2. Create Database and Load Data

Open MySQL command line or MySQL Workbench:

```bash
# Login to MySQL
mysql -u root -p

# Run the schema script (creates database and tables)
source C:/Users/mrlav/Desktop/CSCE548/Project 1/CSCE548-Spr2026/sql/schema.sql

# Run the test data script
source C:/Users/mrlav/Desktop/CSCE548/Project 1/CSCE548-Spr2026/sql/test_data.sql

# Verify data loaded
USE bazaar_tracker;
SELECT COUNT(*) FROM items;
SELECT COUNT(*) FROM price_snapshots;
SELECT COUNT(*) FROM orders;
SELECT COUNT(*) FROM trades;
SELECT COUNT(*) FROM notes;
```

**Alternative - Using PowerShell:**
```powershell
cd "C:\Users\mrlav\Desktop\CSCE548\Project 1\CSCE548-Spr2026"

# Create database and tables
Get-Content sql\schema.sql | mysql -u root -p

# Load test data
Get-Content sql\test_data.sql | mysql -u root -p
```

### 3. Configure Database Connection

Edit [src/main/java/com/bazaar/BazaarConsoleApp.java](src/main/java/com/bazaar/BazaarConsoleApp.java) (lines 19-21):

```java
String url = "jdbc:mysql://localhost:3306/bazaar_tracker";
String username = "root";        // Change to your MySQL username
String password = "your_password"; // Change to your MySQL password
```

### 4. Build the Project

```powershell
cd "C:\Users\mrlav\Desktop\CSCE548\Project 1\CSCE548-Spr2026"

# Clean and build
mvn clean package

# This will:
# - Compile all Java classes
# - Download MySQL Connector/J dependency
# - Create executable JAR in target/ directory
```

### 5. Run the Application

**Option A - Using Maven:**
```powershell
mvn exec:java
```

**Option B - Using JAR file:**
```powershell
java -jar target/bazaar-tracker-1.0.0.jar
```

## Application Features

The console application provides:

1. **View All Items** - Display all bazaar items in the database
2. **View All Price Snapshots** - Show price history data
3. **View All Orders** - List all buy/sell orders
4. **View All Trades** - Display executed trades
5. **View All Notes** - Show item notes and strategies
6. **Create New Item** - Add a new bazaar item
7. **Create New Order** - Place a new buy/sell order
8. **Update Order Status** - Change order status (OPEN/PARTIAL/FILLED/CANCELED)
9. **Delete Item** - Remove an item (cascades to related records)
10. **View Profit Summary** - Calculate total profits, fees, and statistics

## CRUD Operations

The `BazaarDAO` class provides complete CRUD operations for all 5 tables:

- **Create**: `createItem()`, `createPriceSnapshot()`, `createOrder()`, `createTrade()`, `createNote()`
- **Read**: `readItem(id)`, `readAllItems()`, etc. for each table
- **Update**: `updateItem()`, `updatePriceSnapshot()`, `updateOrder()`, `updateTrade()`, `updateNote()`
- **Delete**: `deleteItem(id)`, `deletePriceSnapshot(id)`, `deleteOrder(id)`, `deleteTrade(id)`, `deleteNote(id)`

## Test Data Summary

The test data includes:
- **10 items** (various Hypixel Skyblock enchanted materials)
- **20 price snapshots** (2 per item at different times)
- **15 orders** (mix of BUY/SELL with various statuses)
- **10 trades** (executed trade records with profits)
- **5 notes** (investment strategies and observations)

**Total: 60 rows across all tables** (exceeds 50 row requirement)

## Verifying Database Diagram

To generate a database diagram in MySQL Workbench:

1. Open MySQL Workbench
2. Connect to your MySQL server
3. Go to **Database** → **Reverse Engineer**
4. Select the `bazaar_tracker` database
5. View the ER diagram showing all tables and foreign key relationships

## Troubleshooting

**Connection Error:**
- Verify MySQL service is running: `net start MySQL80`
- Check username/password in BazaarConsoleApp.java
- Confirm database exists: `SHOW DATABASES;`

**Build Error:**
- Ensure Maven is installed: `mvn -version`
- Check Java version: `java -version` (must be 11+)
- Delete `target/` folder and rebuild

**SQL Script Error:**
- Ensure paths use forward slashes or escaped backslashes
- Verify you're in the correct directory
- Check MySQL user has CREATE/INSERT privileges

## Technologies Used

- **Java 11** - Programming language
- **MySQL 8.0** - Relational database
- **JDBC** - Database connectivity
- **Maven** - Build and dependency management
- **MySQL Connector/J 8.3.0** - JDBC driver

## Author

Nicolas Rossetti - CSCE548 Spring 2026
