package plshark.net.notes.repository.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Notes data access using Room
 */
@Dao
public interface NotesDao {

    /**
     * Insert a note
     * @param note the note to insert
     * @return the generated ID
     */
    @Insert
    long insert(Note note);

    /**
     * Update a note
     * @param note the note to update
     */
    @Update
    void update(Note note);

    /**
     * Delete a note
     * @param note the note to delete
     */
    @Delete
    void delete(Note note);

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note or null if no match
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    Note getById(long id);

    /**
     * Get all stored notes
     * @return the list of notes
     */
    @Query("SELECT * FROM notes")
    List<Note> getAll();
}
