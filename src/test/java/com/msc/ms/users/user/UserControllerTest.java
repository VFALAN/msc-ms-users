package com.msc.ms.users.user;

import base.BaseTestConfiguration;
import com.msc.ms.users.crypto.CryptoService;
import com.msc.ms.users.user.model.request.UserRegistryRequest;
import com.msc.ms.users.user.services.UserService;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Slf4j
@Sql(value = "classpath:scripts/uniqueUserCasesTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "classpath:scripts/uniqueUserCasesCleanTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class UserControllerTest extends BaseTestConfiguration {

    @Value("${msc.security.own.key}")
    private String key;
    @Value("${msc.security.header}")
    private String header;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Validator validator;
    @Autowired
    private CryptoService cryptoService;

    @Test
    void testValidations() {
        final var mRegistryRequest = UserRegistryRequest.builder()
                .name("name")
                .middleName("middleName")
                .lastName("lastName")
                .age(18)
                .email("email000000@email.com")
                .phoneNumber("000000000000")
                .userName("userName0000000")
                .password("Hola123$")
                .birthDate(new Date())
                .build();
        final var errors = this.validator.validate(mRegistryRequest);
        errors.forEach(error -> {
            log.info("error in: {} due: {}", error.getPropertyPath().toString(), error.getMessage());
        });
        assert errors.isEmpty();
    }

    @Test
    void testFaillValidations() throws Exception {
        final var mRegistryRequest = UserRegistryRequest.builder()
                .name("name")
                .middleName("middleName")
                .lastName("lastName")
                .age(18)
                .email("existingUsername@mail.com")
                .phoneNumber("5578303479")
                .userName("existingUsername")
                .password("Hola123$")
                .birthDate(new Date())
                .build();
        final var errors = this.validator.validate(mRegistryRequest);
        log.info("total errores: {}", errors.size());
        assert errors.size() == 3;
    }

    @Test
    void testRegistryUser() throws Exception {
        final var mObjectMapper = new ObjectMapper();
        final var mRegistryRequest = UserRegistryRequest.builder()
                .name("name")
                .middleName("middleName")
                .lastName("lastName")
                .age(18)
                .email("email@email.com")
                .phoneNumber("525578303479")
                .userName("userName")
                .birthDate(new Date())
                .password("Hola123$")
                .build();
        this.mockMvc.perform(post("/api/users/v1/registry")
                        .header(this.header, this.key)
                        .content(mObjectMapper.writeValueAsString(mRegistryRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testCrypto() {
        List.of(
                "msc-users", "msc-auth", "msc-address", "msc-file",
                "msc-ui-web", "msc-process", "msc-notification", "msc-event",
                "msc-registry", "msc-api-gateway", "msc-message", "msc-report",
                "msc-geo", "msc-scheduler", "msc-configuration"
        ).forEach(app -> {
            final String encrypted;
            try {
                encrypted = this.cryptoService.encrypt(app);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("app : {} ,encrypted: {}", app, encrypted);
        });
    }
}
