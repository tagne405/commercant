package com.ecommerce.ecommerce2.Service;

import java.util.*;

import com.ecommerce.ecommerce2.Entity.*;
import com.ecommerce.ecommerce2.Repository.*;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;


    public OrderService(OrderDetailRepository orderDetailRepository, ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

//    public OrderDetailDto transfert(Long order_id){
//
////        OrderDetail orderDetail1 = orderDetailRepository.findOrderDetailByOrder_Id(order_id);
//
//        OrderDetailDto orderDetailDto = new OrderDetailDto();
//        orderDetailDto.setQuantite(orderDetail1.getQuantite());
//        orderDetailDto.setPrixUnitaire(orderDetail1.getPrixUnitaire());
//        orderDetailDto.setProduit(orderDetail1.getProduit());
//        return orderDetailDto;
//
//    }

    //envoi du panier au detail de la commande et definition du status de la commade
    public void saveOrder(ShoppingCart Cart,String idPaiement){
        Order order = new Order();
        order.setStatusOrdre("EN Cours...");
        order.setOrderDate(new Date());
        order.setCustomer(Cart.getCustomer());
        order.setPrixTotal(Cart.getPrixTotal());

        // Ajouter 3 jours à la date de livraison
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date dateLivraison = calendar.getTime();

        order.setDateLivraison(dateLivraison);

        //enregistrement de idPaiement
        order.setIdPaiement(idPaiement);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(CartItem item : Cart.getCartItem()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setQuantite(item.getQuantite());
            orderDetail.setProduit(item.getProduit());
            orderDetail.setPrixUnitaire(item.getProduit().getPrix());
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
            cartItemRepository.delete(item);
        }

        //hashSet recupre une aucurrence unique de l'element
        order.setOrderDetail(orderDetailList);
        Cart.setCartItem(new HashSet<>());
        Cart.setTotalItems(0);
        Cart.setPrixTotal(0);
        shoppingCartRepository.save(Cart);
        orderRepository.save(order);
    }

    public void acceptOrder(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        assert order != null;
        order.setDateLivraison(new Date());
        order.setStatusOrdre("EXPEDITION");
        orderRepository.save(order);
    }



    public void cancelOrder(Long id){
        orderRepository.deleteById(id);
    }

    /*find the order of customer by the admin*/
    public List<OrderDetail> findOrderDetail(){
//        List<Customer> customer = customerRepository.findAll();
//        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrder_Id(order_id);
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        return orderDetails;

    }

    public List<OrderDetail> findOrderDetail(Long id){
        return orderDetailRepository.findOrdere(id);
    }

    public List<Order> findOrderValided(){
        return orderRepository.findAllByStatusOrdre("EXPEDITION");
    }
    public List<OrderDetail> findOrderDetailById(Long id){
        return orderDetailRepository.findOrderDetailByOrder_Id(id);
    }
    public List<Order> findAllOrder(){
        List<Order> order = orderRepository.findAllByStatusOrdreNot("EXPEDITION");
        return order;
    }

    public Order findOrderById(Long id){
        return orderRepository.findById(id).get();
    }
}
