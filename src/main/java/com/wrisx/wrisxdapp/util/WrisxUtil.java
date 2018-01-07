package com.wrisx.wrisxdapp.util;

import com.wrisx.wrisxdapp.errorhandling.UnauthorizedException;
import com.wrisx.wrisxdapp.errorhandling.ValidationException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.isEmpty;

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
            keywordList.add(tokenizer.nextToken().trim().toUpperCase());
        }

        return keywordList;
    }

    public static void verifyUserAuthorisation(String userAddress, String userAuthorised) {
        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", userAddress));
        }
    }

    public static void validateStringArgument(String arg, String errorMessage) {
        if (isEmpty(arg)) {
            throw new ValidationException(errorMessage);
        }
    }

    public static void validateStringArguments(String ... args) {
        asList(args).stream().forEach(arg -> {
            if (isEmpty(arg)) {
                throw new ValidationException("No value can be null or empty");
            }
        });
    }
}
