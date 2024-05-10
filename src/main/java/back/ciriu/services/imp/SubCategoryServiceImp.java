package back.ciriu.services.imp;

import back.ciriu.entities.CategoryEntity;
import back.ciriu.entities.SubCategoryEntity;
import back.ciriu.models.Response.SubCategoryResponse;
import back.ciriu.repositories.CategoryRepository;
import back.ciriu.repositories.SubCategoryJpaRepository;
import back.ciriu.services.SubCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubCategoryServiceImp implements SubCategoryService {

    @Autowired
    private SubCategoryJpaRepository subCategoryJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<SubCategoryResponse> getAllSubCategories() {
        List<SubCategoryEntity> subCategoryEntities = subCategoryJpaRepository.findAll();
        List<SubCategoryResponse> subCategoryResponses = new ArrayList<>();
        for(SubCategoryEntity e : subCategoryEntities) {
            SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
            subCategoryResponse.setId(e.getId());
            CategoryEntity category = categoryRepository.getReferenceById(e.getId_category().getId());
            subCategoryResponse.setCategory(category.getCategory());
            subCategoryResponse.setSub_category(e.getSub_category());
            subCategoryResponses.add(subCategoryResponse);
        }
        return subCategoryResponses;
    }

    @Override
    public List<SubCategoryResponse> getAllSubCategoriesByCategory(String category) {
        List<SubCategoryEntity> subCategoryEntities = subCategoryJpaRepository.findAll();
        List<SubCategoryResponse> subCategoryResponses = new ArrayList<>();
        for(SubCategoryEntity e : subCategoryEntities) {
            SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
            if(e.getId_category().getCategory().equals(category)) {
                subCategoryResponse.setId(e.getId());
                CategoryEntity categoryEntity = categoryRepository.getReferenceById(e.getId_category().getId());
                subCategoryResponse.setCategory(categoryEntity.getCategory());
                subCategoryResponse.setSub_category(e.getSub_category());
                subCategoryResponses.add(subCategoryResponse);
            }
        }
        return subCategoryResponses;
    }
}
