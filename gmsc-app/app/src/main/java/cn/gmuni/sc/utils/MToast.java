package cn.gmuni.sc.utils;

import android.widget.Toast;

import cn.gmuni.sc.app.BaseApplication;

public class MToast {

    public static void showLongToast(String message){
        Toast.makeText(BaseApplication.getInstance().getBaseContext(),message,Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String message){
        Toast.makeText(BaseApplication.getInstance().getBaseContext(),message,Toast.LENGTH_SHORT).show();
    }
}
