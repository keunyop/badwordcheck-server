package com.badwordcheck.webservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.badwordcheck.webservice.dto.Alert;
import com.badwordcheck.webservice.service.AlertHardcodedService;

@RestController
public class AlertResource {

    @Autowired
    private AlertHardcodedService alertService;

    @GetMapping("/users/{username}/alerts")
    public List<Alert> getAllAlerts(@PathVariable String username) {
	return alertService.findAll();
    }
}
