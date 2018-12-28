package cn.gmuni.sc.module.more.express.decoding;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.DecodeHintType;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: zhuxin
 * @Date: 2018/9/28 11:22
 * @Description:
 */
public class DecodeThread extends Thread {
    private final CaptureActivityHandler captureHandler;
    private final Hashtable<DecodeHintType, Object> hints;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    DecodeThread(CaptureActivityHandler captureHandler, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.captureHandler = captureHandler;
        handlerInitLatch = new CountDownLatch(1);

        hints = new Hashtable<>(3);

        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<>();
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);// 一维码
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);// 二维码
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        if (characterSet != null) {
            hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }
    }

    Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new DecodeHandler(captureHandler, hints);
        handlerInitLatch.countDown();
        Looper.loop();
    }
}
