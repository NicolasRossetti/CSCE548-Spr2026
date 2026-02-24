package com.bazaar.api;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final BazaarBusinessService businessService;

    public ItemController(BazaarBusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<IdResponse> create(@RequestBody Item item) {
        int id = businessService.addItem(item);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable int id) {
        Item item = businessService.findItemById(id);
        return item == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(item);
    }

    @GetMapping
    public List<Item> getAll() {
        return businessService.findAllItems();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Item item) {
        item.setItemId(id);
        boolean updated = businessService.modifyItem(item);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = businessService.removeItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
