package com.dotorom.slientinstaller.method;

import android.content.Intent;

import com.dotorom.slientinstaller.assist.BaseInstallActivity;
import com.dotorom.slientinstaller.MyApplication;
import com.dotorom.slientinstaller.constants.Constants;
import com.dotorom.slientinstaller.utils.PropertyUtils;

public class MethodDisplayed extends Method {
    @Override
    public int install(String appPath) {
        PropertyUtils.setProp(Constants.ENABLED_SLIENT_INSTALLER_PROP, "false");
        //Utils.startActivityByClass(MyApplication.getAppContext(), DownAct.class);

        Intent intent = new Intent(MyApplication.getAppContext(), BaseInstallActivity.class);
        intent.putExtra("path", appPath);
        MyApplication.getAppContext().startActivity(intent);

        return Method.INSTALL_START;
    }
}
