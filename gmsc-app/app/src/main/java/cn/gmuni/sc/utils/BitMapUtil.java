package cn.gmuni.sc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitMapUtil {
    /**
     *
     * @Title: bitmapToBase64
     * @Description: TODO(Bitmap 转换为字符串)
     * @param @param bitmap
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        // 要返回的字符串
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reslut;
    }
    /**
     *
     * @Title: base64ToBitmap
     * @Description: (base64转换为Bitmap)
     * @param @param base64String
     * @param @return    设定文件
     * @return Bitmap    返回类型
     * @throws
     */
    public static Bitmap base64ToBitmap(String base64String){
        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }

    /**
     * 缩放bitmap
     * @param bitmap
     * @param wScale 宽度缩放比例
     * @param hScale 高度缩放比例
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap,float wScale,float hScale){
        Matrix matrix = new Matrix();
        matrix.postScale(wScale,hScale);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    /**
     * 等比例缩放
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap,float scale){
        return scaleBitmap(bitmap,scale,scale);
    }

    /**
     *裁剪图片
     * @param bitmap 原图
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * @param retX 左上角x坐标
     * @param retY 左上角y坐标
     * @return
     */
    public static Bitmap cropBitmap(Bitmap bitmap,float width,float height,int retX,int retY){
        Bitmap bmp = Bitmap.createBitmap(bitmap,retX,retY,(int)width,(int)height,null,true);
        return bmp;
    }

    //修改bitmap的宽高
    public static Bitmap setImgSize(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static Bitmap view2Bitmap(final View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }
}
