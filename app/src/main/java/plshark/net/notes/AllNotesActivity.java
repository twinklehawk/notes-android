package plshark.net.notes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import plshark.net.notes.db.NotesDatabase;
import plshark.net.notes.xml.NoteXml;

/**
 * Activity for displaying all the notes
 */
// TODO split this up into multiple classes
public class AllNotesActivity extends AppCompatActivity implements FileSelectDialog.IFileSelectListener {

    private static final int WRITE_STORAGE_EXPORT = 1;
    private static final int WRITE_STORAGE_IMPORT = 2;

    private NoteListAdapter adapter;
    private NotesDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new NotesDatabase(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
            }
        });

        // setup the list view
        ListView listView = (ListView) findViewById(R.id.list_view_all_notes);
        adapter = new NoteListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Note note = (Note) adapterView.getItemAtPosition(position);
                editNote(note);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.setNotes(db.getAllNotes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_import) {
            importNotes();
        } else if (id == R.id.action_export) {
            exportNotes();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_EXPORT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    exportNotesPermissionGranted();
                break;
            case WRITE_STORAGE_IMPORT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    importNotesPermissionGranted();
                break;
        }
    }

    @Override
    public void onFileSelected(String filename) {
        try {
            List<Note> notes = parseFile(filename);
            for (Note note : notes)
                db.insertNote(note);
            adapter.setNotes(db.getAllNotes());
        } catch (XmlPullParserException | IOException e) {
            Toast.makeText(this, R.string.failed_to_read_file, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open the activity to edit a note
     * @param note the note to edit
     */
    private void editNote(Note note) {
        Intent intent = new Intent(this, EditNoteActivity.class);

        intent.putExtra(EditNoteActivity.EDIT_NOTE, note);
        startActivity(intent);
    }

    /**
     * Open the activity to create a note
     */
    private void createNote() {
        startActivity(new Intent(this, EditNoteActivity.class));
    }

    /**
     * Import notes from a file
     */
    private void importNotes() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_IMPORT);
        else
            importNotesPermissionGranted();
    }

    /**
     * Import notes from a file after permissions have been granted
     */
    private void importNotesPermissionGranted() {
        FileSelectDialog dlg = new FileSelectDialog();
        Bundle args = new Bundle();
        String dir = Environment.getExternalStoragePublicDirectory("notes").getAbsolutePath();

        args.putString(FileSelectDialog.BASE_DIRECTORY, dir);
        dlg.setArguments(args);
        dlg.show(getFragmentManager(), "file_select");
    }

    /**
     * Parse a file containing serialized notes
     * @param file the file to parse
     * @return the parsed notes
     * @throws XmlPullParserException if unable to parse the file
     * @throws IOException if an IO error occurs
     */
    private List<Note> parseFile(String file) throws XmlPullParserException, IOException {
        NoteXml xml = new NoteXml();
        List<Note> notes;

        try (FileInputStream stream = new FileInputStream(file)) {
            notes = xml.readNotes(stream);
        }

        return notes;
    }

    /**
     * Export all notes to a file
     */
    private void exportNotes() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_EXPORT);
        else
            exportNotesPermissionGranted();

    }

    /**
     * Export all notes to a file after permissions have been granted
     */
    private void exportNotesPermissionGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            try (FileOutputStream stream = new FileOutputStream(getExportFile())) {
                List<Note> notes = db.getAllNotes();
                NoteXml xml = new NoteXml();

                xml.write(notes, stream);
            } catch (IOException e) {
                Toast.makeText(this, R.string.failed_to_write_file, Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * Get the file to use when exporting notes
     * @return the file
     * @throws IOException if unable to create the file
     */
    private File getExportFile() throws IOException {
        File file;
        File dir = Environment.getExternalStoragePublicDirectory("notes");

        if (dir.exists() || dir.mkdirs()) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            Date now = new Date(System.currentTimeMillis());
            file = new File(dir, "export_" + df.format(now));
        } else
            throw new IOException("Failed to create file");

        return file;
    }
}
