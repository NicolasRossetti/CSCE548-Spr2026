package com.bazaar.api;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.Trade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final BazaarBusinessService businessService;

    public TradeController(BazaarBusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<IdResponse> create(@RequestBody Trade trade) {
        int id = businessService.addTrade(trade);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trade> getById(@PathVariable int id) {
        Trade trade = businessService.findTradeById(id);
        return trade == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(trade);
    }

    @GetMapping
    public List<Trade> getAll() {
        return businessService.findAllTrades();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Trade trade) {
        trade.setTradeId(id);
        boolean updated = businessService.modifyTrade(trade);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = businessService.removeTrade(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
