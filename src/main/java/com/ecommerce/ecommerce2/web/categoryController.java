package com.ecommerce.ecommerce2.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Service.CategoryService;

@Controller
public class categoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/category")
    public String categories(Model model){

        List<Category> categorList=categoryService.findAlList();
        model.addAttribute("categorie", categorList);
        model.addAttribute("size", categorList.size());
        model.addAttribute("titre", "category");
        model.addAttribute("categoryNouveau", new Category());
        return "category";
    }

    @PostMapping("/admin/add-category")
    public String add(@ModelAttribute("categoryNouveau") Category category,RedirectAttributes attributes){
        try{
            categoryService.save(category);
            attributes.addFlashAttribute("Success", "Ajout Reussie");
            
        }catch(DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("Failed","Degage Erruer du a la Duplication");
        }
        catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("Failed", "Pas reussie Degage");
        }


        return "redirect:/admin/category";
    }

    @RequestMapping(value="/admin/findById", method={RequestMethod.PUT, RequestMethod.GET})
    @ResponseBody
    public Category findById(Long id){
        System.out.println("lol");
        return categoryService.findId(id);
    }

    @GetMapping("/admin/update-category")
    public String update(Category category, RedirectAttributes attributes){
        try{
            categoryService.update(category);
            attributes.addFlashAttribute("Success", "modification accorde");
        }catch(DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "erreur de modif degage");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "erreur de dagage");

        }

        return "redirect:/admin/category";
    }
    

    @RequestMapping(value="/admin/delete-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id, RedirectAttributes attributes){
        try{
            categoryService.deleteById(id);
            attributes.addFlashAttribute("Success", "Suppression reussie");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("Failed", "Suppression Errone Degage");
        }

        return "redirect:/admin/category";
    }

    @RequestMapping(value = "/admin/enable-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id, RedirectAttributes attributes){
        try {
            categoryService.EnableId(id);
            attributes.addFlashAttribute("Success", "Activation Reussie Tintamar");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("Failed", "failed Enabled");
        }

        return "redirect:/admin/category";
    }
}
