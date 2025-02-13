/*
 * Decompiled with CFR 0.152.
 */
package Math;

public class NumberNotation {
    private static final String[] SYMBOLS = new String[]{"a", "f", "p", "n", "u", "m", "k", "M", "G", "T", "P", "E"};

    public static String getNotation(double num) {
        return NumberNotation.getNotation(num, 3);
    }

    public static String getNotation(double num, int precision) {
        String result;
        boolean isNega = num < 0.0;
        if ((num = Math.abs(num)) >= 1000.0) {
            int i = -1;
            while (num >= 1000.0) {
                num /= 1000.0;
                ++i;
            }
            i = Math.min(i, SYMBOLS.length / 2);
            num = (double)((int)(num * Math.pow(10.0, precision)) / 1) / Math.pow(10.0, precision);
            result = String.format("%." + precision + "f %s", num, SYMBOLS[i + SYMBOLS.length / 2 + 1]);
        } else if (num < 0.01) {
            int i = 0;
            while (num < 0.01) {
                num *= 1000.0;
                ++i;
            }
            i = Math.min(i, SYMBOLS.length / 2);
            num = Math.round(num * Math.pow(10.0, precision));
            result = String.format("%." + precision + "f %s", num /= Math.pow(10.0, precision), SYMBOLS[SYMBOLS.length / 2 - i]);
        } else {
            result = String.format("%." + precision + "f", num);
        }
        if (isNega) {
            result = "-" + result;
        }
        return result;
    }

    public static long getValue(float notatedValue, String symbol) {
        int symbolIndex = -1;
        for (int i = 0; i < SYMBOLS.length; ++i) {
            if (!SYMBOLS[i].equals(symbol)) continue;
            symbolIndex = i;
            break;
        }
        if (symbolIndex == -1) {
            return (long)notatedValue;
        }
        int notation = symbolIndex - SYMBOLS.length / 2 * 3;
        return (long)(notatedValue * (float)notation);
    }
}

