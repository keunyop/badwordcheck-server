package com.badwordcheck.webservice.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {
        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<K, V> reverseSortedMap = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        return reverseSortedMap;
    }
}
