package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
}
