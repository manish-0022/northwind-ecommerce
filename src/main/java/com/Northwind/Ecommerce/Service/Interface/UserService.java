package com.Northwind.Ecommerce.Service.Interface;

import com.Northwind.Ecommerce.dto.LoginRequest;
import com.Northwind.Ecommerce.dto.Response;
import com.Northwind.Ecommerce.dto.UserDto;
import com.Northwind.Ecommerce.entity.User;

public interface UserService {

    Response registerUser(UserDto registrationRequest);

    Response loginRequest(LoginRequest loginRequest);

    Response getAllUsers();

    User getLoginUser();

    Response getUserInfoAndOrderHistory();
}
