package cn.gmuni.sc.utils;

import java.util.regex.Pattern;

public class RegExUtil {

    /**
     * 正则表达式：验证手机号码
     */
    public static final String PHONENUBER_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String EMAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String CHINESE_REGEX = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String ID_CARD_REGEX = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";


    /**
     * 校验正则是否匹配
     * @param text
     * @param regex
     * @return
     */
    public static boolean  test(String text,String regex){
        return Pattern.matches(regex,text);
    }

    public static void main(String[] args){

        System.out.println(test("1810818994",PHONENUBER_REGEX));
    }
}
