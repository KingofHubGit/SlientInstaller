package com.dotorom.slientinstaller.method;

import android.util.Log;

import com.dotorom.slientinstaller.utils.ShellUtil;

public class MethodByPm extends Method {

    private static final String TAG = "dengtl";

    @Override
    public int install(String appPath) {
        try {
            String installRet = ShellUtil.execute(" pm install -i " + "com.dotorom.slientinstaller" + " --user 0 " + appPath, 10000);
            Log.e(TAG, "installRet :" + installRet);
        } catch (Exception e) {
            e.printStackTrace();
            return Method.INSTALL_FAILED;
        }
        return Method.INSTALL_SUCCESSED;
    }
}
