package com.Northwind.Ecommerce.Service.Interface;

import com.Northwind.Ecommerce.dto.CategoryDto;
import com.Northwind.Ecommerce.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryRequest);

    Response updateCategory(Long categoryId, CategoryDto categoryRequest);

    Response getAllCategory();

    Response getAllCategoryById(Long categoryId );

    Response deleteCategory(Long categoryId);
}
