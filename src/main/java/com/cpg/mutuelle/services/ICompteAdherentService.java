package com.cpg.mutuelle.services;

import com.cpg.mutuelle.entities.Adherent;
import com.cpg.mutuelle.entities.CompteAdherent;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ICompteAdherentService {
    List<CompteAdherent> getAllACpAdherents();
    CompteAdherent createCpAdherent(CompteAdherent cpAdherent);
    CompteAdherent updateCpAdherent(CompteAdherent cpAdherent);
    void deleteCpAdherent(Long id);
    CompteAdherent findById(Long id);
    UserDetails loadUserByUsername(String matricule);
}
