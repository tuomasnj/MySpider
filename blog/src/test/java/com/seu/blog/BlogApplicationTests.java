package com.seu.blog;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        String pwd="666666";
        String salt = "mszlu!@#";
        String password= DigestUtils.md5Hex(pwd+salt);
        System.out.println(password);
    }

}
