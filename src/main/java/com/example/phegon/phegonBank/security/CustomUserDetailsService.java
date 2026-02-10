package com.example.phegon.phegonBank.security;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.auth_user.repository.UserRepository;
import com.example.phegon.phegonBank.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("Email Not Found"));
        return AuthUser.builder()
                .user(user).build();
    }
}
