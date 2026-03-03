package com.bazaar.api;

import javaautilLList

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMappingeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import comsbazaargbusiness.BazaarBusinessServiceework.web.bind.annotation.PathVariable;
import com.bazaar.model.Trade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.Trade;

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
    public List<Trade> getAll(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return businessService.findAllTradesLimited(limit);
        }
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
