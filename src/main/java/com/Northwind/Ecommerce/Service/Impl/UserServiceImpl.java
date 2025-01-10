package com.Northwind.Ecommerce.Service.Impl;

import com.Northwind.Ecommerce.Exception.InvalidCredentialException;
import com.Northwind.Ecommerce.Exception.NotFoundException;
import com.Northwind.Ecommerce.Repository.UserRepo;
import com.Northwind.Ecommerce.Service.Interface.UserService;
import com.Northwind.Ecommerce.dto.LoginRequest;
import com.Northwind.Ecommerce.dto.Response;
import com.Northwind.Ecommerce.dto.UserDto;
import com.Northwind.Ecommerce.entity.User;
import com.Northwind.Ecommerce.enums.UserRole;
import com.Northwind.Ecommerce.mapper.EntityDtoMapper;
import com.Northwind.Ecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role = UserRole.USER;

        if(registrationRequest.getRole()!= null && registrationRequest.getRole().equalsIgnoreCase("admin")){
            role = UserRole.ADMIN;
        }
        User user = User.builder().
                name(registrationRequest.getName()).
                email(registrationRequest.getEmail()).
                password(passwordEncoder.encode(registrationRequest.getPassword())).
                phoneNumber(registrationRequest.getPhoneNumber()).
                role(role).build();

        User saveUser = userRepo.save(user);

        UserDto userDto = entityDtoMapper.mapUserToDto(saveUser);
        return Response.builder().status(200).message("User created successfully").
                user(userDto)
                .build();
    }

    @Override
    public Response loginRequest(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException("Email not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);
        return Response.builder().status(200).message("User Successfully logged in").
                token(token).
                expirationTime("6 month").role(user.getRole().name()).build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream()
                .map(entityDtoMapper::mapUserToDto)
                .toList();

        return Response.builder()
                .status(200).message("Successful").userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User Email is: " +email);
        return userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"));

    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);
        return Response.builder()
                .status(200).user(userDto)
                .build();
    }
}
