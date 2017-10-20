package com.asiainfo.omm.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ai.appframe2.common.ServiceManager;
import com.ai.appframe2.complex.cache.CacheFactory;
import com.ai.appframe2.complex.cache.impl.SysDateCacheImpl;
import com.ai.appframe2.complex.datasource.DataSourceFactory;

public class TimeUtils {
	
	public static String DEAULT_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
	
	public static String getTimeStrDefault(){
		return getTimeStr(new Date(),DEAULT_FORMAT);
	}
	
	public static String getTimeStr(Date date,String format){
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
	
	public static Date getTimeFromStr(String timeStr,String format){
		Date date = null;
		try{
			SimpleDateFormat f = new SimpleDateFormat(format);
			date = f.parse(timeStr);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 获取数据库时间
	 * @return
	 * @throws Exception
	 */
    public static Timestamp getDefaultSysDate() throws Exception {
        Timestamp rtn = null;
        if(CacheFactory._getCacheInstances().containsKey(SysDateCacheImpl.class)){
  	rtn = ServiceManager.getIdGenerator().getSysDate(DataSourceFactory.getDataSource().getPrimaryDataSource());
        }
        else{
  	rtn = new Timestamp(System.currentTimeMillis());
        }
        return rtn;
      }
	
	
}
