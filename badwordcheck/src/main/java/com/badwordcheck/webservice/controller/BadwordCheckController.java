package com.badwordcheck.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.badwordcheck.webservice.dto.CheckResultDto;
import com.badwordcheck.webservice.service.CheckService;

@CrossOrigin("http://localhost:4200")
@RestController
public class BadwordCheckController {
    @Autowired
    private CheckService checkService;

    @PostMapping("/checkbadwords")
    public CheckResultDto check(@RequestBody String text) {
	return checkService.check(text);
    }
}