package com.github.vahundos.breezer.web.filter;

import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DenyBasicAuthenticationFilter extends OncePerRequestFilter {

    private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (authenticationConverter.convert(request) != null) {
            throw new RuntimeException("Basic authentication not supported for " +
                                               request.getMethod() + " " + request.getRequestURI());
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/users/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod());
    }
}
