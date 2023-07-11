package com.ecommerce.ecommerce2.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailDto {

    private int quantite;
    private double prixTotal;
    private double prixUnitaire;

    private Produit produit;
}
