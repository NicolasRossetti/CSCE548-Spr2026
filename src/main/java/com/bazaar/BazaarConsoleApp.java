package com.bazaar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.bazaar.dao.BazaarDAO;
import com.bazaar.model.Item;
import com.bazaar.model.Note;
import com.bazaar.model.Order;
import com.bazaar.model.PriceSnapshot;
import com.bazaar.model.Trade;

public class BazaarConsoleApp {
    private static BazaarDAO dao;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("=== Hypixel Skyblock Bazaar Tracker ===");
        System.out.println("Initializing database connection...\n");

        // Initialize DAO with database connection
        String url = "jdbc:mysql://localhost:3306/bazaar_tracker";
        String username = "root";  // Change this to your MySQL username
        String password = "password";      // Change this to your MySQL password

        dao = new BazaarDAO(url, username, password);

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        displayAllItems();
                        break;
                    case 2:
                        displayAllPriceSnapshots();
                        break;
                    case 3:
                        displayAllOrders();
                        break;
                    case 4:
                        displayAllTrades();
                        break;
                    case 5:
                        displayAllNotes();
                        break;
                    case 6:
                        createNewItem();
                        break;
                    case 7:
                        createNewOrder();
                        break;
                    case 8:
                        updateOrderStatus();
                        break;
                    case 9:
                        deleteItem();
                        break;
                    case 10:
                        displayProfitSummary();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("          MAIN MENU");
        System.out.println("========================================");
        System.out.println("1. View All Items");
        System.out.println("2. View All Price Snapshots");
        System.out.println("3. View All Orders");
        System.out.println("4. View All Trades");
        System.out.println("5. View All Notes");
        System.out.println("6. Create New Item");
        System.out.println("7. Create New Order");
        System.out.println("8. Update Order Status");
        System.out.println("9. Delete Item");
        System.out.println("10. View Profit Summary");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    private static void displayAllItems() throws Exception {
        System.out.println("\n=== ALL ITEMS ===");
        List<Item> items = dao.readAllItems();
        if (items.isEmpty()) {
            System.out.println("No items found.");
        } else {
            System.out.printf("%-5s %-30s %-15s %-15s %-10s%n", "ID", "Name", "Category", "Rarity", "NPC Price");
            System.out.println("--------------------------------------------------------------------------------");
            for (Item item : items) {
                System.out.printf("%-5d %-30s %-15s %-15s $%-9.2f%n",
                        item.getItemId(), item.getName(), item.getCategory(),
                        item.getRarity(), item.getNpcSellPrice());
            }
            System.out.println("Total items: " + items.size());
        }
    }

    private static void displayAllPriceSnapshots() throws Exception {
        System.out.println("\n=== ALL PRICE SNAPSHOTS ===");
        List<PriceSnapshot> snapshots = dao.readAllPriceSnapshots();
        if (snapshots.isEmpty()) {
            System.out.println("No price snapshots found.");
        } else {
            System.out.printf("%-5s %-8s %-10s %-10s %-10s %-10s %-20s%n",
                    "ID", "Item ID", "Buy", "Sell", "Buy Vol", "Sell Vol", "Time");
            System.out.println("-------------------------------------------------------------------------------------");
            for (PriceSnapshot snapshot : snapshots) {
                System.out.printf("%-5d %-8d $%-9.2f $%-9.2f %-10d %-10d %s%n",
                        snapshot.getSnapshotId(), snapshot.getItemId(),
                        snapshot.getBuyPrice(), snapshot.getSellPrice(),
                        snapshot.getBuyVolume(), snapshot.getSellVolume(),
                        snapshot.getSnapshotTime());
            }
            System.out.println("Total snapshots: " + snapshots.size());
        }
    }

