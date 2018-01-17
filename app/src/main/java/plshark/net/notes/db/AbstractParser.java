package plshark.net.notes.db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParser<T> implements CursorHandler {

    private List<T> list = new ArrayList<T>();

    /**
     * Create a new AbstractParser object
     */
    protected AbstractParser() {

    }

    public abstract String[] getColumns();

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
