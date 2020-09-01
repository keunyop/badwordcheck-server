package com.badwordcheck.webservice.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @GetMapping("/users/{username}/alerts/{id}")
    public Alert getAllAlerts(@PathVariable String username, @PathVariable long id) {
	return alertService.findById(id);
    }

    @DeleteMapping("/users/{username}/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable String username, @PathVariable long id) {
	Alert alert = alertService.deleteById(id);

	if (alert != null) {
	    return ResponseEntity.noContent().build();
	}

	return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{username}/alerts/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable String username, @PathVariable long id,
	    @RequestBody Alert alert) {

	Alert alertUpdated = alertService.save(alert);
	return new ResponseEntity<Alert>(alertUpdated, HttpStatus.OK);
    }

    @PostMapping("/users/{username}/alerts")
    public ResponseEntity<Void> insertAlert(@PathVariable String username, @RequestBody Alert alert) {

	Alert createdAlert = alertService.save(alert);

	URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdAlert.getId())
		.toUri();

	return ResponseEntity.created(uri).build();
    }

}
