package com.malaab.ya.action.actionyamalaab.utils;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }


    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }


    public static String insertCharacterAtPosition(String text, int position, String character) {
        return new StringBuilder(text).insert(position, character).toString();
    }


    public static String replaceNull(String value, String replace) {
        if (isEmpty(value))
            return replace;

        return replace + value;
    }


    public static boolean contain(String original, String value) {
        return !isEmpty(original) && !isEmpty(value) && original.toLowerCase().contains(value);
    }

    public static boolean contain(int original, String value) {
        return !isEmpty(String.valueOf(original)) && !isEmpty(value) && String.valueOf(original).toLowerCase().contains(value);
    }


    public static boolean contain(long original, String value) {
        return !isEmpty(String.valueOf(original)) && !isEmpty(value) && String.valueOf(original).toLowerCase().contains(value);
    }


    public static boolean isWordsMatched(String word1, String confirmWord) {
        Pattern pattern = Pattern.compile(word1, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(confirmWord);

        return matcher.matches();
    }


    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : String.valueOf(Character.toUpperCase(c)) + str.substring(1);
    }

    /**
     * Capitalizes each word in the string.
     **/
    public static String capitalizeString(String string) {

        if (string == null) {
            throw new NullPointerException("String to capitalize cannot be null");
        }

        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You
                // can add other
                // chars here
                found = false;
            }
        } // end for

        return String.valueOf(chars);
    }

    @Nullable
    /*
     * Partially capitalizes the string from parameter start and offset.
     *
     * @param string String to be formatted
     * @param start  Starting designation of the substring to be capitalized
     * @param offset Offset of characters to be capitalized
     * @return
     */
    public static String capitalizeString(String string, int start, int offset) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string.substring(start, offset).toUpperCase() + string.substring(offset, string.length());
    }


    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }


    public static String utf8Encode(String str, String defaultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defaultReturn;
            }
        }
        return str;
    }

    public static String[] splitByNumber(String s, int size) {
        if (s == null || size <= 0)
            return null;

        int chunks = s.length() / size + ((s.length() % size > 0) ? 1 : 0);
        String[] arr = new String[chunks];
        for (int i = 0, j = 0, l = s.length(); i < l; i += size, j++)
            arr[j] = s.substring(i, Math.min(l, i + size));

        return arr;
    }


    public static Spannable increaseTextSize(String original, String textToIncrease, int size) {

        int iStart = original.indexOf(textToIncrease);
        int iEnd = iStart + textToIncrease.length();

        SpannableString spannable = new SpannableString(original);
        spannable.setSpan(new AbsoluteSizeSpan(size), iStart, iEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return spannable;
    }

    public static Spannable boldString(String original, String textToColor) {

        int iStart = original.indexOf(textToColor);
        int iEnd = iStart + textToColor.length();

        Spannable spannable = new SpannableString(original);
        spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), iStart, iEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    public static Spannable colorizeString(String original, String textToColor, int color) {

        int iStart = original.indexOf(textToColor);
        int iEnd = iStart + textToColor.length();

        Spannable spannable = new SpannableString(original);
        spannable.setSpan(new ForegroundColorSpan(color), iStart, iEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    public static Spannable increaseAndColorizeTextSize(String original, String textToIncrease, int size, int color) {

        int iStart = original.indexOf(textToIncrease);
        int iEnd = iStart + textToIncrease.length();

        SpannableString spannable = new SpannableString(original);
        spannable.setSpan(new AbsoluteSizeSpan(size), iStart, iEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(color), iStart, iEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

}
