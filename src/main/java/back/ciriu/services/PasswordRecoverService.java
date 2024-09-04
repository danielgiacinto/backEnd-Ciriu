package back.ciriu.services;

import back.ciriu.models.Request.PasswordRecoverRequestDto;
import back.ciriu.models.Request.ResetPasswordRequestDto;
import back.ciriu.models.Response.PasswordRecoverResponse;
import org.springframework.stereotype.Service;

@Service
public interface PasswordRecoverService {

    PasswordRecoverResponse sendPasswordRecoveryEmail(PasswordRecoverRequestDto request);
    void resetPassword(ResetPasswordRequestDto request);
}
