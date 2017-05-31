package practice.mohitattri.cacont;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class AppsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
    }

    private List<ApplicationInfo> getListOfApps() {

        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> listOfApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        return listOfApps;
    }
}
