package com.badwordcheck.webservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.badwordcheck.webservice.dto.Alert;

@Service
public class AlertHardcodedService {
    private static List<Alert> alerts = new ArrayList<>();
    private static int idCounter = 0;

    static {
	alerts.add(new Alert(++idCounter, "Correctnesss", false, new Date()));
	alerts.add(new Alert(++idCounter, "Clarityy", false, new Date()));
	alerts.add(new Alert(++idCounter, "Engagementt", false, new Date()));
    }

    public List<Alert> findAll() {
	return alerts;
    }

    public Alert save(Alert alert) {
	if (alert.getId() == -1 || alert.getId() == 0) {
	    alert.setId(++idCounter);
	    alerts.add(alert);
	} else {
	    deleteById(alert.getId());
	    alerts.add(alert);
	}

	return alert;
    }

    public Alert deleteById(long id) {
	Alert alert = findById(id);

	if (alert == null) {
	    return null;
	}

	if (alerts.remove(alert)) {
	    return alert;
	}

	return null;
    }

    public Alert findById(long id) {
	for (Alert alert : alerts) {
	    if (alert.getId() == id) {
		return alert;
	    }
	}

	return null;
    }
}
