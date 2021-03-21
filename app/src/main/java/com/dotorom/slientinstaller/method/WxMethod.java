package com.dotorom.slientinstaller.method;

import android.widget.Toast;

import com.dotorom.slientinstaller.MyApplication;

import java.io.File;

public class WxMethod extends Method {
    @Override
    public int install(String appPath) {
        return Method.INSTALL_SUCCESSED;
    }
}
