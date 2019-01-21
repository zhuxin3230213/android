package cn.gmuni.sc.module.more.market;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.model.MarketModel;
import cn.gmuni.sc.module.me.me_market.MeMarketActivity;
import cn.gmuni.sc.module.more.market.marketutil.GlideLoader;
import cn.gmuni.sc.module.more.market.marketutil.SlideMenuCode;
import cn.gmuni.sc.network.api.IMarketApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.StringUtil;

public class MarketPublishActivity extends BaseCommonActivity {

    @BindView(R.id.market_publish_title)
    EditText market_publish_title; //标题
    @BindView(R.id.market_publish_introduce_edit)
    EditText market_publish_introduce_edit; //简介
    @BindView(R.id.market_publish_pic_img)
    ImageView market_publish_pic_img; //拍照或选择照片
    @BindView(R.id.market_publish_price_edit)
    EditText market_publish_price_edit; //出售的价格
    @BindView(R.id.radioButton_books)
    RadioButton radioButton_books; //书籍
    @BindView(R.id.radioButton_supplier)
    RadioButton radioButton_supplier; //学习用品
    @BindView(R.id.radioButton_daily)
    RadioButton radioButton_daily; //生活用品
    @BindView(R.id.radioButton_3c)
    RadioButton radioButton_3c; //3c数码
    @BindView(R.id.radioButton_other)
    RadioButton radioButton_other; //其他

    @BindView(R.id.market_publish_pic_layout)
    LinearLayout market_publish_pic_layout; //图片布局器

