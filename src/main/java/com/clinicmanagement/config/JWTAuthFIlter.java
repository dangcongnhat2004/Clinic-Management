package com.clinicmanagement.config;

import com.clinicmanagement.service.JWTUtils;
import com.clinicmanagement.service.OurUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JWTAuthFIlter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final OurUserDetailsService userDetailsService;
    private static final Logger logger = Logger.getLogger(JWTAuthFIlter.class.getName());

    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/", "/booking", "/profile", "/confirmation", "/doctor-details", 
        "/login", "/dashboard", "/auth/**", "/css/**", "/js/**", "/images/**"
    );

    public JWTAuthFIlter(JWTUtils jwtUtils, OurUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Skip JWT validation for public URLs
        if (isPublicURL(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtUtils.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicURL(String requestURI) {
        return PUBLIC_URLS.stream().anyMatch(publicUrl -> 
            publicUrl.endsWith("/**") ? 
                requestURI.startsWith(publicUrl.substring(0, publicUrl.length() - 3)) :
                requestURI.equals(publicUrl)
        );
    }
}
