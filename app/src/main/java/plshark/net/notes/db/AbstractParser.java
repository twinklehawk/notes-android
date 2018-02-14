package plshark.net.notes.db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * CursorHandler that parses each row in the response into an object
 * @param <T> the type of object parsed
 */
public abstract class AbstractParser<T> implements CursorHandler {

    private List<T> list = new ArrayList<>();

    /**
     * @return the columns in the correct order for this parser
     */
    public abstract String[] getColumns();

    /**
     * Parse the current row from the cursor into an instance of T
     * @param cursor the cursor, already positioned on the row to parse
     * @return the parsed object
     */
    public abstract T parseRow(Cursor cursor);

    @Override
    public void handleResults(Cursor cursor) {
        boolean more = cursor.moveToFirst();

        while (more) {
            list.add(parseRow(cursor));

            more = cursor.moveToNext();
        }
    }

    /**
     * @return the list of parsed items
     */
    public final List<T> getList() {
        return list;
    }
}
