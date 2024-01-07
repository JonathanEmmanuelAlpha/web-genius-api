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
                            "/",
                            "/article",
                            "/account/signup",
                            "/account/signup/consumer",
                            "css/**",
                            "/img/**",
                            "libs/**"
                    ).permitAll()
                    .requestMatchers("/dashboard/**").hasAuthority("AUTHOR")
                    .requestMatchers("/dashboard/authors").hasAuthority("ADMIN")
                    .requestMatchers("/dashboard/authors-requests").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(
                form -> form
                    .loginPage("/account/login")
                    .loginProcessingUrl("/account/login")
                        .failureForwardUrl("/account/login-error")
                    .defaultSuccessUrl("/account/profile")
                    .permitAll()
            )
            .logout(
                logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/account/logout"))
                    .logoutSuccessUrl("/account/login")
                    .permitAll()
            );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}