    private boolean isPublishing = false; //检验发布情况
    String marketType = SlideMenuCode.BOOKS; //发布物品类型
    private Uri imgUri;
    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    public final static int RESULT_CODE = 3; //发布成功返回列表页
    private Bitmap bmp;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_market_publish);
    }

    @Override
    public int getToolbar() {
        return R.id.market_publish_toolbar;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String type = getIntent().getStringExtra("type");
        if ("editor".equals(type)) {
            //编辑
            MarketModel marketModel = (MarketModel) getIntent().getSerializableExtra("marketModel");
            market_publish_title.setText(marketModel.getTitle());
            market_publish_introduce_edit.setText(marketModel.getIntroduce());
            if (!StringUtil.isEmpty(marketModel.getImgS())) {
                Bitmap snapshotBm = BitMapUtil.base64ToBitmap(marketModel.getImgS());
                market_publish_pic_img.setImageBitmap(snapshotBm);
            }
            market_publish_price_edit.setText(marketModel.getPrice());
            selectType(marketModel.getType()); //选择类型
            marketModel.setUpdateTime(null);
            bindEditorEvent(marketModel); //编辑事件
        } else {
            bindPublishEvent(); //发布
        }
        bindEvent(); //通用事件
    }

    //选择类型
    private void selectType(String type) {
        switch (type) {
            case SlideMenuCode.BOOKS:
                break;
            case SlideMenuCode.SUPPLIES:
                radioButton_books.setChecked(false);
                radioButton_supplier.setChecked(true);
                break;
            case SlideMenuCode.DAILY:
                radioButton_books.setChecked(false);
                radioButton_daily.setChecked(true);
                break;
            case SlideMenuCode.DIGITAL:
                radioButton_books.setChecked(false);
                radioButton_3c.setChecked(true);
                break;
            case SlideMenuCode.OTHER:
                radioButton_books.setChecked(false);
                radioButton_other.setChecked(true);
                break;
        }
    }

    //修改编辑事件
    private void bindEditorEvent(MarketModel marketModel) {
        //nav中发布按钮
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isPublishing) {
                    MToast.showLongToast("亲，你的信息正在发布中，请不要重复点击哟。");
                    return;
                }
                if (checkEdiTextAndGetValue(marketModel)) {
                    showLoading();
                    cn.gmuni.sc.network.Network.request(cn.gmuni.sc.network.Network.createApi(IMarketApi.class).update(marketModel), new cn.gmuni.sc.network.Network.IResponseListener<MarketModel>() {
                        @Override
                        public void onSuccess(MarketModel data) {
                            isPublishing = true;
                            hideLoading();
                            MToast.showLongToast("发布成功");
                            destoryActivity(); //销毁指定activity
                            startActivity(new Intent(getContext(), MeMarketActivity.class));
                        }

                        @Override
                        public void onFail(int code, String message) {
                            isPublishing = true;
                            MToast.showLongToast("发布失败");
                            hideLoading();
                        }
                    });
                }

            }
        });
    }

    //发布事件
    private void bindPublishEvent() {
        //nav中发布按钮
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isPublishing) {
                    MToast.showLongToast("亲，你的信息正在发布中，请不要重复点击哟。");
                    return;
                }
                MarketModel marketModel = new MarketModel();
                if (checkEdiTextAndGetValue(marketModel)) {
                    showLoading();
                    cn.gmuni.sc.network.Network.request(cn.gmuni.sc.network.Network.createApi(IMarketApi.class).add(marketModel), new cn.gmuni.sc.network.Network.IResponseListener<MarketModel>() {
                        @Override
                        public void onSuccess(MarketModel data) {
                            isPublishing = true;
                            hideLoading();
                            MToast.showLongToast("发布成功");
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("marketModel", data);
                            intent.putExtras(bundle);
                            setResult(RESULT_CODE, intent);
                            finish();
                            destoryActivity(); //销毁指定activity
                        }

                        @Override
                        public void onFail(int code, String message) {
                            isPublishing = true;
                            MToast.showLongToast("发布失败");
                            hideLoading();
                        }
                    });
                }

            }
        });
    }

    //绑定事件
    private void bindEvent() {
        //设置类型改变的事件
        radioButton_books.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (radioButton_books.isChecked()) {
                    radioButton_supplier.setChecked(false);
                    radioButton_daily.setChecked(false);
                    radioButton_3c.setChecked(false);
                    radioButton_other.setChecked(false);
                    marketType = SlideMenuCode.BOOKS;
                }
            }
        });
        radioButton_supplier.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (radioButton_supplier.isChecked()) {
                    radioButton_books.setChecked(false);
                    radioButton_daily.setChecked(false);
                    radioButton_3c.setChecked(false);
                    radioButton_other.setChecked(false);
                    marketType = SlideMenuCode.SUPPLIES;
                }

            }
        });
        radioButton_daily.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (radioButton_daily.isChecked()) {
                    radioButton_books.setChecked(false);
                    radioButton_supplier.setChecked(false);
                    radioButton_3c.setChecked(false);
                    radioButton_other.setChecked(false);
                    marketType = SlideMenuCode.DAILY;
                }

            }
        });
        radioButton_3c.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (radioButton_3c.isChecked()) {
                    radioButton_books.setChecked(false);
                    radioButton_supplier.setChecked(false);
                    radioButton_daily.setChecked(false);
                    radioButton_other.setChecked(false);
                    marketType = SlideMenuCode.DIGITAL;
                }

            }
        });
        radioButton_other.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (radioButton_other.isChecked()) {
                    radioButton_books.setChecked(false);
                    radioButton_supplier.setChecked(false);
                    radioButton_daily.setChecked(false);
                    radioButton_3c.setChecked(false);
                    marketType = SlideMenuCode.OTHER;
                }

            }
        });
        //相机
        market_publish_pic_img.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(new String[]{"图库", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // 调用图库
                                        requestPermissions(new String[]{PermissionConst.PERMISSION_WRITE_EXTERNAL_STORAGE}, new OnPermissionListener() {
                                            @Override
                                            public void onReceive() {
                                                Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                                getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                                getImageByalbum.setType("image/jpeg");
                                                startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                                //多选照片
                                               /* Intent intent = new Intent(getContext(),ImageSelectorActivity.class);
                                                startActivityForResult(intent,ImageSelector.IMAGE_REQUEST_CODE);*/
                                            }
                                        });
                                        break;
                                    case 1:
                                        //拍摄照片
                                        requestPermissions(new String[]{PermissionConst.PERMISSION_WRITE_EXTERNAL_STORAGE, PermissionConst.PERMISSION_CAMERA}, new OnPermissionListener() {
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
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

    }

    //获取编辑内容项，并检验必填项
    private Boolean checkEdiTextAndGetValue(MarketModel marketModel) {
        //标题
        String title = market_publish_title.getText().toString();
        if (isEmptyStr(title)) {
            MToast.showLongToast("请输入标题");
            return false;
        }
        marketModel.setTitle(title);
        //简介
        String introduce = market_publish_introduce_edit.getText().toString();
        if (isEmptyStr(introduce)) {
            MToast.showLongToast("请输入简介");
            return false;
        }
        marketModel.setIntroduce(introduce);
        //价格
        String price = market_publish_price_edit.getText().toString();
        if (isEmptyStr(price)) {
            MToast.showLongToast("请输入价格");
            return false;
        }
        marketModel.setPrice(price);
        //类别
        marketModel.setType(marketType);
        //图片
        if (null != bmp || "".equals(bmp)) {
            marketModel.setImgS(BitMapUtil.bitmapToBase64(compressBitMap(bmp, 32)));
            marketModel.setImgB(BitMapUtil.bitmapToBase64(compressBitMap(bmp, 256)));
        }
        return true;
    }

    //检验输入字符串是否为空
    private Boolean isEmptyStr(String str) {
        return null == str || str.length() == 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
          /*  if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                ArrayList<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                List<Bitmap> list = new ArrayList<>();
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(65,
                        65);//两个400分别为添加图片的大小
                imageView.setLayoutParams(params);

                for (String parth:pathList){
                    Bitmap bitmap = BitmapFactory.decodeFile(parth);
                    Bitmap bitmap1 = compressBitMap(bitmap, 32);
                    list.add(bitmap1);
                    imageView.setImageBitmap(bitmap1);
                    market_publish_pic_layout.addView(imageView);
                }

            }*/
            if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
                Uri mPath = data.getData();
                String file = getPath(mPath);
                bmp = BitmapFactory.decodeFile(file);
                market_publish_pic_img.setImageBitmap(compressBitMap(bmp, 32));
            } else if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
                Uri mPath = imgUri;
                String file = getPath(mPath);
                bmp = BitmapFactory.decodeFile(file);
                market_publish_pic_img.setImageBitmap(compressBitMap(bmp, 32));
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

    //销毁指定activity
    public static void destoryActivity() {
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(), "MarketActivity");//添加到销毁队列
        BaseApplication.destoryActivity("MarketActivity"); //销毁指定的activity
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(), "MeMarketActivity");//添加到销毁队列
        BaseApplication.destoryActivity("MeMarketActivity"); //销毁指定的activity
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(), "MarketPublishActivity");//添加到销毁队列
        BaseApplication.destoryActivity("MarketPublishActivity"); //销毁指定的activity
    }

    @Override
    public void finish() {
        bmp = null;
        super.finish();
    }


    //图库选择器
    private void setImageConfig() {
        ImageConfig imageConfig
                = new ImageConfig.Builder(this, new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）\
                .steepToolBarColor(getResources().getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.blue))
                // 提交按钮字体的颜色 （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选 （默认为多选） (单选 为 singleSelect)
                .mutiSelect()
                // 多选时的最大数量 （默认 9 张）
                .mutiSelectMaxSize(9)
                // 已选择的图片路径
                .pathList(null)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认关闭）
                .showCamera().build();

        ImageSelector.open(imageConfig); // 开启图片选择器
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
