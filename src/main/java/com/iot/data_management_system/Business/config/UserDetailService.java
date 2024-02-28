package com.iot.data_management_system.Business.config;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.abstracts.UserService;
import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailService {
    private final UserService userService;

    public UserDetails findUserByEmail(String email){
        User user = userService.findByEmail(email);
        GrantedAuthority authority = getUserAuthority(user.getRole());
        return buildUserForAuthentication(user, authority);
    }

    private GrantedAuthority getUserAuthority(Role userRole) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.getRole());
        return grantedAuthority;
    }

    private UserDetails buildUserForAuthentication(User user, GrantedAuthority authority) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(authority));
    }

}
