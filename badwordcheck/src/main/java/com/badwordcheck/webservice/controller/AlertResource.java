package com.badwordcheck.webservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.badwordcheck.webservice.dto.Alert;
import com.badwordcheck.webservice.service.AlertHardcodedService;

@CrossOrigin("http://localhost:4200")
@RestController
public class AlertResource {

    @Autowired
    private AlertHardcodedService alertService;

    @GetMapping("/users/{username}/alerts")
    public List<Alert> getAllAlerts(@PathVariable String username) {
	return alertService.findAll();
    }

    @DeleteMapping("/users/{username}/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable String username, @PathVariable long id) {
	Alert alert = alertService.deleteById(id);

	if (alert != null) {
	    return ResponseEntity.noContent().build();
	}

	return ResponseEntity.notFound().build();
    }

}
