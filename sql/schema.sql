-- Create database
CREATE DATABASE IF NOT EXISTS bazaar_tracker;
USE bazaar_tracker;

-- Items table
CREATE TABLE items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    rarity VARCHAR(20) NOT NULL,
    npc_sell_price DECIMAL(10,2) NOT NULL
) ENGINE=InnoDB;

-- PriceSnapshots table
CREATE TABLE price_snapshots (
    snapshot_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    buy_price DECIMAL(10,2) NOT NULL,
    sell_price DECIMAL(10,2) NOT NULL,
    buy_volume INT NOT NULL,
    sell_volume INT NOT NULL,
    snapshot_time DATETIME NOT NULL,
    CONSTRAINT fk_price_snapshots_item
        FOREIGN KEY (item_id) REFERENCES items(item_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    order_type ENUM('BUY', 'SELL') NOT NULL,
    quantity INT NOT NULL,
    target_price DECIMAL(10,2) NOT NULL,
    status ENUM('OPEN', 'PARTIAL', 'FILLED', 'CANCELED') NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_orders_item
        FOREIGN KEY (item_id) REFERENCES items(item_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Trades table
CREATE TABLE trades (
    trade_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    qty_filled INT NOT NULL,
    fill_price DECIMAL(10,2) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    profit DECIMAL(10,2) NOT NULL,
    trade_time DATETIME NOT NULL,
    CONSTRAINT fk_trades_order
        FOREIGN KEY (order_id) REFERENCES orders(order_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Notes table
CREATE TABLE notes (
    note_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    note_text TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_notes_item
        FOREIGN KEY (item_id) REFERENCES items(item_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;
