package com.cpg.mutuelle.services;

import com.cpg.mutuelle.entities.Cotisation;
import com.cpg.mutuelle.entities.dto.CotisationDTO;
import com.cpg.mutuelle.entities.enumerations.TypeCotisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
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
        Cotisation cotisation = cotisationService.getRecentById(idAdherent);
        CotisationDTO cotisationDTO = CotisationDTO.builder().build();
        int intervalle;

        if (cotisations.isEmpty()) {
            intervalle = calculerIntervalleMois(getDateRetraite(idAdherent));
            TypeCotisation typeCotisation = (intervalle <= 12) ?
                    (getMontantCNSS(idAdherent) == 0 ? TypeCotisation.AVANCE : TypeCotisation.COTISATION) :
                    TypeCotisation.PENALITE;
            cotisationDTO.setMontantcnss(getMontantCNSS(idAdherent));
            cotisationDTO.setTypeCotisation(typeCotisation);
            cotisationDTO.setDateDebutComptage((typeCotisation == TypeCotisation.PENALITE) ? LocalDate.now().withDayOfMonth(1) : getDateRetraite(idAdherent));
        } else if (cotisation.getCotisation().equals(TypeCotisation.AVANCE)) {
            cotisationDTO.setMontantcnss(getMontantCNSS(idAdherent));
            cotisationDTO.setTypeCotisation(TypeCotisation.REGULARISATION);
            cotisationDTO.setDateDebutComptage(getDateRetraite(idAdherent));
        } else {
            intervalle = calculerIntervalleMois(cotisation.getDateFin());
            TypeCotisation typeCotisation = (intervalle <= 12) ? TypeCotisation.COTISATION : TypeCotisation.PENALITE;
            cotisationDTO.setMontantcnss(getMontantCNSS(idAdherent));
            cotisationDTO.setTypeCotisation(typeCotisation);
            cotisationDTO.setDateDebutComptage((typeCotisation == TypeCotisation.PENALITE) ? LocalDate.now().withDayOfMonth(1) : cotisation.getDateFin());
        }
        return cotisationDTO;
    }


    @Override
    public double getMontantCotisation(Long idAdherent, int nbr_period) {
        DecimalFormat df = new DecimalFormat("#.##");
        List<Cotisation> cotisations = cotisationService.getByAdherent(idAdherent);
        Cotisation cotisation = cotisationService.getRecentById(idAdherent);
        int intervalle;
        double montantCotisation;

        if (cotisations.isEmpty()) {
            intervalle = calculerIntervalleMois(getDateRetraite(idAdherent));
            if (intervalle <= 12) {
                montantCotisation = (getMontantCNSS(idAdherent) == 0) ?
                        (getAssiette(idAdherent) * 2 * nbr_period) / 100 :
                        (getMontantCNSS(idAdherent) * 2 * nbr_period) / 100;
            } else {
                montantCotisation = (getMontantCNSS(idAdherent) * 2 * (nbr_period + 12)) / 100;
            }
        } else if (cotisation.getCotisation().equals(TypeCotisation.AVANCE)) {
            montantCotisation = ((getMontantCNSS(idAdherent) * 2 * nbr_period) / 100 - cotisation.getMontant());
        } else {
            intervalle = calculerIntervalleMois(cotisation.getDateFin());
            montantCotisation = (intervalle <= 12) ?
                    (getMontantCNSS(idAdherent) * 2 * nbr_period) / 100 :
                    (getMontantCNSS(idAdherent) * 2 * (nbr_period + 12)) / 100;
        }
        montantCotisation = Double.parseDouble(df.format(montantCotisation));
        return montantCotisation;
    }

    public double getMontantCNSS(Long idAdherent){
        return adherentService.findById(idAdherent)
                .getSalaireCNSS();
    }
    public LocalDate getDateRetraite(Long idAdherent){
        return adherentService.findById(idAdherent)
                .getDateDepartRetraite();
    }
    public double getAssiette(Long idAdherent){
        return adherentService.findById(idAdherent)
                .getAssiette();
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
