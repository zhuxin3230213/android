package cn.gmuni.sc.module.more.express.callback;

/**
 * @Author: zhuxin
 * @Date: 2018/9/28 12:49
 * @Description:
 */
public interface Callback<T> {
    void onEvent(T t);
}
