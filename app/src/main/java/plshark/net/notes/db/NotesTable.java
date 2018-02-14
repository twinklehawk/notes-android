package plshark.net.notes.db;

import android.provider.BaseColumns;

/**
 * Definition for the notes table
 */
interface NotesTable extends BaseColumns {
    String TABLE_NAME = "notes";
    String COLUMN_TITLE = "title";
    String COLUMN_CONTENT = "content";
    String COLUMN_SERVER_ID = "server_id";
    String COLUMN_OWNER_ID = "owner_id";

    String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY, " +
            COLUMN_SERVER_ID + " INTEGER, " +
            COLUMN_OWNER_ID + " INTEGER, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_CONTENT + " TEXT)";
}
