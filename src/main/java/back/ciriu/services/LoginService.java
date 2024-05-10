package back.ciriu.services;

import back.ciriu.models.Request.LoginRequestDto;
import back.ciriu.models.Request.SignUpRequestDto;
import back.ciriu.models.Request.VerifyRequest;
import back.ciriu.models.Response.LoginResponse;
import back.ciriu.models.Response.SignUpResponse;
import back.ciriu.models.Response.VerifyReponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface LoginService {

    LoginResponse login(LoginRequestDto request);

    VerifyReponse createAccount(VerifyRequest request);

    SignUpResponse verifyCode(SignUpRequestDto request);
}
