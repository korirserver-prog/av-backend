package com.game.aviator.security;

import com.game.aviator.auth.CustomUserDetailsService;
import com.game.aviator.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println(
                "JWT FILTER: " +
                        request.getMethod() +
                        " " +
                        request.getRequestURI()
        );

        final String authHeader =
                request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

//            System.out.println(
//                    "JWT FILTER: No Bearer token"
//            );

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        String username;

        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {

//            System.out.println(
//                    "JWT FILTER: Could not extract username"
//            );

            filterChain.doFilter(request, response);
            return;
        }

        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(
                    jwt,
                    userDetails
            )) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

//                System.out.println(
//                        "JWT AUTHENTICATED: " +
//                                username +
//                                " | Authorities: " +
//                                userDetails.getAuthorities()
//                );
//
//                System.out.println(
//                        "AUTH SET: " +
//                                SecurityContextHolder
//                                        .getContext()
//                                        .getAuthentication()
//                );

            } else {

                System.out.println(
                        "JWT FILTER: Token is invalid"
                );
            }
        }

//        System.out.println(
//                "AUTH BEFORE CHAIN: " +
//                        SecurityContextHolder
//                                .getContext()
//                                .getAuthentication()
//        );

        filterChain.doFilter(request, response);
    }
}