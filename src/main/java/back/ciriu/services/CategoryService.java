package back.ciriu.services;


import back.ciriu.entities.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryEntity> getAllCategories();
}
