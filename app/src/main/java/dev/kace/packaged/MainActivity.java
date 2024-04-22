package dev.kace.packaged;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton addButton;
    private ListView selectView;

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
        List<AppPackageListGetter.AppInfo> installedApps = AppPackageListGetter.getAllInstalledApps(this);

        // Create custom adapter to display app name, package name, and icon
        AppListAdapter adapter = new AppListAdapter(this, installedApps);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get selected app
                AppPackageListGetter.AppInfo selectedApp = (AppPackageListGetter.AppInfo) parent.getItemAtPosition(position);

                // Open settings for the selected app
                openAppSettings(selectedApp.getPackageName());
            }
        });

        FloatingActionButton openWebsiteButton = findViewById(R.id.fab);

        openWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a website when the button is clicked
                openWebsite("https://www.github.com/userkace/packaged");
            }
        });
    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void openAppSettings(String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }
    private class AppListAdapter extends ArrayAdapter<AppPackageListGetter.AppInfo> {

        public AppListAdapter(Context context, List<AppPackageListGetter.AppInfo> apps) {
            super(context, R.layout.app_list_item, apps);
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.view.View itemView = convertView;
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