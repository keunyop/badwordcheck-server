package com.badwordcheck.webservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badwordcheck.webservice.dto.AuthenticationBean;

@CrossOrigin("http://localhost:4200")
@RestController
public class BasicAuthenticationController {

    @GetMapping("/basicauth")
    public AuthenticationBean authenticationBean() {
	return new AuthenticationBean("You are authenticated");
    }
}
