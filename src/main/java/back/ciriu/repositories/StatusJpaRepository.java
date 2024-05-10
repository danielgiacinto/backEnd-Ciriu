package back.ciriu.repositories;


import back.ciriu.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusJpaRepository extends JpaRepository<StatusEntity, Long> {
}
