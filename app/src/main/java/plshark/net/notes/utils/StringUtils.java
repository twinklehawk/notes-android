package plshark.net.notes.utils;

/**
 * String utility functions
 */
public class StringUtils {

    private StringUtils() {

    }

    /**
     * Determine if a string is null or empty (length == 0)
     * @param cs the string to check
     * @return if the string is null or empty
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
