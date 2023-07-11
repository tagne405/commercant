package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.Role;
import java.util.List;



public interface RoleRepository extends JpaRepository<Role,String>{
    
        Role findByName(String name);
    
        
}
