package plshark.net.notes.repository.room;

/**
 * Converts between {@link plshark.net.notes.repository.room.Note} and {@link plshark.net.notes.Note}
 */
public class NoteConverter {

    /**
     * Convert a note to an entity
     * @param note the note
     * @return the entity
     */
    public plshark.net.notes.repository.room.Note from(plshark.net.notes.Note note) {
        plshark.net.notes.repository.room.Note entity = new plshark.net.notes.repository.room.Note();

        entity.setId(note.getId());
        entity.setTitle(note.getTitle());
        entity.setContent(note.getContent());

        return entity;
    }

    /**
     * Convert an entity to a note
     * @param entity the entity
     * @return the note
     */
    public plshark.net.notes.Note from(plshark.net.notes.repository.room.Note entity) {
        plshark.net.notes.Note note = new plshark.net.notes.Note();

        note.setId(entity.getId());
        note.setTitle(entity.getTitle());
        note.setContent(entity.getContent());

        return note;
    }
}
