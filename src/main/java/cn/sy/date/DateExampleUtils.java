package cn.sy.date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author joy joe
 * @date 2021/12/24 下午9:40
 * @DESC 日期相关的样例
 */
public class DateExampleUtils {
    private final static String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    private final static String YYYYMMDD = "yyyyMMdd";
    private final static String YYYY_MM_DD = "yyyy-MM-dd";

    public static void main(String[] args) throws ParseException {
        //获取当天凌晨0点的时间 2021-12-29 00:00:00

        String sTime2 = DateFormatUtils.format(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), YYYYMMDD_HHMMSS);
        //获取第二天凌晨0点的时间 String：2021-12-30 00:00:00
        String endTime = DateFormatUtils.format(DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), 1), YYYYMMDD_HHMMSS);

        //有效期到第二天凌晨0点 Date类型
        Date seCondDate = DateUtils.truncate(DateUtils.addDays(new Date(), 1), Calendar.DAY_OF_MONTH);
        //此时到第二天距离的秒数
        int timeout = (int) ((seCondDate.getTime() - new Date().getTime()) / 1000);

    }
}
