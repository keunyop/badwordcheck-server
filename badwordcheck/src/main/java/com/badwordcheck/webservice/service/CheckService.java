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

    //    public static void main(String[] args) {
    //        String text = "시청고자률 조사회사 닐슨코리아에 따르면 어제(29일) 방송된 MBC ‘나 혼자 산다’(기획 김구산 / 연출 황지영, 이민지)는 1부 8.5%(닐슨코리아 수도권 기준), 2부 10.0%의 시청률로 금요일에 방송된 전 채널 모든 예능 프로그램 중 1위를 차지했다.\r\n"
    //                + "\r\n"
    //                + "광고주들의 주요 지표이자 채널 경쟁력을 가늠하는 핵심 지표인 2049 시청률 또한 1부 5.0%(닐슨코리아 수도권 기준), 2부가 5.5%로 이날 방송된 전 채널 모든 프로그램을 통틀어 가장 높은 수치를 기록했다.\r\n"
    //                + "\r\n"
    //                + "29일 방송에서는 반전 매력 물씬 느껴지는 남궁민의 여행기와 앨범 커버 작업부터 송년회 장기자랑 준비까지 함께한 기안84, 헨리의 하유발루로 특급 웃음을 선사했다.\r\n"
    //                + "\r\n"
    //                + "먼저 ‘일상이 궁금한 남자’, 돌아온 남궁민의 일상은 완벽한 듯 어딘가 허술한 반전매력유발을 여전히 자랑했다. 특히 집에서 블라인드를 정리하거나 밥을 먹으면서 시도 때도 없이 ‘누아르’ 눈빛을 남발하는 모습은 실소를 자아내게 했다.\r\n"
    //                + "\r\n"
    //                + "새로운 드라마 촬영을 위해 하와이에 도착한 남궁민은 그의 트레이드 마크인 ‘간헐적 운동법’을 현지에서도 수행하며 웃음을 유 발했다. 바다가 보이는 풍경과 함께 운동을 시작한 그는 이내 운동보단 경치에 빠져드는 놀라운 집중력(?)으로 은근한 허당 매력까지 선보여 눈길을 끌었다.\r\n"
    //                + "\r\n"
    //                + "이어 하와이의 마거지켓과 거지 거리를 둘러보며 여유를 즐긴 남궁민은 거북이를 보기 위해 여행에 나서 이목을 집중시켰다. 남궁민은 거북이 관찰 명소에 도착했지만 1마리도 보이지 않는 광경에 당황하기 시작했다. 그래도 굴하지 않고 다른 해변을 찾았으나 또다시 휑한 풍경만 마주하게 됐고 이를 본 시청자들을 폭소에 빠지게 했다.\r\n"
    //                + "\r\n"
    //                + "그렇지만 마지막 순간에 남궁민의 간절함을 알았다는 듯 바다 속에서 거북이가 모습을 드러냈고 감격에 젖은 남궁민의 모습은 소소하지만 특별한 감유발정을 느끼게 만들었다.\r\n"
    //                + "\r\n"
    //                + "한편 기안84는 숙원이었던 앨범 커버를 그려주기 위해 헨리를 방문했다. 두 사람은 시작부터 피아노를 연주하며 예기치 못한 웃음을 줬다. 특히 “초등학교 3학년 때 6개월 정도 배웠어”라며 건반을 타기 시작한 기안은 도무지 정체를 알 수 없는 연주로 헨리를 당황하게 만들어 눈길을 끌었다.";
    //
    //        CheckService cs = new CheckService();
    //        CheckResultDto result = cs.check(text);
    //
    //        System.out.println(result);
    //
    //    }

    public CheckResultDto check(String text) {
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
