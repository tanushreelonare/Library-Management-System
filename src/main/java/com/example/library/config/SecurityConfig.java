package com.example.library.config;

import com.example.library.security.CustomAuthenticationSuccessHandler;
import com.example.library.security.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    private final CustomAuthenticationSuccessHandler
            authenticationSuccessHandler;


    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            CustomAuthenticationSuccessHandler authenticationSuccessHandler) {

        this.userDetailsService = userDetailsService;

        this.authenticationSuccessHandler =
                authenticationSuccessHandler;
    }

    // AUTHENTICATION PROVIDER

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }

    // SECURITY FILTER CHAIN

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                // CSRF

                .csrf(csrf -> csrf.disable())

                // AUTHORIZATION

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(

                                "/",

                                "/student",

                                "/librarian",

                                "/login",

                                "/forgot-password",

                                "/update-password",

                                "/css/**",

                                "/js/**",

                                "/images/**",

                                "/uploads/**",

                                "/error"

                        )
                        .permitAll()


                        // STUDENT PAGES

                        .requestMatchers(

                                "/student-dashboard",

                                "/student-dashboard/**",

                                "/student/profile",
                                
                                "/student/profile/**"

                        )
                        .hasRole("STUDENT")


                        // LIBRARIAN PAGES

                        .requestMatchers(

                                "/librarian-dashboard",

                                "/librarian-dashboard/**"

                        )
                        .hasRole("LIBRARIAN")


                        // EVERYTHING ELSE

                        .anyRequest()
                        .authenticated()
                )

                // LOGIN

                .formLogin(form -> form

                        // Custom login page

                        .loginPage("/login")


                        // Form submission URL

                        .loginProcessingUrl("/login")


                        // IMPORTANT:
                        // Tell Spring Security that the
                        // login identifier is EMAIL.

                        .usernameParameter("email")


                        // Password field name

                        .passwordParameter("password")


                        // Redirect according to role

                        .successHandler(
                                authenticationSuccessHandler
                        )


                        // Failed login

                        .failureUrl(
                                "/login?error=true"
                        )


                        .permitAll()
                )

                // LOGOUT

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl(
                                "/login?logout=true"
                        )

                        .invalidateHttpSession(true)

                        .deleteCookies(
                                "JSESSIONID"
                        )

                        .permitAll()
                )

                // ACCESS DENIED

                .exceptionHandling(exception -> exception

                        .accessDeniedPage("/403")
                );

        return http.build();
    }
}