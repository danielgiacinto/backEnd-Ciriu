package back.ciriu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login")
public class LoginEntity {

    @Id
    private UUID id;

    @Column @Email
    private String email;

    @Column
    private String password;

    @Column
    private String rol;
}
