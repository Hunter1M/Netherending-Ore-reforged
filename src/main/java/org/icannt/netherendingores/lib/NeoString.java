package org.icannt.netherendingores.lib;

import java.util.Locale;

import com.google.common.base.CaseFormat;

/**
 * -
 */
public class NeoString {

    private static final Locale LOCALE = Locale.ENGLISH;

    public static String lowerUnder(String input) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, input);
    }

    public static String colonUnder(String input) {
        return input.replace(":", "_");
    }

    public static String upperCamel(String input) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, input);
    }

    public static String toLower(String input) {
        return input.toLowerCase(LOCALE);
    }

    public static String toUpper(String input) {
        return input.toUpperCase(LOCALE);
    }

    public static String toTitleSpaced(String input) {
        String[] words = input.replace('_', ' ').toLowerCase(LOCALE).split(" ");
        StringBuilder builder = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toTitleCase(word.charAt(0)));
            builder.append(word.substring(1));
        }

        return builder.toString();
    }

}
