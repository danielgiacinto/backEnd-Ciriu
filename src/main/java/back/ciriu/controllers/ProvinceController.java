package back.ciriu.controllers;

import back.ciriu.entities.ProvinceEntity;
import back.ciriu.services.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provinces")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping("")
    public ResponseEntity<List<ProvinceEntity>> getAll() {
        return ResponseEntity.ok(provinceService.getAllProvinces());
    }
}
