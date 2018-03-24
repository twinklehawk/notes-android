package plshark.net.notes.repository.room;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

import plshark.net.notes.Note;
import plshark.net.notes.repository.NotesRepository;

/**
 * Repository that uses Room to access storage
 */
public class NotesRoomRepository implements NotesRepository{

    private final NotesDao notesDao;
    private final NoteConverter converter = new NoteConverter();

    /**
     * Create a new instance
     * @param notesDao the Room DAO to use
     */
    public NotesRoomRepository(NotesDao notesDao) {
        this.notesDao = notesDao;
    }

    @Override
    public Note insert(Note note) {
        long id = notesDao.insert(converter.from(note));
        note.setId(id);
        return note;
    }

    @Override
    public Note update(Note note) {
        notesDao.update(converter.from(note));
        return note;
    }

    @Override
    public void delete(Note note) {
        notesDao.delete(converter.from(note));
    }

    @Override
    public Optional<Note> getById(long id) {
        plshark.net.notes.repository.room.Note entity = notesDao.getById(id);
        return Optional.fromNullable(entity).transform(e -> converter.from(e));
    }

    @Override
    public List<Note> getAll() {
        // TODO better implementation that allows streaming
        List<plshark.net.notes.repository.room.Note> entities = notesDao.getAll();
        List<Note> notes = new ArrayList<>(entities.size());
        for (plshark.net.notes.repository.room.Note entity : entities)
            notes.add(converter.from(entity));
        return notes;
    }
}
