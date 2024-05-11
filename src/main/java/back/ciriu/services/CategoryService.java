package back.ciriu.services;


import back.ciriu.entities.CategoryEntity;
import back.ciriu.models.Request.CategoryRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryEntity> getAllCategories();

    CategoryEntity newCategory(CategoryRequestDto requestDto);

    CategoryEntity editCategory(Long id, String requestDto);
}