    private static void displayAllOrders() throws Exception {
        System.out.println("\n=== ALL ORDERS ===");
        List<Order> orders = dao.readAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.printf("%-5s %-8s %-6s %-8s %-12s %-10s %-20s%n",
                    "ID", "Item ID", "Type", "Qty", "Target $", "Status", "Created");
            System.out.println("-------------------------------------------------------------------------------------");
            for (Order order : orders) {
                System.out.printf("%-5d %-8d %-6s %-8d $%-11.2f %-10s %s%n",
                        order.getOrderId(), order.getItemId(), order.getOrderType(),
                        order.getQuantity(), order.getTargetPrice(),
                        order.getStatus(), order.getCreatedAt());
            }
            System.out.println("Total orders: " + orders.size());
        }
    }

    private static void displayAllTrades() throws Exception {
        System.out.println("\n=== ALL TRADES ===");
        List<Trade> trades = dao.readAllTrades();
        if (trades.isEmpty()) {
            System.out.println("No trades found.");
        } else {
            System.out.printf("%-5s %-9s %-10s %-12s %-10s %-12s %-20s%n",
                    "ID", "Order ID", "Qty", "Fill Price", "Fee", "Profit", "Time");
            System.out.println("-------------------------------------------------------------------------------------");
            for (Trade trade : trades) {
                System.out.printf("%-5d %-9d %-10d $%-11.2f $%-9.2f $%-11.2f %s%n",
                        trade.getTradeId(), trade.getOrderId(), trade.getQtyFilled(),
                        trade.getFillPrice(), trade.getFee(), trade.getProfit(),
                        trade.getTradeTime());
            }
            System.out.println("Total trades: " + trades.size());
        }
    }

    private static void displayAllNotes() throws Exception {
        System.out.println("\n=== ALL NOTES ===");
        List<Note> notes = dao.readAllNotes();
        if (notes.isEmpty()) {
            System.out.println("No notes found.");
        } else {
            for (Note note : notes) {
                System.out.printf("\n[Note #%d] Item ID: %d | Created: %s%n",
                        note.getNoteId(), note.getItemId(), note.getCreatedAt());
                System.out.println("  " + note.getNoteText());
            }
            System.out.println("\nTotal notes: " + notes.size());
        }
    }

    private static void createNewItem() throws Exception {
        System.out.println("\n=== CREATE NEW ITEM ===");
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter category (FARMING/MINING/COMBAT): ");
        String category = scanner.nextLine();
        
        System.out.print("Enter rarity (COMMON/UNCOMMON/RARE/EPIC/LEGENDARY): ");
        String rarity = scanner.nextLine();
        
        System.out.print("Enter NPC sell price: ");
        double npcPrice = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer

        Item item = new Item(0, name, category, rarity, npcPrice);
        int id = dao.createItem(item);
        
        if (id > 0) {
            System.out.println("Item created successfully with ID: " + id);
        } else {
            System.out.println("Failed to create item.");
        }
    }

    private static void createNewOrder() throws Exception {
        System.out.println("\n=== CREATE NEW ORDER ===");
        
        int itemId = getIntInput("Enter item ID: ");
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter order type (BUY/SELL): ");
        String type = scanner.nextLine();
        
        int quantity = getIntInput("Enter quantity: ");
        
        System.out.print("Enter target price: ");
        double targetPrice = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter status (OPEN/PARTIAL/FILLED/CANCELED): ");
        String status = scanner.nextLine();

        Order order = new Order(0, itemId, type, quantity, targetPrice, status, LocalDateTime.now());
        int id = dao.createOrder(order);
        
        if (id > 0) {
            System.out.println("Order created successfully with ID: " + id);
        } else {
            System.out.println("Failed to create order.");
        }
    }

    private static void updateOrderStatus() throws Exception {
        System.out.println("\n=== UPDATE ORDER STATUS ===");
        
        int orderId = getIntInput("Enter order ID to update: ");
        
        Order order = dao.readOrder(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        System.out.println("Current order: " + order);
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter new status (OPEN/PARTIAL/FILLED/CANCELED): ");
        String newStatus = scanner.nextLine();
        
        order.setStatus(newStatus);
        boolean success = dao.updateOrder(order);
        
        if (success) {
            System.out.println("Order updated successfully!");
        } else {
            System.out.println("Failed to update order.");
        }
    }

    private static void deleteItem() throws Exception {
        System.out.println("\n=== DELETE ITEM ===");
        
        int itemId = getIntInput("Enter item ID to delete: ");
        
        Item item = dao.readItem(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Item to delete: " + item);
        System.out.print("Are you sure? (yes/no): ");
        scanner.nextLine(); // Clear buffer
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            boolean success = dao.deleteItem(itemId);
            if (success) {
                System.out.println("Item deleted successfully!");
            } else {
                System.out.println("Failed to delete item.");
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private static void displayProfitSummary() throws Exception {
        System.out.println("\n=== PROFIT SUMMARY ===");
        List<Trade> trades = dao.readAllTrades();
        
        if (trades.isEmpty()) {
            System.out.println("No trades found.");
            return;
        }
        
        double totalProfit = 0;
        double totalFees = 0;
        int profitableTrades = 0;
        
        for (Trade trade : trades) {
            totalProfit += trade.getProfit();
            totalFees += trade.getFee();
            if (trade.getProfit() > 0) {
                profitableTrades++;
            }
        }
        
        double netProfit = totalProfit - totalFees;
        
        System.out.println("Total Trades: " + trades.size());
        System.out.println("Profitable Trades: " + profitableTrades);
        System.out.printf("Total Profit: $%.2f%n", totalProfit);
        System.out.printf("Total Fees: $%.2f%n", totalFees);
        System.out.printf("Net Profit: $%.2f%n", netProfit);
        System.out.printf("Average Profit per Trade: $%.2f%n", totalProfit / trades.size());
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. " + prompt);
        }
        return scanner.nextInt();
    }
}
