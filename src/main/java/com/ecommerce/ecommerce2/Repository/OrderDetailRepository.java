package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.OrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

//   List<OrderDetail> findOrderDetailByOrder_Id(@Param("order_id") Long order_Id);
//   List<OrderDetail> findOrderDetailsByOrder_Id
    @Query("select o.quantite,o.id from OrderDetail o inner join Order r on o.id=r.id WHERE o.id = ?1")
    List<OrderDetail> findOrdere(Long order_id);
//    List<OrderDetail> findOrderDetailBy
    List<OrderDetail> findOrderDetailByOrder_Id(Long id);

}
