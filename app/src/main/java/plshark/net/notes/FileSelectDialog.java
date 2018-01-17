package plshark.net.notes;

import java.io.File;
import java.io.FilenameFilter;

import android.app.DialogFragment;
import android.os.Bundle;
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

    public interface IFileSelectListener {
        void onFileSelected(String filename);
    }

    public static final String BASE_DIRECTORY = FileSelectDialog.class.getCanonicalName() + ".DIRECTORY";

    private ArrayAdapter<String> adapter;

    public FileSelectDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        String[] files = getFiles();

        if ((files != null) && (files.length > 0)) {
            ListView listView;

            view = inflater.inflate(R.layout.dlg_file_select, container, false);
            listView = (ListView) view.findViewById(R.id.lv_files);

            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1);
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

    String getDirectory() {
        Bundle args = getArguments();
        String dirName;

        if (args == null)
            throw new NullPointerException("Arguments can not be null");
        dirName = args.getString(BASE_DIRECTORY);
        if (StringUtils.isEmpty(dirName))
            throw new IllegalArgumentException("Must specify a base directory");

        return dirName;
    }

    String[] getFiles() {
        String dirName = getDirectory();

        return new File(dirName).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File file = new File(dir, name);
                return file.isFile();
            }
        });
    }
}