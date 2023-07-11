package com.ecommerce.ecommerce2.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ecommerce.ecommerce2.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce2.Entity.CartItem;
import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Entity.ShoppingCart;
import com.ecommerce.ecommerce2.Repository.CartItemRepository;
import com.ecommerce.ecommerce2.Repository.ShoppingCartRepository;


@Service
public class ShoppingCartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
   private CustomerRepository customerRepository;

    public ShoppingCart addItemToCart(Produit produit, int quantite, Customer customer){

        ShoppingCart cart = customer.getShoppingcart();

        if(cart == null){
            cart = new ShoppingCart();
        }

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem cartItem = findCartItem(cartItems, produit.getId());
        if(cartItems == null){
            cartItems = new HashSet<>();
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduit(produit);
                cartItem.setPrixTotal(quantite * produit.getPrix());
                cartItem.setQuantite(quantite);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }
        }else{
           
                if(cartItem == null){
                    cartItem = new CartItem();
                    cartItem.setProduit(produit);
                    cartItem.setPrixTotal(quantite * produit.getPrix());
                    cartItem.setQuantite(quantite);
                    cartItem.setCart(cart);
                    cartItems.add(cartItem);
                    cartItemRepository.save(cartItem);
            }else{
                cartItem.setQuantite(cartItem.getQuantite() + quantite);
                cartItem.setPrixTotal(cartItem.getPrixTotal() + (quantite * produit.getPrix()));
                cartItemRepository.save(cartItem);
            }

        }
        cart.setCartItem(cartItems);

            int totalItems = totalItems(cart.getCartItem());
            double totalPrix = totalPrix(cart.getCartItem());

            cart.setPrixTotal(totalPrix);
            cart.setTotalItems(totalItems);
            cart.setCustomer(customer);


         return shoppingCartRepository.save(cart);
    }

    public ShoppingCart updateItemInCart(Produit produit, int quantite, Customer customer){
        
        ShoppingCart cart = customer.getShoppingcart();

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem item = findCartItem(cartItems, produit.getId());

        item.setQuantite(quantite);
        item.setPrixTotal(quantite * produit.getPrix());
        cartItemRepository.save(item);

        int totalItems = totalItems(cartItems);
        double totalPrix = totalPrix(cartItems);

        cart.setTotalItems(totalItems);
        cart.setPrixTotal(totalPrix);

        return shoppingCartRepository.save(cart);

    }

    public List<ShoppingCart> getTotalPrice(String customer){

        return  shoppingCartRepository.findAllByCustomer_Username(customer);

    }


    public ShoppingCart deleteItemFromCart(Produit produit, Customer customer){
        
        ShoppingCart cart = customer.getShoppingcart();

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem item = findCartItem(cartItems, produit.getId());

        cartItems.remove(item);

        cartItemRepository.delete(item);
        //cartItemRepository.deleteById(item.getId());

        double totalPrix = totalPrix(cartItems);
        int totalItems = totalItems(cartItems);

        cart.setCartItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setPrixTotal(totalPrix);


        return shoppingCartRepository.save(cart);

    }

    public ShoppingCart save(ShoppingCart shoppingCart){
        return shoppingCartRepository.save(shoppingCart);
    }


    private CartItem findCartItem(Set<CartItem> cartItems, Long produitId){
        if(cartItems == null){
            return null;
        }

        CartItem cartItem = null;
        for(CartItem item : cartItems){
            if(item.getProduit().getId() == produitId){
                cartItem = item;
            }
        }
        return cartItem;
    }

    private int totalItems(Set<CartItem> cartItems){
        int totalItems = 0;
        for(CartItem item : cartItems){
            totalItems += item.getQuantite();
        }
        return totalItems;
    }

    private double totalPrix(Set<CartItem> cartItems){
        double totalPrix = 0.0;

        for(CartItem item : cartItems){
            totalPrix += item.getPrixTotal();

        }
        return totalPrix;
    }


}
