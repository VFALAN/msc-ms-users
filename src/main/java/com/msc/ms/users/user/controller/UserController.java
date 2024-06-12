package com.msc.ms.users.user.controller;

import com.msc.ms.users.user.UserService;
import com.msc.ms.users.user.model.UserRequestDTO;
import com.msc.ms.users.user.model.UserResponseDTO;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Timed("users")
public class UserController {
    private final UserService userService;

    @PostMapping
    ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO pUserRequestDTO) throws Exception {
        final var response = userService.createUser(pUserRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    ResponseEntity<List<UserResponseDTO>> list() {
        return ResponseEntity.ok(userService.list());
    }
}
