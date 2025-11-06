package com.org.ServiceFinder.controller;

import com.org.ServiceFinder.dto.UserDTO;
import com.org.ServiceFinder.model.User;
import com.org.ServiceFinder.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User user = userService.getCurrentUser();
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(), // FIXED: .name() for enum to string
                user.getAddress()
        );
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        UserDTO userDTO = new UserDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getRole().name(), // FIXED: .name() for enum to string
                updatedUser.getAddress()
        );
        return ResponseEntity.ok(userDTO);
    }
}