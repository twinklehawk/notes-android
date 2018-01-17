package plshark.net.notes.db;

import android.database.Cursor;

import plshark.net.notes.Note;

public class NoteParser extends AbstractParser<Note> {

    public static final String[] COLUMNS = { NotesTable._ID, NotesTable.COLUMN_TITLE, NotesTable.COLUMN_CONTENT };

    private int idIndex;
    private int titleIndex;
    private int contentIndex;
    private int starredIndex;

    public NoteParser() {

    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public Note parseRow(Cursor cursor) {
        Note note = new Note();

        note.setId(cursor.getLong(0));
        if (!cursor.isNull(1))
            note.setTitle(cursor.getString(1));
        if (!cursor.isNull(2))
            note.setContent(cursor.getString(2));

        return note;
    }
}
