package plshark.net.notes;

import java.io.File;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import plshark.net.notes.utils.StringUtils;

/**
 * Dialog for selecting a file
 */
public class FileSelectDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    /**
     * Interface to receive a callback for what file was selected
     */
    public interface IFileSelectListener {
        void onFileSelected(String filename);
    }

    public static final String BASE_DIRECTORY = FileSelectDialog.class.getCanonicalName() + ".DIRECTORY";

    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        String[] files = getFiles();

        if ((files != null) && (files.length > 0)) {
            ListView listView;

            view = inflater.inflate(R.layout.dlg_file_select, container, false);
            listView = view.findViewById(R.id.lv_files);

            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
            adapter.addAll(files);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(this);
        } else {
            view = inflater.inflate(R.layout.dlg_file_select_empty, container,
                    false);
        }
        getDialog().setTitle(R.string.title_dlg_file_select);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String filename = new File(getDirectory(), adapter.getItem(position)).getAbsolutePath();
        ((IFileSelectListener) getActivity()).onFileSelected(filename);
        dismiss();
    }

    /**
     * Get the external notes directory
     * @return the directory name
     */
    private String getDirectory() {
        Bundle args = getArguments();
        String dirName;

        if (args == null)
            throw new NullPointerException("Arguments can not be null");
        dirName = args.getString(BASE_DIRECTORY);
        if (StringUtils.isEmpty(dirName))
            throw new IllegalArgumentException("Must specify a base directory");

        return dirName;
    }

    /**
     * Get the names of all files in the external notes directory
     * @return the file names
     */
    private String[] getFiles() {
        String dirName = getDirectory();

        return new File(dirName).list((dir, name) -> {
            File file = new File(dir, name);
            return file.isFile();
        });
    }
}