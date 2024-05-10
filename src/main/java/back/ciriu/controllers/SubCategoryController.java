package back.ciriu.controllers;

import back.ciriu.entities.SubCategoryEntity;
import back.ciriu.models.Request.SubCategoryRequest;
import back.ciriu.models.Response.SubCategoryResponse;
import back.ciriu.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subCategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("")
    public ResponseEntity<List<SubCategoryResponse>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<SubCategoryResponse>> getAllSubCategoriesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(subCategoryService.getAllSubCategoriesByCategory(category));
    }

    @PostMapping("/new")
    public ResponseEntity<SubCategoryResponse> newSubCategory(@RequestBody SubCategoryRequest request) {
        return ResponseEntity.ok(subCategoryService.newSubCategory(request));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<SubCategoryResponse> newSubCategory(@PathVariable Long id, @RequestBody String sub_category) {
        return ResponseEntity.ok(subCategoryService.editSubCategory(id, sub_category));
    }
}
