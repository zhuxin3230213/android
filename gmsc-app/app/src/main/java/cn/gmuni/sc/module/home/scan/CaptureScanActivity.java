package cn.gmuni.sc.module.home.scan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.module.home.scan.qr_manager.QrcodeManager;
import cn.gmuni.sc.module.more.express.CaptureActivity;
import cn.gmuni.sc.module.more.express.binding.Bind;
import cn.gmuni.sc.module.more.express.binding.ViewBinder;
import cn.gmuni.sc.module.more.express.callback.Callback;
import cn.gmuni.sc.module.more.express.camera.CameraManager;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.module.more.express.decoding.BarcodeFormat;
import cn.gmuni.sc.module.more.express.decoding.CaptureActivityHandler;
import cn.gmuni.sc.module.more.express.decoding.DecodeFile;
import cn.gmuni.sc.module.more.express.decoding.DecodeFormatManager;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.CustomToolbar;

public class CaptureScanActivity extends BaseCommonActivity implements SurfaceHolder.Callback {


    @BindView(R.id.express_capture_flashlight)
    CustomImageView express_capture_flashlight;
    @Bind(R.id.express_capture_preview_view)
    SurfaceView express_capture_preview_view;
    @BindView(R.id.capture_scan_toolbar)
    CustomToolbar capture_scan_toolbar;

    private static final boolean PLAY_BEEP = true;
    private static final boolean VIBRATE = false;
    private static final long VIBRATE_DURATION = 200L;
    private static final float BEEP_VOLUME = 0.1f;
    private static final int REQUEST_ALBUM = 0;


    private boolean isBarcode;
    private CaptureActivityHandler handler;
    private boolean hasSurface = false;
    private final Vector<BarcodeFormat> decodeFormats = new Vector<>();
    private ProgressDialog progressDialog;
    private final String characterSet = "UTF-8";
    private MediaPlayer mediaPlayer;
    private boolean playBeep;


    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_capture_scan);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        isBarcode = getIntent().getBooleanExtra(Extras.BARCODE, false);
        //初始化编码
        initDecodeFormats();
        CameraManager.init(getApplicationContext());
        CameraManager.get().setBarcode(isBarcode);
        progressDialog = new ProgressDialog(this);
        ViewBinder.bind(this);

        //点击事件
        express_capture_flashlight.setOnClickListener(new MyListener());
        capture_scan_toolbar.setOnActionListener(new MyListener()); //如果右侧有操作按钮，则给操作按钮绑定事件
    }

    @Override
    public int getToolbar() {
        return R.id.capture_scan_toolbar;
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = express_capture_preview_view.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        playBeep = PLAY_BEEP && (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);
        initBeepSound();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            return;
        }
        CaptureActivity captureActivity = new CaptureActivity();
        if (handler == null) {
            handler = new CaptureActivityHandler(this, captureActivity, decodeFormats, characterSet, Extras.CAPTURESCAN_ACTIVITY);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    //初始化编码
    private void initDecodeFormats() {
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        if (!isBarcode) {
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
    }

    /**
     * Handler scan result
     */
    public void handleDecode(Result result) {
        playBeepSoundAndVibrate();

        if (TextUtils.isEmpty(result.getText())) {
            Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String barcodeFormat = result.getBarcodeFormat().toString();
            handleScanResult(result.getText(), barcodeFormat);
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (VIBRATE) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    //处理扫描结果
    private void handleScanResult(final String result, String barcodeFormat) {
        if (isBarcode) {
            Intent data = new Intent();
            data.putExtra(Extras.SCAN_RESULT, result);
            setResult(RESULT_OK, data);
            finish();
        } else {
            QrcodeManager.execute(result, barcodeFormat); //将结果与编码格式传给注册类
        }
    }

    class MyListener extends OnMultiClickListener {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.express_capture_flashlight:
                    express_capture_flashlight.setSelected(CameraManager.get().setFlashlight());
                    break;
                case R.id.capture_scan_toolbar:
                    openAlbum();
                    break;
            }
        }
    }

    private void openAlbum() {
        Intent innerIntent = new Intent();
        innerIntent.setAction(Intent.ACTION_PICK);
        innerIntent.setType("image/*");
        startActivityForResult(innerIntent, REQUEST_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == REQUEST_ALBUM) {
            Uri uri = data.getData();
            decodeFile(uri);
        }
    }

    private void decodeFile(Uri uri) {
        showProgress();
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        hints.put(DecodeHintType.CHARACTER_SET, characterSet);

        DecodeFile.decodeFile(getContentResolver(), uri, hints, new Callback<Result>() {
            @Override
            public void onEvent(Result result) {
                cancelProgress();
                if (result != null && !TextUtils.isEmpty(result.getText())) {
                    handleScanResult(result.getText(), result.getBarcodeFormat().toString());
                } else {
                    new AlertDialog.Builder(CaptureScanActivity.this)
                            .setTitle(R.string.express_capture_tips)
                            .setMessage(R.string.express_capture_analyze_fail)
                            .setPositiveButton(R.string.express_capture_sure, null)
                            .show();
                }
            }
        });
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void cancelProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}
