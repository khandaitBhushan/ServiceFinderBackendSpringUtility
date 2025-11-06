package com.org.ServiceFinder.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String path = request.getServletPath();
        String method = request.getMethod();

        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("Path: " + path);
        System.out.println("Method: " + method);
        System.out.println("Auth Header Present: " + (authHeader != null));

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            System.out.println("Token: " + token.substring(0, 20) + "...");

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token); // Make sure this method exists!

                System.out.println("Extracted Email: " + email);
                System.out.println("Extracted Role: " + role);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                System.out.println("Created Authority: " + authority.getAuthority());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set in SecurityContext");
            } else {
                System.out.println("Token validation FAILED");
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String method, String path) {
        // Public GET endpoints
        if ("GET".equals(method)) {
            return path.startsWith("/auth/") ||
                    path.startsWith("/providers") ||
                    path.startsWith("/provider-services/") ||
                    path.startsWith("/map/");
        }
        // Public POST endpoints (only auth)
        if ("POST".equals(method)) {
            return path.startsWith("/auth/");
        }
        return false;
    }
}