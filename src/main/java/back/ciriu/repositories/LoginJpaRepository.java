package back.ciriu.repositories;
import back.ciriu.entities.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginJpaRepository extends JpaRepository<LoginEntity, UUID> {

    LoginEntity getLoginEntityByEmail(String email);

}
