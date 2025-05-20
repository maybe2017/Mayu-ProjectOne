package com.mayu.practice;

import com.mayu.NeptuneApplication;
import com.mayu.practice.service.impl.UserInfoApiClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class WebServiceTest {

    @Autowired
    private UserInfoApiClient userInfoApiClient;

    @Test
    public void queryUsersTest() throws Exception {
        System.out.println("***************");
        String resp = userInfoApiClient.queryUsersWithDynamic();
        System.out.println(resp);
        System.out.println("***************");
    }
}
