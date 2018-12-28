package cn.gmuni.sc.module.home.scan;

import java.util.Comparator;

import cn.gmuni.sc.module.home.scan.model.Scan;

/**
 * @Author: zhuxin
 * @Date: 2018/11/7 16:29
 * @Description:
 */
public class ComparatorImpl implements Comparator<Scan> {


    @Override
    public int compare(Scan o1, Scan o2) {
        //升序
        int orderOne = o1.order();
        int orderTwo = o2.order();
        if (orderOne > orderTwo) {
            return 1;
        } else if (orderOne < orderTwo) {
            return -1;
        } else {
            return 0;
        }
    }
}
