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
	alerts.add(new Alert(++idCounter, "Correctness", false, new Date()));
	alerts.add(new Alert(++idCounter, "Clarity", false, new Date()));
	alerts.add(new Alert(++idCounter, "Engagement", false, new Date()));
    }

    public List<Alert> findAll() {
	return alerts;
    }
}
