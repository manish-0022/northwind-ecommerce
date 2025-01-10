package com.Northwind.Ecommerce.security;

import com.Northwind.Ecommerce.Exception.NotFoundException;
import com.Northwind.Ecommerce.Repository.UserRepo;
import com.Northwind.Ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).
                orElseThrow(()-> new NotFoundException("User/ email is not found"));

        return AuthUser.builder().user(user)
                .build();
    }
}
