package com.badwordcheck.webservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.badwordcheck.webservice.dto.CheckResultDto;

@Service
public class CheckService {
	public CheckResultDto check(String text) {
		
		Map<String, Integer> badwords = new HashMap<>();
		badwords.put("bad", 1);
		badwords.put("not good", 2);
		
		return CheckResultDto.newInstance(badwords, null);
	}
}
