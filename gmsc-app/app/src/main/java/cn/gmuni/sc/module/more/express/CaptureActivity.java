package cn.gmuni.sc.module.more.express;


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
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.module.home.scan.CaptureScanActivity;
import cn.gmuni.sc.module.more.express.binding.Bind;
import cn.gmuni.sc.module.more.express.binding.ViewBinder;
import cn.gmuni.sc.module.more.express.callback.Callback;
import cn.gmuni.sc.module.more.express.camera.CameraManager;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.module.more.express.decoding.BarcodeFormat;
import cn.gmuni.sc.module.more.express.decoding.CaptureActivityHandler;
import cn.gmuni.sc.module.more.express.decoding.DecodeFile;
import cn.gmuni.sc.module.more.express.decoding.DecodeFormatManager;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IExpressQueryApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.CustomToolbar;

public class CaptureActivity extends BaseCommonActivity implements SurfaceHolder.Callback {


    @BindView(R.id.express_capture_flashlight)
    CustomImageView express_capture_flashlight;
    @Bind(R.id.express_capture_preview_view)
    SurfaceView express_capture_preview_view;
    @BindView(R.id.express_capture_toolbar)
    CustomToolbar express_capture_toolbar;

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

    private Map<String, CompanyModel> companyMap = new HashMap<>();

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_capture);
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
        express_capture_toolbar.setOnActionListener(new MyListener()); //如果右侧有操作按钮，则给操作按钮绑定事件
    }

    @Override
    public int getToolbar() {
        return R.id.express_capture_toolbar;
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
        if (handler == null) {
            CaptureScanActivity captureScanActivity = new CaptureScanActivity();
            handler = new CaptureActivityHandler(captureScanActivity, this, decodeFormats, characterSet,Extras.CAPTURE_ACTIVITY);
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
            handleScanResult(result.getText());
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


    private void handleScanResult(final String result) {
        if (isBarcode) {
            Intent data = new Intent();
            data.putExtra(Extras.SCAN_RESULT, result);
            setResult(RESULT_OK, data);
            finish();
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.activity_capture_dialog, null);
            TextView tvResult = (TextView) view.findViewById(R.id.express_capture_result);
            tvResult.setText(result);
            tvResult.setAutoLinkMask(Linkify.ALL);
            tvResult.setMovementMethod(LinkMovementMethod.getInstance());
            //扫码后跳转快递初始页面
          /*  Intent intent = new Intent(getContext(),ExpressQueryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("expNo",result);
            intent.putExtras(bundle);
            startActivity(intent);*/

            //直接跳转至结果页
            boolean reback = result.matches("[0-9]+"); //判断字符串
            if (reback) {
                readCompany(); //读取物流公司列表
                Map<String, String> map = new HashMap<>();
                map.put("expNo", result);
                getData(map);
            } else {
                //跳转网页
                Intent intent = new Intent(getContext(), BaseWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", result);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        /*    new AlertDialog.Builder(this)
                    .setTitle("扫描结果")
                    .setView(view)
                    .setPositiveButton("复制文本", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            cmb.setText(result);
                            SnackbarUtils.show(CaptureActivity.this, "复制成功");
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (handler != null) {
                                // 继续扫描
                                handler.restartPreviewAndDecode();
                            }
                        }
                    })
                    .show();*/
        }
    }

    //获取单号对应物流公司信息
    private void getData(Map<String, String> map) {
        Network.request(Network.createApi(IExpressQueryApi.class).expressByExpNo(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                List list = removeDuplicate(data);
                if (list.size() != 0) {
                    CompanyModel companyModel = handleData(list);
                    Intent intent = new Intent(getContext(), ExpressResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("hisCache", Extras.CAPTURE_CACHE);
                    bundle.putString("expNo", map.get("expNo"));
                    bundle.putSerializable("company", companyModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    CompanyModel companyModel = new CompanyModel();
                    Intent intent = new Intent(getContext(), ExpressResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("hisCache", Extras.CAPTURE_CACHE);
                    bundle.putString("expNo", map.get("expNo"));
                    bundle.putSerializable("company", companyModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });
    }


    //物流公司去重
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    //处理数据
    private CompanyModel handleData(List<Map<String, String>> data) {
        CompanyModel companyModel = new CompanyModel();
        if (data.size() != 0) {
            Map<String, String> map = data.get(0);
            companyModel.setName(map.get("ShipperName"));
            companyModel.setCode(map.get("ShipperCode"));
            CompanyModel companyModel1 = companyMap.get(map.get("ShipperCode"));
            if (companyModel1 != null) {
                companyModel.setLogo(companyModel1.getLogo());
            }
        }
        return companyModel;

    }

    //读取物流公司信息
    private void readCompany() {
        try {
            InputStream is = getAssets().open("company.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(json).getAsJsonArray();
            for (JsonElement obj : jArray) {
                CompanyModel company = gson.fromJson(obj, CompanyModel.class);
                if (!TextUtils.isEmpty(company.getCode())) {
                    companyMap.put(company.getCode(), company);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class MyListener extends OnMultiClickListener {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.express_capture_flashlight:
                    express_capture_flashlight.setSelected(CameraManager.get().setFlashlight());
                    break;
                case R.id.express_capture_toolbar:
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
                    handleScanResult(result.getText());
                } else {
                    new AlertDialog.Builder(CaptureActivity.this)
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
