package com.dotorom.slientinstaller.method;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.dotorom.slientinstaller.assist.BaseInstallActivity;
import com.dotorom.slientinstaller.MyApplication;
import com.dotorom.slientinstaller.constants.Constants;
import com.dotorom.slientinstaller.utils.PropertyUtils;

import java.io.File;

public class MethodByAccessibilityService extends WxMethod {

    @Override
    public int install(String appPath) {
        PropertyUtils.setProp(Constants.ENABLED_SLIENT_INSTALLER_PROP, "false");
        if (TextUtils.isEmpty(appPath)) {
            Toast.makeText(MyApplication.getAppContext(), "请选择安装包！", Toast.LENGTH_SHORT).show();
            return Method.INSTALL_FAILED;
        }

/*        //参数1 上下文, 参数2 在AndroidManifest中的android:authorities值, 参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(MyApplication.getAppContext(), Constants.AUTHORITY, new File(appPath));
        Intent install = new Intent(Intent.ACTION_VIEW);
        //由于没有在Activity环境下启动Activity,设置下面的标签
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        MyApplication.getAppContext().startActivity(install);*/

        Intent intent = new Intent(MyApplication.getAppContext(), BaseInstallActivity.class);
        intent.putExtra("path", appPath);
        MyApplication.getAppContext().startActivity(intent);

        return Method.INSTALL_START;
    }



}
