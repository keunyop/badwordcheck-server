package com.badwordcheck.webservice.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.badwordcheck.webservice.dto.CheckResultDto;
import com.badwordcheck.webservice.util.Constants;
import com.badwordcheck.webservice.util.MapUtil;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

@Service
public class CheckService {

    public CheckResultDto check(String text) {
        // Remove all new lines
        text = text.replace("\\n", "").replace("\\r", "");

        Map<String, Integer> badWords = new HashMap<>(); // 금지어 목록
        Map<String, Integer> keyWords = new HashMap<>(); // 키워드 목록

        // 금지어 검사
        for (String bWord : Constants.BAD_WORDS) {

            // remove all white space
            if (text.replaceAll("\\s", "").contains(bWord)) {
                Set<String> subBWords = _makeSubPatterns(bWord, null, null);

                for (String subBWord : subBWords) {
                    if (text.contains(subBWord)) {
                        int cnt = badWords.containsKey(subBWord) ? badWords.get(subBWord) : 0;
                        badWords.put(subBWord, ++cnt);
                    }
                }
            }
        }

        // 키워드 검사
        for (String keyword : _analyzeKeywords(text)) {
            if (keyword.length() > 1) {
                int cnt = keyWords.containsKey(keyword) ? keyWords.get(keyword) : 0;
                keyWords.put(keyword, ++cnt);
            }
        }

        return CheckResultDto.newInstance(badWords, MapUtil.sortByValueDesc(keyWords));
    }

    // 키워드 형태소 분석
    private List<String> _analyzeKeywords(String text) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        return komoran.analyze(text).getNouns();
    }

    /**
     * Recursive하게 다양한 금칙어 패턴 생성 
     */
    private Set<String> _makeSubPatterns(String bWord, String prefix, String postfix) {
        Set<String> patternSet = new HashSet<>();

        /**
         * 금지어 자체 & 금지어 전체 뛰어쓰기
         */
        if (prefix == null && postfix == null) {
            // 금지어 차제
            patternSet.add(bWord);

            // 금지어 전체 뛰어쓰기
            patternSet.add(_appendSpaces(bWord));
        }

        /**
         * 금지어 그외 나올 수 있는 모든 케이스
         */
        if (bWord.length() > 1) {
            for (int i = 1; i < bWord.length(); i++) {
                StringBuilder patternBuilder = new StringBuilder();

                if (prefix != null && !prefix.isEmpty()) {
                    patternBuilder.append(prefix);
                }

                int cnt = 1;
                for (char c : bWord.toCharArray()) {
                    patternBuilder.append(c);

                    if (cnt == i) {
                        patternBuilder.append(" ");
                    }

                    cnt++;
                }

                if (postfix != null && !postfix.isEmpty()) {
                    patternBuilder.append(postfix);
                }

                String pattern = patternBuilder.toString();

                patternSet.add(pattern);

                String[] wordGroup = pattern.split(" ");

                for (int j = 0; j < wordGroup.length; j++) {

                    if (wordGroup[j].length() > 2) {
                        StringBuilder prefixBuilder = new StringBuilder();
                        if (j != 0) {
                            for (int k = 0; k < j; k++) {
                                prefixBuilder.append(wordGroup[k]);
                                prefixBuilder.append(" ");
                            }
                        }

                        StringBuilder postfixBuilder = new StringBuilder();
                        if (j != wordGroup.length - 1) {
                            for (int k = j + 1; k <= wordGroup.length - 1; k++) {
                                postfixBuilder.append(" ");
                                postfixBuilder.append(wordGroup[k]);
                            }
                        }

                        patternSet.addAll(
                                _makeSubPatterns(wordGroup[j], prefixBuilder.toString(), postfixBuilder.toString()));
                    }
                }
            }
        }

        return patternSet;
    }

    /**
     * 각 글자에 뛰어쓰기 추가 
     */
    private String _appendSpaces(String word) {
        StringBuilder allSpaceBuilder = new StringBuilder();

        int cnt = 1;
        for (char c : word.toCharArray()) {
            allSpaceBuilder.append(c);

            if (cnt < word.length()) {
                allSpaceBuilder.append(" ");
            }

            cnt++;
        }

        return allSpaceBuilder.toString();
    }
}
