package com.bazaar.business;

import com.bazaar.dao.BazaarDAO;
import com.bazaar.model.Item;
import com.bazaar.model.Note;
import com.bazaar.model.Order;
import com.bazaar.model.PriceSnapshot;
import com.bazaar.model.Trade;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class BazaarBusinessServiceImpl implements BazaarBusinessService {

    private final BazaarDAO dao;

    public BazaarBusinessServiceImpl(BazaarDAO dao) {
        this.dao = dao;
    }

    @Override
    public int addItem(Item item) {
        return runSql(() -> dao.createItem(item));
    }

    @Override
    public Item findItemById(int itemId) {
        return runSql(() -> dao.readItem(itemId));
    }

    @Override
    public List<Item> findAllItems() {
        return runSql(dao::readAllItems);
    }

    @Override
    public boolean modifyItem(Item item) {
        return runSql(() -> dao.updateItem(item));
    }

    @Override
    public boolean removeItem(int itemId) {
        return runSql(() -> dao.deleteItem(itemId));
    }

    @Override
    public int addPriceSnapshot(PriceSnapshot snapshot) {
        return runSql(() -> dao.createPriceSnapshot(snapshot));
    }

    @Override
    public PriceSnapshot findPriceSnapshotById(int snapshotId) {
        return runSql(() -> dao.readPriceSnapshot(snapshotId));
    }

    @Override
    public List<PriceSnapshot> findAllPriceSnapshots() {
        return runSql(dao::readAllPriceSnapshots);
    }

    @Override
    public boolean modifyPriceSnapshot(PriceSnapshot snapshot) {
        return runSql(() -> dao.updatePriceSnapshot(snapshot));
    }

    @Override
    public boolean removePriceSnapshot(int snapshotId) {
        return runSql(() -> dao.deletePriceSnapshot(snapshotId));
    }

    @Override
    public int addOrder(Order order) {
        return runSql(() -> dao.createOrder(order));
    }

    @Override
    public Order findOrderById(int orderId) {
        return runSql(() -> dao.readOrder(orderId));
    }

    @Override
    public List<Order> findAllOrders() {
        return runSql(dao::readAllOrders);
    }

    @Override
    public boolean modifyOrder(Order order) {
        return runSql(() -> dao.updateOrder(order));
    }

    @Override
    public boolean removeOrder(int orderId) {
        return runSql(() -> dao.deleteOrder(orderId));
    }

    @Override
    public int addTrade(Trade trade) {
        return runSql(() -> dao.createTrade(trade));
    }

    @Override
    public Trade findTradeById(int tradeId) {
        return runSql(() -> dao.readTrade(tradeId));
    }

    @Override
    public List<Trade> findAllTrades() {
        return runSql(dao::readAllTrades);
    }

    @Override
    public boolean modifyTrade(Trade trade) {
        return runSql(() -> dao.updateTrade(trade));
    }

    @Override
    public boolean removeTrade(int tradeId) {
        return runSql(() -> dao.deleteTrade(tradeId));
    }

    @Override
    public int addNote(Note note) {
        return runSql(() -> dao.createNote(note));
    }

    @Override
    public Note findNoteById(int noteId) {
        return runSql(() -> dao.readNote(noteId));
    }

    @Override
    public List<Note> findAllNotes() {
        return runSql(dao::readAllNotes);
    }

    @Override
    public boolean modifyNote(Note note) {
        return runSql(() -> dao.updateNote(note));
    }

    @Override
    public boolean removeNote(int noteId) {
        return runSql(() -> dao.deleteNote(noteId));
    }

    private <T> T runSql(SqlSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (SQLException e) {
            throw new IllegalStateException("Database operation failed", e);
        }
    }

    @FunctionalInterface
    private interface SqlSupplier<T> {
        T get() throws SQLException;
    }
}
