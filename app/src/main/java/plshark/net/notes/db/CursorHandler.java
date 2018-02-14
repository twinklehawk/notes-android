package plshark.net.notes.db;

import android.database.Cursor;

/**
 * Interface for handling the results of a query
 */
public interface CursorHandler {
    /**
     * Handle the results of a query
     * @param cursor the query results
     */
    void handleResults(Cursor cursor);
}
