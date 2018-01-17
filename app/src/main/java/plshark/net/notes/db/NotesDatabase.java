package plshark.net.notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import plshark.net.notes.Note;

public class NotesDatabase {

    private static final String LOG_TAG = NotesDatabase.class.getSimpleName();
    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 2;

    private SQLiteOpenHelper helper;

    public NotesDatabase(Context context) {
        helper = new NotesOpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void insertNote(Note note) {
        long id;
        ContentValues values = new ContentValues();

        values.put(NotesTable.COLUMN_TITLE, note.getTitle());
        values.put(NotesTable.COLUMN_CONTENT, note.getContent());

        id = insert(NotesTable.TABLE_NAME, null, values);

        note.setId(id);
    }

    public void updateNote(Note note) {
        if (note.getId() == null)
            throw new NullPointerException("Cannot update note with no ID");

        String where;
        String[] args;
        ContentValues values;

        where = createBoundEquals(NotesTable._ID);
        args = new String[1];
        args[0] = note.getId().toString();

        values = new ContentValues();
        values.put(NotesTable.COLUMN_TITLE, note.getTitle());
        values.put(NotesTable.COLUMN_CONTENT, note.getContent());

        update(NotesTable.TABLE_NAME, values, where, args);
    }

    public void deleteNote(Note note) {
        if (note.getId() == null)
            throw new NullPointerException("Cannot delete note with no ID");

        String query;
        String[] args;

        query = createBoundEquals(NotesTable._ID);
        args = new String[1];
        args[0] = note.getId().toString();

        delete(NotesTable.TABLE_NAME, query, args);
    }

    public Note getNote(long id) {
        Note note = null;
        List<Note> list;
        NoteParser parser = new NoteParser();

        select(new NoteParser(), NotesTable.TABLE_NAME,
                NoteParser.COLUMNS, createBoundEquals(NotesTable._ID), new String[] { String.valueOf(id) }, null);
        list = parser.getList();

        // TODO throw exception if not found
        if (!list.isEmpty())
            note = list.get(0);

        return note;
    }

    public List<Note> getAllNotes() {
        NoteParser parser = new NoteParser();

        select(parser, NotesTable.TABLE_NAME, NoteParser.COLUMNS, null, null, createAscending(NotesTable._ID));

        return parser.getList();
    }

    /**
     * Perform a select and return a list of parsed items
     * @param handler the handler to handle the results of the select
     * @param tableName the table to select
     * @param columns the columns to return
     * @param where the where statement
     * @param args arguments for the where statement
     * @param orderBy the order by statement
     */
    void select(CursorHandler handler, String tableName, String[] columns, String where, String[] args, String orderBy) {
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            try (Cursor cursor = db.query(tableName, columns, where, args, null, null, orderBy)) {
                handler.handleResults(cursor);
            }
        }
    }

    long insert(String tableName, String nullColumnHack, ContentValues values) {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            return db.insertOrThrow(tableName, nullColumnHack, values);
        }
    }

    void delete(String tableName, String where, String[] args) {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(tableName, where, args);
        }
    }

    void update(String tableName, ContentValues values, String where, String[] args) {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.update(tableName, values, where, args);
        }
    }

    /**
     * Create an ascending order by clause
     * @param columnName the column to order by
     * @return the clause
     */
    String createAscending(String columnName) {
        return createOrderBy(columnName, "ASC");
    }

    /**
     * Create a descending order by clause
     * @param columnName the column to order by
     * @return the clause
     */
    String createDescending(String columnName) {
        return createOrderBy(columnName, "DESC");
    }

    /**
     * Create a bound parameter clause using equals. This is intended to be used
     * in where clauses.
     * @param columnName the column that should have the bound parameter
     * @return the clause
     */
    String createBoundEquals(String columnName) {
        return new StringBuilder(columnName).append(" = ?").toString();
    }

    /**
     * Create an order by clause
     * @param columnName the column to order by
     * @param order ascending or descending
     * @return the clause
     */
    String createOrderBy(String columnName, String order) {
        return new StringBuilder(columnName).append(" ").append(order).toString();
    }
}
