package com.zxk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化类
 *
 * @author wangyunfeng
 */
public class DateUtil {
    public static Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);//属性很多也有月等等，可以操作各种时间日期
        return c.getTime();
    }

    public static String formatToSecond(Date date) {
        java.text.DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    public static String formatToDay(Date date) {
        java.text.DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }
    public static String formatToDay(Date date,String pattern) {
        java.text.DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatToMinute(Date date) {
        java.text.DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        return format.format(date);
    }

    public static String formatToMonth(Date date) {
        java.text.DateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format(date);
    }

    public static String formatToHour(Date date) {
        java.text.DateFormat format = new SimpleDateFormat("yyyyMMddHH");
        return format.format(date);
    }

    public static String formatToYear(Date date) {
        java.text.DateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public static Date stringSecondToDate(String str) {
        return str2Date(str, "yyyyMMddHHmmss");
    }

    public static Date stringDateToDate(String str) {
        return str2Date(str, "yyyyMMdd");
    }

    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);//属性很多也有月等等，可以操作各种时间日期
        return c.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minutes);//属性很多也有月等等，可以操作各种时间日期
        return c.getTime();
    }

    /**
     * 字符串转时间
     *
     * @param date 字符串时间
     * @return
     */
    public static Date str2Date(String date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return str2Date(date, pattern);
    }

    /**
     * 字符串转时间
     *
     * @param date    字符串时间
     * @param pattern 时间格式
     * @return
     */
    public static Date str2Date(String date, String pattern) {
    		if(null == date){
    			return null;
    		}
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 得到昨日日期
     *
     * @param date 日期
     * @return Date
     */
    public static Date getYesterdayForDay(Date date) {
        return stringDateToDate(formatToDay(addDay(date, -1)));
    }

    /**
     * 把时间戳转化成str
     *
     * @param time
     * @return
     */
    public static String timesTamp2Str(Long time, String pattern) {
        if (time == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(time);
    }

    /**
     * 把时间戳转化成str
     *
     * @param time
     * @return
     */
    public static Date timesTamp2Date(Long time) {
        if (time == null) {
            return null;
        }
        return new Date(time);
    }
    
	/**
	 * @param date
	 * @return
	 */
	public static String format2Second(Date date) {
		return formatToSecond(date, null);
	}

    /**
     * 重载formatToSecond方法
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatToSecond(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        java.text.DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    
    /**      
	 * @Description: 当天最大时间戳  
	 * @param  date 时间字符串
	 * @param  offset 偏移量  eg:-1 向前推一天
	 * @return long  时间戳
	 * @throws   
	 */
	public static long getMaxDateOfDay(String date, int offset) {
		if (date == null || date.equals("")) {
			return 0l;
		} else {
			Date cdate = str2Date(date, "yyyy-MM-dd");
			if (null == cdate)
				return 0l;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(cdate);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + offset);
			calendar.set(11, calendar.getActualMaximum(11));
			calendar.set(12, calendar.getActualMaximum(12));
			calendar.set(13, calendar.getActualMaximum(13));
			calendar.set(14, calendar.getActualMaximum(14));
			return calendar.getTimeInMillis();
		}
	}
	
	/**      
	 * @Description: 当天最小时间戳  
	 * @param  date 时间字符串
	 * @param  offset 偏移量  eg:-1 向前推一天
	 * @return long  时间戳
	 * @throws   
	 */
	public static long getMinDateOfDay(String date, int offset) {
		if (date == null || date.equals("")) {
			return 0l;
		} else {
			Date cdate = str2Date(date, "yyyy-MM-dd");
			if(null == cdate)
				return 0l;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(cdate);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + offset);
			calendar.set(11, calendar.getActualMinimum(11));
			calendar.set(12, calendar.getActualMinimum(12));
			calendar.set(13, calendar.getActualMinimum(13));
			calendar.set(14, calendar.getActualMinimum(14));
			return calendar.getTimeInMillis();
		}
	}
	
	/**
	 * 获取某天的 最大时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMaxDateOfDay(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(11, calendar.getActualMaximum(11));
			calendar.set(12, calendar.getActualMaximum(12));
			calendar.set(13, calendar.getActualMaximum(13));
			calendar.set(14, calendar.getActualMaximum(14));
			return calendar.getTime();
		}
	}
	
	/**
	 * 获取某天的 最小时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMinDateOfDay(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(11, calendar.getActualMinimum(11));
			calendar.set(12, calendar.getActualMinimum(12));
			calendar.set(13, calendar.getActualMinimum(13));
			calendar.set(14, calendar.getActualMinimum(14));
			return calendar.getTime();
		}
	}
	
	
	/**      
	 * @Description: 获取当前时间的小时  
	 * @return int  当前时间小时
	 * @throws   
	 */
	public static int getCurrentHours() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	

}

