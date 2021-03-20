package com.dotorom.slientinstaller.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;

import com.wxpayface.os.IWxDeviceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Utils {
    public static boolean checkAppInstalled(Context context, String pkgName) throws PackageManager.NameNotFoundException {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        /*try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }*/
        packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        if(packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }

    /**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }
    /**
     * 获取程序 图标
     * @param context
     * @param packname 应用包名
     * @return
     */
    public Drawable getAppIcon(Context context, String packname){
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            //获取到应用信息
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取程序的版本号
     * @param context
     * @param packname
     * @return
     */
    public static String getAppVersion(Context context, String packname) throws PackageManager.NameNotFoundException{
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
/*        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;*/

        PackageInfo packinfo = pm.getPackageInfo(packname, 0);
        return packinfo.versionName;
    }
    /**
     * 获取程序的名字
     * @param context
     * @param packname
     * @return
     */
    public String getAppName(Context context,String packname){
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return packname;
    }
    /*
     * 获取程序的权限
     */
    public String[] getAllPermissions(Context context,String packname){
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo =  pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取程序的签名
     * @param context
     * @param packname
     * @return
     */
    public static String getAppSignature(Context context,String packname){
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取当前应用签名
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }
    /**
     * 获取当前展示 的Activity名称
     * @return
     */
    public static String getCurrentActivityName(Context context){
        ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IBinder getSystemService(String serviceName) {
        //反射获取ServiceManager
        try {
            //指定反射类
            Class<?> forName = Class.forName("android.os.ServiceManager");
            //获取方法，参数是String类型
            Method method = forName.getMethod("getService", String.class);
            //传入参数
            IBinder iBinder = (IBinder) method.invoke(null, serviceName);
            //初始化AIDL
            //service = IWxDeviceManager.Stub.asInterface(iBinder);
            return iBinder;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
