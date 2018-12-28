package cn.gmuni.sc.module.more.express.decoding;

import java.util.Vector;

/**
 * @Author: zhuxin
 * @Date: 2018/9/28 11:06
 * @Description:
 */
public class DecodeFormatManager {
    public static final Vector<BarcodeFormat> PRODUCT_FORMATS;
    public static final Vector<BarcodeFormat> ONE_D_FORMATS;
    public static final Vector<BarcodeFormat> QR_CODE_FORMATS;
    public static final Vector<BarcodeFormat> DATA_MATRIX_FORMATS;

    static {
        PRODUCT_FORMATS = new Vector<>(5);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
        PRODUCT_FORMATS.add(BarcodeFormat.RSS_14);
        ONE_D_FORMATS = new Vector<>(PRODUCT_FORMATS.size() + 4);
        ONE_D_FORMATS.addAll(PRODUCT_FORMATS); //此处将PRODUCT_FORMATS中添加的码加入到ONE_D_FORMATS
        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
        ONE_D_FORMATS.add(BarcodeFormat.ITF);
        QR_CODE_FORMATS = new Vector<>(1);//QR_CODE即二维码
        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
        DATA_MATRIX_FORMATS = new Vector<>(1);
        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);//也属于一种二维码
    }

    private DecodeFormatManager() {
    }
}
