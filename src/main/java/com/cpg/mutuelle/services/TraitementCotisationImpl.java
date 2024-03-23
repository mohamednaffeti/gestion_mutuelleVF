package com.cpg.mutuelle.services;

import com.cpg.mutuelle.entities.Cotisation;
import com.cpg.mutuelle.entities.dto.CotisationDTO;
import com.cpg.mutuelle.entities.enumerations.TypeCotisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TraitementCotisationImpl implements ITraitementCotisation {
    @Autowired
    private ICotisationService cotisationService;
    @Autowired
    private IAdherentService adherentService;

    @Override
    public CotisationDTO getCotisation(Long idAdherent) {
        List<Cotisation> cotisations = cotisationService.getByAdherent(idAdherent);
        CotisationDTO cotisationDTO = CotisationDTO.builder().build();
        //cas 1 : historique de cotisation non vide
        if(!cotisations.isEmpty()){
            Cotisation cotisation = cotisationService.getRecentById(idAdherent);
            int intervalle =calculerIntervalleMois(cotisation.getDateFin());
            System.out.println(intervalle);
            if(intervalle<=12){
                cotisationDTO.setDateDebutComptage(cotisation.getDateFin());
                cotisationDTO.setTypeCotisation(TypeCotisation.COTISATION);
                cotisationDTO.setMontantcnss(getMontantCNSS(idAdherent));
                return  cotisationDTO;

            }else{
                cotisationDTO.setDateDebutComptage(LocalDate.now().withDayOfMonth(1));
                cotisationDTO.setTypeCotisation(TypeCotisation.PECHE);
                cotisationDTO.setMontantcnss(getMontantCNSS(idAdherent));
                return  cotisationDTO;
            }
        // historique de cotisation vide
        }else{

        }
        return null;
    }

    @Override
    public double getMontantCotisation(Long idAdherent,int nbr_period) {
        List<Cotisation> cotisations = cotisationService.getByAdherent(idAdherent);

        //cas 1 : historique de cotisation non vide
        if(!cotisations.isEmpty()){
            Cotisation cotisation = cotisationService.getRecentById(idAdherent);
            int intervalle =calculerIntervalleMois(cotisation.getDateFin());
            System.out.println(intervalle);
            if(intervalle<=12){

               return (getMontantCNSS(idAdherent)*2*nbr_period)/100;

            }else{

                return (getMontantCNSS(idAdherent)*2*(nbr_period+12))/100;
            }
        // historique de cotisation vide
        }else{

        }
        return 0;
    }


    public double getMontantCNSS(Long idAdherent){
        return adherentService.findById(idAdherent)
                .getSalaireCNSS();
    }
    public static int calculerIntervalleMois(LocalDate dateFin) {
        LocalDate now = LocalDate.now();
        LocalDate dateDebut = dateFin.plusMonths(1);
        int intervalle = 0;

        while (dateDebut.isBefore(now) || dateDebut.equals(now)) {
            intervalle++;
            dateDebut = dateDebut.plusMonths(1);
        }

        return intervalle;
    }

}
