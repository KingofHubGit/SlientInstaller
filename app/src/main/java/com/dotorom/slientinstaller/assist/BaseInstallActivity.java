package com.dotorom.slientinstaller.assist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dotorom.slientinstaller.utils.PackageInstallerUtil;

import java.io.File;

public class BaseInstallActivity extends Activity {

    private static final String TAG = "dengtl";
    private PackageInstallerUtil mPackageInstallerUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String appPath = intent.getStringExtra("path");

        File file = new File(appPath);
        Log.e(TAG, "onCreate: "+  file.getAbsolutePath());
        mPackageInstallerUtil = new PackageInstallerUtil(this, file.getAbsolutePath());
        mPackageInstallerUtil.install();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PackageInstallerUtil.UNKNOWN_CODE) {
            mPackageInstallerUtil.install();
        }
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
