package com.dotorom.slientinstaller.method;

import android.content.Intent;

import com.dotorom.slientinstaller.assist.BaseInstallActivity;
import com.dotorom.slientinstaller.MyApplication;
import com.dotorom.slientinstaller.constants.Constants;
import com.dotorom.slientinstaller.utils.PackageInstallerUtil;
import com.dotorom.slientinstaller.utils.PropertyUtils;
import com.dotorom.slientinstaller.utils.Utils;


public class MethodByPackageInstaller extends Method {
    private static final String TAG = "dengtl";
    private PackageInstallerUtil mPackageInstallerUtil;
    @Override
    public int install(String appPath) {
        PropertyUtils.setProp(Constants.ENABLED_SLIENT_INSTALLER_PROP, "true");

        //Utils.startActivityByClass(MyApplication.getAppContext(), DownAct.class);
        //创建意图对象
        Intent intent = new Intent(MyApplication.getAppContext(), BaseInstallActivity.class);
        //设置传递键值对
        intent.putExtra("path", appPath);
        //激活意图
        MyApplication.getAppContext().startActivity(intent);
        return Method.INSTALL_START;
    }

}
