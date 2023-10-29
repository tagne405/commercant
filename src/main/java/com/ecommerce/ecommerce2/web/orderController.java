package com.ecommerce.ecommerce2.web;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.ecommerce.ecommerce2.Entity.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.OrderService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class orderController {
    
    private final CustomerService customerService;

    private final OrderService orderService;

    public orderController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping("/testons")
    public ModelAndView processPayment(@RequestParam("user_email") String userEmail,
                                       @RequestParam("amount") String amount,
                                       @RequestParam("cart_reference") String cartReference,
                                       @RequestParam("currency") String currency,
                                       @RequestParam("callback") String callback,
                                       HttpServletResponse response) {

        // Récupérer la date système
        LocalDateTime now = LocalDateTime.now();

        // Formatter la date selon le format souhaité
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);

        // Concaténer la date avec la valeur de la référence
        String cartReferenceWithDate = formattedDateTime + cartReference;

        String apiUrl = "https://api.notchpay.co/payments/initialize";
//        String publicKey = "sb.kMUqExOBxstY0CC6IAj5b3letgDVLMqqb5IPqnsSFL9qQ9aLd6Iq0EoNL5ReLQz21CWlcFKahIuPoRbmESwXF6RK2sdCTZEGwDLM2WUhvzeovtOhrNPxk2ILISAVg"; // Remplacez par votre clé publique NotchPay
        String publicKey = "sb.CaxACZeTB1YLycgqGaAD2fcW9DYTcQiXirGx9buiF8U7iHcL0m6hfgnk6qoTEFuivOzd3w147bZndiNrbjcd51H8m7VUGFavjJSzNeGpiSlnYqwSciNoIdGQUD7qs"; // Remplacez par votre clé publique NotchPay

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", publicKey);

        // Création du corps de la requête
        NotchPayRequestData requestData = new NotchPayRequestData(userEmail, amount, cartReferenceWithDate, currency, callback);
        HttpEntity<NotchPayRequestData> requestEntity = new HttpEntity<>(requestData, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NotchPayResponseData> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                NotchPayResponseData.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String authorizationUrl = responseEntity.getBody().getAuthorization_url();

            // Créer un cookie pour stocker l'identifiant de l'utilisateur
            Cookie cookie = new Cookie("username", userEmail);
            cookie.setMaxAge(24 * 60 *60); // Durée de validité du cookie en secondes (ici, 15min)
            cookie.setPath("/"); // Chemin sur lequel le cookie est valide (ici, la racine du domaine)
            response.addCookie(cookie);

            return new ModelAndView("redirect:" + authorizationUrl);
        } else {
            // Gérer les erreurs si nécessaire
            return new ModelAndView("redirect:/payment-failure"); // Rediriger vers une page d'échec de paiement
        }
    }

    @GetMapping("/art")
    public String handleCartCallback(@RequestParam("reference") String reference,
                                     HttpServletRequest request,
                                     @RequestParam("trxref") String trxref,
                                     @RequestParam("notchpay_trxref") String notchpayTrxref,
                                     @RequestParam("status") String status,
                                     RedirectAttributes redirectAttributes) {

        Cookie[] cookies2 = request.getCookies();
        String username = null;
        if (cookies2 != null) {
            for (Cookie cookie : cookies2) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        // Créer une instance de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Configurer les en-têtes de la requête
        HttpHeaders headers = new HttpHeaders();
        // Ajouter les en-têtes nécessaires, si nécessaire

        if (status.equals("complete")) {
            // Le paiement a été effectué
            String message = "Paiement effectué mon petit ";
            // Créer un objet HttpEntity avec les en-têtes
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

            // Effectuer une requête GET vers l'endpoint /checkout avec les données renvoyées
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8085/order?message={message}",
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    message
            );

            // Renvoyer la réponse renvoyée par l'endpoint /checkout
            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute("reference", reference);
            return "redirect:/order";
        } else {
            // Le callback est différent de "callback"
            // Le paiement a été annulé
            String message = "Tu aime trop ton Argent";
            // Créer un objet HttpEntity avec les en-têtes
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

            // Effectuer une requête GET vers l'endpoint /checkout avec les données renvoyées
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8085/checkout?message={message}",
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    message
            );
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/checkout";
        }
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal,
                           HttpServletRequest request,
                           @RequestParam(required = false) String reference) {
        System.out.println(reference);
        Cookie[] cookies2 = request.getCookies();
        String username = null;
        if (cookies2 != null) {
            for (Cookie cookie : cookies2) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        if (username == null) {
            return "redirect:/login";
        }

        Customer customer = customerService.findByUsername(username);
        // ajoute la methode trim()
        if (customer.getTelephone() == null || customer.getAddress() == null || customer.getCity() == null) {

            model.addAttribute("customer", customer);
            model.addAttribute("error", "tu doit renseigner les informations ");
            return "monCompte";
        } else {
            model.addAttribute("success", "ce bon Bao tu peut maintenant Payer");

            // Ajout de nouvelle methode
            model.addAttribute("customer", customer);
            ShoppingCart cart = customer.getShoppingcart();
            model.addAttribute("cart", cart);

            // affiche du message pour le paiement
            String message = (String) model.asMap().get("message");
            model.addAttribute("message", message);
        }

        return "payement";
    }


    @GetMapping("/order")
    public String order(Principal principal, Model model,
                        HttpServletRequest request,
                        HttpSession session,
                        RedirectAttributes redirectAttributes){

        Cookie[] cookies3 = request.getCookies();
        String username = null;
        if (cookies3 != null) {
            for (Cookie cookie : cookies3) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username == null) {
            return "redirect:/login";
        }

//        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        List<Order> orderList = customer.getOrder();
        model.addAttribute("orders", orderList);

        //enregistrer un Order
        Customer customer2 = customerService.findByUsername(username);
        ShoppingCart cart = customer2.getShoppingcart();
        String reference = (String) model.asMap().get("reference");
        orderService.saveOrder(cart,reference);


        String message = (String) model.asMap().get("message");
        model.addAttribute("message",message);
        System.out.println(message);

        return "order";
    }


    @GetMapping("/saveOrder")
    public String saveOrder(HttpServletRequest request,
                            Principal principal,Model model,
                            RedirectAttributes redirectAttributes){
        Cookie[] cookies4 = request.getCookies();
        String username = null;
        if (cookies4 != null) {
            for (Cookie cookie : cookies4) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username == null) {
            return "redirect:/login";
        }
//        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getShoppingcart();

//        String message = (String) model.asMap().get("message");
//        model.addAttribute("message",message);
//
//        redirectAttributes.addFlashAttribute("message", message);

        //mise en commentaire de cet methode car methode saveOrder plus utiliser
//        orderService.saveOrder(cart);
        return "redirect:/order";
    }


    /*Order for Admin*/
//    @GetMapping("/admin/order")
//    public String ListOrder(Principal principal, Model model){
//        if(principal == null){
//            return "redirect:/login";
//        }
//        List<Order> order = orderService.findAllOrder();
//        model.addAttribute("orders",order);
//        return "ListOrder";
//    }


    @GetMapping("/admin/order")
    public String getOrder(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Order> order = orderService.findAllOrder();
        model.addAttribute("orders", order);

        List<OrderDetail> orderDetails = orderService.findOrderDetail();
        model.addAttribute("orderDetails", orderDetails);

//        OrderDetail orderDetail = orderService.findOrderDetail(id);
        if (order.isEmpty()) {
            redirectAttributes.addFlashAttribute("vide", "La liste des Commandes est vide");
        }

        return "ListOrder";
    }

//    @RequestMapping(value = "/admin/orderById", method={RequestMethod.PUT, RequestMethod.GET})
//    @ResponseBody
//    public OrderDetail getOrderDetail(@RequestParam("id") Long id){
//        System.out.println(id);
//        OrderDetail orderDetail = orderService.findOrderDetail(id);
//        System.out.println(orderDetail.getId());
//        return orderService.findOrderDetail(id);
//    }

    @GetMapping("/admin/orderDetailById")
//    @ResponseBody
    public String getOrderById(@RequestParam("id")Long id,Model model) {
        List<OrderDetail> orderDetail = orderService.findOrderDetailById(id);
        model.addAttribute("orderDetail",orderDetail);
        System.out.println("detail");
        return "ListorderDetail";
    }

    //les commandes valides
    @GetMapping("/admin/saveOrder")
    public String valideOrder(@RequestParam("id")Long id){

         orderService.acceptOrder(id);
         return "redirect:/admin/order";
    }

    //affiche les commandes valides
    @GetMapping("/admin/validedOrder")
    public String OrderValid(Model model){
        List<Order> orderDetailValided = orderService.findOrderValided();

        model.addAttribute("orderDetailValided",orderDetailValided);
        return "ListOrderValided";
    }

    //Factures
    @GetMapping("/generate-invoice-pdf")
    public ResponseEntity<byte[]> generateInvoicePDF() throws IOException {
        // Récupérez les données nécessaires pour générer la facture
//        Invoice invoice = invoiceService.generateInvoice();

        // Créez un document PDF
        PDDocument document = new PDDocument();

        // Ajoutez une page au document
        PDPage page = new PDPage();
        document.addPage(page);

        // Créez un contenu pour la page
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Ajoutez le contenu de la facture au PDF
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Invoice");
        contentStream.newLineAtOffset(0, -20);
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText("Invoice Number: " + 3);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Customer: " + 4);
        // Ajoutez d'autres informations de facture
        contentStream.endText();

        // Fermez le contenu de la page et le document
        contentStream.close();

        // Convertissez le document en tableau de bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        // Spécifiez les en-têtes de la réponse HTTP pour le téléchargement du PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("invoice.pdf").build());

        // Retournez la réponse avec le PDF
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePDF() throws IOException {
        // Création d'un nouveau document PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // Initialisation du contenu de la page
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Définition des dimensions et des marges du tableau
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;
        float tableHeight = 100f; // Hauteur du tableau
        float cellMargin = 10f;

        // Définition du contenu du tableau
        String[][] content = {
                {"Header 1", "Header 2", "Header 3", "Header 4"},
                {"Row 1 - Col 1", "Row 1 - Col 2", "Row 1 - Col 3", "Row 1 - Col 4"},
                {"Row 2 - Col 1", "Row 2 - Col 2", "Row 2 - Col 3", "Row 2 - Col 4"},
                {"Row 3 - Col 1", "Row 3 - Col 2", "Row 3 - Col 3", "Row 3 - Col 4"}
        };

        // Définition des dimensions des cellules
        float rowHeight = 20f;
        float tableWidthPercentage = 100f;
        float[] columnWidths = {25f, 25f, 25f, 25f};

        // Dessin de l'en-tête du tableau
        drawTableHeader(contentStream, yStart, tableWidth, tableWidthPercentage, cellMargin, rowHeight, columnWidths);

        // Dessin des lignes du tableau
        drawTableContent(contentStream, yStart, tableWidth, tableWidthPercentage, cellMargin, rowHeight, columnWidths, content);

        // Fermeture du contenu de la page et du document
        contentStream.close();

        // Conversion du document en tableau de bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        // Spécification des en-têtes de la réponse HTTP pour le téléchargement du PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "table_example.pdf");

        // Retour de la réponse avec le PDF
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private void drawTableHeader(PDPageContentStream contentStream, float y, float tableWidth,
                                 float tableWidthPercentage, float cellMargin, float rowHeight, float[] columnWidths) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

        float yStart = y;
        float currentY = yStart;

        for (int i = 0; i < columnWidths.length; i++) {
            float currentX = tableWidth * (i / tableWidthPercentage) + cellMargin;

            String header = "Header " + (i + 1);
            contentStream.beginText();
            contentStream.newLineAtOffset(currentX, currentY);
            contentStream.showText(header);
            contentStream.endText();
        }

        currentY -= rowHeight;
    }

    private void drawTableContent(PDPageContentStream contentStream, float y, float tableWidth,
                                  float tableWidthPercentage, float cellMargin, float rowHeight,
                                  float[] columnWidths, String[][] content) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 10);

        float yStart = y;
        float currentY = yStart;

        for (int i = 0; i < content.length; i++) {
            currentY -= rowHeight;

            float currentX = tableWidth * (i / tableWidthPercentage) + cellMargin;

            for (int j = 0; j < content[i].length; j++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(currentX, currentY);
                contentStream.showText(content[i][j]);
                contentStream.endText();

                currentX += columnWidths[j];
            }
        }
    }

    @GetMapping("/facture")
    public String previewFacture(@RequestParam("id")Long id, Model model) {
        List<OrderDetail> orderDetail = orderService.findOrderDetailById(id);// Chargez les données de la facture depuis la source de données
        Order order = orderService.findOrderById(id);
        model.addAttribute("orders", order);
        model.addAttribute("detail", orderDetail);
        return "facture";
    }
}

