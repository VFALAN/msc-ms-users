package com.msc.ms.users.user;

import base.BaseTestConfiguration;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Slf4j
public class UserControllerTest extends BaseTestConfiguration {

    @Value("${msc.security.own.key}")
    private String key;
    @Value("${msc.security.header}")
    private String header;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Validator validator;

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
                .birthDate(new Date())
                .build();
        final var errors = this.validator.validate(mRegistryRequest);
        errors.forEach(error -> {
            log.info("error in: {} due: {}", error.getInvalidValue().toString(), error.getMessage());
        });
        assert errors.isEmpty();
    }

    @Test
    @Sql(value = "classpath:scripts/uniqueUserCasesTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:scripts/uniqueUserCasesCleanTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFaillValidations() throws Exception {
        final var mRegistryRequest = UserRegistryRequest.builder()
                .name("name")
                .middleName("middleName")
                .lastName("lastName")
                .age(18)
                .email("existingUsername@mail.com")
                .phoneNumber("5578303479")
                .userName("existingUsername")
                .birthDate(new Date())
                .build();
        final var errors = this.validator.validate(mRegistryRequest);
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
                .build();
        this.mockMvc.perform(post("/api/users/v1/registry")
                        .header(this.header, this.key)
                        .content(mObjectMapper.writeValueAsString(mRegistryRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
