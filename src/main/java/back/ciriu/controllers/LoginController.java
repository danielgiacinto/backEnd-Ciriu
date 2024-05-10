package back.ciriu.controllers;

import back.ciriu.models.Request.LoginRequestDto;
import back.ciriu.models.Request.SignUpRequestDto;
import back.ciriu.models.Request.VerifyRequest;
import back.ciriu.models.Response.LoginResponse;
import back.ciriu.models.Response.SignUpResponse;
import back.ciriu.models.Response.VerifyReponse;
import back.ciriu.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequestDto requestDto) {
        return ResponseEntity.ok(loginService.login(requestDto));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyReponse> createAccount(@RequestBody VerifyRequest request) {
        return ResponseEntity.ok(loginService.createAccount(request));
    }

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> verifyAccount(@RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.ok(loginService.verifyCode(requestDto));
    }
}
