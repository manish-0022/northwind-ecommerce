package com.Northwind.Ecommerce.Controller;

import com.Northwind.Ecommerce.Exception.InvalidCredentialException;
import com.Northwind.Ecommerce.Service.Interface.ProductService;
import com.Northwind.Ecommerce.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createProduct(
            @RequestParam Long categoryId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price ) throws IOException {

        if(categoryId == null || name.isEmpty() || description.isEmpty() || price == null){
            throw new InvalidCredentialException("All fields are required");
        }
        return ResponseEntity.ok(productService.createProduct(categoryId, name, description, price));
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(

            @RequestParam Long productId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal price) throws IOException {
        return ResponseEntity.ok(productService.updateProduct(productId, categoryId, name, description, price));
    }


    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long productId){
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping("/get-all-product-id/{productId}")
    public ResponseEntity<Response> getProductById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProduct() );
    }

    @GetMapping("/get-all-category-id/{categoryId}")
    public ResponseEntity<Response> getProductByCategory(@PathVariable Long categoryId){
        return ResponseEntity.ok(productService.getProductByCategory(categoryId));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchForProduct(@RequestParam String searchValue){
        return ResponseEntity.ok(productService.searchProduct(searchValue));
    }











}
