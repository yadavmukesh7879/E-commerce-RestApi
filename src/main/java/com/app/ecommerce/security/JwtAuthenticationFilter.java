package com.app.ecommerce.security;

import com.app.ecommerce.exception.SecurityApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //Inject dependency

    public static String CURRENT_USER = "";
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Get JWT(token) from http request
        String token = getJWTfromRequest(request);
        // Validate token
        try {
            if (StringUtils.hasText(token) && this.jwtTokenProvider.validateToken(token)) {
                // Get username from token
                String userName = this.jwtTokenProvider.getUserNameFromJWT(token);
                this.CURRENT_USER = userName;

                // load user with associated with token
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set spring security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (SecurityApiException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    // Bearer <access Token>
    private String getJWTfromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
