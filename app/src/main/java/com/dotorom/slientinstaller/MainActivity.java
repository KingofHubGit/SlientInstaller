package com.dotorom.slientinstaller;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.dotorom.slientinstaller.utils.CmdUtils;
import com.dotorom.slientinstaller.utils.ShellUtil;
import com.dotorom.slientinstaller.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "dengtl";

    @BindView(R.id.bt1)
    Button bt1;

    @BindView(R.id.bt2)
    Button bt2;

    @BindView(R.id.bt3)
    Button bt3;

    @BindView(R.id.bt4)
    Button bt4;

    @BindView(R.id.bt5)
    Button bt5;

    @BindView(R.id.bt6)
    Button bt6;

    @BindView(R.id.bt7)
    Button bt7;

    @BindView(R.id.bt8)
    Button bt8;

    @BindView(R.id.tv1)
    TextView tv1;

    File apkFile;
    String installRet;

    public static final String DEFAULT_INSTALL_APP_PACKAGE_NAME = "com.pax.debugtest";
    public static final String DEFAULT_INSTALL_APP_ACTIVITY = "com.pax.debugtest.VersionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            apkFile = new File(Environment.getExternalStorageDirectory(), "/Downloads/InstallApks/debugtest_V2.0.apk");
        }else{
            Toast.makeText(this, "Sdcard is not Existed!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4,R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt1:
                Log.e(TAG, "==================onClick: 1");
                Log.e(TAG, "apkFile :" + apkFile.getAbsolutePath());
                try {
                    //installRet = CmdUtils.execShellCommand("pm install -r " + apkFile);
                    //installRet = CmdUtils.execShell(" pm install -i " + "com.dotorom.slientinstaller" +" --user 0 "+ " /sdcard/Download/InstallApks/debugtest_V2.1.apk");
                    installRet = ShellUtil.execute(" pm install -i " + "com.dotorom.slientinstaller" + " --user 0 " + " /sdcard/Download/InstallApks/debugtest_V2.1.apk", 10000);
                    //installRet = CmdUtils.execShellCommand("pm install -r " + " sdcard/Download/InstallApks/debugtest_V2.1.apk");
                    Log.e(TAG, "installRet :" + installRet);
                    tv1.setText(installRet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt2:
                Log.e(TAG, "==================onClick: 2");

                break;
            case R.id.bt3:
                Log.e(TAG, "==================onClick: 3");

                break;
            case R.id.bt4:
                Log.e(TAG, "==================onClick: 4");
                break;
            case R.id.bt5:
                Log.e(TAG, "==================onClick: 5");
                break;
            case R.id.bt6:
                Log.e(TAG, "==================onClick: 6");
                break;
            case R.id.bt7:
                Log.e(TAG, "==================onClick: 7");
                break;
            case R.id.bt8:
                Log.e(TAG, "==================onClick: 8");
                try {
                    if(Utils.checkAppInstalled(this,DEFAULT_INSTALL_APP_PACKAGE_NAME)){
                        tv1.setText(Utils.getAppVersion(this, DEFAULT_INSTALL_APP_PACKAGE_NAME));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    tv1.setText("未检测到应用");
                }
                break;
        }
    }
}