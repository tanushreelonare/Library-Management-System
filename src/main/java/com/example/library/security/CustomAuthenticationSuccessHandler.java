package com.example.library.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(

            HttpServletRequest request,

            HttpServletResponse response,

            Authentication authentication)

            throws IOException, ServletException {

        boolean isStudent = authentication.getAuthorities()

                .stream()

                .anyMatch(authority ->

                        authority.getAuthority()

                                .equals("ROLE_STUDENT"));

        if (isStudent) {

            response.sendRedirect(

                    request.getContextPath()

                            + "/student-dashboard"
            );

            return;

        }

        boolean isLibrarian = authentication.getAuthorities()

                .stream()

                .anyMatch(authority ->

                        authority.getAuthority()

                                .equals("ROLE_LIBRARIAN"));

        if (isLibrarian) {

            response.sendRedirect(

                    request.getContextPath()

                            + "/librarian-dashboard"
            );

            return;

        }

        response.sendRedirect(

                request.getContextPath()

                        + "/login"

        );

    }

}