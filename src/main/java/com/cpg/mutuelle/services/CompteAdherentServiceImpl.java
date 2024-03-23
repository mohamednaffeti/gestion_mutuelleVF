package com.cpg.mutuelle.services;

import com.cpg.mutuelle.detailsServices.CpAdherentInfoDetails;
import com.cpg.mutuelle.entities.*;
import com.cpg.mutuelle.entities.enumerations.Role;
import com.cpg.mutuelle.exceptions.DataNotFoundException;
import com.cpg.mutuelle.exceptions.UserAlreadyExistException;
import com.cpg.mutuelle.repositories.AdherentRepository;
import com.cpg.mutuelle.repositories.CompteAdherentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteAdherentServiceImpl implements ICompteAdherentService, UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CompteAdherentRepository cpAdherentRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private IReclamationService reclamationService;
    @Autowired ICotisationService cotisationService;

    @Override
    public List<CompteAdherent> getAllACpAdherents() {
        return cpAdherentRepository.findAll();
    }

    @Override
    public void createAdmin(CompteAdherent admin) {
        System.out.println(admin);
        if (cpAdherentRepository.findByMatricule(admin.getMatricule()).isPresent()) {
            throw new UserAlreadyExistException("Username already exist to another user");
        } else if (adherentRepository.findByMatricule(admin.getMatricule()).isEmpty() && admin.getRole().equals(Role.ADHERENT)) {
            throw new DataNotFoundException("Adhrent not found with this matricule");
        }else{
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            cpAdherentRepository.save(admin);
        }
    }

    @Override
    public CompteAdherent createCpAdherent(CompteAdherent cpAdherent) {

        Adherent adherent =adherentRepository.findByMatricule(cpAdherent.getMatricule()).orElse(null);
        if(adherent== null){
            throw new DataNotFoundException("Adhrent not found");
        }else{
            cpAdherent.setNom(adherent.getNom());
            cpAdherent.setPrenom(adherent.getPrenom());
            cpAdherent.setCin(adherent.getCin());
            cpAdherent.setTel(adherent.getTel());
            cpAdherent.setRole(Role.ADHERENT);
            cpAdherent.setDateDeNaissance(adherent.getDateDeNaissance());
            cpAdherent.setPassword(passwordEncoder.encode(cpAdherent.getPassword()));
            cpAdherent.setAdherent(adherentRepository.findByMatricule(cpAdherent.getMatricule()).orElse(null));
            cpAdherent.setSexe(adherent.getSexe());
            cpAdherent.setEmail(adherent.getMail());
            return cpAdherentRepository.save(cpAdherent);
        }



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
                .orElseThrow(() -> new DataNotFoundException("User not found with matricule: " + matricule));
        if (adherent.getRole().equals(Role.ADHERENT)) {
            Adherent adherent1 = adherentRepository.findByMatricule(matricule).orElse(null);
            if(adherent1 == null){
                throw new DataNotFoundException("Adherent not found");
            }
            List<Reclamation> reclamations = reclamationService.findByAdherent(adherent1.getId());
            List<AyantsDroits> ayantsDroits = adherent.getAdherent().getAyantsDroits();
            List<Cotisation> cotisations = cotisationService.getByAdherent(adherent1.getId());
            return new CpAdherentInfoDetails(adherent.getAdherent(), adherent, ayantsDroits,reclamations,cotisations);
        } else {
            List<Reclamation> reclamationsNonLues = reclamationService.reclamationsNonLues();
            return new CpAdherentInfoDetails(null, adherent, null,reclamationsNonLues,null);
        }

    }


}
