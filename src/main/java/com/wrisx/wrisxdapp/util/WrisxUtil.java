package com.wrisx.wrisxdapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WrisxUtil {
    public static <T> List<T> getListFromIterable(Iterable<T> iterable) {
        List<T> items = new ArrayList<>();
        iterable.forEach(k -> items.add(k));
        return items;
    }

    public static List<String> getKeywordList(String keywords) {
        List<String> keywordList = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(keywords, ",");
        while (tokenizer.hasMoreElements()) {
            keywordList.add(tokenizer.nextToken().toUpperCase());
        }
        return keywordList;
    }
}
