package cn.gmuni.sc.app;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 配置输出日志
 */
public class LoggerAdapter {

    public void init(){
        FormatStrategy fs = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(1)
                .tag("智慧校园")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(fs));
    }
}
