package com.youruniversity.marketplace.campus_marketplace;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterchain) throws ServletException, IOException{
        String header = request.getHeader("Authorization");
        log.info("Processing request to: {}", request.getRequestURI());
        
        if(header!=null && header.startsWith("Bearer ")){
            log.debug("Found Bearer token in request");
            String token = header.substring(7);

            if(jwtUtils.validateToken(token)){
                log.debug("Token is valid");
                String email = jwtUtils.getEmailFromToken(token);
                Optional<User> userOpt = userRepository.findByEmail(email);
                if(userOpt.isPresent()){
                    log.info("Authentication successful for user: {}", email);
                    UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(email, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.warn("User not found for email: {}", email);
                }
            } else {
                log.warn("Invalid token received");
            }
        } else {
            log.debug("No Bearer token found in request");
        }
        filterchain.doFilter(request, response);
    }
} 
    