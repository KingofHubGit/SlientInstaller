package com.dotorom.slientinstaller.method;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.dotorom.slientinstaller.MyApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class MethodByReflcetPMS extends Method{
    public static final int INSTALL_REPLACE_EXISTING = 0x00000002;
    private static final String TAG = "dengtl";

    @Override
    public int install(String appPath) {
        File file = new File(appPath);
        Log.e(TAG, "install: " + file.getAbsolutePath());
        if (file.exists()) {
            if (Build.VERSION.SDK_INT > 23) {
                //ApkInSilence(path, getApkPackageName(path),INSTALL_APK);
                //也可以用这种方式
                installUpperSdk23(appPath);
            } else {
                installLowerSdk23(appPath);
            }
        } else {
            Toast.makeText(MyApplication.getAppContext(), "文件不存在", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    private class MyObserver extends IPackageInstallObserver.Stub{
        @Override
        public void packageInstalled(String packageName, int returnCode)
                throws RemoteException {
            Log.e(TAG, "returnCode:"+returnCode);

        }
    }


    public String getApkPackageName(String path) {
        String packageName = "";
        PackageManager pm = MyApplication.getAppContext().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            packageName = appInfo.packageName;  //获取安装包名称
        }
        return packageName;
    }

    private Class<?>[] getParamTypes(Class<?> cls, String mName) {
        Class<?> cs[] = null;
        java.lang.reflect.Method[] mtd = cls.getMethods();
        for (int i = 0; i < mtd.length; i++) {
            if (!mtd[i].getName().equals(mName)) {
                continue;
            }

            cs = mtd[i].getParameterTypes();
        }
        return cs;
    }
    public static final int PER_USER_RANGE = 100000;
    public static int getUserId(int uid) {
        return uid / PER_USER_RANGE;
    }

    private void installUpperSdk23(String appPath) {
        PackageManager mPm = MyApplication.getAppContext().getPackageManager();
        Class<?> paramTypes[] = getParamTypes(mPm.getClass(), "installPackage");
        try {
            java.lang.reflect.Method method = mPm.getClass().getMethod("installPackage", paramTypes);
            method.invoke(mPm, Uri.fromFile(new File(appPath)), null, INSTALL_REPLACE_EXISTING, getApkPackageName(appPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int installLowerSdk23(String appPath) {
        // 静默安装
        IPackageManager mPm;
        try {
            Class<?> forName = Class.forName("android.os.ServiceManager");
            java.lang.reflect.Method method = forName.getMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, "package");
            Log.e(TAG, "iBinder------------:");
            mPm = IPackageManager.Stub.asInterface(iBinder);
            File apkFile = new File(appPath);
            mPm.installPackage(Uri.fromFile(apkFile), new MyObserver(), INSTALL_REPLACE_EXISTING, apkFile.getPath());
            Toast.makeText(MyApplication.getAppContext(), "安装成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception:"+e.toString());
            Toast.makeText(MyApplication.getAppContext(), "安装失败", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    /**
     * 23版本以上的静默安装方法
     * void installPackageAsUser(in String originPath,
     * in IPackageInstallObserver2 observer,
     * int flags,
     * in String installerPackageName,
     * int userId);
     * @param installPath
     */
    private void ApkInSilence(String installPath, String packageName,int type) {
        Class<?> pmService;
        Class<?> activityTherad;
        java.lang.reflect.Method method;
        try {
            activityTherad = Class.forName("android.app.ActivityThread");
            Class<?> paramTypes[] = getParamTypes(activityTherad, "getPackageManager");
            method = activityTherad.getMethod("getPackageManager", paramTypes);
            Object PackageManagerService = method.invoke(activityTherad);
            pmService = PackageManagerService.getClass();
            if(type == INSTALL_OPT) {
                Class<?> paramTypes1[] = getParamTypes(pmService, "installPackageAsUser");
                method = pmService.getMethod("installPackageAsUser", paramTypes1);
                method.invoke(PackageManagerService, installPath, null, 0x00000040, packageName, getUserId(Binder.getCallingUid()));//getUserId
            }
            else {
                Class<?> paramTypes1[] = getParamTypes(pmService, "deletePackageAsUser");
                method = pmService.getMethod("deletePackageAsUser", paramTypes1);
                method.invoke(PackageManagerService, packageName, null, getUserId(Binder.getCallingUid()),0x00000040);//getUserId
            }
            Toast.makeText(MyApplication.getAppContext(), "成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MyApplication.getAppContext(), "失败", Toast.LENGTH_SHORT).show();
        }
    }


}
