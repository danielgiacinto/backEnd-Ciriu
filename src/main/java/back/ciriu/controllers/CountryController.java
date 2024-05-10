package back.ciriu.controllers;

import back.ciriu.entities.CountryEntity;
import back.ciriu.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("")
    public ResponseEntity<List<CountryEntity>> getAll() {
        return ResponseEntity.ok(countryService.getAllCountry());
    }
}
