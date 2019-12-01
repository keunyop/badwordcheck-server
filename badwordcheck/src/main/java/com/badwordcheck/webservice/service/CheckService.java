package com.badwordcheck.webservice.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.badwordcheck.webservice.dto.CheckResultDto;
import com.badwordcheck.webservice.util.Constants;
import com.badwordcheck.webservice.util.MapUtil;

@Service
public class CheckService {

    public static void main(String[] args) {

        String text = "1) 증빙서류 : 실물제출 → 이미지형식으로 업로드\r\n" + "\r\n"
                + "    - 주의사항 : 이미지형식이기 때문에 내용이 제대로 보이지 않으면 반려되오니(처음부터 다시 재기안 진행) 유의부탁드립니다!\r\n" + "\r\n"
                + "        * 이미지형식 : 사진파일 압축(zip) OR PDF스캔본\r\n" + "\r\n" + " \r\n" + "\r\n"
                + " 2) PINKIE 경비청구서에 관리자 승인서명 → 전결규정에 따른 전자결재로 진행\r\n" + "\r\n"
                + "     - 주의사항 : 지정한 결재자가 모두 결재가 완료가 되어야 총무팀으로 해당 청구건이 넘어옵니다. \r\n" + "\r\n"
                + "        넘어온 시점부터 경비지급처리가 진행되오니 참고부탁드립니다!\r\n" + "\r\n" + " \r\n" + "\r\n"
                + " 3) 증빙보완이 있을시, 관리자승인이 된 상태에서 반려가 아닌 총무팀과 별도 메일을 주고받아 증빙보완처리 → 재기안 진행\r\n" + "\r\n"
                + "      - 주의사항 : 결재완료-총무팀수신-증빙보완 발생시, 사유와 함께 반려-재기안 되므로 증빙에 대한 문의사항이 있을시 \r\n" + "\r\n"
                + "            먼저 총무팀(이소라)에게 문의후 진행하여 재기안되는 일이 없도록 주의바랍니다.\r\n" + "\r\n"
                + "            (모든 증빙서류는 이제 실물보관이 아닌 다우오피스에 업로드된 파일로 보관하기 때문에(향후 감사대비) \r\n" + "\r\n"
                + "              별도로 메일로 받지 않으며, 재기안하여 증빙 업로드를 요합니다.)";

        CheckService checkService = new CheckService();
        CheckResultDto resultDto = checkService.check(text);
        System.out.println(resultDto.toString());
    }

    public CheckResultDto check(String text) {
        // Remove all new line
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
        for (String word : text.split("\\s")) {
            if (!word.isEmpty() && word.length() > 1) {
                int cnt = keyWords.containsKey(word) ? keyWords.get(word) : 0;
                keyWords.put(word, ++cnt);
            }
        }

        return CheckResultDto.newInstance(badWords, MapUtil.sortByValueDesc(keyWords));
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
