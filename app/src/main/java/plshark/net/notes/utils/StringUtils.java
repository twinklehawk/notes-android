package plshark.net.notes.utils;

/**
 * Created by alex on 11/19/16.
 */

public class StringUtils {
    private StringUtils() {

    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
