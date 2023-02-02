package com.api.parkingcontrol.configs.security;

import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = this.repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new User(
                userModel.getUsername(),
                userModel.getPassword(),
                true,
                true,
                true,
                true,
                userModel.getAuthorities());
    }
}
