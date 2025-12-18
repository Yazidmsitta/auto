package com.atelier.entities;

import javax.persistence.*;

@Entity
@Table(name = "lignes_prestation")
public class LignePrestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "or_id", nullable = false)
    private OrdreReparation ordreReparation;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePrestation type;
    
    @Column(nullable = false)
    private Integer quantite;
    
    @Column(nullable = false)
    private Double prix;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "piece_id")
    private Piece piece;
    
    public enum TypePrestation {
        PIECE, MAIN_D_OEUVRE
    }
    
    public LignePrestation() {
        this.quantite = 1;
    }
    
    public LignePrestation(OrdreReparation ordreReparation, String description, 
                          TypePrestation type, Integer quantite, Double prix) {
        this.ordreReparation = ordreReparation;
        this.description = description;
        this.type = type;
        this.quantite = quantite;
        this.prix = prix;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public OrdreReparation getOrdreReparation() {
        return ordreReparation;
    }
    
    public void setOrdreReparation(OrdreReparation ordreReparation) {
        this.ordreReparation = ordreReparation;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TypePrestation getType() {
        return type;
    }
    
    public void setType(TypePrestation type) {
        this.type = type;
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    public double getSousTotal() {
        return quantite * prix;
    }
    
    @Override
    public String toString() {
        return description + " (" + type + ") - " + quantite + " x " + prix + "€";
    }
}



