package com.bazaar.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.bazaar.model.Item;
import com.bazaar.model.Note;
import com.bazaar.model.Order;
import com.bazaar.model.PriceSnapshot;
import com.bazaar.model.Trade;

public class BazaarDAO {
    private final String url;
    private final String username;
    private final String password;

    public BazaarDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // ==================== ITEM CRUD OPERATIONS ====================

    public int createItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (name, category, rarity, npc_sell_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setString(3, item.getRarity());
            stmt.setDouble(4, item.getNpcSellPrice());
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Item readItem(int itemId) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }
        return null;
    }

    public List<Item> readAllItems() throws SQLException {
        String sql = "SELECT * FROM items";
        List<Item> items = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(mapItem(rs));
            }
        }
        return items;
    }

    public List<Item> readAllItemsLimited(int limit) throws SQLException {
        String sql = "SELECT * FROM items LIMIT ?";
        List<Item> items = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapItem(rs));
                }
            }
        }
        return items;
    }

    public boolean updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET name = ?, category = ?, rarity = ?, npc_sell_price = ? WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setString(3, item.getRarity());
            stmt.setDouble(4, item.getNpcSellPrice());
            stmt.setInt(5, item.getItemId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ==================== PRICE SNAPSHOT CRUD OPERATIONS ====================

    public int createPriceSnapshot(PriceSnapshot snapshot) throws SQLException {
        String sql = "INSERT INTO price_snapshots (item_id, buy_price, sell_price, buy_volume, sell_volume, snapshot_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, snapshot.getItemId());
            stmt.setDouble(2, snapshot.getBuyPrice());
            stmt.setDouble(3, snapshot.getSellPrice());
            stmt.setInt(4, snapshot.getBuyVolume());
            stmt.setInt(5, snapshot.getSellVolume());
            stmt.setTimestamp(6, Timestamp.valueOf(snapshot.getSnapshotTime()));
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public PriceSnapshot readPriceSnapshot(int snapshotId) throws SQLException {
        String sql = "SELECT * FROM price_snapshots WHERE snapshot_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, snapshotId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPriceSnapshot(rs);
                }
            }
        }
        return null;
    }

    public List<PriceSnapshot> readAllPriceSnapshots() throws SQLException {
        String sql = "SELECT * FROM price_snapshots";
        List<PriceSnapshot> snapshots = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                snapshots.add(mapPriceSnapshot(rs));
            }
        }
        return snapshots;
    }

    public List<PriceSnapshot> readAllPriceSnapshotsLimited(int limit) throws SQLException {
        String sql = "SELECT * FROM price_snapshots LIMIT ?";
        List<PriceSnapshot> snapshots = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    snapshots.add(mapPriceSnapshot(rs));
                }
            }
        }
        return snapshots;
    }

    public boolean updatePriceSnapshot(PriceSnapshot snapshot) throws SQLException {
        String sql = "UPDATE price_snapshots SET item_id = ?, buy_price = ?, sell_price = ?, buy_volume = ?, sell_volume = ?, snapshot_time = ? WHERE snapshot_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, snapshot.getItemId());
            stmt.setDouble(2, snapshot.getBuyPrice());
            stmt.setDouble(3, snapshot.getSellPrice());
            stmt.setInt(4, snapshot.getBuyVolume());
            stmt.setInt(5, snapshot.getSellVolume());
            stmt.setTimestamp(6, Timestamp.valueOf(snapshot.getSnapshotTime()));
            stmt.setInt(7, snapshot.getSnapshotId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePriceSnapshot(int snapshotId) throws SQLException {
        String sql = "DELETE FROM price_snapshots WHERE snapshot_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, snapshotId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ==================== ORDER CRUD OPERATIONS ====================

    public int createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (item_id, order_type, quantity, target_price, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getItemId());
            stmt.setString(2, order.getOrderType());
            stmt.setInt(3, order.getQuantity());
            stmt.setDouble(4, order.getTargetPrice());
            stmt.setString(5, order.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Order readOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        }
        return null;
    }

    public List<Order> readAllOrders() throws SQLException {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        }
        return orders;
    }

    public List<Order> readAllOrdersLimited(int limit) throws SQLException {
        String sql = "SELECT * FROM orders LIMIT ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    public boolean updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET item_id = ?, order_type = ?, quantity = ?, target_price = ?, status = ?, created_at = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getItemId());
            stmt.setString(2, order.getOrderType());
            stmt.setInt(3, order.getQuantity());
            stmt.setDouble(4, order.getTargetPrice());
            stmt.setString(5, order.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
            stmt.setInt(7, order.getOrderId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ==================== TRADE CRUD OPERATIONS ====================

    public int createTrade(Trade trade) throws SQLException {
        String sql = "INSERT INTO trades (order_id, qty_filled, fill_price, fee, profit, trade_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, trade.getOrderId());
            stmt.setInt(2, trade.getQtyFilled());
            stmt.setDouble(3, trade.getFillPrice());
            stmt.setDouble(4, trade.getFee());
            stmt.setDouble(5, trade.getProfit());
            stmt.setTimestamp(6, Timestamp.valueOf(trade.getTradeTime()));
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Trade readTrade(int tradeId) throws SQLException {
        String sql = "SELECT * FROM trades WHERE trade_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tradeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTrade(rs);
                }
            }
        }
        return null;
    }

    public List<Trade> readAllTrades() throws SQLException {
        String sql = "SELECT * FROM trades";
        List<Trade> trades = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trades.add(mapTrade(rs));
            }
        }
        return trades;
    }

    public List<Trade> readAllTradesLimited(int limit) throws SQLException {
        String sql = "SELECT * FROM trades LIMIT ?";
        List<Trade> trades = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trades.add(mapTrade(rs));
                }
            }
        }
        return trades;
    }

    public boolean updateTrade(Trade trade) throws SQLException {
        String sql = "UPDATE trades SET order_id = ?, qty_filled = ?, fill_price = ?, fee = ?, profit = ?, trade_time = ? WHERE trade_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trade.getOrderId());
            stmt.setInt(2, trade.getQtyFilled());
            stmt.setDouble(3, trade.getFillPrice());
            stmt.setDouble(4, trade.getFee());
            stmt.setDouble(5, trade.getProfit());
            stmt.setTimestamp(6, Timestamp.valueOf(trade.getTradeTime()));
            stmt.setInt(7, trade.getTradeId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteTrade(int tradeId) throws SQLException {
        String sql = "DELETE FROM trades WHERE trade_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tradeId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ==================== NOTE CRUD OPERATIONS ====================

    public int createNote(Note note) throws SQLException {
        String sql = "INSERT INTO notes (item_id, note_text, created_at) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, note.getItemId());
            stmt.setString(2, note.getNoteText());
            stmt.setTimestamp(3, Timestamp.valueOf(note.getCreatedAt()));
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Note readNote(int noteId) throws SQLException {
        String sql = "SELECT * FROM notes WHERE note_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, noteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapNote(rs);
                }
            }
        }
        return null;
    }

    public List<Note> readAllNotes() throws SQLException {
        String sql = "SELECT * FROM notes";
        List<Note> notes = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notes.add(mapNote(rs));
            }
        }
        return notes;
    }

    public List<Note> readAllNotesLimited(int limit) throws SQLException {
        String sql = "SELECT * FROM notes LIMIT ?";
        List<Note> notes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(mapNote(rs));
                }
            }
        }
        return notes;
    }

    public boolean updateNote(Note note) throws SQLException {
        String sql = "UPDATE notes SET item_id = ?, note_text = ?, created_at = ? WHERE note_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, note.getItemId());
            stmt.setString(2, note.getNoteText());
            stmt.setTimestamp(3, Timestamp.valueOf(note.getCreatedAt()));
            stmt.setInt(4, note.getNoteId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteNote(int noteId) throws SQLException {
        String sql = "DELETE FROM notes WHERE note_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, noteId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ==================== HELPER METHODS ====================

    private Item mapItem(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("item_id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("rarity"),
                rs.getDouble("npc_sell_price")
        );
    }

    private PriceSnapshot mapPriceSnapshot(ResultSet rs) throws SQLException {
        return new PriceSnapshot(
                rs.getInt("snapshot_id"),
                rs.getInt("item_id"),
                rs.getDouble("buy_price"),
                rs.getDouble("sell_price"),
                rs.getInt("buy_volume"),
                rs.getInt("sell_volume"),
                rs.getTimestamp("snapshot_time").toLocalDateTime()
        );
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getInt("item_id"),
                rs.getString("order_type"),
                rs.getInt("quantity"),
                rs.getDouble("target_price"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    private Trade mapTrade(ResultSet rs) throws SQLException {
        return new Trade(
                rs.getInt("trade_id"),
                rs.getInt("order_id"),
                rs.getInt("qty_filled"),
                rs.getDouble("fill_price"),
                rs.getDouble("fee"),
                rs.getDouble("profit"),
                rs.getTimestamp("trade_time").toLocalDateTime()
        );
    }

    private Note mapNote(ResultSet rs) throws SQLException {
        return new Note(
                rs.getInt("note_id"),
                rs.getInt("item_id"),
                rs.getString("note_text"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
