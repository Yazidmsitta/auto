package com.atelier.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordres_reparation")
public class OrdreReparation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EtatReparation etat;
    
    @OneToMany(mappedBy = "ordreReparation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LignePrestation> lignesPrestation = new ArrayList<>();
    
    @OneToOne(mappedBy = "ordreReparation", cascade = CascadeType.ALL)
    private Facture facture;
    
    public enum EtatReparation {
        DIAGNOSTIC, ATELIER, TERMINE
    }
    
    public OrdreReparation() {
        this.date = LocalDate.now();
        this.etat = EtatReparation.DIAGNOSTIC;
    }
    
    public OrdreReparation(Vehicule vehicule, LocalDate date, EtatReparation etat) {
        this.vehicule = vehicule;
        this.date = date;
        this.etat = etat;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Vehicule getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public EtatReparation getEtat() {
        return etat;
    }
    
    public void setEtat(EtatReparation etat) {
        this.etat = etat;
    }
    
    public List<LignePrestation> getLignesPrestation() {
        return lignesPrestation;
    }
    
    public void setLignesPrestation(List<LignePrestation> lignesPrestation) {
        this.lignesPrestation = lignesPrestation;
    }
    
    public Facture getFacture() {
        return facture;
    }
    
    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    
    public double getTotal() {
        if (lignesPrestation == null || lignesPrestation.isEmpty()) {
            return 0.0;
        }
        try {
            return lignesPrestation.stream()
                    .mapToDouble(lp -> lp.getQuantite() * lp.getPrix())
                    .sum();
        } catch (Exception e) {
            // Si la collection n'est pas initialisée, retourner 0
            return 0.0;
        }
    }
    
    @Override
    public String toString() {
        return "OR #" + id + " - " + vehicule + " (" + etat + ")";
    }
}


