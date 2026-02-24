package com.bazaar;

import com.bazaar.api.IdResponse;
import com.bazaar.model.Item;
import com.bazaar.model.Note;
import com.bazaar.model.Order;
import com.bazaar.model.PriceSnapshot;
import com.bazaar.model.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

// Service Layer Console Test Client
// Tests all 5 entities (Item, PriceSnapshot, Order, Trade, Note) with full CRUD operations
// Set BAZAAR_API_URL environment variable to test against Railway or local server
// Default: http://localhost:8080/api (local), or https://YOUR-RAILWAY-URL/api (Railway)
public class ServiceConsoleClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void main(String[] args) throws Exception {
        String baseUrl = System.getenv().getOrDefault("BAZAAR_API_URL", "http://localhost:8080/api");

        HttpClient client = HttpClient.newHttpClient();

        System.out.println("=== Service Layer Full CRUD Test ===");
        System.out.println("Base URL: " + baseUrl);

        Item createdItem = new Item(0, "AI_TEST_ITEM", "FARMING", "COMMON", 42.50);
        int itemId = createItem(client, baseUrl, createdItem);
        System.out.println("Created item id: " + itemId);

        Item fetchedItemAfterCreate = getItem(client, baseUrl, itemId);
        System.out.println("Fetched item after create: " + fetchedItemAfterCreate);

        fetchedItemAfterCreate.setNpcSellPrice(55.25);
        fetchedItemAfterCreate.setRarity("UNCOMMON");
        updateItem(client, baseUrl, fetchedItemAfterCreate);
        System.out.println("Updated item id: " + itemId);

        Item fetchedItemAfterUpdate = getItem(client, baseUrl, itemId);
        System.out.println("Fetched item after update: " + fetchedItemAfterUpdate);

        PriceSnapshot createdSnapshot = new PriceSnapshot(0, itemId, 120.00, 123.50, 1000, 850, LocalDateTime.now());
        int snapshotId = createPriceSnapshot(client, baseUrl, createdSnapshot);
        System.out.println("Created price snapshot id: " + snapshotId);

        PriceSnapshot fetchedSnapshot = getPriceSnapshot(client, baseUrl, snapshotId);
        System.out.println("Fetched price snapshot after create: " + fetchedSnapshot);

        fetchedSnapshot.setSellPrice(124.25);
        fetchedSnapshot.setSellVolume(900);
        updatePriceSnapshot(client, baseUrl, fetchedSnapshot);
        System.out.println("Updated price snapshot id: " + snapshotId);

        PriceSnapshot fetchedSnapshotAfterUpdate = getPriceSnapshot(client, baseUrl, snapshotId);
        System.out.println("Fetched price snapshot after update: " + fetchedSnapshotAfterUpdate);

        Order createdOrder = new Order(0, itemId, "BUY", 64, 88.40, "OPEN", LocalDateTime.now());
        int orderId = createOrder(client, baseUrl, createdOrder);
        System.out.println("Created order id: " + orderId);

        Order fetchedOrder = getOrder(client, baseUrl, orderId);
        System.out.println("Fetched order after create: " + fetchedOrder);

        fetchedOrder.setStatus("PARTIAL");
        fetchedOrder.setTargetPrice(89.10);
        updateOrder(client, baseUrl, fetchedOrder);
        System.out.println("Updated order id: " + orderId);

        Order fetchedOrderAfterUpdate = getOrder(client, baseUrl, orderId);
        System.out.println("Fetched order after update: " + fetchedOrderAfterUpdate);

        Trade createdTrade = new Trade(0, orderId, 32, 89.50, 4.25, 33.40, LocalDateTime.now());
        int tradeId = createTrade(client, baseUrl, createdTrade);
        System.out.println("Created trade id: " + tradeId);

        Trade fetchedTrade = getTrade(client, baseUrl, tradeId);
        System.out.println("Fetched trade after create: " + fetchedTrade);

        fetchedTrade.setProfit(36.00);
        fetchedTrade.setFee(4.30);
        updateTrade(client, baseUrl, fetchedTrade);
        System.out.println("Updated trade id: " + tradeId);

        Trade fetchedTradeAfterUpdate = getTrade(client, baseUrl, tradeId);
        System.out.println("Fetched trade after update: " + fetchedTradeAfterUpdate);

        Note createdNote = new Note(0, itemId, "Console integration test note", LocalDateTime.now());
        int noteId = createNote(client, baseUrl, createdNote);
        System.out.println("Created note id: " + noteId);

        Note fetchedNote = getNote(client, baseUrl, noteId);
        System.out.println("Fetched note after create: " + fetchedNote);

        fetchedNote.setNoteText("Console integration test note (updated)");
        updateNote(client, baseUrl, fetchedNote);
        System.out.println("Updated note id: " + noteId);

        Note fetchedNoteAfterUpdate = getNote(client, baseUrl, noteId);
        System.out.println("Fetched note after update: " + fetchedNoteAfterUpdate);

        deleteNote(client, baseUrl, noteId);
        System.out.println("Deleted note id: " + noteId);
        System.out.println("Get note after delete status (expected 404): " + getStatus(client, baseUrl + "/notes/" + noteId));

        deleteTrade(client, baseUrl, tradeId);
        System.out.println("Deleted trade id: " + tradeId);
        System.out.println("Get trade after delete status (expected 404): " + getStatus(client, baseUrl + "/trades/" + tradeId));

