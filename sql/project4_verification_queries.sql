USE bazaar_tracker;

-- Replace these IDs with values from scripts/project4_system_test.ps1 output.
SET @item_id = 0;
SET @snapshot_id = 0;
SET @order_id = 0;
SET @trade_id = 0;
SET @note_id = 0;

-- -----------------------------------------------------------------
-- PROOF FOR "GET BY ID" (after create/update)
-- Capture screenshots of each query result.
-- -----------------------------------------------------------------
SELECT * FROM items WHERE item_id = @item_id;
SELECT * FROM price_snapshots WHERE snapshot_id = @snapshot_id;
SELECT * FROM orders WHERE order_id = @order_id;
SELECT * FROM trades WHERE trade_id = @trade_id;
SELECT * FROM notes WHERE note_id = @note_id;

-- -----------------------------------------------------------------
-- PROOF FOR "GET ALL"
-- Capture screenshots that show multiple rows in each table.
-- -----------------------------------------------------------------
SELECT * FROM items ORDER BY item_id DESC LIMIT 10;
SELECT * FROM price_snapshots ORDER BY snapshot_id DESC LIMIT 10;
SELECT * FROM orders ORDER BY order_id DESC LIMIT 10;
SELECT * FROM trades ORDER BY trade_id DESC LIMIT 10;
SELECT * FROM notes ORDER BY note_id DESC LIMIT 10;

-- -----------------------------------------------------------------
-- OPTIONAL PROOF FOR DELETE (encouraged by assignment)
-- Run after delete steps. Each count should return 0.
-- -----------------------------------------------------------------
SELECT COUNT(*) AS item_rows_after_delete FROM items WHERE item_id = @item_id;
SELECT COUNT(*) AS snapshot_rows_after_delete FROM price_snapshots WHERE snapshot_id = @snapshot_id;
SELECT COUNT(*) AS order_rows_after_delete FROM orders WHERE order_id = @order_id;
SELECT COUNT(*) AS trade_rows_after_delete FROM trades WHERE trade_id = @trade_id;
SELECT COUNT(*) AS note_rows_after_delete FROM notes WHERE note_id = @note_id;

-- -----------------------------------------------------------------
-- QUICK LOOKUP FOR MOST RECENT SYSTEM-TEST ITEM
-- Useful if IDs are not copied yet.
-- -----------------------------------------------------------------
SELECT *
FROM items
WHERE name LIKE 'P4_TEST_ITEM_%'
ORDER BY item_id DESC
LIMIT 5;
