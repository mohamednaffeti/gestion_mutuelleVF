package com.cpg.mutuelle.repositories;

import com.cpg.mutuelle.entities.Adherent;
import com.cpg.mutuelle.entities.CompteAdherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteAdhrentRepository extends JpaRepository<CompteAdherent,Long> {
    Optional<CompteAdherent> findByMatricule(String matricule);
}
