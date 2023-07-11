package com.ecommerce.ecommerce2.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotchPayRequestData {
    private String email;
    private String amount;
    private String reference;
    private String currency;
    private String callback;
}
