package cn.gmuni.sc.module.lost;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILostFoundApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.widget.CustomDatePicker;

public class LostFoundPublishActivity extends BaseCommonActivity {

    @BindView(R.id.lost_found_publish_lost_btn)
    ImageView lostBtn;

    @BindView(R.id.lost_found_publish_found_btn)
    ImageView foundBtn;
    @BindView(R.id.lost_found_publish_pic_img)
    ImageView img;
    @BindView(R.id.lost_found_publish_name_edit)
    EditText nameEdit;

    @BindView(R.id.lost_found_publish_address_edit)
    EditText addressEdit;

    @BindView(R.id.lost_found_publish_time_edit)
    EditText timeEdit;

    @BindView(R.id.lost_found_publish_desc_edit)
    EditText descEdit;

    private Uri imgUri;
    Context context = null;

    private boolean isPublishing = false;

    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    public final static int RESULT_CODE = 3;
    String lostFoundType = "0";

    private CustomDatePicker customDatePicker;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lost_found_publish);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        context = LostFoundPublishActivity.this;
//        getPermissoin();
        initDatePicker();
        bindEvent();
    }

    private void initDatePicker() {
        String now = sdf.format(new Date());
        timeEdit.setText(now);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                timeEdit.setText(time);
            }
        }, "1970-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }
    private void bindEvent() {
        lostBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                lostFoundType = "0";
                lostBtn.setBackgroundResource(R.drawable.image_loat);
                foundBtn.setBackgroundResource(R.drawable.jiandao);
            }
        });
        foundBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                lostFoundType = "1";
                lostBtn.setBackgroundResource(R.drawable.diushi);
                foundBtn.setBackgroundResource(R.drawable.image_found);
            }
        });
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if(isPublishing){
                    Toast.makeText(context, "亲，你的信息正在发布中，请不要重复点击哟。", Toast.LENGTH_LONG).show();
                    return;
                }
                LostFoundModel lost = new LostFoundModel();
                if (checkTextEditAndGetValue(lost)) {
                    showLoading();
                    Network.request(Network.createApi(ILostFoundApi.class).add(lost), new Network.IResponseListener<LostFoundModel>() {
                        @Override
                        public void onSuccess(LostFoundModel data) {
                            isPublishing = true;
                            hideLoading();
                            Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("lost_found", data);
                            intent.putExtras(bundle);
                            setResult(RESULT_CODE, intent);
                            finish();
                        }

                        @Override
                        public void onFail(int code, String message) {
                            isPublishing = true;
                            Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT).show();
                            hideLoading();
                        }
                    });
                }
            }
        });
        timeEdit.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                customDatePicker.show(timeEdit.getText().toString());
            }
        });
        img.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 1:
                                        requestPermissions(new String[]{PermissionConst.PERMISSION_WRITE_EXTERNAL_STORAGE,PermissionConst.PERMISSION_CAMERA}, new OnPermissionListener() {
                                            @Override
                                            public void onReceive() {
                                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                                ContentValues values = new ContentValues(1);
                                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                                imgUri = uri;
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                                startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                            }
                                        });
                                        break;
                                    case 0:
                                        requestPermissions(new String[]{PermissionConst.PERMISSION_WRITE_EXTERNAL_STORAGE}, new OnPermissionListener() {
                                            @Override
                                            public void onReceive() {
                                                Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                                getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                                getImageByalbum.setType("image/jpeg");
                                                startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                            }
                                        });

                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private boolean checkTextEditAndGetValue(LostFoundModel lost) {
        String name = nameEdit.getText().toString();
        if (isEmptyStr(name)) {
            Toast.makeText(context, "请输入物品名称", Toast.LENGTH_LONG).show();
            return false;
        }
        lost.setObjName(name);
        String address = addressEdit.getText().toString();
        if (isEmptyStr(address)) {
            Toast.makeText(context, "请输入地址", Toast.LENGTH_LONG).show();
            return false;
        }
        lost.setObjAddress(address);
        String time = timeEdit.getText().toString();
        if(isEmptyStr(time)){
            Toast.makeText(context, "请输入大致时间", Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            lost.setObjTime(sdf.parse(time));
        }catch (Exception e){
            lost.setObjTime(new Date());
        }
        lost.setLfType(lostFoundType);
        String desc = descEdit.getText().toString();
        if(isEmptyStr(desc)){
            Toast.makeText(context, "请输入描述信息", Toast.LENGTH_LONG).show();
            return false;
        }
        lost.setDescription(desc);
        if (null != bmp) {
            lost.setSnapshot(BitMapUtil.bitmapToBase64(compressBitMap(bmp, 32)));
            lost.setImage(BitMapUtil.bitmapToBase64(compressBitMap(bmp, 256)));
        }
        return true;
    }

    private boolean isEmptyStr(String str) {
        return null == str || str.length() == 0;
    }

    @Override
    public int getToolbar() {
        return R.id.lost_found_publish_toolbar;
    }

    Bitmap bmp;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
                Uri mPath = data.getData();
                String file = getPath(mPath);
                bmp = BitmapFactory.decodeFile(file);
                img.setImageBitmap(compressBitMap(bmp, 32));
            } else if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
                Uri mPath = imgUri;
                String file = getPath(mPath);
                bmp = BitmapFactory.decodeFile(file);
                img.setImageBitmap(compressBitMap(bmp, 32));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap compressBitMap(Bitmap bmp, int maxSize) {
        // 首先进行一次大范围的压缩
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        float zoom = (float) Math.sqrt(1024 * 1024 / (float) output.toByteArray().length); //获取缩放比例
        // 设置矩阵数据
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);
        // 根据矩阵数据进行新bitmap的创建
        Bitmap resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        output.reset();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        // 如果进行了上面的压缩后，依旧大于maxSize*K，就进行小范围的微调压缩
        while (output.toByteArray().length > maxSize * 1024) {
            matrix.setScale(0.9f, 0.9f);//每次缩小 1/10
            resultBitmap = Bitmap.createBitmap(
                    resultBitmap, 0, 0,
                    resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);
            output.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }
        return BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.toByteArray().length);
    }

    @Override
    public void finish() {
        bmp = null;
        super.finish();
    }

    /**
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
