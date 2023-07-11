package com.ecommerce.ecommerce2.Entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="order_detail")

public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    private int quantite;
    private double prixTotal;
    private double prixUnitaire;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="produit_id", referencedColumnName = "produit_id")
    private Produit produit;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName = "order_id")
    private Order order;
    
}
