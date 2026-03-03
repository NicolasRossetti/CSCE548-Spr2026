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
    List<Item> findAllItemsLimited(int limit);
    boolean modifyItem(Item item);
    boolean removeItem(int itemId);

    int addPriceSnapshot(PriceSnapshot snapshot);
    PriceSnapshot findPriceSnapshotById(int snapshotId);
    List<PriceSnapshot> findAllPriceSnapshots();
    List<PriceSnapshot> findAllPriceSnapshotsLimited(int limit);
    boolean modifyPriceSnapshot(PriceSnapshot snapshot);
    boolean removePriceSnapshot(int snapshotId);

    int addOrder(Order order);
    Order findOrderById(int orderId);
    List<Order> findAllOrders();
    List<Order> findAllOrdersLimited(int limit);
    boolean modifyOrder(Order order);
    boolean removeOrder(int orderId);

    int addTrade(Trade trade);
    Trade findTradeById(int tradeId);
    List<Trade> findAllTrades();
    List<Trade> findAllTradesLimited(int limit);
    boolean modifyTrade(Trade trade);
    boolean removeTrade(int tradeId);

    int addNote(Note note);
    Note findNoteById(int noteId);
    List<Note> findAllNotes();
    List<Note> findAllNotesLimited(int limit);
    boolean modifyNote(Note note);
    boolean removeNote(int noteId);
}
