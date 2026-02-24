package com.bazaar.api;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.PriceSnapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-snapshots")
public class PriceSnapshotController {

    private final BazaarBusinessService businessService;

    public PriceSnapshotController(BazaarBusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<IdResponse> create(@RequestBody PriceSnapshot snapshot) {
        int id = businessService.addPriceSnapshot(snapshot);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceSnapshot> getById(@PathVariable int id) {
        PriceSnapshot snapshot = businessService.findPriceSnapshotById(id);
        return snapshot == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(snapshot);
    }

    @GetMapping
    public List<PriceSnapshot> getAll() {
        return businessService.findAllPriceSnapshots();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody PriceSnapshot snapshot) {
        snapshot.setSnapshotId(id);
        boolean updated = businessService.modifyPriceSnapshot(snapshot);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = businessService.removePriceSnapshot(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
