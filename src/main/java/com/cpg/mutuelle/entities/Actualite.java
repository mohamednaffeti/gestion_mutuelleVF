package com.cpg.mutuelle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actualite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titreActualite;
    private String contenuActualite;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="admin_id")
    private CompteAdherent compteAdherent;
}
