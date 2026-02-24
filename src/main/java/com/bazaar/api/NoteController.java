package com.bazaar.api;

import com.bazaar.business.BazaarBusinessService;
import com.bazaar.model.Note;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final BazaarBusinessService businessService;

    public NoteController(BazaarBusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<IdResponse> create(@RequestBody Note note) {
        int id = businessService.addNote(note);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable int id) {
        Note note = businessService.findNoteById(id);
        return note == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(note);
    }

    @GetMapping
    public List<Note> getAll() {
        return businessService.findAllNotes();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Note note) {
        note.setNoteId(id);
        boolean updated = businessService.modifyNote(note);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = businessService.removeNote(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
