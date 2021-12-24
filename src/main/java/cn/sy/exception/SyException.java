package cn.sy.exception;

/**
 * @author joy joe
 * @date 2021/11/28 下午4:05
 * @DESC
 */
public class SyException extends RuntimeException {
    private String errorCode;
    public SyException(String msg) {
        super(msg);
    }

    public SyException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
