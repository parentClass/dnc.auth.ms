package com.dnc.auth.config.filter;

import com.dnc.auth.model.UserCredentialDTO;
import com.dnc.auth.repository.UserCredentialRepository;
import com.dnc.auth.service.TokenService;
import com.dnc.auth.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class TokenRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private TokenService tokenService;

    private static Logger logger = LoggerFactory.getLogger(TokenRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, ResponseStatusException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String subject = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = TokenUtil.stripTokenFromHeader(requestTokenHeader);

            try {
                // attempt to retrieve subject from token
                subject = tokenService.getSubjectFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Token supplied is incorrect");
            } catch (ExpiredJwtException e) {
                logger.error("Token supplied is expired");
            } catch (SignatureException e) {
                logger.error("Token supplied is unrecognizable");
            }
        }

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserCredentialDTO userCredentialDTO = userCredentialRepository.findByOid(subject)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not exists"));

            // if token is valid configure Spring Security to manually set authentication
            if (Boolean.TRUE.equals(tokenService.validateToken(jwtToken, userCredentialDTO))) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userCredentialDTO, null, null);

                // set verified jwt token to security context details
                usernamePasswordAuthenticationToken.setDetails(jwtToken);

                // set to security context after auth
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
