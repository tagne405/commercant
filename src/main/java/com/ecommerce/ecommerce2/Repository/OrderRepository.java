package com.ecommerce.ecommerce2.Repository;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.OrderDetail;
import com.ecommerce.ecommerce2.Entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


//    @Query("SELECT Order.prixTotal, Customer.firstName, Produit.nom, Produit.quantite FROM Order, Customer, OrderDetail, Produit WHERE OrderDetail.id = Produit.id")
//    @Query("select DISTINCT(p.nom),o.prixTotal,c.firstName, p.quantite FROM Order o, Customer c, OrderDetail od, Produit p WHERE od.produit_id = p.produit_id Order BY c.firstname")
//    @Query("SELECT DISTINCT(p.nom), p.quantite FROM Order o, Customer c, OrderDetail od, Produit p WHERE od.order_id = p.produit_id order by firstName")
//    @Query("SELECT DISTINCT(p.nom), p.quantite FROM Order o, Customer c, OrderDetail od, Produit p WHERE od.order_id = p.produit_id order by firstName")


    List<Order> findOrderByCustomer_Username(@Param("username") String username);
    List<Order> findAllByStatusOrdre(String EXPEDITION);
    List<Order> findAllByStatusOrdreNot(String EXPEDITION);

    @Query("select p from Produit p where p.description like %?1% or p.nom like %?1%")
    Page<Produit> searchProduit(String keyWord, Pageable pageable);

    @Query("select p from Produit p")
    Page<Produit> pageProduit(Pageable pageable);
}
