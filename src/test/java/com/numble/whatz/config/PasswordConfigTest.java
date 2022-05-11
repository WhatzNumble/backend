package com.numble.whatz.config;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RepeatedTest(5)
    void passwordEncoder() {
        String encode = passwordEncoder.encode("13579abcd");

        System.out.println(encode);
        assertThat(passwordEncoder.matches("13579abcd", encode)).isTrue();

    }
}