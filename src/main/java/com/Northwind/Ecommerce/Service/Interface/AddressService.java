package com.Northwind.Ecommerce.Service.Interface;

import com.Northwind.Ecommerce.dto.AddressDto;
import com.Northwind.Ecommerce.dto.Response;

public interface AddressService {

    Response saveAndUpdateAddress(AddressDto addressDto);
}
