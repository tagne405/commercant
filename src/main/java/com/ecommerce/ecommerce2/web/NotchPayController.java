package com.ecommerce.ecommerce2.web;

import com.ecommerce.ecommerce2.Entity.NotchPayRequestData;
import com.ecommerce.ecommerce2.Entity.NotchPayResponseData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NotchPayController {



    @GetMapping("/access-url")
    public ResponseEntity<String> accessUrl(@RequestParam() String reference) {
        String authorization = "sb.kMUqExOBxstY0CC6IAj5b3letgDVLMqqb5IPqnsSFL9qQ9aLd6Iq0EoNL5ReLQz21CWlcFKahIuPoRbmESwXF6RK2sdCTZEGwDLM2WUhvzeovtOhrNPxk2ILISAVg";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.notchpay.co/payments/"+reference,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        return response;
    }


}
