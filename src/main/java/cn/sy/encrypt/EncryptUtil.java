package cn.sy.encrypt;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密类
 */
@Slf4j
public class EncryptUtil {
    /**
     * 16进制 : 16
     */
    private static final int HEX = 16;
    /**
     * SHA: 0xFF
     */
    private static final int SHA_FF = 0xFF;
    /**
     * SHA: 0x100
     */
    private static final int SHA_100 = 0x100;

    /**
     * SHA（Secure Hash Algorithm，安全散列算法）是消息摘要算法的一种，被广泛认可的MD5算法的继任者。
     * SHA算法家族目前共有SHA-0、SHA-1、SHA-224、SHA-256、SHA-384和SHA-512五种算法，
     * 通常将后四种算法并称为SHA-2算法
     *
     * @param msg  明文
     * @param salt 盐
     * @return 密文
     */
    public static String encryptSHA(final String msg, String salt) {

        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(msg.getBytes());
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & SHA_FF) + SHA_100, HEX).substring(1));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * MD5签名后再base64编码
     *
     * @param signContext 待签名字符串
     * @param appKey      签名key
     * @return
     */
    public static String md5AndBase64Sign(final String signContext, String appKey) {
        if (StringUtils.isBlank(signContext)) {
            return null;
        }
        String context = signContext + appKey;
        try {
            String base64Sign = new String(Base64.encodeBase64(DigestUtils.md5(context.getBytes(StandardCharsets.UTF_8))));
            log.info("请求报文体：{},待签名数据:{},签名后数据:{}", signContext, context, base64Sign);
            return base64Sign;
        } catch (Exception e) {
            log.error("签名出错：签名报文：{}, 签名key:{}", signContext, appKey);
        }
        return null;
    }

    public static String base64Sign(final String signContext, String appKey) {
        if (StringUtils.isBlank(signContext)) {
            return null;
        }
        String context = signContext + appKey;
        try {
            String base64Sign = new String(Base64.encodeBase64((context.getBytes(StandardCharsets.UTF_8))));
            log.info("请求报文体：{},待签名数据:{},签名后数据:{}", signContext, context, base64Sign);
            return base64Sign;
        } catch (Exception e) {
            log.error("签名出错：签名报文：{}, 签名key:{}", signContext, appKey);
        }
        return null;
    }

    public static void main(String[] args) {
        String aaa = "APP_ID=1627454362594&NONCE=8571b3105ad84cb9a7a61c6d73f395e3&TIMESTAMP=20210813111847&bizContent={stationCode=0000676689, noticeRoute=ytoSms, phoneLastPart=3691, mobile=18192483691, logisticsCode=BLC00001, errorMsg=null, limitTime=2021-08-05 21:34:42, replyTime=2021-08-12 21:34:32.473, createTime=2021-08-12 21:32:13, orgCode=999999, id=yjt_0A07108F77ED439F5B3D3D470F101F2D, seriNumber=ys_re_yjt_0A07108F77ED439F5B3D3D470F101F2D, status=DELIVRD, waybillNo=YT1110000156899}&SECRET_KEY=VJJsW9qVuUGPkJFky6V7a1YuZXCRvXRv";
        String signStr = org.springframework.util.DigestUtils.md5DigestAsHex(aaa.toString().getBytes()).toUpperCase();
        System.out.println(signStr);

        Map<String, String> paramm = new HashMap<>();
        paramm.put("APP_ID", "1627454362594");
        paramm.put("NONCE", "8571b3105ad84cb9a7a61c6d73f395e3");
        paramm.put("TIMESTAMP", "20210813111847");
        paramm.put("bizContent", "{stationCode=0000676689, noticeRoute=ytoSms, phoneLastPart=3691, mobile=18192483691, logisticsCode=BLC00001, errorMsg=null, limitTime=2021-08-05 21:34:42, replyTime=2021-08-12 21:34:32.473, createTime=2021-08-12 21:32:13, orgCode=999999, id=yjt_0A07108F77ED439F5B3D3D470F101F2D, seriNumber=ys_re_yjt_0A07108F77ED439F5B3D3D470F101F2D, status=DELIVRD, waybillNo=YT1110000156899}");
        Long s = 1640306294385L;
        String context = "syLuckyMoney" + s;
        byte[] bytes = DigestUtils.md5(context.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(bytes));

    }

}
