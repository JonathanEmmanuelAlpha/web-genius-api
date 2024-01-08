package com.webgenius.webgeniusapi.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http
            .authorizeHttpRequests((authorize) ->
                authorize
                    .requestMatchers(
                            "/api/v1/auth/signup",
                            "/api/v1/auth/login-error",
                            "/api/v1/articles/all"
                    ).permitAll()
                    //.requestMatchers("/api/v1/dashboard/**").hasAuthority("AUTHOR")
                    //.requestMatchers("/api/v1/dashboard/authors").hasAuthority("ADMIN")
                    //.requestMatchers("/api/v1/dashboard/authors-requests").hasAuthority("ADMIN")
                    .anyRequest().permitAll()
            );
            /*.formLogin(
                form -> form
                    .loginPage("/api/v1/auth/login")
                    .loginProcessingUrl("/api/v1/auth/login")
                        .failureForwardUrl("/api/v1/auth/login-error")
                    .defaultSuccessUrl("/api/v1/auth/profile")
                    .permitAll()
            )
            .logout(
                logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout"))
                    .logoutSuccessUrl("/api/v1/auth/login")
                    .permitAll()
            );*/

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}