package plshark.net.notes.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import plshark.net.notes.Note;
import plshark.net.notes.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

/**
 * Writes Notes to XML and creates Notes from XML
 */
public class NoteXml {

    private static final String ENCODING = "UTF-8";
    private static final String NAMESPACE = "http://plshark.net/notes";
    private static final String NOTES_TAG = "notes";
    private static final String NOTE_TAG = "note";
    private static final String ID_TAG = "id";
    private static final String TITLE_TAG = "title";
    private static final String CONTENT_TAG = "content";

    public NoteXml() {

    }

    public void write(Note note, OutputStream stream) throws IOException {
        XmlSerializer xml = Xml.newSerializer();

        xml.setOutput(stream, ENCODING);
        xml.startDocument(ENCODING, null);

        writeNote(note, xml);

        xml.endDocument();
        xml.flush();
    }

    public void write(List<Note> notes, OutputStream stream) throws IOException {
        XmlSerializer xml = Xml.newSerializer();

        xml.setOutput(stream, ENCODING);
        xml.startDocument(ENCODING, null);

        xml.startTag(NAMESPACE, NOTES_TAG);

        for (Note note : notes)
            writeNote(note, xml);

        xml.endTag(NAMESPACE, NOTES_TAG);

        xml.endDocument();
        xml.flush();
    }

    public Note readNote(InputStream stream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        Note note;

        parser.setInput(stream, ENCODING);
        parser.nextTag();
        note = readNote(parser);

        return note;
    }

    public List<Note> readNotes(InputStream stream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        List<Note> notes;

        parser.setInput(stream, ENCODING);
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, NAMESPACE, NOTES_TAG);

        notes = new ArrayList<Note>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (NOTE_TAG.equals(tag))
                    notes.add(readNote(parser));
            }
        }

        return notes;
    }

    void writeNote(Note note, XmlSerializer xml) throws IOException {
        xml.startTag(NAMESPACE, NOTE_TAG);

        if (note.getId() != null)
            Util.writeLong(note.getId(), NAMESPACE, ID_TAG, xml);
        if (!StringUtils.isEmpty(note.getTitle()))
            Util.writeString(note.getTitle(), NAMESPACE, TITLE_TAG, xml);
        if (!StringUtils.isEmpty(note.getContent()))
            Util.writeString(note.getContent(), NAMESPACE, CONTENT_TAG, xml);

        xml.endTag(NAMESPACE, NOTE_TAG);
    }

    Note readNote(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        Note note;

        parser.require(XmlPullParser.START_TAG, NAMESPACE, NOTE_TAG);

        note = new Note();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (ID_TAG.equals(tag))
                    note.setId(Util.readLong(parser, NAMESPACE, ID_TAG));
                else if (TITLE_TAG.equals(tag))
                    note.setTitle(Util.readString(parser, NAMESPACE, TITLE_TAG));
                else if (CONTENT_TAG.equals(tag))
                    note.setContent(Util.readString(parser, NAMESPACE, CONTENT_TAG));
                else
                    Util.skip(parser);
            }
        }

        return note;
    }
}
