package com.badwordcheck.webservice.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckResultDto {
	private Map<String, Integer> badwords;
	private Map<String, Integer> keywords;
	
	private CheckResultDto(Map<String, Integer> badwords, Map<String, Integer> keywords) {
		this.badwords = badwords;
		this.keywords = keywords;
	}

	public static CheckResultDto newInstance(Map<String, Integer> badwords, Map<String, Integer> keywords) {
		return new CheckResultDto(badwords, keywords);
	}
}