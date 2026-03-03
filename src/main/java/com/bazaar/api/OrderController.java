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
import com.bazaar.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.Order;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final BazaarBusinessService businessService;

    public OrderController(BazaarBusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<IdResponse> create(@RequestBody Order order) {
        int id = businessService.addOrder(order);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable int id) {
        Order order = businessService.findOrderById(id);
        return order == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }

    @GetMapping
    public List<Order> getAll(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return businessService.findAllOrdersLimited(limit);
        }
        return businessService.findAllOrders();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Order order) {
        order.setOrderId(id);
        boolean updated = businessService.modifyOrder(order);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = businessService.removeOrder(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
