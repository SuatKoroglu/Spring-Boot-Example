package com.example.demo.user;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<LocalUser> updateUser(
            @AuthenticationPrincipal LocalUser user,
            @RequestBody LocalUser localUser){
        return  ResponseEntity.ok(userService.updateUser(user.getId(), localUser));
    }
}
