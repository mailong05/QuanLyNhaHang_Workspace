package com.QuanLyDatBanNhaHang.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneratorTest {
    @Test
    public void generatePasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("==================================================");
        System.out.println("HASH FOR admin123: " + encoder.encode("admin123"));
        System.out.println("HASH FOR staff01: " + encoder.encode("staff01"));
        System.out.println("HASH FOR password123: " + encoder.encode("password123"));
        System.out.println("==================================================");
    }
}
