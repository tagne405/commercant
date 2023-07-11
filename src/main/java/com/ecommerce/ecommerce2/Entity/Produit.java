package com.ecommerce.ecommerce2.Entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Table(name = "produits", uniqueConstraints = @UniqueConstraint(columnNames = {"nom","photo"}))
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder

public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produit_id")
    private Long id;
    private String nom;
    private String categories;
    private String  description;
    private int quantite;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String photo;
    private int prix;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",referencedColumnName = "category_id")
    private Category category;
    private boolean is_activated;
    private boolean is_delete;
}
