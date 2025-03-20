package com.msc.ms.users.user;

import base.BaseTestConfiguration;
import com.msc.ms.users.user.model.UserEntity;
import com.msc.ms.users.user.model.request.UserRegistryRequest;
import com.msc.ms.users.user.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest


@Slf4j
class UserServiceTest extends BaseTestConfiguration {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    public UserServiceTest() {
    }


    @Test
    void registryUser() throws Exception {
        final var mRegistryRequest = UserRegistryRequest.builder()
                .name("name")
                .middleName("middleName")
                .lastName("lastName")
                .age(18)
                .email("email@email.com")
                .phoneNumber("525578303479")
                .userName("userName")
                .password("Hola123$")
                .birthDate(new Date())
                .build();
        final var savedUser = this.userService.userRegistry(mRegistryRequest);
        assertNotNull(savedUser.getIdUser());
    }

    @Test
    @Sql(value = "classpath:scripts/uniqueUserCasesTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:scripts/uniqueUserCasesCleanTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testErrorCaseForSPUniqueUsername() {
        final var noExistingUser = "existingUsername";
        assertFalse(this.userService.validUsername(noExistingUser));
    }

    @Test
    void testSP() {
        final var noExistingUser = "noExistingUser";
        assertTrue(this.userService.validUsername(noExistingUser));
    }

    @Test
    public void testMapper() throws ParseException {
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
        final var user = this.modelMapper.map(mRegistryRequest, UserEntity.class);
        assertEquals(mRegistryRequest.getName(), user.getName());
        assertEquals(mRegistryRequest.getMiddleName(), user.getMiddleName());
        assertEquals(mRegistryRequest.getLastName(), user.getLastName());
        assertEquals(mRegistryRequest.getAge(), user.getAge());
        assertEquals(mRegistryRequest.getEmail(), user.getEmail());
        assertEquals(mRegistryRequest.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(mRegistryRequest.getUserName(), user.getUserName());
        assertEquals(mRegistryRequest.getBirthDate(), user.getBirthDate());
    }


}
