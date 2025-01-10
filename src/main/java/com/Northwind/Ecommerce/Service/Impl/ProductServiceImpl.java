package com.Northwind.Ecommerce.Service.Impl;

import com.Northwind.Ecommerce.Exception.NotFoundException;
import com.Northwind.Ecommerce.Repository.CategoryRepo;
import com.Northwind.Ecommerce.Repository.ProductRepo;
import com.Northwind.Ecommerce.Service.Interface.ProductService;
import com.Northwind.Ecommerce.dto.ProductDto;
import com.Northwind.Ecommerce.dto.Response;
import com.Northwind.Ecommerce.entity.Category;
import com.Northwind.Ecommerce.entity.Product;
import com.Northwind.Ecommerce.mapper.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createProduct(Long categoryId, /*MultipartFile image,*/ String name, String description, BigDecimal price) throws IOException {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);

      /*  if (image != null && !image.isEmpty()) {
            product.setImageUrl(image.getBytes());
        }*/

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("Product created successfully")
                .build();

    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, /*MultipartFile image,*/ String name, String description, BigDecimal price) throws IOException {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("product not found"));

        Category category = null;


        if(categoryId != null){
            category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
        }


        if(category != null) product.setCategory(category);
        if(name != null) product.setName(name);
        if(price != null) product.setPrice(price);
        if(description != null) product.setDescription(description);
       /* if (image != null && !image.isEmpty()){
             product.setImageUrl(image.getBytes());
        }*/

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();
    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("Product not found"));
        productRepo.delete(product);
        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("Product not found"));
        ProductDto productDto = entityDtoMapper.mapProductToDto(product);
        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProduct() {
        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDto)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productList)
                .build();
    }

    @Override
    public Response getProductByCategory(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if(products.isEmpty()){
            throw new NotFoundException("No product found for this category");
        }

        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDto)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);
        if(products.isEmpty()){
            throw new NotFoundException("No product found");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDto)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200).message("search product is listed below.")
                .productList(productDtoList)
                .build();


    }
}
