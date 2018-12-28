package cn.gmuni.sc.utils;

import android.graphics.drawable.GradientDrawable;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;

public class DrawableUtil {

    /**
     * 获取渐变色
     * @param start
     * @param end
     * @param orientation
     * @return
     */
    public static GradientDrawable getGradientDrawable(int start,int end,GradientDrawable.Orientation orientation){
        GradientDrawable gradientDrawable = new GradientDrawable(orientation,
                new int[]{BaseApplication.getInstance().getResources().getColor(start),
                        BaseApplication.getInstance().getResources().getColor(end)});
        return gradientDrawable;
    }

    /**
     * 获取默认渐变色
     * @return
     */
    public static GradientDrawable getDefaultGradientDrawable(){
        return getDefaultGradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT);
    }

    /**
     * 获取默认渐变色
     * @return
     */
    public static GradientDrawable getDefaultGradientDrawable(GradientDrawable.Orientation orientation){
        return getGradientDrawable(R.color.colorGradientStart,R.color.colorGradientEnd, orientation);
    }
}
