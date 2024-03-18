package com.cpg.mutuelle.services;

import com.cpg.mutuelle.entities.Adherent;
import com.cpg.mutuelle.repositories.AdherentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdherentServiceImpl implements IAdherentService {
    @Autowired
    AdherentRepository adherentRepository;
    @Override
    public List<Adherent> getAllAdherent() {
        return adherentRepository.findAll();
    }

    @Override
    public Adherent createAdherent(Adherent adherent) {
        return adherentRepository.save(adherent);
    }

    @Override
    public Adherent updateAdherent(Adherent adherent) {
        return null;
    }

    @Override
    public void deleteAdherent(Long id) {
        adherentRepository.deleteById(id);
    }

    @Override
    public Adherent findById(Long id) {
        return adherentRepository.findById(id).orElse(null);
    }

    @Override
    public Adherent loadUserByMatricule(String matricule) {
        return adherentRepository.findByMatricule(matricule).orElse(null);
    }
}
