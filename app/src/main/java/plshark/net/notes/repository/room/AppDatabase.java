package plshark.net.notes.repository.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Class for accessing Room DAO objects for the application
 */
@Database(entities = { Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Retrieve a NotesDao instance
     * @return the instance
     */
    public abstract NotesDao notesDao();
}
