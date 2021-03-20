package com.dotorom.slientinstaller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.dotorom.slientinstaller.method.Method;
import com.dotorom.slientinstaller.method.MethodByAIDL;
import com.dotorom.slientinstaller.method.MethodByAccessibilityService;
import com.dotorom.slientinstaller.method.MethodByPackageInstaller;
import com.dotorom.slientinstaller.method.MethodByPm;
import com.dotorom.slientinstaller.method.MethodByReflcetPMS;
import com.dotorom.slientinstaller.method.MethodByReflectCustomer;
import com.dotorom.slientinstaller.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.bt9)
    Button bt9;

    @BindView(R.id.bt10)
    Button bt10;

    @BindView(R.id.tv1)
    TextView tv1;

    File apkFile;
    String installRet;

    public static final String DEFAULT_INSTALL_APP_PACKAGE_NAME = "com.pax.debugtest";
    public static final String DEFAULT_INSTALL_APP_ACTIVITY = "com.pax.debugtest.VersionActivity";

    Method method;

    private static Dialog chooseDialog;
    private static String[] appVersion = {"debugtest_V2.0.apk","debugtest_V2.1.apk","debugtest_V2.2.apk","debugtest_V2.3.apk"};
    private static String targetVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            apkFile = new File(Environment.getExternalStorageDirectory(), "/Download/InstallApks/" + targetVersion);
        }else{
            Toast.makeText(this, "Sdcard is not Existed!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4,R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9, R.id.bt10})
    public void onClick(View v){
        init();
        switch (v.getId()){
            case R.id.bt1:
                Log.e(TAG, "==================onClick: 1");
                Log.e(TAG, "apkFile :" + apkFile.getAbsolutePath());
                method = new MethodByAccessibilityService();

                //break;

            case R.id.bt2:
                Log.e(TAG, "==================onClick: 2");
                //method = new MethodByPm();
                //method.install(apkFile.getAbsolutePath());
                method = new MethodByPackageInstaller();
                //break;
            case R.id.bt3:
                Log.e(TAG, "==================onClick: 3");
                method = new MethodByPm();
                //method.install(apkFile.getAbsolutePath());
                //break;
            case R.id.bt4:
                Log.e(TAG, "==================onClick: 4");
                method = new MethodByReflcetPMS();
                //method.install(apkFile.getAbsolutePath());

                //MethodByAIDL.installApp();

                //break;
            case R.id.bt5:
                Log.e(TAG, "==================onClick: 5");
                method = new MethodByAIDL();


                //break;
            case R.id.bt6:
                Log.e(TAG, "==================onClick: 6");
                method = new MethodByReflectCustomer();

                //break;
            case R.id.bt7:
                Log.e(TAG, "==================onClick: 7");
                //待定
                method.install(apkFile.getAbsolutePath());
                break;
            case R.id.bt8:
                Log.e(TAG, "==================onClick: 8");
                showChooseDialog(MainActivity.this);
                break;
            case R.id.bt9:
                Log.e(TAG, "==================onClick: 8");
                try {
                    if(Utils.checkAppInstalled(this,DEFAULT_INSTALL_APP_PACKAGE_NAME)){
                        tv1.setText(Utils.getAppVersion(this, DEFAULT_INSTALL_APP_PACKAGE_NAME));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    tv1.setText("未检测到应用");
                }
                break;
            case R.id.bt10:
                Log.e(TAG, "==================onClick: 8");

                break;
        }
    }

    private void showChooseDialog(Context context) {
        Resources r = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("请选择你要升级的应用版本");

        final String[] singleChoiceItems = appVersion;

        builder.setSingleChoiceItems(singleChoiceItems, 0 , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                targetVersion = singleChoiceItems[which];
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        chooseDialog = builder.create();
        chooseDialog.setCanceledOnTouchOutside(false);
        chooseDialog.show();
    }

}