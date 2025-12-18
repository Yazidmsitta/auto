package com.atelier.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "factures")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "or_id", nullable = false, unique = true)
    private OrdreReparation ordreReparation;
    
    @Column(nullable = false)
    private Double total;
    
    @Column(nullable = false)
    private LocalDate date;
    
    public Facture() {
        this.date = LocalDate.now();
    }
    
    public Facture(OrdreReparation ordreReparation, Double total, LocalDate date) {
        this.ordreReparation = ordreReparation;
        this.total = total;
        this.date = date;
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
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "Facture #" + id + " - " + total + "€ (" + date + ")";
    }
}



