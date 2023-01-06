package com.steveperkins.fitnessjiffy.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

class JwtFilter extends GenericFilterBean {

    final static private List<String> EXCLUDED_URIS = Arrays.asList(
            "/login.html",
            "/api/auth/userpass",
            "/favicon.ico"
    );

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // TODO: More robust route matching
        // TODO: Will probably need separate filters for the "page load" stuff, versus the AJAX "api" stuff
        // TODO: Make "page load" stuff use cookies, and "api" stuff use headers exclusively
        if (
                EXCLUDED_URIS.contains(httpServletRequest.getRequestURI())
                    || httpServletRequest.getRequestURI().startsWith("/static")
        ) {
            chain.doFilter(request, response);
            return;
        }

        final String token =
            Optional.ofNullable(httpServletRequest.getHeader("Authorization")).orElse("").startsWith("Bearer ")
                ? httpServletRequest.getHeader("Authorization").substring(7)
                : Arrays.stream(Optional.ofNullable(httpServletRequest.getCookies()).orElse(new Cookie[0]))
                    .filter(cookie -> cookie.getName().equals("Authorization"))
                    .findFirst()
                    .orElse(new Cookie("Authorization", ""))
                    .getValue();
        if (token.isEmpty()) {
            logger.error("No valid Authorization header or cookie value found");
            ((HttpServletResponse) response).sendRedirect("/login.html");
            return;
        }

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
        } catch (final SignatureException e) {
            logger.error("Invalid token");
            ((HttpServletResponse) response).sendRedirect("/login.html");
            return;
        }

        if (claims.getExpiration().before(new Date())) {
            ((HttpServletResponse) response).sendRedirect("/login.html?logout=true");
            return;
        }

        request.setAttribute("email", claims.get("email"));
        chain.doFilter(request, response);
    }

}
