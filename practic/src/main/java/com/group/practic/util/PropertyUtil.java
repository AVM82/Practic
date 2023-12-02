package com.group.practic.util;

import java.io.File;

public interface PropertyUtil {
    
    public static final char DOT = '.';

    public static final String NAME_KEY = "name";

    public static final String AUTHOR_KEY = "author";

    public static final String TYPE_KEY = "type";

    public static final String DESCRIPTION_KEY = "description";

    public static final String PURPOSE_KEY = "purpose";

    public static final String LEVEL_KEY = "level";

    public static final String SLUG_KEY = "slug";

    public static final String SVG_KEY = "svg-icon";

    static final int LEVEL_KEY_LENGTH = LEVEL_KEY.length();

    public static final String SEPARATOR = File.separator;

    public static final String NAME_SEPARATOR = "<>";

    public static final String REFERENCE_SEPARATOR = " ";

    public static final String AUTHOR_SEPARATOR = ", ";
    
    public static final String SKILL_SEPARATOR = ",";

    public static final String PART_SEPARATOR = "-";

    public static final String PRAXIS_PART = "prac.";

    public static final String ADDITIONAL_PART = "add.";

    public static final String TOPIC_REPORT_PART = "topic.";
    
    public static final String SKILL_PART = "skills";
    

    public static boolean keyStartsWith(Object key, String start) {
        return ((String) key).startsWith(start);
    }


    public static int getNumber(String string) {
        try {
            return Integer.parseUnsignedInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public static int getChapterNumber(int nest, String key) {
        int start = 0;
        int pos = key.indexOf(DOT);
        for (int i = 1; i < nest; i++) {
            start = pos + 1;
            pos = key.indexOf(DOT, start);
        }
        if (pos < 0) {
            pos = key.length();
        }
        return getNumber(key.substring(start, pos));
    }


    public static int countDots(String key) {
        int result = 0;
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == DOT) {
                result++;
            }
        }
        return result;
    }


    public static String createKeyStarts(int number, String part) {
        return String.valueOf(number) + DOT + part;
    }

}
