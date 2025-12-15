package com.gkfcsolution.gkfcsolutionhotel.jwt;

import com.gkfcsolution.gkfcsolutionhotel.entity.User;
import com.gkfcsolution.gkfcsolutionhotel.repository.UserRepository;
import com.gkfcsolution.gkfcsolutionhotel.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Created on 2025 at 21:33
 * File: null.java
 * Project: springboot-movies-bookinks
 *
 * @author Frank GUEKENG
 * @date 13/12/2025
 * @time 21:33
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        // Vérifie si le header contient un Bearer token
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Vérifie si le header commence par "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        // Si on a un username et que personne n'est encore authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userRepository.findByUsername(username).get();
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRoles().stream().map(SimpleGrantedAuthority::new).toList())
                    .build();

            if (jwtUtil.validateToken(token, userDetails)){
                // Create the authentication with user roles
              /*  List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();*/

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // On met l'auth dans le contexte Spring
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
