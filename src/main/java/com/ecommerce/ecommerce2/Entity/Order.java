package com.ecommerce.ecommerce2.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ordres")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private Date orderDate;
    private Date dateLivraison;
    //id de paiement
    private String idPaiement;
    private Double prixTotal;
    private String statusOrdre;
    @ManyToOne(cascade =  CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "order")
    private List<OrderDetail> OrderDetail;
}
