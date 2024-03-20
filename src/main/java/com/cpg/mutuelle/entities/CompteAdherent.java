package com.cpg.mutuelle.entities;

import com.cpg.mutuelle.entities.enumerations.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompteAdherent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matricule;
    private String password;
    private String cin;
    private String tel;
    private String nom;
    private String prenom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDeNaissance;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    private Adherent adherent;


}
