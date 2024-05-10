package back.ciriu.controllers;

import back.ciriu.entities.BrandEntity;
import back.ciriu.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
