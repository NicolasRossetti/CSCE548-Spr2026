USE bazaar_tracker;

-- Insert test data for items (10 items)
INSERT INTO items (name, category, rarity, npc_sell_price) VALUES
('Enchanted Sugar Cane', 'FARMING', 'UNCOMMON', 3.20),
('Enchanted Cocoa Beans', 'FARMING', 'UNCOMMON', 4.00),
('Enchanted Iron', 'MINING', 'UNCOMMON', 3.50),
('Enchanted Diamond', 'MINING', 'RARE', 5.00),
('Enchanted Rotten Flesh', 'COMBAT', 'UNCOMMON', 2.50),
('Enchanted String', 'COMBAT', 'UNCOMMON', 2.75),
('Enchanted Carrot', 'FARMING', 'UNCOMMON', 3.25),
('Enchanted Potato', 'FARMING', 'UNCOMMON', 3.10),
('Enchanted Pork', 'COMBAT', 'UNCOMMON', 3.80),
('Enchanted Bone', 'COMBAT', 'UNCOMMON', 3.40);

-- Insert test data for price_snapshots (20 snapshots)
INSERT INTO price_snapshots (item_id, buy_price, sell_price, buy_volume, sell_volume, snapshot_time) VALUES
(1, 90.00, 92.50, 1200, 1100, '2026-02-01 08:00:00'),
(1, 91.50, 93.00, 1250, 1125, '2026-02-01 16:00:00'),
(2, 85.00, 88.00, 900, 850, '2026-02-01 08:00:00'),
(2, 86.50, 89.00, 950, 900, '2026-02-01 16:00:00'),
(3, 70.00, 72.50, 1400, 1300, '2026-02-01 08:00:00'),
(3, 71.00, 73.00, 1420, 1320, '2026-02-01 16:00:00'),
(4, 105.00, 108.00, 800, 760, '2026-02-01 08:00:00'),
(4, 106.50, 109.50, 820, 780, '2026-02-01 16:00:00'),
(5, 60.00, 62.00, 1500, 1400, '2026-02-01 08:00:00'),
(5, 61.00, 63.50, 1520, 1410, '2026-02-01 16:00:00'),
(6, 58.00, 60.00, 1550, 1450, '2026-02-01 08:00:00'),
(6, 59.50, 61.00, 1580, 1475, '2026-02-01 16:00:00'),
(7, 75.00, 78.00, 1000, 950, '2026-02-01 08:00:00'),
(7, 76.00, 79.00, 1020, 960, '2026-02-01 16:00:00'),
(8, 68.00, 70.50, 1100, 1020, '2026-02-01 08:00:00'),
(8, 69.00, 71.00, 1120, 1030, '2026-02-01 16:00:00'),
(9, 95.00, 98.00, 900, 860, '2026-02-01 08:00:00'),
(9, 96.50, 99.50, 920, 870, '2026-02-01 16:00:00'),
(10, 80.00, 82.50, 980, 920, '2026-02-01 08:00:00'),
(10, 81.00, 83.00, 1000, 930, '2026-02-01 16:00:00');

-- Insert test data for orders (15 orders)
INSERT INTO orders (item_id, order_type, quantity, target_price, status, created_at) VALUES
(1, 'BUY', 500, 90.00, 'FILLED', '2026-02-01 09:00:00'),
(2, 'BUY', 400, 85.00, 'FILLED', '2026-02-01 09:05:00'),
(3, 'BUY', 600, 70.00, 'PARTIAL', '2026-02-01 09:10:00'),
(4, 'BUY', 300, 105.00, 'FILLED', '2026-02-01 09:15:00'),
(5, 'BUY', 800, 60.00, 'FILLED', '2026-02-01 09:20:00'),
(1, 'SELL', 500, 93.00, 'FILLED', '2026-02-01 14:00:00'),
(2, 'SELL', 400, 89.00, 'FILLED', '2026-02-01 14:05:00'),
(4, 'SELL', 300, 110.00, 'FILLED', '2026-02-01 14:10:00'),
(5, 'SELL', 800, 63.50, 'FILLED', '2026-02-01 14:15:00'),
(6, 'BUY', 350, 58.00, 'OPEN', '2026-02-02 09:00:00'),
(7, 'BUY', 450, 75.00, 'OPEN', '2026-02-02 09:15:00'),
(8, 'BUY', 500, 68.00, 'PARTIAL', '2026-02-02 09:30:00'),
(9, 'BUY', 300, 95.00, 'FILLED', '2026-02-02 09:45:00'),
(10, 'BUY', 250, 80.00, 'FILLED', '2026-02-02 10:00:00'),
(9, 'SELL', 300, 99.50, 'OPEN', '2026-02-02 15:00:00');

-- Insert test data for trades (10 trades)
INSERT INTO trades (order_id, qty_filled, fill_price, fee, profit, trade_time) VALUES
(1, 500, 90.20, 11.28, 0.00, '2026-02-01 09:02:00'),
(2, 400, 85.10, 8.51, 0.00, '2026-02-01 09:07:00'),
(3, 300, 70.50, 5.29, 0.00, '2026-02-01 10:00:00'),
(4, 300, 105.30, 7.89, 0.00, '2026-02-01 09:18:00'),
(5, 800, 60.05, 12.01, 0.00, '2026-02-01 09:22:00'),
(6, 500, 92.80, 11.60, 1250.00, '2026-02-01 14:03:00'),
(7, 400, 88.90, 8.89, 1516.00, '2026-02-01 14:08:00'),
(8, 300, 109.50, 8.21, 1233.00, '2026-02-01 14:13:00'),
(9, 800, 63.40, 12.68, 2656.00, '2026-02-01 14:18:00'),
(13, 300, 95.20, 7.14, 0.00, '2026-02-02 09:48:00');

-- Insert test data for notes (5 notes)
INSERT INTO notes (item_id, note_text, created_at) VALUES
(1, 'Sugar cane prices spike during baking events - buy early and sell during event peak.', '2026-02-01 11:00:00'),
(3, 'Iron tends to dip after mining events. Wait for the dip before buying.', '2026-02-01 11:05:00'),
(4, 'Diamond market is volatile. Set conservative buy orders and be patient.', '2026-02-01 11:10:00'),
(7, 'Carrot margins are stable but small. Good for consistent small profits.', '2026-02-01 11:15:00'),
(9, 'Pork prices usually rebound by midweek. Great weekend investment opportunity.', '2026-02-01 11:20:00');
