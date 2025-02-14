package com.msc.ms.users.security.filter;

import com.msc.ms.users.crypto.CryptoService;
import com.msc.ms.users.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value("${msc.security.header}")
    private String securityHeader;
    @Value("#{'${msc.security.allowed.consumers}'.split(',')}")
    private String[] consumers;
    private final JwtService jwtService;
    private final CryptoService cryptoService;


    private String getTokenFromRequest(HttpServletRequest request) {
        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validConsumer(String consumerEncrypted) {
        final var allowed = Arrays.stream(this.consumers).filter(c -> Objects.equals(c, consumerEncrypted)).toList();
        return !allowed.isEmpty();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var token = this.getTokenFromRequest(request);
        final var securityHeader = request.getHeader(this.securityHeader);
        if (token == null && securityHeader == null) {
            filterChain.doFilter(request, response);
            return;
        } else if (token != null) {
            final var username = jwtService.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (!jwtService.isTokenExpired(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,"", null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } else if (securityHeader != null) {
            final String microservice;
            try {
                microservice = cryptoService.decrypt(securityHeader);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (validConsumer(microservice)) {
                final var microServiceAuthentication = new UsernamePasswordAuthenticationToken(microservice, "",null);
                microServiceAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(microServiceAuthentication);
            }

        }
        filterChain.doFilter(request, response);
    }
}

