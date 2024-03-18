package com.cpg.mutuelle.controllers;

import com.cpg.mutuelle.entities.Reclamation;
import com.cpg.mutuelle.entities.enumerations.StatusReclamation;
import com.cpg.mutuelle.services.IReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reclamation")
@CrossOrigin(origins = "*")
public class ReclamationController {
    @Autowired
    private IReclamationService reclamationService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        List<Reclamation> reclamations = reclamationService.getAll();
        return new ResponseEntity<>(reclamations, HttpStatus.OK);
    }

    @GetMapping("/{adherentId}")
    public ResponseEntity<List<Reclamation>> getReclamationsByAdherent(@PathVariable("adherentId") Long adherentId) {
        List<Reclamation> reclamations = reclamationService.findByAdherent(adherentId);
        return new ResponseEntity<>(reclamations, HttpStatus.OK);
    }
    @PutMapping("/updateLuStatus/{id}")
    public ResponseEntity<Reclamation> updateLuStatus(@PathVariable Long id) {
        Reclamation updatedReclamation = reclamationService.updateLuStatus(id);
        return new ResponseEntity<>(updatedReclamation, HttpStatus.OK);
    }
    @PutMapping("/updateStatusReclamation/{id}/{newStatus}")
    public ResponseEntity<Reclamation> updateStatusReclamation(@PathVariable Long id, @PathVariable StatusReclamation newStatus) {
        Reclamation updatedReclamation = reclamationService.updateStatusReclamation(id, newStatus);
        return new ResponseEntity<>(updatedReclamation, HttpStatus.OK);
    }

}
