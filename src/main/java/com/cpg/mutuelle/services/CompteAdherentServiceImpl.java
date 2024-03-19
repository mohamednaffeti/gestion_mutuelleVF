package com.cpg.mutuelle.services;

import com.cpg.mutuelle.detailsServices.CpAdherentInfoDetails;
import com.cpg.mutuelle.entities.Adherent;
import com.cpg.mutuelle.entities.AyantsDroits;
import com.cpg.mutuelle.entities.CompteAdherent;
import com.cpg.mutuelle.entities.Reclamation;
import com.cpg.mutuelle.entities.enumerations.Role;
import com.cpg.mutuelle.exceptions.DataNotFoundException;
import com.cpg.mutuelle.exceptions.UserAlreadyExistException;
import com.cpg.mutuelle.repositories.AdherentRepository;
import com.cpg.mutuelle.repositories.CompteAdhrentRepository;
import com.cpg.mutuelle.repositories.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompteAdherentServiceImpl implements ICompteAdherentService, UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CompteAdhrentRepository cpAdherentRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private IReclamationService reclamationService;


    @Override
    public List<CompteAdherent> getAllACpAdherents() {
        return cpAdherentRepository.findAll();
    }

    @Override
    public CompteAdherent createCpAdherent(CompteAdherent cpAdherent) {
        if (cpAdherentRepository.findByMatricule(cpAdherent.getMatricule()).isPresent()) {
            throw new UserAlreadyExistException("Username already exist to another user");
        } else if (adherentRepository.findByMatricule(cpAdherent.getMatricule()).isEmpty() && cpAdherent.getRole().equals(Role.ADHERENT)) {
            throw new DataNotFoundException("Adhrent not found with this matricule");
        }

        cpAdherent.setPassword(passwordEncoder.encode(cpAdherent.getPassword()));
        cpAdherent.setAdherent(adherentRepository.findByMatricule(cpAdherent.getMatricule()).orElse(null));
        return cpAdherentRepository.save(cpAdherent);


    }

    @Override
    public CompteAdherent updateCpAdherent(CompteAdherent cpAdherent) {
        return null;
    }

    @Override
    public void deleteCpAdherent(Long id) {
        cpAdherentRepository.deleteById(id);
    }

    @Override
    public CompteAdherent findById(Long id) {
        return cpAdherentRepository.findById(id).orElse(null);
    }


    @Override
    public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {
        CompteAdherent adherent = cpAdherentRepository.findByMatricule(matricule)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with matricule: " + matricule));
        if (adherent.getRole().equals(Role.ADHERENT)) {
            Adherent adherent1 = adherentRepository.findByMatricule(matricule).orElse(null);
            if(adherent1 == null){
                throw new DataNotFoundException("Adherent not found");
            }
            List<Reclamation> reclamations = reclamationService.findByAdherent(adherent1.getId());
            List<AyantsDroits> ayantsDroits = adherent.getAdherent().getAyantsDroits();

            return new CpAdherentInfoDetails(adherent.getAdherent(), adherent, ayantsDroits,reclamations);
        } else {
            List<Reclamation> reclamationsNonLues = reclamationService.reclamationsNonLues();
            return new CpAdherentInfoDetails(null, adherent, null,reclamationsNonLues);
        }

    }
}
