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
import androidx.annotation.NonNull;
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
        List<ApplicationInfo> appList = packageManager.getInstalledApplications(
                PackageManager.MATCH_ALL);

        for (ApplicationInfo appInfo : appList) {
            String packageName = appInfo.packageName;
            String appName = appInfo.loadLabel(packageManager).toString();
            Drawable appIcon = appInfo.loadIcon(packageManager);
            AppInfo app = new AppInfo(packageName, appName, appIcon);
            apps.add(app);
        }

        // Sort the list alphabetically by app name
        Collections.sort(apps, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo app1, AppInfo app2) {
                return app1.getAppName().compareToIgnoreCase(app2.getAppName());
            }
        });

        return apps;
    }
}