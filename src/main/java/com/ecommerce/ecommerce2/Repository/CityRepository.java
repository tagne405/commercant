package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.City;

public interface CityRepository extends JpaRepository<City,Long>{
    
}
