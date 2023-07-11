package com.ecommerce.ecommerce2.web;


import com.ecommerce.ecommerce2.Entity.CartItem;
import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Entity.ShoppingCart;
import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.ProduitService;
import com.ecommerce.ecommerce2.Service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("ShoppingCart")
public class PanierController {

    @Autowired
    ProduitService produitService;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    CustomerService customerService;

    @GetMapping("/addtocart/{productId}")
    public String ajouterAuPanier(@PathVariable Long produitId, Model model, HttpSession session) throws NotFoundException {
        Produit produit = produitService.findById(produitId);
        if(produit == null){
            throw new NotFoundException("produit non trouve");
        }
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (shoppingCart == null){
            shoppingCart = new ShoppingCart();
            session.setAttribute("shoppingCart",shoppingCart);
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduit(produit);
        cartItem.setQuantite(1);
        shoppingCart.getCartItem().add(cartItem);

        return "redirect:/cart";
    }

//    ajout du contenu de la session dans la bd

    @PostMapping("/connexion")
    public String connexion(@ModelAttribute Customer customer,Model model, HttpSession session){
        Customer customerBdd = customerService.findByUsername(customer.getUsername());
        if(customerBdd == null || !customerBdd.getPassword().equals(customer.getPassword())){
            model.addAttribute("erreur","Email ou mot de passe incorrect");
        }
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        if(shoppingCart != null){
            shoppingCart.setCustomer(customerBdd);
            shoppingCartService.save(shoppingCart);
            session.removeAttribute("shoppingCart");
        }
        return "/connexion";
    }
}
