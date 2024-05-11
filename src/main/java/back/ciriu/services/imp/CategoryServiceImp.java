package back.ciriu.services.imp;

import back.ciriu.entities.CategoryEntity;
import back.ciriu.models.Request.CategoryRequestDto;
import back.ciriu.repositories.CategoryRepository;
import back.ciriu.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity newCategory(CategoryRequestDto requestDto) {
        try {
            CategoryEntity category = categoryRepository.getByCategory(requestDto.getCategory().toLowerCase());
            if(category != null){
                throw new RuntimeException("Ya existe esa categoria");
            } else {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setCategory(requestDto.getCategory().toLowerCase());
                CategoryEntity categorySaved = categoryRepository.save(categoryEntity);
                return categorySaved;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error, " + e.getMessage());
        }

    }

    @Override
    public CategoryEntity editCategory(Long id, String requestDto) {
        try{
            CategoryEntity category = categoryRepository.getReferenceById(id);
            if(category != null){
                category.setCategory(requestDto.toLowerCase());
                return categoryRepository.save(category);
            } else {
                throw new RuntimeException("No existe esa categoria");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error, " + e.getMessage());
        }

    }
}
