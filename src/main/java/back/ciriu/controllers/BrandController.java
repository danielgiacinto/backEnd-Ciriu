package back.ciriu.controllers;

import back.ciriu.entities.BrandEntity;
import back.ciriu.entities.CategoryEntity;
import back.ciriu.models.Request.BrandRequest;
import back.ciriu.models.Request.CategoryRequestDto;
import back.ciriu.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("")
    public ResponseEntity<List<BrandEntity>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }
    @PostMapping("/new")
    public ResponseEntity<BrandEntity> newBrand(@RequestBody @Valid BrandRequest request){
        return ResponseEntity.ok(brandService.newBrand(request));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<BrandEntity> editBrand(@PathVariable Long id, @RequestBody @Valid String request){
        return ResponseEntity.ok(brandService.editBrand(id, request));
    }
}
