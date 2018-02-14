package plshark.net.notes.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import plshark.net.notes.utils.StringUtils;

/**
 * Utility functions for working with XML
 */
class Util {

    private Util() {

    }

    static void writeString(String value, String namespace, String tag,
                                   XmlSerializer xml) throws IOException {
        xml.startTag(namespace, tag);
        xml.text(value);
        xml.endTag(namespace, tag);
    }

    static String readString(XmlPullParser parser, String namespace,
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

    static void writeLong(Long l, String namespace, String tag,
                                 XmlSerializer xml) throws IOException {
        writeString(l.toString(), namespace, tag, xml);
    }

    static Long readLong(XmlPullParser parser, String namespace,
                                String tag) throws XmlPullParserException, IOException {
        Long l = null;
        String str = readString(parser, namespace, tag);

        if (!StringUtils.isEmpty(str)) {
            try {
                l = Long.parseLong(str);
            } catch (NumberFormatException e) {
                throw new IOException(e);
            }
        }

        return l;
    }

    static void skip(XmlPullParser parser)
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