package com.rest.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vikasnaiyar on 18/09/17.
 */
public class WordToNumberUtil {

    private static final List<String> allowedStrings = Arrays.asList
            (
                    "zero", "one", "two", "three", "four", "five", "six", "seven",
                    "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
                    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
                    "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
                    "hundred", "thousand", "million", "billion", "trillion"
            );

    private static final Map<String, Integer> timeToMonthsMapping = new HashMap<String,Integer>(){{
        put("year",12);
        put("years",12);
        put("month",1);
        put("months",1);
    }};

    public  static  void main(String[] args) {
        String exp = "forty years";
        System.out.println(getExpInMonths(exp));
    }

    public static int getExpInMonths(String experience) {
        int months = 0;

        String[] expTokens = experience.split(" ");

        if(expTokens != null && expTokens.length > 1) {
            String token1 = expTokens[0];

            int firstTokenValue = parseInteger(token1);

            if(firstTokenValue == 0) {

                firstTokenValue = parseInteger(token1.replaceAll("[^0-9]", ""));

                if(firstTokenValue == 0) {
                    firstTokenValue = convertToInteger(token1);
                }
            }

            if(firstTokenValue != 0) {
                String token2 = expTokens[1];
                if(timeToMonthsMapping.containsKey(token2.toLowerCase())){
                     months = firstTokenValue * timeToMonthsMapping.get(token2.toLowerCase());
                }
            }
        }

        return months;
    }

    private  static int parseInteger(String token) {
        int tokenValue = 0;
        try {
            tokenValue = Integer.parseInt(token);
        } catch (NumberFormatException nfe) {
            // TODO
        }
        return  tokenValue;
    }

    private static int convertToInteger(String input) {

        boolean isValidInput = true;
        int result = 0;
        int finalResult = 0;

        if (input != null && input.length() > 0) {
            input = input.replaceAll("-", " ");
            input = input.toLowerCase().replaceAll(" and", " ");
            String[] splittedParts = input.trim().split("\\s+");

            for (String str : splittedParts) {
                if (!allowedStrings.contains(str)) {
                    isValidInput = false;
                    System.out.println("Invalid word found : " + str);
                    break;
                }
            }
            if (isValidInput) {
                for (String str : splittedParts) {
                    if (str.equalsIgnoreCase("zero")) {
                        result += 0;
                    } else if (str.equalsIgnoreCase("one")) {
                        result += 1;
                    } else if (str.equalsIgnoreCase("two")) {
                        result += 2;
                    } else if (str.equalsIgnoreCase("three")) {
                        result += 3;
                    } else if (str.equalsIgnoreCase("four")) {
                        result += 4;
                    } else if (str.equalsIgnoreCase("five")) {
                        result += 5;
                    } else if (str.equalsIgnoreCase("six")) {
                        result += 6;
                    } else if (str.equalsIgnoreCase("seven")) {
                        result += 7;
                    } else if (str.equalsIgnoreCase("eight")) {
                        result += 8;
                    } else if (str.equalsIgnoreCase("nine")) {
                        result += 9;
                    } else if (str.equalsIgnoreCase("ten")) {
                        result += 10;
                    } else if (str.equalsIgnoreCase("eleven")) {
                        result += 11;
                    } else if (str.equalsIgnoreCase("twelve")) {
                        result += 12;
                    } else if (str.equalsIgnoreCase("thirteen")) {
                        result += 13;
                    } else if (str.equalsIgnoreCase("fourteen")) {
                        result += 14;
                    } else if (str.equalsIgnoreCase("fifteen")) {
                        result += 15;
                    } else if (str.equalsIgnoreCase("sixteen")) {
                        result += 16;
                    } else if (str.equalsIgnoreCase("seventeen")) {
                        result += 17;
                    } else if (str.equalsIgnoreCase("eighteen")) {
                        result += 18;
                    } else if (str.equalsIgnoreCase("nineteen")) {
                        result += 19;
                    } else if (str.equalsIgnoreCase("twenty")) {
                        result += 20;
                    } else if (str.equalsIgnoreCase("thirty")) {
                        result += 30;
                    } else if (str.equalsIgnoreCase("forty")) {
                        result += 40;
                    } else if (str.equalsIgnoreCase("fifty")) {
                        result += 50;
                    } else if (str.equalsIgnoreCase("sixty")) {
                        result += 60;
                    } else if (str.equalsIgnoreCase("seventy")) {
                        result += 70;
                    } else if (str.equalsIgnoreCase("eighty")) {
                        result += 80;
                    } else if (str.equalsIgnoreCase("ninety")) {
                        result += 90;
                    } else if (str.equalsIgnoreCase("hundred")) {
                        result *= 100;
                    }
                }

                finalResult += result;
                result = 0;

            }
        }

        return finalResult;
    }
}
