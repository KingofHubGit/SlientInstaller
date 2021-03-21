package com.dotorom.slientinstaller.method;

public class MethodByJNIInterface extends Method {
    //由于时间问题，这块功能暂未实现
    /*static {
        System.load("slient_install");
    }*/
    @Override
    public int install(String appPath) {
        //int ret = com_dotorom_slientinstaller_method_MethodByJNIInterface_install(appPath);
        //return ret;
        return Method.INSTALL_FAILED;
    }

}
