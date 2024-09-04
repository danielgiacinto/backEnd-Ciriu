package back.ciriu.repositories;

import back.ciriu.entities.PasswordResetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetEntity, UUID> {

    Optional<PasswordResetEntity> findByToken(String token);

    Optional<PasswordResetEntity> findByEmail(String email);
}
