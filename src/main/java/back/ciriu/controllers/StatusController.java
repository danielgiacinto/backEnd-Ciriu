package back.ciriu.controllers;

import back.ciriu.entities.StatusEntity;
import back.ciriu.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("")
    public ResponseEntity<List<StatusEntity>> getAll() {
        return ResponseEntity.ok(statusService.getAllStatus());
    }
}
