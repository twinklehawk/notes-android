package plshark.net.notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.common.base.Optional;

import java.util.List;

import plshark.net.notes.Note;
import plshark.net.notes.repository.NotesRepository;

/**
 * Notes repository that uses MySQL for storage
 */
public class NotesDatabase implements NotesRepository {

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 2;

    private SQLiteOpenHelper helper;

    /**
     * Create a new instance
     * @param context the context for opening and creating the database
     */
    public NotesDatabase(Context context) {
        helper = new NotesOpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public Note insert(Note note) {
        insertNote(note);
        return note;
    }

    /**
     * Save a new note
     * @param note the note
     */
    public void insertNote(Note note) {
        long id;
        ContentValues values = new ContentValues();

        values.put(NotesTable.COLUMN_TITLE, note.getTitle());
        values.put(NotesTable.COLUMN_CONTENT, note.getContent());

        id = insert(NotesTable.TABLE_NAME, null, values);

        note.setId(id);
    }

    @Override
    public Note update(Note note) {
        updateNote(note);
        return note;
    }

    /**
     * Update an existing note
     * @param note the note
     */
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

    @Override
    public void delete(Note note) {
        deleteNote(note);
    }

    /**
     * Delete a note
     * @param note the note
     */
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

    /**
     * Find a note by ID
     * @param id the note ID
     * @return the matching note or null if not found
     */
    public Note getNote(long id) {
        Note note = null;
        List<Note> list;
        NoteParser parser = new NoteParser();

        select(new NoteParser(), NotesTable.TABLE_NAME,
                NoteParser.COLUMNS, createBoundEquals(NotesTable._ID), new String[] { String.valueOf(id) }, null);
        list = parser.getList();

        if (!list.isEmpty())
            note = list.get(0);

        return note;
    }

    @Override
    public Optional<Note> getById(long id) {
        return Optional.fromNullable(getNote(id));
    }

    @Override
    public List<Note> getAll() {
        return getAllNotes();
    }

    /**
     * Get all stored notes
     * @return the list of notes
     */
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
    private void select(CursorHandler handler, String tableName, String[] columns, String where, String[] args, String orderBy) {
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            try (Cursor cursor = db.query(tableName, columns, where, args, null, null, orderBy)) {
                handler.handleResults(cursor);
            }
        }
    }

    /**
     * Build and execute an insert and return the generated ID
     * @param tableName the table to insert into
     * @param nullColumnHack the null column hack, see {@link SQLiteDatabase#insert(String, String, ContentValues)}
     * @param values the values to insert
     * @return the generated ID
     */
    private long insert(String tableName, String nullColumnHack, ContentValues values) {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            return db.insertOrThrow(tableName, nullColumnHack, values);
        }
    }

    /**
     * Delete a row from a table
     * @param tableName the table to delete from
     * @param where the filter to use to delete rows
     * @param args any arguments for the where clause
     */
    private void delete(String tableName, String where, String[] args) {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(tableName, where, args);
        }
    }

    /**
     * Update a row in a table
     * @param tableName the table to delete from
     * @param values the values to update
     * @param where the filter to use to choose what is updated
     * @param args the arguments for the where clause
     */
    private void update(String tableName, ContentValues values, String where, String[] args) {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.update(tableName, values, where, args);
        }
    }

    /**
     * Create an ascending order by clause
     * @param columnName the column to order by
     * @return the clause
     */
    private String createAscending(String columnName) {
        return createOrderBy(columnName, "ASC");
    }

    /**
     * Create a bound parameter clause using equals. This is intended to be used
     * in where clauses.
     * @param columnName the column that should have the bound parameter
     * @return the clause
     */
    private String createBoundEquals(String columnName) {
        return columnName + " = ?";
    }

    /**
     * Create an order by clause
     * @param columnName the column to order by
     * @param order ascending or descending
     * @return the clause
     */
    private String createOrderBy(String columnName, String order) {
        return columnName + " " + order;
    }
}
