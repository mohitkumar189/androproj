package in.visheshagya.visheshagya.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.getterSetter.ElockerFolders;

/**
 * Created by VISHESHAGYA on 10/17/2016.
 */

public class FilesListAdapter extends BaseAdapter {
    public ArrayList<ElockerFolders> filesList;
    LayoutInflater inflater = null;
    private Context mcontext;

    public FilesListAdapter(Context mcontext, ArrayList<ElockerFolders> filesList) {
        this.mcontext = mcontext;
        this.filesList = filesList;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filesList.size();
    }

    @Override
    public Object getItem(int i) {
        return filesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.document_top_view, null);
            Log.d("adapter", "first time view created");
        }
        TextView fleName=(TextView) view.findViewById(R.id.documentName);
        fleName.setText(filesList.get(i).getFolderName().toString());
        return view;    }
}
