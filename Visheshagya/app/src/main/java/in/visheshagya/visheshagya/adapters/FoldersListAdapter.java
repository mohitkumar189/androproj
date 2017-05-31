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
 * Created by VISHESHAGYA on 10/14/2016.
 */

public class FoldersListAdapter extends BaseAdapter {

    public ArrayList<ElockerFolders> foldersList;
    LayoutInflater inflater = null;
    private Context mcontext;

    public FoldersListAdapter(ArrayList<ElockerFolders> foldersList, Context mcontext) {
        this.foldersList = foldersList;
        this.mcontext = mcontext;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return foldersList.size();
    }

    @Override
    public Object getItem(int i) {
        return foldersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.top_view_elocker, null);
            Log.d("adapter", "first time view created");
        }
        TextView foldrName=(TextView) view.findViewById(R.id.fNameTv);
        foldrName.setText(foldersList.get(i).getFolderName().toString());
        return view;
    }
}
