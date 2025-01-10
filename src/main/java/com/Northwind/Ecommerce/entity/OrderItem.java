package com.Northwind.Ecommerce.entity;

import com.Northwind.Ecommerce.Repository.OrderItemRepo;
import com.Northwind.Ecommerce.Repository.OrderRepo;
import com.Northwind.Ecommerce.Repository.ProductRepo;
import com.Northwind.Ecommerce.Service.Interface.UserService;
import com.Northwind.Ecommerce.enums.OrderStatus;
import com.Northwind.Ecommerce.mapper.EntityDtoMapper;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private BigDecimal price;

    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    
}
