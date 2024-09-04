package back.ciriu.controllers;

import back.ciriu.models.Request.PasswordRecoverRequestDto;
import back.ciriu.models.Request.ResetPasswordRequestDto;
import back.ciriu.models.Response.PasswordRecoverResponse;
import back.ciriu.services.PasswordRecoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recoverPassword")
public class PasswordController {

    @Autowired
    private PasswordRecoverService passwordRecoverService;

    @PostMapping("/sendRecoveryEmail")
    public PasswordRecoverResponse passwordRecover(@RequestBody PasswordRecoverRequestDto request) {
        return passwordRecoverService.sendPasswordRecoveryEmail(request);
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody ResetPasswordRequestDto request) {
        passwordRecoverService.resetPassword(request);
    }
}
