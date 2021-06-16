package com.malaab.ya.action.actionyamalaab.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class NumberUtils {

    public static String formatOneDecimal(float number) {
        DecimalFormat oneDec = new DecimalFormat("##0.0");
        return oneDec.format(number);
    }

    public static String formatTwoDecimal(float number) {
        DecimalFormat twoDec = new DecimalFormat("##0.00");
        return twoDec.format(number);
    }

    public static String formatTwoDecimalPercent(float number) {
        return formatTwoDecimal(number) + "%";
    }


    public static String roundingNumber(float number) {
        return String.valueOf((float) Math.round(number));
    }


    public static String round(double i, int v) {
        float res = Math.round(i / v) * v;
        return String.valueOf(formatTwoDecimal(res));
    }

    public static String round2(float number, int decimalPlace) {
        int pow = 10;
        for (int i = 1; i < decimalPlace; i++)
            pow *= 10;
        float tmp = number * pow;
        return String.valueOf((float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow);
    }

    public static double round3(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);

        double res = (double) tmp / factor;
        return res;
    }

    public static float round4f(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_DOWN);

        return bd.floatValue();
    }

    public static String round4(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_DOWN);

        float res = bd.floatValue();
        return formatTwoDecimal(res);
    }

    public static float getAdjustBalance(float value) {
        float res = value - Float.valueOf(NumberUtils.round4(value, 1));
        if (res < 0)
            res = res * -1;

        return res;
    }


    public static double round5(float value, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);

        double res = bd.floatValue();
        return res;
    }

    public static double round6(float value) {
        double roundOff = (double) Math.round(value * 100) / 100D;
        return roundOff;
    }

    public static double round7(float value, int decimalPlace) {
        BigDecimal a = new BigDecimal(value);
        BigDecimal roundOff = a.setScale(decimalPlace, BigDecimal.ROUND_HALF_EVEN);

        double res = roundOff.doubleValue();
        return res;
    }

    public static double round8(float value) {
        double res = Math.round(value * 5) / 5.0;
        return res;
    }


    public static double roundHalf(double number) {
        double diff = number - (int) number;
        if (diff < 0.25)
            return (int) number;
        else if (diff < 0.75)
            return (int) number + 0.5;
        else
            return (int) number + 1;
    }

    public static double roundToFraction(double x, long fraction) {
        return (double) Math.round(x * fraction) / fraction;
    }


    public static double roundToMultipleOfFive(double x) {

//        x = input.nextDouble();
        String str = String.valueOf(x);
        int pos = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                pos = i;
                break;
            }
        }

        int after = Integer.parseInt(str.substring(pos + 1, str.length()));
        int Q = after / 5;
        int R = after % 5;

        if ((Q % 2) == 0) {
            after = after - R;
        } else {
            if (5 - R == 5) {
                after = after;
            } else after = after + (5 - R);
        }

        return Double.parseDouble(str.substring(0, pos + 1).concat(String.valueOf(after)));
    }


    public static String roundNumber(double number, int decimalPlace) {
        return String.valueOf(roundingNumber(number, decimalPlace));
    }

    private static float roundingNumber(double number, int decimalPlace) {
        BigDecimal b = new BigDecimal(number);
        return b.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}