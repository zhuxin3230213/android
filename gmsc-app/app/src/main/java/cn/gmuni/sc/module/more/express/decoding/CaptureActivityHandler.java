package cn.gmuni.sc.module.more.express.decoding;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Vector;

import com.google.zxing.Result;

import cn.gmuni.sc.R;
import cn.gmuni.sc.module.home.scan.CaptureScanActivity;
import cn.gmuni.sc.module.more.express.CaptureActivity;
import cn.gmuni.sc.module.more.express.camera.CameraManager;
import cn.gmuni.sc.module.more.express.constants.Extras;

/**
 * @Author: zhuxin
 * @Date: 2018/9/28 11:21
 * @Description:
 */
public class CaptureActivityHandler extends Handler {
    private static final String TAG = "CaptureActivityHandler";
    private CaptureActivity activity;
    private CaptureScanActivity scanActivity;
    private final DecodeThread decodeThread;
    private State state;
    private String activityName;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureScanActivity scanActivity, CaptureActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet, String activityName) {
        this.activityName = activityName;
        if (activityName.equals(Extras.CAPTURE_ACTIVITY)) {
            this.activity = activity;
        }
        if (activityName.equals(Extras.CAPTURESCAN_ACTIVITY)) {
            this.scanActivity = scanActivity;
        }
        decodeThread = new DecodeThread(this, decodeFormats, characterSet);
        decodeThread.start();
        state = State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.auto_focus:
                // When one auto focus pass finishes, start another.
                // This is the closest thing to continuous AF.
                // It does seem to hunt a bit, but I'm not sure what else to do.
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                }
                break;
            case R.id.decode_succeeded:
                Log.d(TAG, "Got decode succeeded message");
                state = State.SUCCESS;
                if (activityName.equals(Extras.CAPTURE_ACTIVITY)) {
                    activity.handleDecode((Result) message.obj);
                }
                if (activityName.equals(Extras.CAPTURESCAN_ACTIVITY)) {
                    scanActivity.handleDecode((Result) message.obj);
                }
                break;
            case R.id.decode_failed:
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
    }
}
