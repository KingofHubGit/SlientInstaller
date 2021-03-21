package com.dotorom.slientinstaller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.dotorom.slientinstaller.constants.Constants;
import com.dotorom.slientinstaller.method.Method;
import com.dotorom.slientinstaller.method.MethodByAIDL;
import com.dotorom.slientinstaller.method.MethodByAccessibilityService;
import com.dotorom.slientinstaller.method.MethodByJNIInterface;
import com.dotorom.slientinstaller.method.MethodByPackageInstaller;
import com.dotorom.slientinstaller.method.MethodByPm;
import com.dotorom.slientinstaller.method.MethodByReflcetPMS;
import com.dotorom.slientinstaller.method.MethodByReflectCustomer;
import com.dotorom.slientinstaller.method.MethodDisplayed;
import com.dotorom.slientinstaller.utils.AssetsUtils;
import com.dotorom.slientinstaller.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "dengtl";

    @BindView(R.id.bt0)
    Button bt0;

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

    @BindView(R.id.bt11)
    Button bt11;

    @BindView(R.id.tv1)
    TextView tv1;

    File apkFile;
    String installRet;



    Method method;

    private static Dialog chooseDialog;
    private static String[] appVersion = {"debugtest_V2.0.apk","debugtest_V2.1.apk","debugtest_V2.2.apk","debugtest_V2.3.apk"};
    private static String targetVersion = "debugtest_V2.0.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        update();
    }

    private void init() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            apkFile = new File(Environment.getExternalStorageDirectory(), "/Download/InstallApks/" + targetVersion);
            Log.e(TAG, "apkFile :" + apkFile.getAbsolutePath());
            try {
                AssetsUtils.copyFileFromAssets(this, "InstallApks/" + targetVersion, apkFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "不存在外置SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.bt0,R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4,R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9, R.id.bt10,R.id.bt11})
    public void onClick(View v){
        init();
        switch (v.getId()){
            case R.id.bt0:
                Log.e(TAG, "==================onClick: 0");

                method = new MethodDisplayed();
                WxInstallMethod(method, apkFile);
                break;

            case R.id.bt1:
                Log.e(TAG, "==================onClick: 1");
                Intent intentAccessibility = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                MyApplication.getAppContext().startActivity(intentAccessibility);
                break;

            case R.id.bt11:
                Log.e(TAG, "==================onClick: 1");
                method = new MethodByAccessibilityService();
                WxInstallMethod(method, apkFile);
                break;

            case R.id.bt2:
                Log.e(TAG, "==================onClick: 2");
                method = new MethodByPackageInstaller();
                WxInstallMethod(method, apkFile);
                break;
            case R.id.bt3:
                Log.e(TAG, "==================onClick: 3");
                method = new MethodByPm();
                WxInstallMethod(method, apkFile);
                break;
            case R.id.bt4:
                Log.e(TAG, "==================onClick: 4");
                method = new MethodByReflcetPMS();
                WxInstallMethod(method, apkFile);
                break;
            case R.id.bt5:
                Log.e(TAG, "==================onClick: 5");
                method = new MethodByAIDL();
                WxInstallMethod(method, apkFile);
                break;
            case R.id.bt6:
                Log.e(TAG, "==================onClick: 6");
                method = new MethodByReflectCustomer();
                WxInstallMethod(method, apkFile);
                break;
            case R.id.bt7:
                Log.e(TAG, "==================onClick: 7");
                //未实现，后续实现
                method = new MethodByJNIInterface();
                WxInstallMethod(method, apkFile);
                break;
            case R.id.bt8:
                Log.e(TAG, "==================onClick: 8");
                showChooseDialog(MainActivity.this);
                break;
            case R.id.bt9:
                Log.e(TAG, "==================onClick: 8");
                try {
                    if(Utils.checkAppInstalled(this, Constants.DEFAULT_INSTALL_APP_PACKAGE_NAME)){
                        tv1.setText("被安装应用Version:" + Utils.getAppVersion(this, Constants.DEFAULT_INSTALL_APP_PACKAGE_NAME));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    tv1.setText("未检测到应用");
                }
                break;
            case R.id.bt10:
                Log.e(TAG, "==================onClick: 8");
                showAboutDialog(MainActivity.this);
                break;
        }
    }

    private MyHandler wxHandler = new MyHandler();
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.HANDLER_SUCCESSED:
                    Log.e(TAG, "handleMessage: HANDLER_SUCCESSED");
                    Toast.makeText(MainActivity.this, msg.obj.toString()+"安装成功！",Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HANDLER_FAILED:
                    Log.e(TAG, "handleMessage: HANDLER_FAILED");
                    Toast.makeText(MainActivity.this, msg.obj+"安装失败！",Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HANDLER_START_INSTALL:
                    Log.e(TAG, "handleMessage: HANDLER_START_INSTALL");
                    Toast.makeText(MainActivity.this, msg.obj+"开始安装！", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HANDLER_UPDATE:
                    Log.e(TAG, "handleMessage: HANDLER_UPDATE");
                    try {
                        tv1.setText("被安装应用Version:" + Utils.getAppVersion(MainActivity.this, Constants.DEFAULT_INSTALL_APP_PACKAGE_NAME));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void WxInstallMethod(Method method, File file){
        int ret = method.install(file.getAbsolutePath());
        Message message = new Message();
        if(ret == Method.INSTALL_SUCCESSED){
            message.what = Constants.HANDLER_SUCCESSED;
        }else if( ret == Method.INSTALL_FAILED){
            message.what = Constants.HANDLER_FAILED;
        }else {
            message.what = Constants.HANDLER_START_INSTALL;
        }
        message.obj = file.getName();
        wxHandler.sendMessage(message);
        update();
    }

    public void update(){
        Message message = Message.obtain();
        message.what = Constants.HANDLER_UPDATE;
        wxHandler.sendMessageAtTime(message,10000);
    }

    private void showChooseDialog(Context context) {
        Resources r = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_wxpayface_icon);
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

    private void showAboutDialog(Context context) {
        Resources r = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_wxpayface_icon);
        builder.setTitle("关于");
        try {
            builder.setMessage(
                    this.getString(R.string.app_name) + "\n"
                    + "软件版本：" + Utils.getAppVersion(this, this.getApplicationInfo().packageName) + "\n"
                    + "软件作者：DoToRom" + "\n"
                    + "开发版本：Android 7.1" + "\n\n"
                    + "注意事项：" + "\n"
                    + "方案1需要先打开微信无障碍服务" + "\n"
                    + "方案2需要定制系统PackageInstaller" + "\n"
                    + "方案3需要root状态" + "\n"
                    + "方案4兼容7.1~10.0" + "\n"
                    + "方案5需要系统定制WxDeviceManagerService" + "\n"
                    + "方案6需要系统定制WxDeviceManagerService" + "\n"
                    + "方案7需要提供so库，兼容7.1~10.0" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }


        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
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