package back.ciriu.repositories;


import back.ciriu.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    CategoryEntity getByCategory(String category);

    List<CategoryEntity> findAllByOrderByIdAsc();
}
