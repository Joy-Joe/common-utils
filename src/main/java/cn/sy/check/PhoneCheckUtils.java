package cn.sy.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author joy joe
 * @date 2021/12/29 下午9:34
 * @DESC TODO
 */
public class PhoneCheckUtils {
    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆号码/香港号码/固定电话
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isTel(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str) || isChinaTel(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        if (null == str || str.length() <= 0) {
            return Boolean.FALSE;
        }
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        if (null == str || str.length() <= 0) {
            return Boolean.FALSE;
        }
        String regExp = "^(1|2|3|4|5|6|7|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isChinaTel(String str) throws PatternSyntaxException {
        if (null == str || str.length() <= 0) {
            return Boolean.FALSE;
        }
        String regExp = "^(\\d{3,4}-)\\d{7,9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 400固话【18-8-13更新】
     * 如4008208820,4008-208-820
     */
    public static boolean isFourZeroZeroTel(String str) throws PatternSyntaxException {
        if (null == str || str.length() <= 0) {
            return Boolean.FALSE;
        }

        String regExp = "^(400\\d{1}\\d{3}\\d{3})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
