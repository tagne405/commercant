package com.ecommerce.ecommerce2.Service;


import java.util.Base64;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Repository.ProduitRepository;
import com.ecommerce.ecommerce2.utils.ImageUpload;

@Service
public class ProduitService {
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private ImageUpload imageUpload;
    
    public Produit save(MultipartFile imageProduct, Produit produit){
       try {
        
        Produit produit2=new Produit();
        if (imageProduct == null){
            produit2.setPhoto(null);
        }else{
            if(imageUpload.uploadImage(imageProduct)){
                System.out.println("update Success");
            }
            
            produit2.setPhoto(Base64.getEncoder().encodeToString(imageProduct.getBytes()));

            
        }
            produit2.set_activated(true);
            produit2.set_delete(false);
            produit2.setCategory(produit.getCategory());
            produit2.setDescription(produit.getDescription());
            produit2.setNom(produit.getNom());
            produit2.setPrix(produit.getPrix());
            produit2.setQuantite(produit.getQuantite());
            return produitRepository.save(produit2);
             
       } catch (Exception e) {
        e.printStackTrace();
        
        return null;
       }
    }

    public List<Produit> findAllProduit(){
         return produitRepository.findAll();
     }

    public Produit update(MultipartFile imageProduit, Produit produit){
        try{
            Produit produit2= produitRepository.findById(produit.getId()).get();
            if(imageProduit == null){
                produit2.setPhoto(produit.getPhoto());
            }else{
                if(imageUpload.checkExisted(imageProduit) == false){
                    imageUpload.uploadImage(imageProduit);
                }
                System.out.println("image existe");
                produit2.setPhoto(Base64.getEncoder().encodeToString(imageProduit.getBytes()));
            }
            produit2.setNom(produit.getNom());
            produit2.setCategory(produit.getCategory());
            produit2.setPrix(produit.getPrix());
            produit2.setQuantite(produit.getQuantite());
            produit2.setDescription(produit.getDescription());
            // System.out.println(produit2.getNom()+ ": " + produit2.getCategory());
            return produitRepository.save(produit2);

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }   
    }

    public Produit findById(Long id){
        Produit produit = produitRepository.findById(id).get();
        produit.setId(produit.getId());
        produit.setNom(produit.getNom());
        produit.setCategories(produit.getCategories());
        produit.setPrix(produit.getPrix());
        produit.setQuantite(produit.getQuantite());
        produit.setDescription(produit.getDescription());
        produit.setPhoto(produit.getPhoto());
        produit.set_delete(produit.is_delete());
        produit.set_activated(produit.is_activated());
        return produit;
        
    }

    public void deleteById(Long id){
        Produit produit = produitRepository.findById(id).get();
        produit.set_delete(true);
        produit.set_activated(false);
        produitRepository.save(produit);
    }

    public void enabled(Long id){
        Produit produit = produitRepository.findById(id).get();
        produit.set_delete(false);
        produit.set_activated(true);
        produitRepository.save(produit);
    }

    /*Customers */
    
    public List<Produit> find(){
        return produitRepository.getAllProducts();
    }

    public Produit getProduitById(Long id){
        return produitRepository.findById(id).orElse(null);
    }

    public List<Produit> findProduitInCategory(Long categoryId){
        return produitRepository.findProduitInCategory(categoryId);
    }

    //count Product
    public Long countProduct(){
        return produitRepository.count();
    }

    public Page<Produit> searchProduit(int pageNo,String keyWord){
        Pageable pageable = PageRequest.of(pageNo,10);
        Page<Produit> produit = produitRepository.searchProduit(keyWord,pageable);
        return produit;
    }

    public Page<Produit> pageProduit(int pageNo){
        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Produit> produitPage = produitRepository.pageProduit(pageable);
        return produitPage;
    }

    //reduction de la quantite dans la bd
    public void UpdateQuantite(Long id,int qte){
        Produit produit = produitRepository.findById(id).orElse(null);
        int qte2 = produit.getQuantite();
        int qteTotal = qte2-qte;
        produit.setQuantite(qteTotal);
        produitRepository.save(produit);
    }
}
