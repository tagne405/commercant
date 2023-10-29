package com.ecommerce.ecommerce2.web;

import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Service.CategoryService;
import com.ecommerce.ecommerce2.Service.ProduitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProduitController {

    @Autowired
    ProduitService produitService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/admin/listeProduit")
    public String listeProduit(Model model, Principal principal, HttpSession session){
        if(principal == null){
            return "redirect:/login";
        }
        session.setAttribute("username", principal.getName());
        model.addAttribute("titre","liste de produit");
        List<Produit> produit = produitService.findAllProduit();
        model.addAttribute("produit",produit);
        model.addAttribute("size", produit.size());
        return "listeProduit";
    }

//    @GetMapping("/user/index")
//    public String index(Model model,
//                        @RequestParam(name="page",defaultValue = "0" )int page,
//                        @RequestParam(name = "size",defaultValue="4") int size,
//                        @RequestParam(name = "Keyword",defaultValue="") String kw
//    ){
//        Page<patient> pagePatients=patientrepository.findByNomContains(kw,PageRequest.of(page, size));
//        model.addAttribute("listePatients",pagePatients.getContent());
//        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("Keyword", kw);
//        return "patients";
//    }

    //pagination
    @GetMapping("/admin/listeProduit/{pageNo}")
    public String produitPage(@PathVariable("pageNo")int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<Produit> produit = produitService.pageProduit(pageNo);
        model.addAttribute("titre","liste de produit");
        model.addAttribute("size", produit.getSize());
        model.addAttribute("totalPage", produit.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("produit",produit);
        return "listeProduit";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchProduit(@PathVariable("pageNo")int pageNo,
                                @RequestParam("keyword") String keyword,
                                Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        AcceuilController.rechercheProduit(pageNo, keyword, model, produitService);
        return "resultatproduit";
    }

    @GetMapping("/admin/newProduit")
    public String newProduit(Model model){   
        List<Category> categories= categoryService.findAllByActivated();
        Produit produit=new Produit();
        model.addAttribute("categorie",categories);
        model.addAttribute("produit", produit);
        return "createProduit";
    }

    @PostMapping("/admin/saveProduct")
    public String saveProduit(@ModelAttribute("produit")Produit produit,
                              @RequestParam("image")MultipartFile imageProduct,
                              RedirectAttributes attributes){
        if (produit == null) {
            attributes.addFlashAttribute("error", "Le produit est manquant.");
            return "redirect:/admin/listeProduit";
        }
        if (imageProduct == null || imageProduct.isEmpty()) {
            attributes.addFlashAttribute("failed", "L'image du produit est manquante.");
            return "redirect:/admin/listeProduit/0";
        }
        try {
            produitService.save(imageProduct, produit);
            attributes.addFlashAttribute("success", "Le produit a été enregistré avec succès.");
        } catch (Exception e) {
            attributes.addFlashAttribute("failed", "Erreur lors de l'enregistrement du produit : " + e.getMessage());
        }                        
                                
        return "redirect:/admin/listeProduit/0";
    }


    @GetMapping("/admin/updateProduit/{id}")
    public String updateProdduit(@PathVariable("id") Long id, Model model ){
        model.addAttribute("title", "update produit");
        List<Category> categories = categoryService.findAllByActivated();
        Produit produit= produitService.findById(id);
        model.addAttribute("categories",categories);
        model.addAttribute("produit",produit);
        return "updateProduit";

    }

    @PostMapping("/admin/updateProduit/{id}")
    public String update(@PathVariable("id") Long id, 
                         @ModelAttribute("produit") Produit produit,
                         @RequestParam("image")MultipartFile image,
                         RedirectAttributes attributes){
        
        try{
            produitService.update(image, produit);
            attributes.addFlashAttribute("success", "Modification Reussie Salot");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Modif Echouer Batard");
        }
        return "redirect:/admin/listeProduit/0";

    }

    @RequestMapping(value = "/admin/enabledProduit/{id}/{page}", method = {RequestMethod.PUT,RequestMethod.GET})
    public String enabledProduit(@PathVariable("id")Long id,@PathVariable("page")Long page, RedirectAttributes attributes){

        try {
            produitService.enabled(id);
            attributes.addFlashAttribute("success", "Activation reussi");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Erreur D'actiavtion vas Chercher L'erreur");
        }

        return "redirect:/admin/listeProduit/"+ page;

    }

    @RequestMapping(value = "/admin/deleteProduit/{id}/{page}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduit(@PathVariable("id") Long id,@PathVariable("page")Long page, RedirectAttributes attributes){
        try {
            produitService.deleteById(id);
            attributes.addFlashAttribute("success", "suppression reussie Bao");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "suppression echouer vas Chercher l'erreur");
        }

        return "redirect:/admin/listeProduit/"+ page;
    }


    /*categoriers and product */

    @GetMapping("/admin/produit")
    public String products(Model model){
        List<Produit> produits = produitService.find();
        model.addAttribute("produits", produits);
        return "acceuil2";
    }

    @GetMapping("/findProduit/{id}")
    public String findProduitById(@PathVariable("id")Long id,Model model){
        Produit produit = produitService.getProduitById(id);
        List<Category> categories = categoryService.findAlList();
        model.addAttribute("categories", categories);
        model.addAttribute("detailproduit", produit);
        return "detail";
    }


    @GetMapping("/produitInCategory/{id}")
    public String findProduitByCategory(@PathVariable("id") Long categoryId,Model model){
        Category category = categoryService.findId(categoryId);
        List<Category> categories = categoryService.findAlList();
        List<Produit> produits = produitService.findProduitInCategory(categoryId);
        model.addAttribute("categories", categories);
        model.addAttribute("ProduitCategory", produits);
        model.addAttribute("categoryProduit", category);
        return "categoryInProduit";
    }


//    @GetMapping("/")
//    public String CountCategory(Model model){
////        Long produits = categoryService.countCategories();
////        System.out.println("lol");
//////        Integer nbreCategory = produits.intValue();
////        model.addAttribute("nbreCategory", produits);
//        return "Acceuil2";
//    }

    //count Product
    public Long countproduct(){
        return produitService.countProduct();
    }

    //compare products
    @GetMapping("/compare/{id}/{idcat}")
    public String compareProduct(@PathVariable("id")Long id,@PathVariable("idcat")Long categoryId, Model model){
        Produit produit = produitService.getProduitById(id);
        List<Produit> listProduit = produitService.findProduitInCategory(categoryId);
        List<Category> categories = categoryService.findAlList();
        Category category = categoryService.findId(categoryId);
        model.addAttribute("listProduit",listProduit);
        model.addAttribute("categoryProduit",category);
        model.addAttribute("produitCategory",produit);
        model.addAttribute("categories",categories);
        return "compareProduit";
    }

    //find Produit by description or name
//    @GetMapping("/searchProduit/{}")
//    public String searchProduit()
}
