package com.dotorom.slientinstaller.method;

import android.content.Context;
import android.os.IBinder;

import com.dotorom.slientinstaller.MyApplication;
import com.wxpayface.os.IWxDeviceManager;

import java.lang.reflect.InvocationTargetException;

public class MethodByReflectCustomer extends Method {
    @Override
    public int install(String appPath) {
        try {
            Class<?> forName = Class.forName("com.wxpayface.os.WxDeviceManager");
            java.lang.reflect.Method method = forName.getDeclaredMethod("getInstance", Context.class);
            method.setAccessible(true);
            Object WxDeviceManager = method.invoke(null, MyApplication.getAppContext());

            java.lang.reflect.Method updateMethod = forName.getDeclaredMethod("update", int.class, String.class);
            updateMethod.setAccessible(true);
            updateMethod.invoke(WxDeviceManager,1,appPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return Method.INSTALL_FAILED;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return Method.INSTALL_FAILED;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Method.INSTALL_FAILED;
        }
        return Method.INSTALL_SUCCESSED;
    }

}
