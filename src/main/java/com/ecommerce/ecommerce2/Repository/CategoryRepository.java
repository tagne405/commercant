package com.ecommerce.ecommerce2.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.ecommerce2.Entity.Category;


public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.is_activated = true and c.is_delected = false")
    List<Category> findByActivated();
    

    // Customers

    // @Query("select * from Category c inner join Produit p on p.category.id = c.id" + "where c.is_activated = true and c.is_delete = false")
    // List<Category> getCotegoryAndProduit();
}
