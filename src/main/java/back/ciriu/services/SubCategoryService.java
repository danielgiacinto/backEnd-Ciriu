package back.ciriu.services;

import back.ciriu.entities.SubCategoryEntity;
import back.ciriu.models.Request.SubCategoryRequest;
import back.ciriu.models.Response.SubCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubCategoryService {

    List<SubCategoryResponse> getAllSubCategories();

    List<SubCategoryResponse> getAllSubCategoriesByCategory(String category);

    SubCategoryResponse newSubCategory(SubCategoryRequest request);

    SubCategoryResponse editSubCategory(Long id, String sub_category);
}
