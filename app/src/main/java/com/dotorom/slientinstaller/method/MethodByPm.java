package com.dotorom.slientinstaller.method;

import android.util.Log;

import com.dotorom.slientinstaller.utils.ShellUtil;

public class MethodByPm extends Method {

    private static final String TAG = "dengtl";

    @Override
    public int install(String appPath) {
        try {
            //installRet = CmdUtils.execShellCommand("pm install -r " + apkFile);
            //installRet = CmdUtils.execShell(" pm install -i " + "com.dotorom.slientinstaller" +" --user 0 "+ " /sdcard/Download/InstallApks/debugtest_V2.1.apk");
            String installRet = ShellUtil.execute(" pm install -i " + "com.dotorom.slientinstaller" + " --user 0 " + " /sdcard/Download/InstallApks/debugtest_V2.1.apk", 10000);
            //installRet = CmdUtils.execShellCommand("pm install -r " + " sdcard/Download/InstallApks/debugtest_V2.1.apk");
            Log.e(TAG, "installRet :" + installRet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
