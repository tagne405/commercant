package com.ecommerce.ecommerce2.web;

import java.security.Principal;

import com.ecommerce.ecommerce2.Entity.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.ProduitService;
import com.ecommerce.ecommerce2.Service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    private final CustomerService customerService;

    private final ShoppingCartService cartService;

    private final ProduitService produitService;

    public CartController(CustomerService customerService, ShoppingCartService cartService, ProduitService produitService) {
        this.customerService = customerService;
        this.cartService = cartService;
        this.produitService = produitService;
    }


//    @PostMapping("/mesomb")
//    public ResponseEntity<String> testmesomb(@RequestParam("payer") String payer,
//                                             @RequestParam("amount") String amount,
//                                             @RequestParam("fees") Boolean fees,
//                                             @RequestParam("service") String service,
//                                             @RequestParam("country") String country,
//                                             @RequestParam("currency") String currency){
//
//        String apiUrl = "https://mesomb.hachther.com/api/v1.1/payment/online/";
//        String authorizationToken = "cc48313a-c2d2-4b8f-82d5-64a4e405505e";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", authorizationToken);
//
//        MesombPayRequestData requestData1 = new MesombPayRequestData(payer, amount, fees, service, country, currency);
//        HttpEntity<MesombPayRequestData> requestEntity = new HttpEntity<>(requestData1, headers);
//
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestData1, String.class);
//
////        if (responseEntity.getStatusCode().is2xxSuccessful()) {
////            String response = responseEntity.getBody();
////            System.out.println(response);
////        } else {
////            System.out.println("Payment request failed.");
////        }
//
//        return responseEntity;
//    }



    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){

        if(principal == null){
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingcart();
        if(shoppingCart == null){
            model.addAttribute("check", "FOULE KAN Y'a rien dans ton Panier");
        }
        if(shoppingCart != null){
        session.setAttribute("totalItem", shoppingCart.getTotalItems());
        double subTotal = shoppingCart.getPrixTotal();
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shoppingCart", shoppingCart);}
        return "cart";
    }


    // Endpoint Ajax pour récupérer le nombre total d'articles
    @GetMapping("/cart/nbpanier")
    @ResponseBody
    public int nbPannier(Model model,Principal principal, HttpSession session){

        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingcart();
        if(shoppingCart == null){
            return 0;
        }else{

        return shoppingCart.getTotalItems();

        }
    }
    // Endpoint Ajax pour récupérer le nombre total d'articles
//    @GetMapping("/cart/total-items")
//    @ResponseBody
//    public int getTotalItems(HttpSession session) {
//
//
//        ShoppingCart shoppingCart = (ShoppingCart)session.getAttribute("shoppingCart");
//        if (shoppingCart != null) {
//            return shoppingCart.getTotalItems();
//        } else {
//            return 0;
//        }
//    }


//    @PostMapping("/addToCart{id}")
//    public String addToCart(@PathVariable("id")Long produitId,
//                            @RequestParam(value = "quantite", required = false, defaultValue = "1") int quantite,
//                            HttpServletRequest request,
//                            Principal principal,
//                            Model model,
//                            RedirectAttributes redirectAttributes){
//
//        if(principal == null){
//            return "Redirect:/login";
//        }
//        Produit produit = produitService.getProduitById(produitId);
//        String username = principal.getName();
//        Customer customer = customerService.findByUsername(username);
//
//        ShoppingCart cart = cartService.addItemToCart(produit, quantite, customer);
//
//        // redirige vers la page precedente vers laquelle cet requete a ete emise
////        return "redirect:"+ request.getHeader("Referer");
//        cart.getTotalItems();
//        redirectAttributes.addFlashAttribute("success","Ajout au panier reussie Vas voir si tu veut");
//        return "valide";
//    }



    @GetMapping("/addToCart/{id}")
    public ResponseEntity<String> addToCart(@PathVariable("id") Long produitId,
                                            @RequestParam(value = "quantite", required = false, defaultValue = "1") int quantite,
                                            Principal principal) {


        if(principal == null){
            // Si l'utilisateur n'est pas connecté, retourne une réponse indiquant qu'il doit se connecter
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous devez être connecté pour ajouter un produit au panier.");
        }

        Produit produit = produitService.getProduitById(produitId);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

        ShoppingCart cart = cartService.addItemToCart(produit, quantite, customer);

        // Retourne une réponse indiquant que l'ajout au panier a réussi et le nombre total d'articles dans le panier
        return ResponseEntity.ok("" + cart.getTotalItems());
    }



    @RequestMapping(value = "/update-cart/{id}/{prixvente}/{quantite}", method = RequestMethod.GET)
    public String updateCart(@PathVariable("quantite")int quantite,@PathVariable("prixvente")int prixvente,
                            @PathVariable("id") Long produitId,
                            Model model,
                            Principal principal ){
        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Produit produit = produitService.getProduitById(produitId);

            ShoppingCart cart = cartService.updateItemInCart(produit, quantite, customer);

            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }
    }

    @GetMapping("/prixTotal")
    @ResponseBody
    public double getTotalPrice(Model model,Principal principal, HttpSession session){
        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingcart();
        if(shoppingCart == null){
            return 0;
        }else{
            return shoppingCart.getPrixTotal();
        }
    }

    //envoi par la methode post
   @RequestMapping(value = "/delete-cart/{id}",  method = {RequestMethod.GET,RequestMethod.DELETE})
   public String deleteItemFromCart(@PathVariable("id")Long produitId,
                                    Model model,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes){
        System.out.println(produitId);
        if(principal == null){
            return ("Vous devez être connecté pour Spprimer un produit au panier.");
        }else{
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Produit produit = produitService.getProduitById(produitId);
            ShoppingCart cart = cartService.deleteItemFromCart(produit, customer);
            model.addAttribute("shoppingCart", cart);
            redirectAttributes.addFlashAttribute("delete","supperssion Reussie");
            return "redirect:/cart";

        }

   }

    // Endpoint pour mettre à jour la quantité totale de produit
    @PostMapping("/update-quantity/{id}/{quantity}")
    public ResponseEntity<String> updateQuantity(@PathVariable("id") Long id, @PathVariable("quantity") int quantity) {
        // Code pour mettre à jour la quantité totale de produit dans la base de données
         produitService.UpdateQuantite(id,quantity);

        // Retournez une réponse avec le statut OK si la mise à jour est réussie
        return ResponseEntity.ok("La quantité a été mise à jour avec succès.");
    }

}
