package back.ciriu.controllers;

import back.ciriu.models.Request.UserRequest;
import back.ciriu.models.Response.UserResponse;
import back.ciriu.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@Valid @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateInfo(@RequestBody UserRequest request, @PathVariable UUID id) {
        return ResponseEntity.ok(userService.updateInfo(request, id));
    }
}
