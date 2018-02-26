package plshark.net.notes.repository;

import com.google.common.base.Optional;

import java.util.List;

import plshark.net.notes.Note;

/**
 * Repository for storing, retrieving, and modifying notes
 */
public interface NotesRepository {

    /**
     * Insert a new note
     * @param note the note, must not have an ID set
     * @return the inserted note
     */
    Note insert(Note note);

    /**
     * Update an existing note
     * @param note the note, must have an ID set
     * @return the updated note
     */
    Note update(Note note);

    /**
     * Delete a note. Nothing happens if the note is not found
     * @param note the note to delete, must have an ID set
     */
    void delete(Note note);

    /**
     * Retrieve a note by ID
     * @param id the ID
     * @return the matching note or empty if not found
     */
    Optional<Note> getById(long id);

    /**
     * Retrieve all notes stored in the repository
     * @return the list of notes
     */
    List<Note> getAll();
}
