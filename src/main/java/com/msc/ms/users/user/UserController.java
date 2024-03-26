package com.msc.ms.users.user;

import com.msc.ms.users.user.model.UserRequestDTO;
import com.msc.ms.users.user.model.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO pUserRequestDTO) throws Exception {
        final var response = userService.createUser(pUserRequestDTO);
        return ResponseEntity.ok(response);
    }

}
