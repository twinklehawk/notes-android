package plshark.net.notes;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import plshark.net.notes.utils.StringUtils;

public class NoteListAdapter implements ListAdapter {

    private Context context;
    private List<Note> notes;
    private List<DataSetObserver> observers = new ArrayList<>();

    NoteListAdapter(Context context) {
        this.context = context;
    }

    /**
     * @param notes the list of notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    /**
     * Add a note to the adapter
     * @param note the note
     */
    public void addNote(Note note) {
        if (notes == null)
            notes = new ArrayList<>();

        notes.add(note);
        notifyDataSetChanged();
    }

    /**
     * Remove a note from the adapter
     * @param position the position of the note
     */
    public void removeNote(int position) {
        if (notes != null) {
            notes.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return ((notes == null) ? 0 : notes.size());
    }

    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        // all items use the same view type
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
            view = createView(position, parent);
        updateView(position, view);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // all items use the same view type
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return ((notes == null) || notes.isEmpty());
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        observers.remove(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        // this doesn't use separators, so all items are enabled
        return true;
    }

    /**
     * Notify registered DataSetObservers that the data has changed
     */
    private void notifyDataSetChanged() {
        for (DataSetObserver observer : observers)
            observer.onChanged();
    }

    /**
     * Create a new view for a note
     * @param position the position of the note
     * @param parent the parent ViewGroup
     * @return the view
     */
    private View createView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.note_list_item, parent, false);
    }

    /**
     * Update an existing view with data from a different note
     * @param position the position of the new note
     * @param view the existing view
     */
    private void updateView(int position, View view) {
        Note note = notes.get(position);
        String content = note.getContent();

        if (!StringUtils.isEmpty(content) && content.length() > 40)
            content = content.substring(0, 37) + "...";

        ((TextView) view.findViewById(R.id.text_note_list_item)).setText(note.getTitle());
        ((TextView) view.findViewById(R.id.content_note_list_item)).setText(content);
    }
}
