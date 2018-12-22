package com.lotus.lotusapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 */
public class DateUtil {
	public static final int H_M = 0;
	public static final int H_M_S = 1;
	public static final int Y_H_M = 2;
	public static final int Y_H_M_H_M = 3;
	public static final int Y_H_M_H_M_S = 4;
	public static final int Y=5;
	public static final int Y_H=6;
	public static final int Y_H_M_H_M_S_2=7;
	public static final int yyyy_MM_ddTHH_mm_ss_z=8;
	
	static class CalendarFactory {
	    private ThreadLocal<Calendar> calendarRef = new ThreadLocal<Calendar>() {
	    	protected Calendar initialValue() { 
	    		return new GregorianCalendar(); 
	    	} 
	    };
	    private static CalendarFactory instance = new CalendarFactory();
	    public static CalendarFactory getFactory(){return instance;}
	    public Calendar getCalendar() { 
	    	return calendarRef.get();
	    }
	    private CalendarFactory(){} 
	}
	/**
	 * 日期格式转换
	 * 
	 * @param num
	 * @return
	 */
	public static SimpleDateFormat getFormat(int num){
		SimpleDateFormat dateformat = null;
		switch(num){
			case DateUtil.H_M:
				dateformat = new SimpleDateFormat("HH:mm");
				break;
			case DateUtil.H_M_S:
				dateformat = new SimpleDateFormat("HH:mm:ss");
				break;
			case DateUtil.Y_H_M:
				dateformat = new SimpleDateFormat("yyyy-MM-dd");
				break;
			case DateUtil.Y_H_M_H_M:
				dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				break;
			case DateUtil.Y_H_M_H_M_S:
				dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				break;
			case DateUtil.Y:
				dateformat = new SimpleDateFormat("yyyy年");
				break;
			case DateUtil.Y_H:
				dateformat = new SimpleDateFormat("yyyy年MM月");
				break;
			case DateUtil.Y_H_M_H_M_S_2:
				dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
				break;
			case DateUtil.yyyy_MM_ddTHH_mm_ss_z:
				dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+08:00'");
				break;
			default:break;
		}
		return dateformat;
	}
	/**
	 * @description 返回格式化字符串型日期
	 * @param num 类型
	 * @param date 需要处理的日期
	 * @return 返回需要的日期字符串形式
	 */
	public static String getFormatString(int num, Date date){
		if(date == null) 
			return "";
		else{
			SimpleDateFormat dateformat = getFormat(num);
			return dateformat==null?"":dateformat.format(date);
		}
	}
    /**
     * 是否是当月最后一天  返回0
     * @param date
     * @return
     */
    public static int isLastDayOfMonth(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.add(Calendar.MONTH,1);
    	c.set(Calendar.DATE, 1);
    	c.add(Calendar.DATE, -1);
    	Date last = c.getTime();
    	return last.compareTo(date);
    }
    /**
	 * @description 返回格日期
	 * @param num 格式化类型标识
	 * @param stringDate 日期的字符串形式
	 * @return Date对象
	 */
	public static Date getFormatDate(int num, String stringDate){
		Date resultDate = null;
		SimpleDateFormat dateformat = getFormat(num);
		try {
			 if(dateformat!=null)
				 resultDate = dateformat.parse(stringDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultDate;
	}
	/**
	 * 返回指定日期的下一天
	 * 
	 * @param currentDate
	 * @return
	 */
	public static Date getNextDate(Date currentDate){
		return getAddDate(currentDate,1,1);
	}
	/**
	 * 
	 * @Title getAddDate
	 * @Description 给指定日期添加时间
	 * @param date 需要操作日期
	 * @param type 添加类型
	 * @param num  具体时间值
	 * @return
	 */
	public static Date getAddDate(Date date, int type, int num){
		Calendar cal = CalendarFactory.getFactory().getCalendar();
		try {
			cal.setTime(date);
			if(type==1){
				cal.add(Calendar.DATE, num);
			}else if(type==2){
				cal.add(Calendar.HOUR, num);
			}else if(type==3){
				cal.add(Calendar.MINUTE, num);
			}else if(type==4){
				cal.add(Calendar.SECOND, num);
			}else if(type==5){
				cal.add(Calendar.MONTH, num);
			}else if(type==6){
                cal.add(Calendar.YEAR, num);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cal.getTime();
	}
	/**
	 * 转换日期格式
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formate(Date date, String pattern){
		if(null == date){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
    /**
     * 是否是当月第一天 返回 0
     * @param date
     * @return
     */
    public static int isFirstDayOfMonth(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.set(Calendar.DATE, 1);
    	Date first = c.getTime();
    	return first.compareTo(date);
    }

    /**
     * 是否是当年最后一天  返回0
     * @param date
     * @return
     */
    public static int isLastDayOfYear(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.add(Calendar.YEAR,1);
    	c.set(Calendar.MONTH, 1);
    	c.set(Calendar.DATE, 1);
    	c.add(Calendar.DATE, -1);
    	Date last = c.getTime();
    	return last.compareTo(date);
    }
    
    /**
     * 是否是当年第一天 返回 0
     * @param date
     * @return
     */
    public static int isFirstDayOfYear(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.set(Calendar.MONTH, 1);
    	c.set(Calendar.DATE, 1);
    	Date first = c.getTime();
    	return first.compareTo(date);
    }
    
    /**
     * 购买媒体资源时结束日期计算
     * @param startTime
     * @param mediaPutnum
     * @param makeofferType
     * @return
     */
    public static Date getEndTime(Date startTime,Integer mediaPutnum,Integer makeofferType){
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		switch (makeofferType){
			case 4:
				c.add(Calendar.DAY_OF_MONTH, mediaPutnum-1);
				break;
			case 3:
				c.add(Calendar.WEEK_OF_MONTH, mediaPutnum);
				c.add(Calendar.DAY_OF_MONTH, -1);
				break;
			case 2:
				if(DateUtil.isFirstDayOfMonth(c.getTime())==0){
					c.add(Calendar.MONTH, mediaPutnum);
				}else{
					c.add(Calendar.DAY_OF_MONTH, mediaPutnum*30-1);
				}
				break;
			case 1:
				if(DateUtil.isFirstDayOfYear(c.getTime())==0){
					c.add(Calendar.YEAR, mediaPutnum);
					c.add(Calendar.DAY_OF_MONTH, -1);
				}else{
					c.add(Calendar.DAY_OF_MONTH, mediaPutnum*365-1);
				}
				break;
		}
		return c.getTime();
	}
    
    /**
	 * 字符串转换到时间格式
	 * @param dateStr 需要转换的字符串
	 * @param formatStr 需要格式的目标字符串  举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException 转换异常
	 */
	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat sdf=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrDateTime(){
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 累加时间-秒
	 * @param dateTime
	 * @param addTime
	 * @return
	 */
	public static String countTime(String dateTime,int addTime){
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date toDate = null;
		try {
			Date inDate = sdf.parse(dateTime);
			long posL = inDate.getTime() + addTime * 1000;
			toDate = new Date(posL);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(toDate);
	}
	
	/**
	 * 格式化输出日期
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				DateFormat df = new SimpleDateFormat(format);
				result = df.format(date);
			}
		} catch (Exception ignored) {
		}
		return result;
	}
}
