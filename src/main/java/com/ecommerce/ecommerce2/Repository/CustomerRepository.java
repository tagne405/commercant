package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Customer findByUsername(String username);
}
