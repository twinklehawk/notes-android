package plshark.net.notes.xml;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import plshark.net.notes.utils.StringUtils;

/**
 * Utility functions for working with XML
 */
class Util {

    private Util() {

    }

    public static void writeString(String value, String namespace, String tag,
                                   XmlSerializer xml) throws IOException {
        xml.startTag(namespace, tag);
        xml.text(value);
        xml.endTag(namespace, tag);
    }

    public static String readString(XmlPullParser parser, String namespace,
                                    String tag) throws XmlPullParserException, IOException {
        String text = null;

        parser.require(XmlPullParser.START_TAG, namespace, tag);
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, namespace, tag);

        return text;
    }

    public static void writeInteger(Integer i, String namespace, String tag,
                                    XmlSerializer xml) throws IOException {
        writeString(i.toString(), namespace, tag, xml);
    }

    public static Integer readInteger(XmlPullParser parser, String namespace,
                                      String tag) throws XmlPullParserException, IOException {
        Integer i = null;
        String str = readString(parser, namespace, tag);

        if (!StringUtils.isEmpty(str)) {
            try {
                i = Integer.parseInt(str);
            } catch (NumberFormatException e) {

            }
        }

        return i;
    }

    public static void writeLong(Long l, String namespace, String tag,
                                 XmlSerializer xml) throws IOException {
        writeString(l.toString(), namespace, tag, xml);
    }

    public static Long readLong(XmlPullParser parser, String namespace,
                                String tag) throws XmlPullParserException, IOException {
        Long l = null;
        String str = readString(parser, namespace, tag);

        if (!StringUtils.isEmpty(str)) {
            try {
                l = Long.parseLong(str);
            } catch (NumberFormatException e) {

            }
        }

        return l;
    }

    public static void writeDate(Date date, String namespace, String tag,
                                 XmlSerializer xml) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
                Locale.US);
        writeString(df.format(date), namespace, tag, xml);
    }

    public static Date readDate(XmlPullParser parser, String namespace,
                                String tag) throws XmlPullParserException, IOException {
        String str = readString(parser, namespace, tag);
        Date date = null;

        if (!StringUtils.isEmpty(str)) {
            SimpleDateFormat df = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }

        return date;
    }

    public static void writeBoolean(boolean b, String namespace, String tag,
                                    XmlSerializer xml) throws IOException {
        writeString(b ? "TRUE" : "FALSE", namespace, tag, xml);
    }

    public static boolean readBoolean(XmlPullParser parser, String namespace,
                                      String tag) throws XmlPullParserException, IOException {
        String str = readString(parser, namespace, tag);
        boolean b = false;
        if (!StringUtils.isEmpty(str) && (str.equals("TRUE")))
            b = true;
        return b;
    }

    public static void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG)
            throw new IllegalStateException();

        int depth = 1;

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    --depth;
                    break;
                case XmlPullParser.START_TAG:
                    ++depth;
                    break;
            }
        }
    }
}