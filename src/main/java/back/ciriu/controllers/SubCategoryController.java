package back.ciriu.controllers;

import back.ciriu.entities.SubCategoryEntity;
import back.ciriu.models.Response.SubCategoryResponse;
import back.ciriu.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
