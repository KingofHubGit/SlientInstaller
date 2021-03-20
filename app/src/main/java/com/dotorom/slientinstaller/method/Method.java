package com.dotorom.slientinstaller.method;

public abstract class Method {
    public static final int INSTALL_SUCCESSED = 0;
    public static final int INSTALL_FAILED = -1;

    public static final int INSTALL_OPT = 1;
    public static final int UNINSTALL_OPT = 2;

    public abstract int install(String appPath);

}
