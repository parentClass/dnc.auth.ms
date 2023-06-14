package com.dnc.auth.service.impl;

import com.dnc.auth.model.UserCredentialDTO;
import com.dnc.auth.model.enums.AuthenticationType;
import com.dnc.auth.repository.UserCredentialRepository;
import com.dnc.auth.service.AuthenticateService;
import com.dnc.auth.service.FieldValidatorService;
import com.dnc.auth.service.TokenService;
import com.dnc.auth.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FieldValidatorService fieldValidatorService;

    @Autowired
    private HttpServletRequest request;

    AuthenticateServiceImpl(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public String authenticateByUsername(UserCredentialDTO userCredential) {
        Map<String, Object> claims = new HashMap<>();

        // mark authentication by username
        UserCredentialDTO userCredentialDTO = validateCredential(userCredential, AuthenticationType.BY_USERNAME);

        fillTokenClaims(claims, userCredentialDTO);

        return tokenService.generateToken(claims, userCredentialDTO.getId());
    }

    @Override
    public String authenticateByEmail(UserCredentialDTO userCredential) {
        Map<String, Object> claims = new HashMap<>();

        // mark authentication by email
        UserCredentialDTO userCredentialDTO = validateCredential(userCredential, AuthenticationType.BY_EMAIL);

        fillTokenClaims(claims, userCredentialDTO);

        return tokenService.generateToken(claims, userCredentialDTO.getId());
    }

    @Override
    public UserCredentialDTO signup(UserCredentialDTO userCredentialDTO) {

        // validate user credential before signing up
        fieldValidatorService.validateUserCredentialForSignup(userCredentialDTO);

        // encode user pass
        userCredentialDTO.setPassword(passwordEncoder.encode(userCredentialDTO.getPassword()));

        // set user credential fields
        userCredentialDTO.setDeleted(false);
        userCredentialDTO.setCreatedAt(LocalDateTime.now());

        return userCredentialRepository.save(userCredentialDTO);
    }

    @Override
    public Boolean validate() {
        // retrieve token from request
        String token = TokenUtil.stripTokenFromHeader(request.getHeader("Authorization"));

        UserCredentialDTO userCredentialDTO = userCredentialRepository.findByOid(
                tokenService.getSubjectFromToken(token)).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is invalid"));

        return tokenService.validateToken(token, userCredentialDTO);
    }

    private UserCredentialDTO validateCredential(UserCredentialDTO userCredential, AuthenticationType authenticationType) {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();

        if (AuthenticationType.BY_USERNAME.equals(authenticationType)) {
            userCredentialDTO = userCredentialRepository.findByUsername(userCredential.getUsername()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not exists"));
        }

        if (AuthenticationType.BY_EMAIL.getType().equals(authenticationType)) {
            userCredentialDTO = userCredentialRepository.findByEmail(userCredential.getEmail()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not exists"));
        }

        if (!passwordEncoder.matches(userCredential.getPassword(), userCredentialDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return userCredentialDTO;
    }

    private void fillTokenClaims(Map<String, Object> claims, UserCredentialDTO userCredentialDTO) {
        claims.put("username", userCredentialDTO.getUsername());
        claims.put("email", userCredentialDTO.getEmail());
    }
}
