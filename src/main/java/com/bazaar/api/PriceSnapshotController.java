package com.bazaar.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.PriceSnapshot;

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
    public List<PriceSnapshot> getAll(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return businessService.findAllPriceSnapshotsLimited(limit);
        }
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
