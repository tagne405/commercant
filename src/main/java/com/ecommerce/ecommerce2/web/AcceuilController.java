package com.ecommerce.ecommerce2.web;


import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Entity.ShoppingCart;
import com.ecommerce.ecommerce2.Service.CategoryService;
import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.ProduitService;

import jakarta.servlet.http.HttpSession;




@Controller
public class AcceuilController {
    @Autowired
    private ProduitService produitService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    
    @RequestMapping(value = {"/index", "/"},method = RequestMethod.GET)
    public String index(Model model, Principal principal, HttpSession session,
                        HttpServletRequest request, HttpServletResponse response){


        if(principal != null){

            // Créer un cookie pour stocker l'identifiant de l'utilisateur
            Cookie cookie = new Cookie("username", principal.getName());
            cookie.setMaxAge(24 * 60 *60); // Durée de validité du cookie en secondes (ici, 15min)
            cookie.setPath("/"); // Chemin sur lequel le cookie est valide (ici, la racine du domaine)
            response.addCookie(cookie);

            session.setAttribute("username", principal.getName());

            Cookie[] cookies = request.getCookies();
            String username = null;
            if (cookies != null) {
                for (Cookie cookie2 : cookies) {
                    if (cookie.getName().equals("username")) {
                        username = cookie2.getValue();
                        break;
                    }
                }
            }

            List<Category> categoryList = categoryService.findAlList();
            List<Produit> produitList=produitService.find();
            model.addAttribute("categories", categoryList);
            model.addAttribute("produit", produitList);

            Customer customer = customerService.findByUsername(principal.getName());

            ShoppingCart shoppingCart = customer.getShoppingcart();

            if(shoppingCart != null){
                session.setAttribute("totalItem", shoppingCart.getTotalItems());
                double subTotal = shoppingCart.getPrixTotal();
                model.addAttribute("subTotal", subTotal);
                model.addAttribute("shoppingCart", shoppingCart);}

            model.addAttribute("role", customer);
            // ajout du nbre de commande

            ShoppingCart cart = customer.getShoppingcart();
            if(cart != null){
                session.setAttribute("totalItem", cart.getTotalItems());}
        }else{
            List<Category> categoryList = categoryService.findAlList();
            List<Produit> produitList=produitService.find();
            model.addAttribute("categories", categoryList);
            model.addAttribute("produit", produitList);
            session.removeAttribute("username");
        }

        return "Acceuil2";
    }


    @GetMapping("/search/{pageNo}")
    public String searchProduit(@PathVariable("pageNo")int pageNo,
                                @RequestParam("keyword") String keyword,
                                Model model, Principal principal){
//        if(principal == null){
//            return "redirect:/login";
//        }
        rechercheProduit(pageNo, keyword, model, produitService);
        return "searchProduit";
    }

    static void rechercheProduit(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model,
                                 ProduitService produitService) {

        Page<Produit> produit = produitService.searchProduit(pageNo,keyword);
        model.addAttribute("titre","resultat recherche");
        model.addAttribute("produit", produit);
        model.addAttribute("size", produit.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage",produit.getTotalPages());
    }


//    @GetMapping("/cart")
//    public String cart(Model model,Principal principal, HttpSession session){
//
//        if(principal == null){
//            return "redirect:/login";
//        }
//        Customer customer = customerService.findByUsername(principal.getName());
//        ShoppingCart shoppingCart = customer.getShoppingcart();
//        if(shoppingCart == null){
//            model.addAttribute("check", "FOULE KAN Y'a rien dans ton Panier");
//        }
//        if(shoppingCart != null){
//            session.setAttribute("totalItem", shoppingCart.getTotalItems());
//            double subTotal = shoppingCart.getPrixTotal();
//            model.addAttribute("subTotal", subTotal);
//            model.addAttribute("shoppingCart", shoppingCart);}
//        return "Acceuil2";
//
//    }


    @GetMapping("/admin/AcceuilAdmin")
    public String AcceuilAdmin(Model model){
        Long client = customerService.countUser();
        Long produit = produitService.countProduct();
        model.addAttribute("client",client);
        model.addAttribute("produit",produit);
        return "AcceuilAdmin";
    }

    // @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    // public String home(Model model, Principal principal, HttpSession session){
    //     if(principal != null){
    //         session.setAttribute("username", principal.getName());
    //         Customer customer = customerService.findByUsername(principal.getName());
    //         ShoppingCart cart = customer.getShoppingCart();
    //         session.setAttribute("totalItems", cart.getTotalItems());
    //     }else{
    //         session.removeAttribute("username");
    //     }
    //     return "home";
    // }

    
    

    
}
