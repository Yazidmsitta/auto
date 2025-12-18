package com.atelier.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pieces")
public class Piece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String ref;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false)
    private Double prix;
    
    @OneToMany(mappedBy = "piece")
    private List<LignePrestation> lignesPrestation = new ArrayList<>();
    
    public Piece() {
        this.stock = 0;
    }
    
    public Piece(String ref, String nom, Integer stock, Double prix) {
        this.ref = ref;
        this.nom = nom;
        this.stock = stock;
        this.prix = prix;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRef() {
        return ref;
    }
    
    public void setRef(String ref) {
        this.ref = ref;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public List<LignePrestation> getLignesPrestation() {
        return lignesPrestation;
    }
    
    public void setLignesPrestation(List<LignePrestation> lignesPrestation) {
        this.lignesPrestation = lignesPrestation;
    }
    
    public void consommer(int quantite) {
        if (stock - quantite < 0) {
            throw new IllegalArgumentException("Stock insuffisant. Stock disponible: " + stock);
        }
        this.stock -= quantite;
    }
    
    public void ajouterStock(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        this.stock += quantite;
    }
    
    @Override
    public String toString() {
        return ref + " - " + nom + " (Stock: " + stock + ")";
    }
}



