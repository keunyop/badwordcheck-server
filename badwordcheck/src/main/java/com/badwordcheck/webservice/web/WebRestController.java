package com.badwordcheck.webservice.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.badwordcheck.webservice.dto.CheckResultDto;
import com.badwordcheck.webservice.service.CheckService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class WebRestController {
    private CheckService checkService;

    @PostMapping("/check")
    public CheckResultDto check(@RequestBody
    String text) {
        return checkService.check(text);
    }
}
