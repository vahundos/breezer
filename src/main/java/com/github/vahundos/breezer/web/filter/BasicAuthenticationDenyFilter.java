package com.github.vahundos.breezer.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationDenyFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (authenticationConverter.convert(request) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, String> responseBody = Map.of("message", "Basic authentication is supported only for " +
                                                                 "POST /users/login");
            response.getWriter().println(objectMapper.writeValueAsString(responseBody));
            response.getWriter().flush();

            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/users/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod());
    }
}
