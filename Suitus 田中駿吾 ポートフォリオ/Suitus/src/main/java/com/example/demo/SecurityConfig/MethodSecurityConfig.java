package com.example.demo.SecurityConfig;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
}