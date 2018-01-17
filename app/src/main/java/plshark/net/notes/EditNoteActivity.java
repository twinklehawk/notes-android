package plshark.net.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import plshark.net.notes.db.NotesDatabase;
import plshark.net.notes.utils.StringUtils;

public class EditNoteActivity extends AppCompatActivity {

    /**
     * They key for the note to edit
     */
    public static final String EDIT_NOTE = "net.plshark.notes.editnote";

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the note to edit or create a new one if one was not passed in
        Intent intent = getIntent();

        if (intent != null)
            note = intent.getParcelableExtra(EDIT_NOTE);
        if (note == null)
            note = new Note();

        // fill in the text boxes
        setupTextViews(note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled;

        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteNote();
                handled = true;
                finish();
                break;
            case R.id.action_cancel:
                handled = true;
                finish();
                break;
            case android.R.id.home:
                save();
            default:
                handled = super.onOptionsItemSelected(item);
        }

        return handled;
    }

    /**
     * Delete the new or existing note
     */
    void deleteNote() {
        if (note.getId() != null) {
            NotesDatabase db = new NotesDatabase(this);
            db.deleteNote(note);
        }
    }

    /**
     * Save the new or updated note
     */
    void save() {
        // update the note with the text from the screen
        EditText title = (EditText) findViewById(R.id.title_edit_edit_note);
        EditText content = (EditText) findViewById(R.id.content_edit_edit_note);

        note.setTitle(title.getText().toString());
        note.setContent(content.getText().toString());

        if (!StringUtils.isEmpty(note.getTitle()) || !StringUtils.isEmpty(note.getContent())) {
            NotesDatabase db = new NotesDatabase(this);

            // If the ID field is null, it's a new note so insert it.
            // Otherwise, update the existing note.
            if (note.getId() == null)
                db.insertNote(note);
            else
                db.updateNote(note);
        }
    }

    /**
     * Fill in the title and content edit text boxes from a note
     * @param note the note
     */
    void setupTextViews(Note note) {
        if (!StringUtils.isEmpty(note.getTitle())) {
            EditText title = (EditText) findViewById(R.id.title_edit_edit_note);

            title.setText(note.getTitle());
        }

        if (!StringUtils.isEmpty(note.getContent())) {
            EditText content = (EditText) findViewById(R.id.content_edit_edit_note);

            content.setText(note.getContent());
            content.setSelection(content.length());
        }
    }
}
