package com.cpg.mutuelle.controllers;

import com.cpg.mutuelle.entities.Actualite;
import com.cpg.mutuelle.services.IActualiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/actualite")
@CrossOrigin(origins = "*")
public class ActualiteController {
    @Autowired
    private IActualiteService actualiteService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Actualite>> getAllActualites() {
        List<Actualite> actualites = actualiteService.getAll();
        return new ResponseEntity<>(actualites, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Actualite> addActualite(@RequestBody Actualite actualite) {
        Actualite createdActualite = actualiteService.addActualite(actualite);
        return new ResponseEntity<>(createdActualite, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Actualite> updateActualite(@PathVariable Long id, @RequestBody Actualite actualite) {
        actualite.setId(id);
        Actualite updatedActualite = actualiteService.updateActualite(actualite);
        if (updatedActualite != null) {
            return new ResponseEntity<>(updatedActualite, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActualite(@PathVariable Long id) {
        actualiteService.deleteActualite(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
