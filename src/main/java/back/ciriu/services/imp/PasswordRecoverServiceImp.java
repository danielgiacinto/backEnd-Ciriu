package back.ciriu.services.imp;

import back.ciriu.entities.LoginEntity;
import back.ciriu.entities.PasswordResetEntity;
import back.ciriu.models.Request.PasswordRecoverRequestDto;
import back.ciriu.models.Request.ResetPasswordRequestDto;
import back.ciriu.models.Response.PasswordRecoverResponse;
import back.ciriu.repositories.LoginJpaRepository;
import back.ciriu.repositories.PasswordResetTokenJpaRepository;
import back.ciriu.services.PasswordRecoverService;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordRecoverServiceImp implements PasswordRecoverService {

    @Autowired
    private LoginJpaRepository loginJpaRepository;

    @Autowired
    private PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public PasswordRecoverResponse sendPasswordRecoveryEmail(PasswordRecoverRequestDto request) {
        Optional<LoginEntity> userOpt = Optional.ofNullable(loginJpaRepository.getLoginEntityByEmail(request.getEmail()));
        if (userOpt.isPresent()) {
            UUID token = UUID.randomUUID();
            PasswordResetEntity resetToken = new PasswordResetEntity();
            resetToken.setEmail(request.getEmail());
            resetToken.setToken(token.toString());
            LocalDateTime now = LocalDateTime.now();
            resetToken.setExpiration_date(now.plusHours(12)); // 12 horas de expiración
            passwordResetTokenJpaRepository.save(resetToken);

            // Enviar correo electrónico
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(request.getEmail());
                helper.setSubject("Recuperación de contraseña de Ciriú");

                Context context = new Context();
                context.setVariable("token", token);
                String contenidoHtml = templateEngine.process("password_recovery_email", context);
                helper.setText(contenidoHtml, true);

                javaMailSender.send(message);
                PasswordRecoverResponse response = new PasswordRecoverResponse();
                response.setSuccess(true);
                return response;

            } catch (Exception e) {
                throw new RuntimeException("Error al enviar el correo de recuperación: " + e.getMessage(), e);
            }
        } else {
            throw new EntityNotFoundException("El correo electrónico no está registrado.");
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto request) {
        Optional<PasswordResetEntity> resetTokenOpt = passwordResetTokenJpaRepository.findByToken(request.getToken());

        if (resetTokenOpt.isPresent()) {
            PasswordResetEntity resetToken = resetTokenOpt.get();
            if (LocalDateTime.now().isBefore(resetToken.getExpiration_date())) {
                LoginEntity userEntity = loginJpaRepository.getLoginEntityByEmail(resetToken.getEmail());
                userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
                loginJpaRepository.save(userEntity);
                passwordResetTokenJpaRepository.delete(resetToken); // Elimina el token después de usarlo
            } else {
                throw new RuntimeException("El token de recuperación ha expirado.");
            }
        } else {
            throw new EntityNotFoundException("Token no válido.");
        }
    }
}
