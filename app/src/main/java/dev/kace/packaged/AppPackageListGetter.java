package dev.kace.packaged;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppPackageListGetter {

    public static class AppInfo {
        private String packageName;
        private String appName;
        private Drawable appIcon;

        public AppInfo(String packageName, String appName, Drawable appIcon) {
            this.packageName = packageName;
            this.appName = appName;
            this.appIcon = appIcon;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getAppName() {
            return appName;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }
    }

    public static List<AppInfo> getAllInstalledApps(Context context) {
        List<AppInfo> apps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> appList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : appList) {
            String packageName = appInfo.packageName;
            String appName = appInfo.loadLabel(packageManager).toString();
            Drawable appIcon = appInfo.loadIcon(packageManager);
            AppInfo app = new AppInfo(packageName, appName, appIcon);
            apps.add(app);
        }

        // Sort the list by label name
        Collections.sort(apps, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo app1, AppInfo app2) {
                return app1.getAppName().compareToIgnoreCase(app2.getAppName());
            }
        });

        return apps;
    }

    public static class MainActivity extends AppCompatActivity {
        private ListView listView;
        private FloatingActionButton addButton;
        private ArrayAdapter<AppInfo> selectAdapter;
        public static List<AppInfo> selectedApps;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            listView = findViewById(R.id.list_view);

            // Get all installed apps
            List<AppInfo> installedApps = getAllInstalledApps(this);

            // Create custom adapter to display app name, package name, and icon
            AppListAdapter adapter = new AppListAdapter(this, installedApps);

            // Set the adapter to the ListView
            listView.setAdapter(adapter);

            // Handle click event of list items in app_list_item.xml
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get selected app
                    AppPackageListGetter.AppInfo selectedApp = (AppPackageListGetter.AppInfo) parent.getItemAtPosition(position);

                    // Open settings for the selected app
                    openAppSettings(selectedApp.getPackageName());
                }
            });

        }

        private void openAppSettings(String packageName) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }

        private class AppListAdapter extends ArrayAdapter<AppInfo> {

            public AppListAdapter(Context context, List<AppInfo> apps) {
                super(context, R.layout.app_list_item, apps);
            }

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View itemView = convertView;
                if (itemView == null) {
                    itemView = getLayoutInflater().inflate(R.layout.app_list_item, parent, false);
                }

                AppPackageListGetter.AppInfo currentApp = getItem(position);

                // Set app name
                android.widget.TextView appNameTextView = itemView.findViewById(R.id.app_name);
                appNameTextView.setText(currentApp.getAppName());

                // Set package name
                android.widget.TextView packageNameTextView = itemView.findViewById(R.id.package_name);
                packageNameTextView.setText(currentApp.getPackageName());

                // Set app icon
                android.widget.ImageView appIconImageView = itemView.findViewById(R.id.app_icon);
                appIconImageView.setImageDrawable(currentApp.getAppIcon());

                return itemView;
            }
        }
    }
}