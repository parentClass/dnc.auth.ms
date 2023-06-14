package com.dnc.auth.controller;

import com.dnc.auth.model.UserCredentialDTO;
import com.dnc.auth.model.responses.BaseResponse;
import com.dnc.auth.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin
class AuthenticateController {
    @Autowired
    private AuthenticateService authenticateService;

    @PostMapping("/authenticate")
    public ResponseEntity<BaseResponse> authenticate(@RequestBody UserCredentialDTO userCredentialDTO) {
        // identify what type of authenticate will be used
        String authResp = authenticateCredential(userCredentialDTO);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK.value())
                        .body(authResp).build());
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody UserCredentialDTO userCredentialDTO) {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .body(authenticateService.signup(userCredentialDTO))
                        .build());
    }

    @GetMapping("/validate")
    public ResponseEntity<BaseResponse> validate() {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK.value())
                        .body(authenticateService.validate())
                        .build());
    }

    private String authenticateCredential(UserCredentialDTO userCredentialDTO) {
        if (!userCredentialDTO.getUsername().isEmpty()) {
            return authenticateService.authenticateByUsername(userCredentialDTO);
        }

        if (!userCredentialDTO.getEmail().isEmpty()) {
            return authenticateService.authenticateByEmail(userCredentialDTO);
        }

        return "";
    }
}
