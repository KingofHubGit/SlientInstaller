package com.dotorom.slientinstaller.utils;

import java.lang.reflect.Method;

public class PropertyUtils {
	/**
     * Get the value for the given key.
     * @return an empty string if the key isn't found
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static String getProp(String key, String defstr) {
        String result="";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class, String.class);
            result=(String)get.invoke(c, key, defstr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Set the value for the given key.
     * @throws IllegalArgumentException if the key exceeds 32 characters
     * @throws IllegalArgumentException if the value exceeds 92 characters
     */
    public static void setProp(String key, String val) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class,String.class);
            set.invoke(c, key,val);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static String getWifiMacAddress() {
        String result="";
        try {
            Class<?> c = Class.forName("android.net.wifi.WifiInfo");

            Method get = c.getMethod("getMacAddress");
            result=(String)get.invoke(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String getBtMacAddress() {
        String result="";
        try {
            Class<?> c = Class.forName("android.bluetooth.BluetoothAdapter");

            Method get = c.getMethod("getAddress");
            result=(String)get.invoke(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
}
