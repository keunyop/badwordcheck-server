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
import com.badwordcheck.webservice.repository.AlertJpaRepository;

@CrossOrigin("http://localhost:4200")
@RestController
public class AlertResource {

    @Autowired
    private AlertJpaRepository alertJpaRepository;

    @GetMapping("/users/{username}/alerts")
    public List<Alert> getAllAlerts(@PathVariable String username) {
	return alertJpaRepository.findByUsername(username);
    }

    @GetMapping("/users/{username}/alerts/{id}")
    public Alert getAlert(@PathVariable String username, @PathVariable long id) {
	return alertJpaRepository.findById(id).get();
    }

    @DeleteMapping("/users/{username}/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable String username, @PathVariable long id) {
	alertJpaRepository.deleteById(id);
	return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{username}/alerts/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable String username, @PathVariable long id,
	    @RequestBody Alert alert) {

	Alert alertUpdated = alertJpaRepository.save(alert);
	return new ResponseEntity<Alert>(alertUpdated, HttpStatus.OK);
    }

    @PostMapping("/users/{username}/alerts")
    public ResponseEntity<Void> createAlert(@PathVariable String username, @RequestBody Alert alert) {

	alert.setUsername(username);

	Alert createdAlert = alertJpaRepository.save(alert);

	URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdAlert.getId())
		.toUri();

	return ResponseEntity.created(uri).build();
    }

}
