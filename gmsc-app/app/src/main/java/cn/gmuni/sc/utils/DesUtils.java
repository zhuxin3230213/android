package cn.gmuni.sc.utils;

//import org.apache.tomcat.util.codec.binary.Base64;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;




public class DesUtils {

    private static final String CODE_TYPE = "UTF-8";


    public static String encode(String datasource, String key) {
        try {
            key = processSalt(key);
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes(CODE_TYPE));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, random);
            return Base64.encodeToString(cipher.doFinal(datasource.getBytes()),Base64.DEFAULT).trim();
        } catch (Throwable var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        System.out.println(encode("11111","1111"));
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return String
     */
    private static String byte2String(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase(Locale.CHINA);
    }


    private static String processSalt(String salt) {
        if (salt.length() >= 16) {
            return salt.substring(0, 16);
        }
        for (int i = salt.length(); i < 16; i++) {
            salt += "1";
        }
        return salt;
    }

    /**
     * 解密
     * @param src 解密对象
     * @param key 盐
     * @return
     */
    public static String decode(String src, String key) {
        // DES算法要求有一个可信任的随机数源
        try {
            key = processSalt(key);
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(key.getBytes(CODE_TYPE));
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作

            return new String(cipher.doFinal(Base64.decode(src,Base64.DEFAULT)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
