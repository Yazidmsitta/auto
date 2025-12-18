package com.atelier.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @Column(nullable = false, unique = true)
    private String immatriculation;
    
    @Column(nullable = false)
    private String marque;
    
    @Column(nullable = false)
    private String modele;
    
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdreReparation> ordresReparation = new ArrayList<>();
    
    public Vehicule() {}
    
    public Vehicule(Client client, String immatriculation, String marque, String modele) {
        this.client = client;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public String getImmatriculation() {
        return immatriculation;
    }
    
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
    
    public String getMarque() {
        return marque;
    }
    
    public void setMarque(String marque) {
        this.marque = marque;
    }
    
    public String getModele() {
        return modele;
    }
    
    public void setModele(String modele) {
        this.modele = modele;
    }
    
    public List<OrdreReparation> getOrdresReparation() {
        return ordresReparation;
    }
    
    public void setOrdresReparation(List<OrdreReparation> ordresReparation) {
        this.ordresReparation = ordresReparation;
    }
    
    @Override
    public String toString() {
        return marque + " " + modele + " (" + immatriculation + ")";
    }
}



