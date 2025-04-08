package org.meghana.OnboardingAndAssetsMgmt.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.meghana.OnboardingAndAssetsMgmt.service.RedisInvalidatedTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtInvalidatedTokenFilter extends OncePerRequestFilter {

    private final RedisInvalidatedTokenService redisInvalidatedTokenService;

    public JwtInvalidatedTokenFilter(RedisInvalidatedTokenService redisInvalidatedTokenService) {
        this.redisInvalidatedTokenService = redisInvalidatedTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            if (redisInvalidatedTokenService.isTokenInvalidated(jwt)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("JWT has been invalidated. Please log in again.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}