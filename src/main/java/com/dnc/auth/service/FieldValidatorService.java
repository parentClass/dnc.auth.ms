package com.dnc.auth.service;

import com.dnc.auth.model.UserCredentialDTO;

public interface FieldValidatorService {
    void validateUserCredentialForSignup(UserCredentialDTO userCredentialDTO);
}
