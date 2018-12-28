package cn.gmuni.sc.base;

import android.content.Context;

/**
 * 公共View接口
 *
 * @author Ht
 */
public interface IBaseView {


    /**
     * 显示无文字进度条
     */
    void showLoading();

    /**
     * 隐藏进度条
     */
    void hideLoading();


    /**
     * 获取当前上下文对象
     */
    Context getContext();



}
