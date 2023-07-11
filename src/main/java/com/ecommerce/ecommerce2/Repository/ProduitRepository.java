package com.ecommerce.ecommerce2.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecommerce2.Entity.Produit;


public interface ProduitRepository extends JpaRepository<Produit,Long>{
    
    @Query("select p from Produit p where p.is_activated = true and p.is_delete = false")
    List<Produit> getAllProducts();

    @Query(value = "select p from Produit p inner join Category c on c.id = p.category.id where c.id = ?1 and p.is_delete = false and p.is_activated = true")
    List<Produit> findProduitInCategory(Long categoryId);

    @Query("select p from Produit p where p.description like %?1% or p.nom like %?1%")
    Page<Produit> searchProduit(String keyWord, Pageable pageable);




    @Query("select p from Produit p")
    Page<Produit> pageProduit(Pageable pageable);
}
