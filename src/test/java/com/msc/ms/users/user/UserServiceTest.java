package com.msc.ms.users.user;

import com.msc.ms.users.address.AddressEntity;
import com.msc.ms.users.user.model.UserEntity;
import com.msc.ms.users.user.model.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    public UserServiceTest() {
    }

    @Test
    public void testMapper() throws ParseException {
        final var requiredObject = UserRequestDTO.builder()
                .age(18)
                .lastName("villafan")
                .middleName("flores")
                .name("alan")
                .userName("loco25")
                .phoneNumber("525578303479")
                .email("alan.villafan@gmail.com")
                .birthDate(new Date())
                .description("porton azul")
                .idProfile(1)
                .idLocation(1)
                .street("tehotihuacan")
                .number("mz 2")
                .build();
        final var user = this.modelMapper.map(requiredObject, UserEntity.class);
        final var address = this.modelMapper.map(requiredObject, AddressEntity.class);

        Assert.assertNotNull(user);
    }

    @Test
    public void addTestUser() throws Exception {
        final var requiredObject = UserRequestDTO.builder()
                .age(18)
                .lastName("villafan")
                .middleName("flores")
                .name("alan")
                .userName("loco25")
                .phoneNumber("525578303479")
                .email("alan.villafan@gmail.com")
                .birthDate(new Date())
                .description("porton azul")
                .idProfile(1)
                .idLocation(1)
                .street("tehotihuacan")
                .number("mz 2")
                .build();
        final var response = this.userService.createUser(requiredObject);
        Assert.assertNotNull(response.getIdUser());
    }

    @Test
    public void searchUsername() {
        final var username = "vifaAdmin";
        final var count = userRepository.searchUsername(username);
        Assert.assertEquals(1, (int) count);
    }
}
