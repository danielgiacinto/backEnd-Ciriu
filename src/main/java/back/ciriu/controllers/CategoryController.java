package back.ciriu.controllers;


import back.ciriu.entities.CategoryEntity;
import back.ciriu.models.Request.CategoryRequestDto;
import back.ciriu.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryEntity>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/new")
    public ResponseEntity<CategoryEntity> newCategory(@RequestBody @Valid CategoryRequestDto requestDto){
        return ResponseEntity.ok(categoryService.newCategory(requestDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CategoryEntity> editCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto requestDto){
        return ResponseEntity.ok(categoryService.editCategory(id, requestDto));
    }
}
