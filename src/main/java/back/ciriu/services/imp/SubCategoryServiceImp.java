package back.ciriu.services.imp;

import back.ciriu.entities.CategoryEntity;
import back.ciriu.entities.SubCategoryEntity;
import back.ciriu.models.Request.SubCategoryRequest;
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
            subCategoryResponse.setSubcategory(e.getSubcategory());
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
                subCategoryResponse.setSubcategory(e.getSubcategory());
                subCategoryResponses.add(subCategoryResponse);
            }
        }
        return subCategoryResponses;
    }

    @Override
    public SubCategoryResponse newSubCategory(SubCategoryRequest request) {
        try {
            SubCategoryEntity subCategoryEntity = subCategoryJpaRepository.getBySubcategory(request.getSubcategory().toLowerCase());
            if(subCategoryEntity != null){
                throw new RuntimeException("Esta sub categoria ya existe");
            } else {
                SubCategoryEntity subCategory = new SubCategoryEntity();
                subCategory.setSubcategory(request.getSubcategory().toLowerCase());
                CategoryEntity category = categoryRepository.getReferenceById(request.getId_category());
                if(category != null){
                    subCategory.setId_category(category);
                } else {
                    throw new RuntimeException("La categoria principal no existe");
                }
                SubCategoryEntity saved = subCategoryJpaRepository.save(subCategory);
                SubCategoryResponse response = new SubCategoryResponse();
                response.setId(saved.getId());
                CategoryEntity categoryEntity = categoryRepository.getReferenceById(request.getId_category());
                response.setCategory(categoryEntity.getCategory());
                response.setSubcategory(saved.getSubcategory());
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error, " + e.getMessage());
        }
    }

    @Override
    public SubCategoryResponse editSubCategory(Long id, String sub_category) {
        try {
            SubCategoryEntity subCategoryEntity = subCategoryJpaRepository.getReferenceById(id);
            if(subCategoryEntity == null){
                throw new RuntimeException("No existe esta sub categoria");
            } else {
                SubCategoryEntity subCategory = subCategoryJpaRepository.getBySubcategory(sub_category.toLowerCase());
                if(subCategory != null) {
                    throw new RuntimeException("Ya existe esa sub categoria");
                } else {
                    subCategoryEntity.setSubcategory(sub_category.toLowerCase());
                    SubCategoryEntity saved = subCategoryJpaRepository.save(subCategoryEntity);
                    SubCategoryResponse response = new SubCategoryResponse();
                    response.setId(saved.getId());
                    CategoryEntity categoryEntity = categoryRepository.getReferenceById(saved.getId_category().getId());
                    response.setCategory(categoryEntity.getCategory());
                    response.setSubcategory(saved.getSubcategory());
                    return response;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error, " + e.getMessage());
        }
    }
}
