package com.QuanLyDatBanNhaHang.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashPrinter {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("========================================================");
        System.out.println("BCRYPT HASH FOR 'admin123': " + encoder.encode("admin123"));
        System.out.println("BCRYPT HASH FOR 'staff01': " + encoder.encode("staff01"));
        System.out.println("BCRYPT HASH FOR 'password123': " + encoder.encode("password123"));
        System.out.println("========================================================");
    }
}
