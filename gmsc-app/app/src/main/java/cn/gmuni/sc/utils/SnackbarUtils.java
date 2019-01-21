package cn.gmuni.sc.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @Author: zhuxin
 * @Date: 2018/9/28 12:40
 * @Description:
 * 显示简单消息并且提供和用户的一个简单操作的一种弹出式提醒
 */
public class SnackbarUtils {

    public static void show(Activity activity, int resId) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(Activity activity, CharSequence text) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
