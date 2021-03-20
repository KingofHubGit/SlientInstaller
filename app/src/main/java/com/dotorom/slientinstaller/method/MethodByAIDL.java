package com.dotorom.slientinstaller.method;

import android.os.IBinder;
import android.os.RemoteException;

import com.wxpayface.os.IWxDeviceManager;

import java.lang.reflect.InvocationTargetException;

public class MethodByAIDL extends Method {
    public IWxDeviceManager WxDeviceManagerService;

    private IWxDeviceManager getWxDeviceManagerService() {
        //反射获取ServiceManager
        IWxDeviceManager service = null;
        try {
            //指定反射类
            Class<?> forName = Class.forName("android.os.ServiceManager");
            //获取方法，参数是String类型
            java.lang.reflect.Method method = forName.getMethod("getService", String.class);
            //传入参数
            IBinder iBinder = (IBinder) method.invoke(null, "WxDeviceManagerService");
            //初始化AIDL
            service = IWxDeviceManager.Stub.asInterface(iBinder);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return service;
    }

    @Override
    public int install(String appPath) {
        int errMsg = -1;
        WxDeviceManagerService = getWxDeviceManagerService();
        try {
            int i = WxDeviceManagerService.update(1, appPath);
            if (i != 0) {
                errMsg = Method.INSTALL_FAILED;
            }
        } catch (RemoteException e) {
            errMsg = Method.INSTALL_FAILED;
            e.printStackTrace();
        } catch (Exception e) {
            errMsg = Method.INSTALL_FAILED;
            e.printStackTrace();
        }
        return Method.INSTALL_SUCCESSED;
    }
}
