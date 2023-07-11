package com.ecommerce.ecommerce2.Entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "shopping_cart_id")
    private Long id;
   private int totalItems;
   private double prixTotal;

   @OneToOne( fetch = FetchType.EAGER)
   @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
   private Customer customer;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
   private Set<CartItem> cartItem;

}
