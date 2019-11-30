package com.badwordcheck.webservice.dto;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class CheckResultDto {
    private Map<String, Integer> badwords;
    private Map<String, Integer> keywords;

    private CheckResultDto(Map<String, Integer> badwords, Map<String, Integer> keywords) {
        this.badwords = badwords;
        this.keywords = keywords;
    }

    public static CheckResultDto newInstance(Map<String, Integer> badwords, Map<String, Integer> keywords) {
        // ∫Û map¿Ã∏È null return
        return new CheckResultDto(CollectionUtils.isEmpty(badwords) ? null : badwords,
                CollectionUtils.isEmpty(keywords) ? null : keywords);
    }
}