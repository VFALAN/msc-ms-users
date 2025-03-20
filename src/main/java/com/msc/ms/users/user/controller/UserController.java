package com.msc.ms.users.user.controller;

import com.msc.ms.users.common.model.dto.DataTableRequest;
import com.msc.ms.users.user.model.request.UserRegistryRequest;
import com.msc.ms.users.user.model.response.UserRegistryResponse;
import com.msc.ms.users.user.services.UserDatatableService;
import com.msc.ms.users.user.services.UserService;
import com.msc.ms.users.user.model.request.UserRequestDTO;
import com.msc.ms.users.user.model.response.UserResponseDTO;
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
    private final UserDatatableService userDatatableService;

    @PostMapping
    ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO pUserRequestDTO) throws Exception {
        final var response = userService.createUser(pUserRequestDTO);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/student/v1/registry")
    ResponseEntity<UserRegistryResponse> registry(@Valid @RequestBody UserRegistryRequest pUserRequestDTO) throws Exception {
        final var response = userService.userRegistry(pUserRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    ResponseEntity<List<UserResponseDTO>> list() {
        return ResponseEntity.ok(userService.list());
    }

    // TODO add search endpoint
    // todo endpoint for single default search
    // todo endpoint for single sorted or filter 1 field


    // todo extended datatable system
    public ResponseEntity<?> getDatable(@RequestBody @Valid DataTableRequest pDataTableRequest) {
        final var mDynamicDataTableResponse = userDatatableService.userPageByCriteria(pDataTableRequest);
        return ResponseEntity.ok(mDynamicDataTableResponse);
    }
}
