package com.cpg.mutuelle.detailsServices;

import com.cpg.mutuelle.entities.Adherent;
import com.cpg.mutuelle.entities.AyantsDroits;
import com.cpg.mutuelle.entities.CompteAdherent;
import com.cpg.mutuelle.entities.Reclamation;
import com.cpg.mutuelle.entities.enumerations.Role;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Data
public class CpAdherentInfoDetails implements UserDetails {

    @Getter
    private Adherent adherent;
    @Getter
    private Role role;
    @Getter
    private List<AyantsDroits> ayantsDroits;
    @Getter
    private List<Reclamation> reclamations;
    @Getter
    CompteAdherent compteAdherent;

    public CpAdherentInfoDetails(Adherent adherent, CompteAdherent compteAdherent, List<AyantsDroits> ayantsDroits,List<Reclamation> reclamations) {
        this.adherent = adherent;
        this.role = compteAdherent.getRole();
        this.ayantsDroits = ayantsDroits;
        this.compteAdherent = compteAdherent;
        this.reclamations=reclamations;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return compteAdherent.getMatricule();
    }

    @Override
    public String getPassword() {
        return compteAdherent.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
