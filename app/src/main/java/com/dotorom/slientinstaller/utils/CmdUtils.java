package com.dotorom.slientinstaller.utils;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by dengtl on 2020/12/9.
 */

public class CmdUtils {

    private static final String TAG = "dengtl";

    public static String execShell(String cmd) {
        String result = null;
        try {
            // 权限设置
            Process p = Runtime.getRuntime().exec("sh"); // su为root用户,sh普通用户
            // 获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd); // 将命令写入
            dataOutputStream.flush(); // 提交命令
            // 关闭流操作
            dataOutputStream.close();
            outputStream.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            result = stringBuffer.toString();
            in.close();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }









}
