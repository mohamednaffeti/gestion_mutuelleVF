package com.cpg.mutuelle.controllers;

import com.cpg.mutuelle.entities.dto.CotisationDTO;
import com.cpg.mutuelle.services.ITraitementCotisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traitement")
@CrossOrigin(origins = "*")
public class TraitementCotisationController {
    @Autowired
    private ITraitementCotisation traitementCotisationService;

    @GetMapping("/{idAdherent}")
    public ResponseEntity<CotisationDTO> getCotisation(@PathVariable Long idAdherent) {
        CotisationDTO cotisationDTO = traitementCotisationService.getCotisation(idAdherent);
        if (cotisationDTO != null) {
            return new ResponseEntity<>(cotisationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/montant/{idAdherent}/{nbrPeriod}")
    public ResponseEntity<Double> getMontantCotisation(@PathVariable Long idAdherent, @PathVariable int nbrPeriod) {
        double montantCotisation = traitementCotisationService.getMontantCotisation(idAdherent, nbrPeriod);
        return new ResponseEntity<>(montantCotisation, HttpStatus.OK);
    }
}