        deleteOrder(client, baseUrl, orderId);
        System.out.println("Deleted order id: " + orderId);
        System.out.println("Get order after delete status (expected 404): " + getStatus(client, baseUrl + "/orders/" + orderId));

        deletePriceSnapshot(client, baseUrl, snapshotId);
        System.out.println("Deleted price snapshot id: " + snapshotId);
        System.out.println("Get price snapshot after delete status (expected 404): " + getStatus(client, baseUrl + "/price-snapshots/" + snapshotId));

        deleteItem(client, baseUrl, itemId);
        System.out.println("Deleted item id: " + itemId);
        System.out.println("Get item after delete status (expected 404): " + getStatus(client, baseUrl + "/items/" + itemId));

        System.out.println("=== Full CRUD test complete ===");

        /*
         * Usage notes:
         * 1) Start the Spring service first (locally or on Railway).
         * 2) Set BAZAAR_API_URL if not using localhost.
         * 3) Run this client with:
         *      mvn exec:java -Dexec.mainClass=com.bazaar.ServiceConsoleClient
         */
    }

    private static int createItem(HttpClient client, String baseUrl, Item item) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(item);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/items"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Create item failed");
        IdResponse idResponse = OBJECT_MAPPER.readValue(response.body(), IdResponse.class);
        return idResponse.getId();
    }

    private static Item getItem(HttpClient client, String baseUrl, int itemId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/items/" + itemId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Get item failed");
        return OBJECT_MAPPER.readValue(response.body(), Item.class);
    }

    private static int getStatus(HttpClient client, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    private static void updateItem(HttpClient client, String baseUrl, Item item) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(item);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/items/" + item.getItemId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Update item failed");
    }

    private static void deleteItem(HttpClient client, String baseUrl, int itemId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/items/" + itemId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 204, "Delete item failed");
    }

    private static int createPriceSnapshot(HttpClient client, String baseUrl, PriceSnapshot snapshot) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(snapshot);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/price-snapshots"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Create price snapshot failed");
        return OBJECT_MAPPER.readValue(response.body(), IdResponse.class).getId();
    }

    private static PriceSnapshot getPriceSnapshot(HttpClient client, String baseUrl, int snapshotId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/price-snapshots/" + snapshotId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Get price snapshot failed");
        return OBJECT_MAPPER.readValue(response.body(), PriceSnapshot.class);
    }

    private static void updatePriceSnapshot(HttpClient client, String baseUrl, PriceSnapshot snapshot) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(snapshot);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/price-snapshots/" + snapshot.getSnapshotId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Update price snapshot failed");
    }

    private static void deletePriceSnapshot(HttpClient client, String baseUrl, int snapshotId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/price-snapshots/" + snapshotId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 204, "Delete price snapshot failed");
    }

    private static int createOrder(HttpClient client, String baseUrl, Order order) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(order);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Create order failed");
        return OBJECT_MAPPER.readValue(response.body(), IdResponse.class).getId();
    }

    private static Order getOrder(HttpClient client, String baseUrl, int orderId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/orders/" + orderId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Get order failed");
        return OBJECT_MAPPER.readValue(response.body(), Order.class);
    }

    private static void updateOrder(HttpClient client, String baseUrl, Order order) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(order);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/orders/" + order.getOrderId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Update order failed");
    }

    private static void deleteOrder(HttpClient client, String baseUrl, int orderId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/orders/" + orderId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 204, "Delete order failed");
    }

    private static int createTrade(HttpClient client, String baseUrl, Trade trade) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(trade);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trades"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Create trade failed");
        return OBJECT_MAPPER.readValue(response.body(), IdResponse.class).getId();
    }

    private static Trade getTrade(HttpClient client, String baseUrl, int tradeId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trades/" + tradeId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Get trade failed");
        return OBJECT_MAPPER.readValue(response.body(), Trade.class);
    }

    private static void updateTrade(HttpClient client, String baseUrl, Trade trade) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(trade);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trades/" + trade.getTradeId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Update trade failed");
    }

    private static void deleteTrade(HttpClient client, String baseUrl, int tradeId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trades/" + tradeId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 204, "Delete trade failed");
    }

    private static int createNote(HttpClient client, String baseUrl, Note note) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(note);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/notes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Create note failed");
        return OBJECT_MAPPER.readValue(response.body(), IdResponse.class).getId();
    }

    private static Note getNote(HttpClient client, String baseUrl, int noteId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/notes/" + noteId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Get note failed");
        return OBJECT_MAPPER.readValue(response.body(), Note.class);
    }

    private static void updateNote(HttpClient client, String baseUrl, Note note) throws IOException, InterruptedException {
        String json = OBJECT_MAPPER.writeValueAsString(note);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/notes/" + note.getNoteId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 200, "Update note failed");
    }

    private static void deleteNote(HttpClient client, String baseUrl, int noteId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/notes/" + noteId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureStatus(response, 204, "Delete note failed");
    }

    private static void ensureStatus(HttpResponse<String> response, int expectedStatus, String errorMessage) {
        if (response.statusCode() != expectedStatus) {
            throw new IllegalStateException(errorMessage + ". HTTP " + response.statusCode() + ": " + response.body());
        }
    }
}
