package com.Northwind.Ecommerce.dto;

import com.Northwind.Ecommerce.entity.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {
    private BigDecimal totalPrice;

    private List<oderItemRequest> items;

    private Payment paymentInfo;
}
