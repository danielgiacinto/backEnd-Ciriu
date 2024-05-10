package back.ciriu.controllers;

import back.ciriu.models.Request.ToyRequestDto;
import back.ciriu.models.Request.ToyUpdateRequestDto;
import back.ciriu.models.Response.ProductResponseDto;
import back.ciriu.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/toys")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<Page<ProductResponseDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "default") String sortBy,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "false") Boolean nonStock,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String brand

    ) {
        Page<ProductResponseDto> toys = productService.getAllToys(page, sortBy, searchTerm, nonStock, category, brand);
        return ResponseEntity.ok(toys);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductResponseDto> getProductId(@PathVariable String code) {
        return ResponseEntity.ok(productService.getToyByCode(code));
    }

    @PostMapping("/new")
    public ResponseEntity<ProductResponseDto> createToy(@RequestBody @Valid ToyRequestDto toy){
        return ResponseEntity.ok(productService.createToy(toy));
    }

    @PutMapping("/delete")
    public ResponseEntity<Boolean> deleteProduct(@RequestBody String code) {
        return ResponseEntity.ok(productService.deleteProduct(code));
    }

    @PutMapping("update/{code}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable String code, @RequestBody @Valid ToyUpdateRequestDto requestDto) {
        return ResponseEntity.ok(productService.updateToy(code, requestDto));
    }
}
