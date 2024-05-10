package back.ciriu.services.imp;

import back.ciriu.entities.LoginEntity;
import back.ciriu.entities.UserEntity;
import back.ciriu.entities.VerifyEntity;
import back.ciriu.models.Request.LoginRequestDto;
import back.ciriu.models.Request.SignUpRequestDto;
import back.ciriu.models.Request.VerifyRequest;
import back.ciriu.models.Response.LoginResponse;
import back.ciriu.models.Response.SignUpResponse;
import back.ciriu.models.Response.VerifyReponse;
import back.ciriu.repositories.LoginJpaRepository;
import back.ciriu.repositories.UserJpaRepository;
import back.ciriu.repositories.VerifyJpaRepository;
import back.ciriu.services.LoginService;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
public class LoginServiceImp implements LoginService {

    @Autowired
    private LoginJpaRepository loginJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private VerifyJpaRepository verifyJpaRepository;


    @Value("${jwt-secret}")
    private String secretKey;
    @Override
    public LoginResponse login(LoginRequestDto request) {
        LoginEntity userEntity = loginJpaRepository.getLoginEntityByEmail(request.getEmail());
        if(userEntity != null) {
            if(request.getPassword().equals(userEntity.getPassword())) {
                String token = Jwts.builder()
                        .setSubject((userEntity.getEmail()))
                        .claim("rol", userEntity.getRol())
                        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512)
                        .compact();
                LoginResponse response = new LoginResponse();
                response.setToken(token);
                response.setId(userEntity.getId());
                response.setRol(userEntity.getRol());
                return response;
            } else {
                throw new EntityNotFoundException("Contraseña incorrecta");
            }
        }
        throw new EntityNotFoundException("No se encuentra el usuario");
    }

    @Override
    public VerifyReponse createAccount(VerifyRequest request) {
        VerifyEntity verifyEntity = verifyJpaRepository.getByCode(UUID.fromString(request.getCode()));
        if(verifyEntity == null || verifyEntity.getId() != null) {
                try {
                    LoginEntity loginSaved = new LoginEntity();
                    loginSaved.setEmail(verifyEntity.getEmail());
                    loginSaved.setPassword(verifyEntity.getPassword());
                    loginSaved.setRol("Usuario");

                    UserEntity userSaved = new UserEntity();
                    UUID id = UUID.randomUUID();
                    loginSaved.setId(id);
                    userSaved.setId(id);
                    userSaved.setName(verifyEntity.getName());
                    userSaved.setLastname(verifyEntity.getLastname());
                    userSaved.setEmail(verifyEntity.getEmail());

                    loginJpaRepository.save(loginSaved);
                    userJpaRepository.save(userSaved);
                    verifyJpaRepository.delete(verifyEntity);
                    VerifyReponse verifyReponse = new VerifyReponse();
                    verifyReponse.setSuccess(true);
                    return verifyReponse;

                } catch (Exception ex) {
                    throw new RuntimeException("No se pudo crear el nuevo usuario");
                }
        } else {
            throw new EntityNotFoundException("Error no se encuentra la cuenta, registrese primero");
        }
    }

    @Override
    public SignUpResponse verifyCode(SignUpRequestDto request) {
        LoginEntity login = loginJpaRepository.getLoginEntityByEmail(request.getEmailSignUp());
        if(login == null){
            if(request.getPasswordSignUp().equals(request.getRepeatPasswordSignUp())
                    && request.getPasswordSignUp().length() > 8 && request.getNameSignUp().length() > 3
                    && request.getLastnameSignUp().length() > 3 ) {
                UUID verificationCode = UUID.fromString(UUID.randomUUID().toString());
                try {
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                    helper.setTo(request.getEmailSignUp());
                    helper.setSubject("Por favor, verifique su cuenta de Ciriú");

                    // Procesar la plantilla Thymeleaf
                    Context context = new Context();
                    context.setVariable("mensaje", "codigo: " + verificationCode);
                    String contenidoHtml = templateEngine.process("email", context);
                    helper.setText(contenidoHtml, true);

                    // crear cuenta en tabla auxiliar
                    VerifyEntity verifyEntity = new VerifyEntity();
                    verifyEntity.setCode(verificationCode);
                    verifyEntity.setName(request.getNameSignUp());
                    verifyEntity.setLastname(request.getLastnameSignUp());
                    verifyEntity.setEmail(request.getEmailSignUp());
                    verifyEntity.setPassword(request.getPasswordSignUp());

                    verifyJpaRepository.save(verifyEntity);
                    javaMailSender.send(message);
                    SignUpResponse verifyResponse = new SignUpResponse();
                    verifyResponse.setCode(verificationCode.toString());
                    return verifyResponse;
                } catch (Exception e) {
                    throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
                }
            }
            else {
                throw new EntityNotFoundException("La contraseña deben ser iguales y respetar el largo + 8 caracteres");
            }
        }
        else {
            throw new EntityNotFoundException("Ya existe ese correo, utilize otro");
        }

    }


}
