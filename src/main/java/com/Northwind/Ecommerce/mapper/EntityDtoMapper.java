package com.Northwind.Ecommerce.mapper;

import com.Northwind.Ecommerce.dto.*;
import com.Northwind.Ecommerce.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    //user entity to user dto
    
    public UserDto mapUserToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setName(user.getName());
        return userDto;
    }

    //Address to Dto

    public AddressDto mapAddressToDto(Address address){
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setCity(address.getCity());
        addressDto.setStreet(address.getStreet());
        addressDto.setCountry(address.getCountry());
        addressDto.setZipcode(address.getZipcode());
        return addressDto;
    }

    //Category to Dto

    public CategoryDto mapCategoryToDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    //OrderItem to Dto
    public OrderItemDto mapOrderItemToDto(OrderItem orderItem){
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setStatus(orderItemDto.getStatus());
        orderItemDto.setCreatedAt(orderItem.getCreatedAt());
        return orderItemDto;
    }

    //Product to Dto
    public ProductDto mapProductToDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(productDto.getName());
        productDto.setDescription(productDto.getDescription());
        productDto.setPrice(product.getPrice());
        /*productDto.setImageUrl(product.getImageUrl());*/
        return productDto;
    }

    //user to dto plus address

    public UserDto mapUserToDtoPlusAddress(User user){
        UserDto userDto = mapUserToDto(user);
        if(user.getAddress() !=null){
            AddressDto addressDto = mapAddressToDto(user.getAddress());
            userDto.setAddress(addressDto);
        }
        return userDto;
    }

    //orderItem to dto plus product

    public OrderItemDto mapOrderItemToDtoPlusProduct(OrderItem orderItem){
        OrderItemDto orderItemDto = mapOrderItemToDto(orderItem);
        if(orderItem.getProduct() != null){
            ProductDto productDto = mapProductToDto(orderItem.getProduct());
            orderItemDto.setProduct(productDto);
        }
        return orderItemDto;
    }

    //orderItem to dto plus product and user

    public OrderItemDto mapOrderItemToDtoPlusProductAndUser(OrderItem orderItem){
        OrderItemDto orderItemDto = mapOrderItemToDtoPlusProduct(orderItem);
        if(orderItem.getUser() != null){
            UserDto userDto = mapUserToDtoPlusAddress(orderItem.getUser());
            orderItemDto.setUser(userDto);

        }
        return orderItemDto;
    }

    // User to dto with orderItem and address history

    public UserDto mapUserToDtoPlusAddressAndOrderHistory(User user){
        UserDto userDto = mapUserToDto(user);
        if(user.getOrderItemsList()!= null && !user.getOrderItemsList().isEmpty()){
            userDto.setOrderItemList(user.getOrderItemsList().
                    stream().
                    map(this::mapOrderItemToDtoPlusProduct).
                    collect(Collectors.toList()));

        }
        return userDto;
    }
}
