package com.Northwind.Ecommerce.Service.Impl;

import com.Northwind.Ecommerce.Exception.NotFoundException;
import com.Northwind.Ecommerce.Repository.OrderItemRepo;
import com.Northwind.Ecommerce.Repository.OrderRepo;
import com.Northwind.Ecommerce.Repository.ProductRepo;
import com.Northwind.Ecommerce.Service.Interface.OrderItemService;
import com.Northwind.Ecommerce.Service.Interface.UserService;
import com.Northwind.Ecommerce.dto.OrderItemDto;
import com.Northwind.Ecommerce.dto.OrderRequest;
import com.Northwind.Ecommerce.dto.Response;
import com.Northwind.Ecommerce.entity.Order;
import com.Northwind.Ecommerce.entity.OrderItem;
import com.Northwind.Ecommerce.entity.Product;
import com.Northwind.Ecommerce.entity.User;
import com.Northwind.Ecommerce.enums.OrderStatus;
import com.Northwind.Ecommerce.mapper.EntityDtoMapper;
import com.Northwind.Ecommerce.specifications.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {
        User user = userService.getLoginUser();
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(oderItemRequest-> {
            Product product = productRepo.findById(oderItemRequest.getProductId())
                    .orElseThrow(()-> new NotFoundException("product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItem.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(oderItemRequest.getQuantity())));
            orderItem.setOrderStatus(OrderStatus.PENDING);
            orderItem.setUser(user);
            return orderItem;

        }).collect(Collectors.toList());

        BigDecimal totalPrice = orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) >0
                               ? orderRequest.getTotalPrice()
                               : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderItemsList(orderItems);
        order.setTotalPrice(totalPrice);

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        orderRepo.save(order);

        return Response.builder()
                .status(200)
                .message("order was placed successfully")
                .build();

    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId)
                .orElseThrow(()-> new NotFoundException("order item not found"));

        orderItem.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepo.save(orderItem);

        return Response.builder()
                .status(200)
                .message("order status updated successfully")
                .build();


    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {

        Specification<OrderItem> specification =  Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepo.findAll(specification, pageable);
        if(orderItemPage.isEmpty()){
            throw new NotFoundException("No order found");
        }

        List<OrderItemDto> orderItemDto = orderItemPage.getContent().stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDto)
                .totalPage(orderItemPage.getTotalPages())
                .totalElement(orderItemPage.getTotalElements())
                .build();
    }
}
