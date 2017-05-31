package in.visheshagya.visheshagya.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.adapters.FilesListAdapter;
import in.visheshagya.visheshagya.adapters.FoldersListAdapter;
import in.visheshagya.visheshagya.getterSetter.ElockerFolders;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class DocumentsListActivity extends AppCompatActivity {
    ListView listView;
    FilesListAdapter filesListAdapter;
    List<String> filesName;
    String directoryName;
/*    String ListUrl="http://192.168.1.35/Services/filelist.php?role=2&userId="
            +new SharedPrefsClass(DocumentsListActivity.this).getClientId()+"&directoryName="+directoryName;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_list);
        Intent intent=getIntent();

        directoryName=intent.getStringExtra("directoryName");
        String clientId=intent.getStringExtra("clientId");
        listView=(ListView) findViewById(R.id.documentsFileList);
        filesName=new ArrayList<>();
        System.out.println("directory name "+directoryName+" clientId "+clientId);
       // String clientId=new SharedPrefsClass(DocumentsListActivity.this).getClientId();
        String ListUrl= APIUrls.FILES_LIST+"role=2&userId="
                +clientId+"&directoryName="+directoryName;
        new GetFilesList().execute(ListUrl);
    }


    private void setupItems(String s) {
        ArrayList<ElockerFolders> filesList = new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(s);
            for(int i=0;i<=jsonArray.length();i++){
                String fileName= String.valueOf(jsonArray.get(i));
                filesList.add(new ElockerFolders(fileName));
               // System.out.println("jsonobjects : "+fileName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        filesListAdapter = new FilesListAdapter(DocumentsListActivity.this,filesList);
        filesListAdapter.notifyDataSetChanged();
        listView.setAdapter(filesListAdapter);
    }

    private class GetFilesList extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            return new JSONDownloader().jsonData(strings[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setupItems(s);
           System.out.println("Files list : "+s);
        }

    }

}
