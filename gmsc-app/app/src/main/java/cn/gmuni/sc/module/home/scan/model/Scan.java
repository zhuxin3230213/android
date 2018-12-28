package cn.gmuni.sc.module.home.scan.model;

/**
 * @Author: zhuxin
 * @Date: 2018/11/6 11:03
 * @Description:
 */
public interface Scan {

    boolean  support(String scanResult, String barcodeFormat);//判断scanResult是否符合本实现类

    int order();//该实现类排序号

    void execute(String scanResult);//符合后执行操作
}
