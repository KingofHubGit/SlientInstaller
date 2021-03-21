package com.dotorom.slientinstaller.method;

public abstract class Method {
    public static final int INSTALL_SUCCESSED = 0;
    public static final int INSTALL_FAILED = -1;
    public static final int INSTALL_START = 1;

    public static final int INSTALL_OP = 1;
    public static final int UNINSTALL_OP = 2;


    public abstract int install(String appPath);

}
