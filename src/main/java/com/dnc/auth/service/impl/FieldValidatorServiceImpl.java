package com.dnc.auth.service.impl;

import com.dnc.auth.model.UserCredentialDTO;
import com.dnc.auth.repository.UserCredentialRepository;
import com.dnc.auth.service.FieldValidatorService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FieldValidatorServiceImpl implements FieldValidatorService {
    private static final int MIN_USERNAME_LENGTH = 8;
    private static final int MAX_USERNAME_LENGTH = 12;
    private static final int MIN_PASSWORD_LENGTH = 12;
    private static final int MAX_PASSWORD_LENGTH = 16;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public void validateUserCredentialForSignup(UserCredentialDTO userCredentialDTO) {
        /*
          constraints
          - 8 >= username length <= 12, username must be unique
          - email must be a valid address, email must be unique
          - 12 >= password length <= 16
         */
        int usernameLength = userCredentialDTO.getUsername().length();
        int passwordLength = userCredentialDTO.getPassword().length();


        if ((usernameLength < MIN_USERNAME_LENGTH) || (usernameLength > MAX_USERNAME_LENGTH)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Username should be 8 characters long and not exceed 12 characters");
        }

        if (!userCredentialRepository.findByUsername(userCredentialDTO.getUsername()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }

        if (!EmailValidator.getInstance().isValid(userCredentialDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Email provided is not a valid address");
        }

        if (!userCredentialRepository.findByEmail(userCredentialDTO.getEmail()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        if ((passwordLength < MIN_PASSWORD_LENGTH) || (passwordLength > MAX_PASSWORD_LENGTH)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password should be 12 characters long and not exceed 16 characters");
        }
    }
}
