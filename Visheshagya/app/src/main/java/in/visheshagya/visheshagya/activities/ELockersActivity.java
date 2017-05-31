package in.visheshagya.visheshagya.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.adapters.FoldersListAdapter;
import in.visheshagya.visheshagya.getterSetter.ElockerFolders;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ELockersActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ListView listView;
    FoldersListAdapter foldersListAdapter;
    Button uploadDocumentBtn, createDirectoryBtn;
    Dialog dialog;
    File file;
    EditText edFolderName;
    Response response = null;
    String content_type, filePath, fileName;
    List<String> foldersName;
    Spinner folderNameSpinner;
    String diretoryName;
    String clientId;

    private static final int PICK_DOC_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elockers);
        uploadDocumentBtn = (Button) findViewById(R.id.uploadDocumentBtn);
        createDirectoryBtn = (Button) findViewById(R.id.createDirectoryBtn);
        listView = (ListView) findViewById(R.id.documentsList);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView.setVisibility(View.INVISIBLE);
        clientId = new SharedPrefsClass(ELockersActivity.this).getClientId();
        foldersName = new ArrayList<>();
        // setList();
        checkPermissions();
    }

    private void updateAdapter() {

    }

    private void setList(String foldersList) {
        ArrayList<ElockerFolders> folderList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(foldersList);
            for (int i = 0; i < jsonArray.length(); i++) {
                folderList.add(new ElockerFolders((String) jsonArray.get(i)));
                foldersName.add(String.valueOf(jsonArray.get(i)));
                System.out.println("FOLDER at " + i + " position " + jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        foldersListAdapter = new FoldersListAdapter(folderList, ELockersActivity.this);
        foldersListAdapter.notifyDataSetChanged();
        listView.setAdapter(foldersListAdapter);
        enableButton();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ELockersActivity.this, "item name " + foldersName.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ELockersActivity.this, DocumentsListActivity.class);
                intent.putExtra("directoryName", foldersName.get(i));
                intent.putExtra("clientId", clientId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int res_id = view.getId();
        switch (res_id) {
            case R.id.uploadDocumentBtn:
                new MaterialFilePicker().withActivity(ELockersActivity.this).withRequestCode(20).start();
                //createDialog("browse");
                break;
            case R.id.createDirectoryBtn:
                createDialog("createFolder");
                break;
            case R.id.btnDialogDocsCancel:
                dialog.cancel();
                break;
            case R.id.btnDialogDocsUpload:
                new UploadDocsToServer().execute();
                break;
            case R.id.btnCamera:
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_DOC_REQUEST);
                break;
            case R.id.btnBrowse:
                checkPermissions();
                break;
            case R.id.btnCreateFolder:
                diretoryName = edFolderName.getText().toString().trim();
                if (diretoryName.isEmpty() || diretoryName.length() <= 2) {
                    Toast.makeText(ELockersActivity.this, "Please enter directory name", Toast.LENGTH_SHORT).show();
                } else {
                    new CreateDirectoryOnServer().execute(APIUrls.CREATE_FOLDER + "directoryName=" + diretoryName + "&role=" + 2 + "&clientId=" + new SharedPrefsClass(this).getClientId());
                }
                break;
            default:
                break;
        }
    }

    private void createDialog(String type) {
        switch (type) {
            case "browse":
                dialog = new Dialog(this);
                dialog.setTitle("Upload document");   // Set the title
                dialog.setContentView(R.layout.dialog_ask_upload); // inflate the layout
                Button btnCamera = (Button) dialog.findViewById(R.id.btnCamera);
                Button btnBrowse = (Button) dialog.findViewById(R.id.btnBrowse);
                btnCamera.setOnClickListener(this);
                btnBrowse.setOnClickListener(this);
                dialog.show();
                break;
            case "createFolder":
                dialog = new Dialog(this);
                dialog.setTitle("Upload document");   // Set the title
                dialog.setContentView(R.layout.dialog_ask_folder_name); // inflate the layout
                edFolderName = (EditText) dialog.findViewById(R.id.edDialogCreateFolder);
                Button btnCreateFolder = (Button) dialog.findViewById(R.id.btnCreateFolder);
                btnCreateFolder.setOnClickListener(this);
                dialog.show();
                break;
            case "upload":
                dialog = new Dialog(this);
                dialog.setTitle("Upload document");   // Set the title
                dialog.setContentView(R.layout.dialog_upload_docs); // inflate the layout
                Button btnCancel = (Button) dialog.findViewById(R.id.btnDialogDocsCancel);
                Button btnUpload = (Button) dialog.findViewById(R.id.btnDialogDocsUpload);
                folderNameSpinner = (Spinner) dialog.findViewById(R.id.spinnerDialogDocs);

                EditText edFileName = (EditText) dialog.findViewById(R.id.fileNameEditText);
                edFileName.setText(fileName.toString().trim());
                if (loadSpinnerData()) {
                    btnCancel.setOnClickListener(this);
                    btnUpload.setOnClickListener(this);
                }
                dialog.show();
                break;
        }

    }

    private boolean loadSpinnerData() {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, foldersName);
        folderNameSpinner.setAdapter(dataAdapter);
        folderNameSpinner.setOnItemSelectedListener(this);
        return true;
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            } else {
                new GetFolderLists().execute(APIUrls.FOLDER_LIST);
            }
        } else {
            new GetFolderLists().execute(APIUrls.FOLDER_LIST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            new GetFolderLists().execute(APIUrls.FOLDER_LIST);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    public void enableButton() {
        uploadDocumentBtn.setOnClickListener(this);
        createDirectoryBtn.setOnClickListener(this);
    }

    ProgressDialog progressDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("request code " + requestCode + " result code " + resultCode);

        if (requestCode == 20 && resultCode == RESULT_OK) {
            file = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            getFileInfo();
        }
    }

    private String getMimeType(String path) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path).toLowerCase().trim();
        System.out.println("file extension : " + extension);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            return type;
        }
        return type;
    }

    private void getFileInfo() {
        content_type = getMimeType(file.getPath());
        System.out.println("content type : " + content_type);
        filePath = file.getAbsolutePath();
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        createDialog("upload");
    }

    private void uploadImage() {
        SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(ELockersActivity.this);
        String clientId = sharedPrefsClass.getClientId();

        System.out.println("client id : " + clientId);
       // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", content_type)
                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                .addFormDataPart("userId", clientId)
                .addFormDataPart("directoryName", diretoryName)
                .addFormDataPart("role", "2")
                .build();

        Request request = new Request.Builder()
                .url(APIUrls.UPLOAD_URL)
                .post(requestBody)
                .build();
        System.out.println("request url : " + request.url());
        try {
            response = client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int pos = position;
        diretoryName = foldersName.get(pos);
        Toast.makeText(this, "u selected " + foldersName.get(pos) + " folder", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class UploadDocsToServer extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ELockersActivity.this);
            progressDialog.setTitle("uploading");
            progressDialog.setMessage("please wait...");
            progressDialog.show();
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... voids) {
            uploadImage();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String responseBody = null;
            try {
                responseBody = response.body().string();
                System.out.println("response from server " + responseBody);
                if (responseBody.length()> 0) {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (success == 1) {
                        Toast.makeText(ELockersActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ELockersActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class GetFolderLists extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ELockersActivity.this);
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            System.out.println("url for folder list : " + strings[0]);
            return new JSONDownloader().jsonData(strings[0] + "?clientId=" + clientId);

        }

        @Override
        protected void onPostExecute(String foldersList) {
            super.onPostExecute(foldersList);
            setList(foldersList);
            if (listView.getVisibility() != View.VISIBLE) {
                listView.setVisibility(View.VISIBLE);
            }
            System.out.println("FOLDERS LIST : " + foldersList);
            progressDialog.dismiss();
        }
    }

    private class CreateDirectoryOnServer extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ELockersActivity.this);
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println(strings[0]);
            return new JSONDownloader().jsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            dialog.dismiss();
            //listView.setAdapter(null);
            System.out.println("response for folder creation : " + s);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            //setList(s);
        }
    }

    @Override
    public void recreate() {
        super.recreate();
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            super.recreate();
        } else {
            startActivity(getIntent());
            finish();
        }
    }
}
