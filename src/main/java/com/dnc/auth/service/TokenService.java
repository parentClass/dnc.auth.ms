package com.dnc.auth.service;

import com.dnc.auth.model.UserCredentialDTO;

import java.util.Map;

public interface TokenService {
    String generateToken(Map<String, Object> claims, String subject);

    String getSubjectFromToken(String token);

    Boolean validateToken(String token, UserCredentialDTO userCredentialDTO);
}
