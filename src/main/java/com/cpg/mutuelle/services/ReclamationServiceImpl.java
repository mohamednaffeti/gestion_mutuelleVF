package com.cpg.mutuelle.services;

import com.cpg.mutuelle.entities.Reclamation;
import com.cpg.mutuelle.entities.enumerations.StatusReclamation;
import com.cpg.mutuelle.exceptions.DataNotFoundException;
import com.cpg.mutuelle.repositories.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamationServiceImpl implements IReclamationService {
    @Autowired
    private ReclamationRepository reclamationRepository;
    @Override
    public List<Reclamation> getAll() {
        return reclamationRepository.findAll();
    }

    @Override
    public List<Reclamation> findByAdherent(Long id) {
        return reclamationRepository.findByAdherent_Id(id);
    }

    @Override
    public List<Reclamation> reclamationsNonLues() {
        return this.getAll().stream().filter(reclamation -> !reclamation.isLu()).toList();
    }

    @Override
    public Reclamation updateLuStatus(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id).orElse(null);
        if(reclamation==null){
            throw new DataNotFoundException("reclamation not found");
        }else if(reclamation.isLu()){
            throw new DataNotFoundException("Reclamation already read");
        }else{
            reclamation.setLu(true);
            return reclamationRepository.save(reclamation);
        }

    }

    @Override
    public Reclamation updateStatusReclamation(Long id, StatusReclamation newStatus) {
        Reclamation reclamation = reclamationRepository.findById(id).orElse(null);
        if(reclamation==null){
            throw new DataNotFoundException("reclamation not found");
        }else if(reclamation.getStatut()==newStatus){
            throw new DataNotFoundException("it's the same status");
        }else{
            reclamation.setStatut(newStatus);
            return reclamationRepository.save(reclamation);
        }
    }
}
