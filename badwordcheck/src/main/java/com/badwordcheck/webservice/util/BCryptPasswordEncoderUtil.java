package com.badwordcheck.webservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderUtil {
    public static void main(String[] args) {
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	String encodedString = encoder.encode("dummy");
	System.out.println(encodedString);
    }
}
