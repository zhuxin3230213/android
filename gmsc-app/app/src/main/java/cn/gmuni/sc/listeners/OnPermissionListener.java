package cn.gmuni.sc.listeners;

/**
 * 申请权限接口
 */
public abstract class OnPermissionListener {

    /**
     * 接受权限申请
     */
    public abstract void onReceive();


    /**
     * 拒绝申请权限
     */
    public void onReject(String permission) {

    }

    public void onReject(String... permission) {

    }
}
