package com.dnc.auth.service;

import com.dnc.auth.model.UserCredentialDTO;

public interface AuthenticateService {
    /**
     * authenticate user by username
     *
     * @param userCredential
     * @return string token
     */
    String authenticateByUsername(UserCredentialDTO userCredential);

    /**
     * authenticate user by email
     *
     * @param userCredential
     * @return string token
     */
    String authenticateByEmail(UserCredentialDTO userCredential);

    /**
     * register a user credential
     *
     * @param userCredentialDTO
     * @return user credential created
     */
    UserCredentialDTO signup(UserCredentialDTO userCredentialDTO);

    /**
     * validate token received
     *
     * @param token
     * @return validation result
     */
    Boolean validate();
}
