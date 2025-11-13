package com.sajid.college_app.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException {

        log.debug("AuthFilter called for URI: {}", request.getRequestURI());
        String jwt = jwtUtils.getJwtFromHeader(request);

        try {
            if (jwt != null && jwtUtils.validateJwt(jwt)) {
                String username = jwtUtils.getUsernameFromJwt(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired", e.getMessage());
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token", e.getMessage());
        } catch (JwtException e) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT error", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in JWT filter: {}", e.getMessage());
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong", e.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, int status, String message, String actualMessage)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of(
                "success", false,
                "message", message,
                "actualMessage", actualMessage
        ));
    }
}
