package com.example.loginJWt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class WebSercurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
}
