package com.asiainfo.omm.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author oswin
 * 
 */
public class DateUtil {
	// 默认日期格式
	// 默认时间格式
	public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	// 时间格式化
	private static DateFormat dateTimeFormat = null;

	private static DateFormat timeFormat = null;

	static {
		dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
	}

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormat(Date date) {
		return dateTimeFormat.format(date);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getTimeFormat(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param 格式类型
	 * @return
	 */
	public static String getDateFormat(Date date, String formatStr) {
		if (StringUtils.isNotBlank(formatStr)) {
			return new SimpleDateFormat(formatStr).format(date);
		}
		return null;
	}


	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateTimeFormat(String date) {
		try {
			return dateTimeFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 获取当前日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNowDate() {
		return new Date();
	}
	
	/**
	 * 获取当前 Timestamp
	 * @return
	 */
	public static Timestamp getNowTimestamp(){
		return new Timestamp(getNowDate().getTime());
	}
	
	/**
	 * 获取当前 Timestamp(yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static Timestamp getTimestamp(String dateStr){
		return new Timestamp(getDateTimeFormat(dateStr).getTime());
	}

	/**
	 * 获取 Timestamp
	 * @param dateStr
	 * @param formatStr 格式
	 * @return
	 */
	public static Timestamp getTimestamp(String dateStr, String formatStr){
		try {
			return new Timestamp(new SimpleDateFormat(formatStr).parse(dateStr).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return new Timestamp(new Date().getTime());
		}
	}
}