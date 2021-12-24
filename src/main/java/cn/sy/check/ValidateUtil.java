package cn.sy.check;


import cn.sy.exception.SyException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author
 * @Date 2021/12/2
 * @Desc 参数校验
 */
public class ValidateUtil {

    public static void assertEmpty(String value, String errorCode, String errorDesc) {
        if (StringUtils.isBlank(value)) {
            throw new SyException(errorCode, errorDesc);
        }
    }

    public static void assertEmptyNull(String value, String errorCode, String errorDesc) {
        if (StringUtils.isBlank(value)||"null".equalsIgnoreCase(value.trim())) {
            throw new SyException(errorCode, errorDesc);
        }
    }

    public static void assertLength(String value, int maxLength, String errorCode, String errorDesc) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        if(value.length()>maxLength){
            throw new SyException(errorCode, errorDesc);
        }
    }

    public static void assertValueEqual(String value1, String value2, String errorCode, String errorDesc){
        if (!StringUtils.equals(value1,value2)){
            throw new SyException(errorCode, errorDesc);
        }
    }

    public static void assertContains(String strAll, String str, String errorCode, String errorDesc){
        if(StringUtils.isEmpty(strAll)){
            return;
        }

        if(strAll.contains(str)){
            throw new SyException(errorCode, errorDesc);
        }
    }

    public static void assertObjectNull(Object value, String errorCode, String errorDesc) {
        if (value == null) {
            throw new SyException(errorCode, errorDesc);
        }
    }

}
