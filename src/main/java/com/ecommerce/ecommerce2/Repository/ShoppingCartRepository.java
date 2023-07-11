package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>{

//    int PrixTotal(String username);

    List<ShoppingCart> findAllByCustomer_Username(String nom);
}
