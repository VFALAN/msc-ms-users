package com.msc.ms.users.locality;

import com.msc.ms.users.location.ILocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class LocalityRemoteTest {
    private static final Integer idLocality = 1;
    @Autowired
    private ILocationService iLocationService;

    public LocalityRemoteTest() {

    }

    @Test
    public void testRemoteService() {
        final var response = this.iLocationService.getLocation(idLocality);

        Assert.assertSame(response.getStatusCode(), HttpStatus.OK);
    }

}
