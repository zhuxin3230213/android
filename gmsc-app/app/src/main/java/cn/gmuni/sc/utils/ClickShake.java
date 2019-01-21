package cn.gmuni.sc.utils;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClickShake {
    private static Map<Integer,Long> checks = new HashMap<>();
    private static Handler handler;
    static{
        handler = new Handler();
        //调度清理超过一秒钟未点击的记录
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                List<Integer> removes = new ArrayList<>();
                Set<Map.Entry<Integer, Long>> entries = checks.entrySet();
                for(Map.Entry<Integer,Long> entry : entries){
                    if(time - entry.getValue()>1000){
                        removes.add(entry.getKey());
                    }
                }
                for(Integer it : removes){
                    checks.remove(it);
                }
            }
        },5000);
    }

    public static boolean check(int id){
        long time = System.currentTimeMillis();
        if(!checks.containsKey(id)){
            checks.put(id,time);
            return true;
        }else{
            long preTime = checks.get(id);
            checks.put(id,time);
            return time - preTime > 1000;
        }
    }
}
