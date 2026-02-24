package com.bazaar.business;

import java.util.List;

import com.bazaar.model.Item;
import com.bazaar.model.Note;
import com.bazaar.model.Order;
import com.bazaar.model.PriceSnapshot;
import com.bazaar.model.Trade;

public interface BazaarBusinessService {
    int addItem(Item item);
    Item findItemById(int itemId);
    List<Item> findAllItems();
    boolean modifyItem(Item item);
    boolean removeItem(int itemId);

    int addPriceSnapshot(PriceSnapshot snapshot);
    PriceSnapshot findPriceSnapshotById(int snapshotId);
    List<PriceSnapshot> findAllPriceSnapshots();
    boolean modifyPriceSnapshot(PriceSnapshot snapshot);
    boolean removePriceSnapshot(int snapshotId);

    int addOrder(Order order);
    Order findOrderById(int orderId);
    List<Order> findAllOrders();
    boolean modifyOrder(Order order);
    boolean removeOrder(int orderId);

    int addTrade(Trade trade);
    Trade findTradeById(int tradeId);
    List<Trade> findAllTrades();
    boolean modifyTrade(Trade trade);
    boolean removeTrade(int tradeId);

    int addNote(Note note);
    Note findNoteById(int noteId);
    List<Note> findAllNotes();
    boolean modifyNote(Note note);
    boolean removeNote(int noteId);
}
