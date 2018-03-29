package com.ultrapower.ci.common.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-11-9
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

/**
 * 
 * @author yangbin6
 * Description : 日期工具类
 * 2017年12月7日
 */
public class DateTimeUtils {
    /** 日期格式 ** */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN2 = "yyyyMMdd";
    /** 日期格式 ** */
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private static final String SEPARATELine = "-";

	private static final String SEPARATEAT = "@";

	private static final String SEPARATEPOINT = ".";

	private static final String SEPARATECOLON = ":";
	
	private static String dateFormat = "yyyy-MM-dd";
	private static String timeStamp25Bit = "yyyy-MM-dd HH:mm:ss.SSS";
	private static String timeStamp17Bit = "yyyyMMddHHmmssSSS";

	private static final DateTimeUtils instance = new DateTimeUtils();

    /**
     * 获得当前的系统时间
     *
     * @return 当前的系统日期
     */
    public static Date getCurrentTime() {
        return new Date();
    }

    /**
     * 获得当前的系统日期，不带有时分秒
     *
     * @return 当前的系统日期
     */
    public static Date getCurrentDate() {

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.clear(Calendar.HOUR);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);

        date = c.getTime();
        return date;
    }

    /**
     * 得到当前系统日期,格式："yyyy-MM-dd"
     *
     * @return
     */
    public static String getFormatCurrentDate() {
        return format(getCurrentDate(), DATE_PATTERN);
    }
    /**
     * 得到当前系统日期,格式："yyyyMMdd"
     *
     * @return
     */
    public static String getFormatCurrentDate2() {
        return format(getCurrentDate(), DATE_PATTERN2);
    }


    /**
     * 得到当前系统日期,格式："yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getFormatCurrentTime() {
        return format(getCurrentTime(), TIME_PATTERN);
    }

    /**
     * 输出字符串类型的格式化日期
     *
     * @param dt
     *            Date
     * @param pattern
     *            时间格式
     * @return sDate
     */
    public static String format(Date dt, String pattern) {
        String sDate;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        sDate = formatter.format(dt);
        return sDate;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static String getCurrentMonth() {
        return getDateMonth(getCurrentDate());
    }
    /**
     * 获取当前年份
     *
     * @return
     */
    public static String getCurrentYear() {
    	Calendar calendar=Calendar.getInstance();
    	String year = calendar.get(Calendar.YEAR)+"";
    	return year;
    }
    /**
     * 得到指定日期的月份
     *
     * @return
     */
    public static String getDateMonth(Date date) {

        SimpleDateFormat format1 = new SimpleDateFormat(DATE_PATTERN);
        format1.setLenient(false);
        String dateStr = format1.format(date);
        int begin = dateStr.indexOf('-') + 1;
        int end = dateStr.lastIndexOf('-');
        String month = dateStr.substring(begin, end);
        return month;
    }

    /**
     * 得到指定日期后若干天的日期
     *
     * @param date
     *            指定日期
     * @param days
     *            天数
     * @return
     */
    public static Date afterDaysSinceDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        date = c.getTime();
        return date;
    }

    /**
     * 判断两个Date是否在同一天
     *
     * @param date1
     *            date1
     * @param date2
     *            date2
     * @return
     */
    public static boolean isTwoDatesInSameDay(Date date1, Date date2) {
        Date preDate1 = preDay(date1);
        Date nextDate1 = nextDay(date1);
        if (date2.after(preDate1) && date2.before(nextDate1)) {
            return true;
        }
        return false;
    }

    /**
     * 得到指定日期的下一天
     *
     * @param date
     *            日期
     * @return
     */
    public static Date nextDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        return date;
    }

    /**
     * 得到指定日期的前一天
     *
     * @param date
     *            日期
     * @return
     */
    public static Date preDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        date = c.getTime();
        return date;
    }

    /**
     * 得到当前月份的下一个月份
     *
     * @return
     */
    public static Date addMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        date = c.getTime();
        return date;
    }

    /**
     * 得到年份与月份
     *
     * @return String
     */
    public static String getYearMonth(Date date) {
        String yearMonthStr = format(date, DATE_PATTERN);
        int index = yearMonthStr.lastIndexOf('-');
        yearMonthStr = yearMonthStr.substring(0, index);
        return yearMonthStr;
    }

    /**
     * 得到当前月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        date = c.getTime();
        return date;
    }

    /**
     * 得到当前月的第一天
     *
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();
        return date;
    }

    /**
     * 判断一个日期是否在指定的时间段内
     *
     * @return String
     */
    public static boolean inTimeSegment(Date start, Date end, Date date) {
        start = preDay(start);
        end = nextDay(end);
        if (date.after(start) && date.before(end)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前日期是否在指定的时间段内
     *
     * @param start
     *            时间段开始时间
     * @param end
     *            时间段结束时间
     * @return 如果当前日期在指定时间段内，则为true，否则为false
     */
    public static boolean isCurrentDateInTimeSegment(Date start, Date end) {
        Date date = getCurrentDate();
        if (inTimeSegment(start, end, date)) {
            return true;
        }
        return false;
    }

    /**
     * 得到两个日期的间隔天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int getBetweenDays(Date start, Date end) {
        if (start.after(end))
            return -1;

        Calendar startC = Calendar.getInstance();
        startC.setTime(start);
        Calendar endC = Calendar.getInstance();
        endC.setTime(end);
        endC.add(Calendar.DAY_OF_YEAR, 1);
        int days = 0;
        do {
            days++;
            startC.add(Calendar.DAY_OF_YEAR, 1);
        } while (startC.before(endC));
        return days;
    }

    /**
     * 计算两个时间之间相隔秒数
     *
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return
     */
    public static int getIntervalSeconds(Date start, Date end) {
        // 分别得到两个时间的毫秒数
        long sl = start.getTime();
        long el = end.getTime();

        long ei = el - sl;
        return (int) (ei / 1000);
    }

    /**
     * 得到指定月份的天数
     *
     * @param date
     *            日期
     * @return
     */
    public static int daysInMonth(Date date) {
        Date start = getFirstDayOfMonth(date);
        Date end = getLastDayOfMonth(date);
        String startStr = format(start, "yyyyMMdd");
        String endStr = format(end, "yyyyMMdd");
        return Integer.parseInt(endStr) - Integer.parseInt(startStr) + 1;
    }


    
    
    
    
    
    public static final DateTimeUtils getInstance() {
		return instance;
	}
	/**
	 * 返回当前日期
	 * @return （2014-04-15）
	 */
	public static String getNewDate(){
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR)+"";
		String month = c.get(Calendar.MONTH)+1+"";
		String day = c.get(Calendar.DAY_OF_MONTH)+"";
		if(month.length()==1){
			month = "0"+month;
		}
		if(day.length()==1){
			day = "0"+day;
		}		
		return year+"-"+month+"-"+day;
	}
	
	/**
	 * 返回当前日期
	 * @return （20140415）
	 */
	public static String getCurrtntDate(){
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR)+"";
		String month = c.get(Calendar.MONTH)+1+"";
		String day = c.get(Calendar.DAY_OF_MONTH)+"";
		if(month.length()==1){
			month = "0"+month;
		}
		if(day.length()==1){
			day = "0"+day;
		}		
		return year+month+day;
	}

	/**
	 * 根据当前日期返回星期几
	  *@param pTime  2014-01-23
	  *@author:Administrator
	  *@createTime:2015-1-1 下午3:04:07
	 */
	public static String dayForWeek(String pTime) {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar c = Calendar.getInstance();  
		 try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		 int dayForWeek = 0;  
		 if(c.get(Calendar.DAY_OF_WEEK) == 1){  
		  dayForWeek = 7;  
		 }else{  
			 dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
		 }
		 String week = "";
		 if(dayForWeek==1) week = "星期一";
		 if(dayForWeek==2) week = "星期二";
		 if(dayForWeek==3) week = "星期三";
		 if(dayForWeek==4) week = "星期四";
		 if(dayForWeek==5) week = "星期五";
		 if(dayForWeek==6) week = "星期六";
		 if(dayForWeek==7) week = "星期日";
		 
		 return week;  
		}  

	
	/**
	 * 得到日期差值 *
	 * 
	 * @param dateStr
	 *            String
	 * @param cz
	 *            int
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getDate(String dateStr, int cz) {
		int yy = Integer.parseInt(dateStr.substring(0, 4), 10);
		int mm = Integer.parseInt(dateStr.substring(4, 6), 10);
		int dd = Integer.parseInt(dateStr.substring(6, 8), 10);
		java.sql.Date d = new java.sql.Date(yy - 1900, mm - 1, dd + cz);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(d);
	}
	
	/**
	 * 得到日期差值 *
	 * @param dateStr 格式 2014-02-02
	 * @param cz
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getDate2(String dateStr, int cz) {
		int yy = Integer.parseInt(dateStr.split("-")[0]);
		int mm = Integer.parseInt(dateStr.split("-")[1]);
		int dd = Integer.parseInt(dateStr.split("-")[2]);
		java.sql.Date d = new java.sql.Date(yy - 1900, mm - 1, dd + cz);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}

	/**
	 * 得到当前月 格式是2008-9-25 *
	 * 
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getCurrentMonth(String dateStr) {
		String date[] = dateStr.split("-");
		return Integer.parseInt(date[1], 10);
	}
	
	/**
	 * 得到当前月 格式是20080901*
	 * 
	 * @param dateStr
	 *            String
	 * @return String
	 */
	public static String getCurMonth(String dateStr) {
		
		return dateStr.substring(4, 6);
	}

	/**
	 * 得到当前日 格式是20080901*
	 * 
	 * @param dateStr
	 *            String
	 * @return String
	 */
	public static String getCurDay(String dateStr) {
		
		return dateStr.substring(6);
	}
	
	/**
	 * 得到当前年 格式是20080901*
	 * 
	 * @param dateStr
	 *            String
	 * @return String
	 */
	public static String getCurYear(String dateStr) {
		
		return dateStr.substring(0, 4);
	}
	
	 /** 得到当前年月 格式是20080901 *
	 * 
	 * @param dateStr
	 *            String
	 * @return String
	 */
	public static String getCurYM(String dateStr) {
		
		return dateStr.substring(0, 6);
	}
	/**
	 * 将200809中的09的0去掉
	 * @param dateStr
	 * @return
	 */
	public static String removeZero (String dateStr) {
		String rv=dateStr;
		/*if(dateStr.startsWith("0")){
			
		}*/
		if (rv.indexOf("0")==0){
			rv=rv.substring(1);
		}
		
		return rv;
	}
	
	/**
	 * 得到月的天数 *
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */
	public static int getMonthDays(int year, int month) {
		int days = 1;
		boolean isrn = (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) ? true
				: false;
		switch (month) {
		case 1:
			days = 31;
			break;
		case 2:
			if (isrn)
				days = 29;
			else
				days = 28;
			break;
		case 3:
			days = 31;
			break;
		case 4:
			days = 30;
			break;
		case 5:
			days = 31;
			break;
		case 6:
			days = 30;
			break;
		case 7:
			days = 31;
			break;
		case 8:
			days = 31;
			break;
		case 9:
			days = 30;
			break;
		case 10:
			days = 31;
			break;
		case 11:
			days = 30;
			break;
		case 12:
			days = 31;
		}
		return days;
	}

	/**
	 * 得到月的天数，包括当前月过的天数。*
	 * 
	 * @param currDate
	 *            String
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */
	public static int getMonthDays(String currDate, int year, int month) {
		int days = 1;
		String date[] = currDate.split("-");
		if (Integer.parseInt(date[0]) == year
				&& Integer.parseInt(date[1]) == month) {
			days = Integer.parseInt(date[2]);
		} else {
			days = getMonthDays(year, month);
		}
		return days;
	}

	/**
	 * 得到当前年 格式是2008-9-25 *
	 * 
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getCurrentYear(String dateStr) {
		String date[] = dateStr.split("-");
		return Integer.parseInt(date[0], 10);
	}

	/**
	 * 得到当前天数 格式是2008-9-25 *
	 * 
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getCurrentDay(String dateStr) {
		String date[] = dateStr.split("-");
		return Integer.parseInt(date[2], 10);
	}

	/**
	 * 得到季度到现在的天数 格式是2008-9-25 *
	 * 
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getJiDuDays(String dateStr) {
		int days = 0;
		String date[] = dateStr.split("-");
		int day = Integer.parseInt(date[2], 10);
		int yy = Integer.parseInt(date[0], 10);
		boolean isrn = (((yy % 4 == 0) && (yy % 100 != 0)) || (yy % 400 == 0)) ? true
				: false;
		switch (Integer.parseInt(date[1], 10)) {
		case 1:
			days = day;
			break;
		case 2:
			days = 31 + day;
			break;
		case 3:
			if (isrn)
				days = 31 + 29 + day;
			else
				days = 31 + 28 + day;
			break;
		case 4:
			days = day;
			break;
		case 5:
			days = 30 + day;
			break;
		case 6:
			days = 61 + day;
			break;
		case 7:
			days = day;
			break;
		case 8:
			days = 31 + day;
			break;
		case 9:
			days = 62 + day;
			break;
		case 10:
			days = day;
			break;
		case 11:
			days = 31 + day;
			break;
		case 12:
			days = 61 + day;
			break;
		}
		return days;
	}

	/**
	 * 返回两个日期间隔的天数 *
	 * 
	 * @param beginDate
	 *            String
	 * @param endDate
	 *            String
	 * @return int
	 */
	public static int getBetweenDays(String beginDate, String endDate) {
		int sum = 0;
		int beginYear = getCurrentYear(beginDate);
		int beginMonth = getCurrentMonth(beginDate);
		int beginDay = getCurrentDay(beginDate);
		int endYear = getCurrentYear(endDate);
		int endMonth = getCurrentMonth(endDate);
		int endDay = getCurrentDay(endDate);
		String startDateStr = bYearZero(beginYear) + bZero(beginMonth)
				+ "01";

		int sumMonth = (endYear - beginYear + 1) * 12 - (beginMonth)
				- (12 - endMonth);
		for (int i = 0; i < sumMonth; i++) {
			String dateStr = getDateStr(startDateStr, i);
			sum = sum
					+ getMonthDays(getCurrentYear(dateStr),
							getCurrentMonth(dateStr));
		}

		sum = sum - beginDay + endDay;
		return sum;
	}

	/**
	 * 返回日期经过若干月后的日期 *
	 * 
	 * @param dateStr
	 *            String
	 * @param hkm
	 *            int
	 * @return String
	 */
	public  static String getDateStr(String dateStr, int hkm) {
		String reDateStr = "";
		int yy = Integer.parseInt(dateStr.substring(0, 4), 10);
		int mm = Integer.parseInt(dateStr.substring(4, 6), 10);
		int dd = Integer.parseInt(dateStr.substring(6, 8), 10);
		// int yy1=0,mm1=0,dd1=dd;
		int yy2 = 0, mm2 = 0, dd2 = dd;
		if ((mm + hkm) % 12 == 0) {
			yy2 = yy + (mm + hkm) / 12 - 1;
			mm2 = 12;
		} else {
			if ((mm + hkm) % 12 == 1) {
				yy2 = yy + (mm + hkm) / 12;
				mm2 = 1;
			} else {
				yy2 = yy + (mm + hkm) / 12;
				mm2 = (mm + hkm) % 12;
			}
		}
		reDateStr = String.valueOf(yy2) + "-" + bZero(mm2) + "-" + bZero(dd2);
		return reDateStr;
	}

	/**
	 * 返回两位数据字串 *
	 * 
	 * @param sz
	 *            int
	 * @return String
	 */
	public static String bZero(int sz) {
		return (sz < 10 ? ("0" + String.valueOf(sz)) : String.valueOf(sz));
	}
	
	/**
	 * 返回四位字符串
	 * @param y
	 * @return
	 */
	public static String bYearZero(int y){
		if(y<10)
			return "000" + String.valueOf(y);
		else if(y<100)
			return "00"+ String.valueOf(y);
		else if(y<1000)
			return "0"+String.valueOf(y);
		
		return String.valueOf(y);
	}

	/**
	 * 比较日期大小 *
	 * 
	 * @param date1
	 *            String
	 * @param date2
	 *            String
	 * @return int
	 */
	@SuppressWarnings("deprecation")
	public static int compareDate(String date1, String date2) {
		String[] date1Array = date1.split("-");
		String[] date2Array = date2.split("-");
		java.sql.Date date11 = new java.sql.Date(Integer.parseInt(
				date1Array[0], 10), Integer.parseInt(date1Array[1], 10),
				Integer.parseInt(date1Array[2], 10));
		java.sql.Date date22 = new java.sql.Date(Integer.parseInt(
				date2Array[0], 10), Integer.parseInt(date2Array[1], 10),
				Integer.parseInt(date2Array[2], 10));
		return date11.compareTo(date22);
	}

	/**
	 * 把字符串 格式转化成日期型 *
	 * 
	 * @param dateStr
	 *            String
	 * @return Date
	 */
	@SuppressWarnings("deprecation")
	public static Date strToDate(String dateStr) {
		String[] dateArray = dateStr.split("-");
		java.util.Date date = new java.util.Date(Integer.parseInt(dateArray[0],
				10) - 1900, Integer.parseInt(dateArray[1], 10) - 1, Integer
				.parseInt(dateArray[2], 10));
		return date;
	}

	/**
	 * 把日期型转化成字符串型 *
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String dateToStr(java.util.Date date) {
		String str = "";
		try {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			str = sdf.format(date);
		} catch (Exception ex) {
			str = "";
		}
		return str;
	}

	/**
	 * 把日期型转化成字符串型 *
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String dateToStr(java.util.Date date, String fgf) {
		String str = "";
		try {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy" + fgf + "MM" + fgf + "dd");
			str = sdf.format(date);
		} catch (Exception ex) {
			str = "";
		}
		return str;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param
	 * @return 返回信贷规定时间格式类型字符串 YYYY-MM-DD
	 * @throws
	 */
	public static String getCurDate() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String strDate=sdf.format(date);
	
		
		return strDate;

	}
	
	/**
	 * <p>
	 * 如果该方法有问题请用getCurTimeStamp2()
	 * </p>
	 * 
	 * @param
	 * @return 返回信贷规定时间格式类型字符串 YYYY-MM-DD@hh:mm:ss.mmm
	 * @throws
	 */
	public static String getCurTimeStamp() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd@kk:mm:ss.S"); 
		String strStamp=sdf.format(date);
	
		
		return strStamp;
	}
	
	@SuppressWarnings("unused")
	private static String getCurTimeStamp2() {
		
		Calendar rightNow = Calendar.getInstance();
		/**
		 * 当期日
		 */
		int DD = rightNow.get(Calendar.DAY_OF_MONTH);
		/**
		 * 当期年
		 */
		int YYYY = rightNow.get(Calendar.YEAR);
		/**
		 * 当期小时
		 */
		int hh = rightNow.get(Calendar.HOUR_OF_DAY);
		/**
		 * 当期分钟
		 */
		int mm = rightNow.get(Calendar.MINUTE);
		/**
		 * 当前秒
		 */
		int ss = rightNow.get(Calendar.SECOND);
		/**
		 * 当前毫秒
		 */
		int ms = rightNow.get(Calendar.MILLISECOND);

		String strTimeStamp = null;
		String strYYYY = null;
		String strMM = null;
		String strDD = null;
		String strhh = null;
		String strmm = null;
		String strss = null;
		String strms = null;
		
		if (YYYY < 10) {
			strYYYY = "000" + String.valueOf(YYYY);
		} else if (YYYY < 100 && YYYY >= 10) {
			strYYYY = "00" + String.valueOf(YYYY);
		} else if (YYYY < 1000 && YYYY >= 100) {
			strYYYY ="0"+ String.valueOf(YYYY);
		}else if (YYYY < 10000 && YYYY >= 1000) {
			strYYYY = String.valueOf(YYYY);
		}

		if (String.valueOf(DD).length() == 1) {
			strDD = "0" + String.valueOf(DD);
		} else {
			strDD = String.valueOf(DD);
		}

		if (String.valueOf(hh).length() == 1) {
			strhh = "0" + String.valueOf(hh);
		} else {
			strhh = String.valueOf(hh);
		}

		if (String.valueOf(mm).length() == 1) {
			strmm = "0" + String.valueOf(mm);
		} else {
			strmm = String.valueOf(mm);
		}

		if (String.valueOf(ss).length() == 1) {
			strss = "0" + String.valueOf(ss);
		} else {
			strss = String.valueOf(ss);
		}

		if (ms < 10) {
			strms = "00" + String.valueOf(ms);
		} else if (ms < 100 && ms >= 10) {
			strms = "0" + String.valueOf(ms);
		} else if (ms < 1000 && ms >= 100) {
			strms = String.valueOf(ms);

		}
		
		strTimeStamp = strYYYY + DateTimeUtils.SEPARATELine + strMM + DateTimeUtils.SEPARATELine
				+ DateTimeUtils.SEPARATELine + strDD;
		strTimeStamp = strTimeStamp + DateTimeUtils.SEPARATEAT;
		strTimeStamp = strTimeStamp+ strhh + DateTimeUtils.SEPARATECOLON + strmm
				+ DateTimeUtils.SEPARATECOLON + strss;
		strTimeStamp = strTimeStamp + DateTimeUtils.SEPARATEPOINT + strms;
		return strTimeStamp;

	}
	
	/**
	 * <p>
	 * </p>
	 * 
	 * @param
	 * @return 返回指定日期对应的季度末月的日期 YYYY-MM-DD
	 * @throws
	 */
	public static String getLastDateOfQuarter(String date) {
		String year = date.substring(0, 4) ;
		String month = date.substring(5, 7) ;
		String day = date.substring(7) ;
		String quarter ="" ;
		
		switch(Integer.parseInt(month)){
		  case 1:
		  case 2:
		  case 3:
			  quarter = "03";
			  break;
		  case 4:
		  case 5:
		  case 6:
			  quarter = "06";
			  break;			  
		  case 7:
		  case 8:
		  case 9:		  
			  quarter = "09";
			  break;
		  case 10:
		  case 11:
		  case 12:
			  quarter = "12";
			  break;
		}
		return year+quarter+day ;
	}

	/**
	 * <p>得到指定日期的当年年末月的日期</p>
	 * @param date YYYY-MM-DD
	 * @return
	 */
	public static String getLastDateOfYear(String date){

		String year = Integer.parseInt(date.substring(0, 4))+"" ;
		String day = date.substring(8) ;
		
		return year+"12"+day; 
	}

	/**
	 * 判断date1是否比date2早
	 *    date1<date2  --true
	 *    date1>=date2 --false
	 *    日期格式:yyyy-MM-dd
	 * @param date1 
	 * @param date2
	 * @return true/false
	 */
	public static boolean checkDate1BeforeDate2(String date1,String date2){
		Date d1 = strToDate(date1);
		Date d2 = strToDate(date2);
		if(d1.before(d2)){
			return true;
		}
		return false;
	}
	/**
	 * 获得季度
	 * @param str
	 * @return
	 */
	public static String getQuarter(String str){
		String rv="";
		switch (Integer.parseInt(str)){
		case 1:
		case 2:
		case 3:
			rv="1";
		break;
		case 4:
		case 5:
		case 6:
			rv="2";
		break;	
		case 7:
		case 8:
		case 9:
			rv="3";
		break;	
		case 10:
		case 11:
		case 12:
			rv="4";
		break;	
		}
		return rv;
	}
	
	/**
	 * 获得半年
	 * @param str
	 * @return
	 */
	public static String getHelfYear(String str){
		String rv="";
		switch (Integer.parseInt(str)){
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			rv="1";
		break;	
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			rv="2";
		break;	
		}
		return rv;
	}
	
	private static String ADD_DATE(int optype,String date,int num){
		String st_return = "";  
		 try {
			DateFormat daf_date = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.CHINA);
			daf_date.parse(date);
			Calendar  calendar= daf_date.getCalendar();
			calendar.add(optype, num);
			if(optype == Calendar.MONTH){
				calendar.add(Calendar.DATE, -1);
			}
				String st_m = "";
				String st_d = "";
			    int y = calendar.get(Calendar.YEAR);
			    int m = calendar.get(Calendar.MONTH) + 1;
			    int d = calendar.get(Calendar.DAY_OF_MONTH); 
			    if (m <= 9) {
			      st_m = "0" + m;
			    }
			    else {
			      st_m = "" + m;
			    }
			    if (d <= 9) {
			      st_d = "0" + d;
			    }
			    else {
			      st_d = "" + d;
			    }
			    st_return = y + "-" + st_m + "-" + st_d;
		} catch (ParseException e) { 
			e.printStackTrace();
		}
		return st_return;
	}
	/**
	 * 增加天数
	 * @param date
	 * @param n
	 * @return
	 */
	 public static String ADD_DAY(String date,int n){
		 return DateTimeUtils.ADD_DATE(Calendar.DATE, date, n);
	 }
	 /**
	  * 增加月数
	  * @param date
	  * @param n
	  * @return
	  */
	 public static String ADD_MONTH(String date,int n){ 
		 return DateTimeUtils.ADD_DATE(Calendar.MONTH, date, n);
	 }
	 /**
	  * 增加年数
	  * @param date
	  * @param n
	  * @return
	  */
	public static String ADD_YEAR(String date,int n){ 
		return DateTimeUtils.ADD_DATE(Calendar.YEAR, date, n);
	}
   

	
	/**
	 * 取上期时间
	 * @param yyMMdd
	 * @return
	 */
	public static String getPeryyMMdd(String yyMMdd,String termType){
		String rv=yyMMdd;
		
		Calendar  calendar=Calendar.getInstance();
		
		String m = DateTimeUtils.getCurMonth(yyMMdd);
		String y = DateTimeUtils.getCurYear(yyMMdd);
		String d = DateTimeUtils.getCurDay(yyMMdd);
		
		int year=Integer.parseInt(y);
		int month=Integer.parseInt(DateTimeUtils.removeZero(m));
		int date=Integer.parseInt(DateTimeUtils.removeZero(d));
			
		calendar.set(year, month, date);
		
		switch (Integer.parseInt(termType)) {
		case 1:// 月
			calendar.add(Calendar.MONTH, -2);
			break;
		case 2:// 季
			calendar.add(Calendar.MONTH, -4);
			break;
		case 3:// 半年
			calendar.add(Calendar.MONTH, -7);			
			break;
		case 4:// 年
			calendar.add(Calendar.MONTH, -13);
			break;
		default:
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		rv=sdf.format(calendar.getTime());
		return rv;
	}
	
	
	/**
	 * 取上N-1期+本期时间数组
	 * @param yyMMdd
	 * @return
	 */
	public static String[] getPerNyyMMdd(String yyMMdd,String termType,int term){
		String[] rv=new String[term];
		
		Calendar  calendar=Calendar.getInstance();
		
		String m = DateTimeUtils.getCurMonth(yyMMdd);
		String y = DateTimeUtils.getCurYear(yyMMdd);
		String d = DateTimeUtils.getCurDay(yyMMdd);
		
		int year=Integer.parseInt(y);
		int month=Integer.parseInt(DateTimeUtils.removeZero(m));
		int date=Integer.parseInt(DateTimeUtils.removeZero(d));
			
		
		rv[0]=yyMMdd;
		for(int i=0;i<term-1;i++){
			calendar.set(year, month, date);
			switch (Integer.parseInt(termType)) {
			case 1:// 月
				calendar.add(Calendar.MONTH, -(2+i));
				break;
			case 2:// 季
				calendar.add(Calendar.MONTH, -(4+(i*3)));
				break;
			case 3:// 半年
				calendar.add(Calendar.MONTH, -(7+(i*6)));			
				break;
			case 4:// 年
				calendar.add(Calendar.MONTH, -(13+(i*12)));
				break;
			default:
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
			rv[i+1]=sdf.format(calendar.getTime());
		}
	
		return rv;
	}

	/**
	 * 取两个时间的月数
	 * @param yyMMdd
	 * @return
	 */
	public static int countMonth(String yyMMdd1,String yyMMdd2){
		String startDate =yyMMdd1;
		String endDate =yyMMdd2;
		
		int rv=0	;
		
		if (Long.parseLong(startDate)>Long.parseLong(endDate)){
			String tmp=endDate;
			endDate=startDate;
			startDate=tmp;
		}
		
		int yy1=Integer.parseInt(startDate.substring(0,4));
		int mm1=Integer.parseInt(startDate.substring(4,6));
//		int dd1=Integer.parseInt(startDate.substring(6));
		
		int yy2=Integer.parseInt(endDate.substring(0,4));
		int mm2=Integer.parseInt(endDate.substring(4,6));
//		int dd2=Integer.parseInt(endDate.substring(6));
		
		rv=(yy2-yy1)*12+(mm2-mm1);
		
	/*	if(dd1>dd2){
			rv=rv-1;
		}*/
		return rv;
	}
	
	
	/**
	 * 获得N位随机整数
	 * @param n
	 * @return
	 */
	public static String getRandomNum(int length){
		String rt="";
		for(int i=0;i<length;i++){
			Random r=new Random();
			rt=rt+String.valueOf(r.nextInt(9));
		}
		
		return rt;
	}
	
	/**
	 * 返回N位字符串（只含字母和数字）
	 * @param length
	 * @return
	 */
	public static String getCharacterAndNumber(int length)   
	{   
	    String rt = "";   
	           
	    Random random = new Random();   
	    for(int i = 0; i < length; i++)   
	    {   
	        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字   
	               
	        if("char".equalsIgnoreCase(charOrNum)) // 字符串   
	        {   
	            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母   
	            rt += (char) (choice + random.nextInt(26));   
	        }   
	        else if("num".equalsIgnoreCase(charOrNum)) // 数字   
	        {   
	            rt += String.valueOf(random.nextInt(10));   
	        }   
	    }   
	           
	    return rt;   
	}  

	/**
	 * 生成32位主键 为时间戳到毫秒（17位）+大小写及数字的随机数（15位即62的15次幂=768909704948766668552634368分之一的重复几率）
	 * @return
	 */
	public static String getPK(){
		String rt="";
		rt=rt+DateTimeUtils.getCurTimeStamp4PK();
		rt=rt+DateTimeUtils.getCharacterAndNumber(15);
		return rt;
	}
	
	public static String getCurTimeStamp4PK() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmssSSS"); 
		String strStamp=sdf.format(date);
	
		
		return strStamp;
	}
	
	public static String getCurTimeStamp5PK() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss"); 
		String strStamp=sdf.format(date);
	
		
		return strStamp;
	}
	
	/**
    * 获取昨天
    * @param date
    * @return
    */
    public static String getYesterday(String date) {
        try {
    	        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	        Calendar cal= Calendar.getInstance();
    	        Date d = sf.parse(date);
    	        cal.setTime(d);
    	        cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)-1);
    	        date = sf.format(cal.getTime());
        } catch (Exception e) {
        }
        return date;
    }
    
    public final static String DTPattern	="yyyy-MM-dd  HH:mm:ss ";
    
	public static String getDateTime(String pattern){
		String dateTime="";
		Calendar calender = Calendar.getInstance();
		if(pattern==null||pattern.equals(""))
			pattern=DTPattern;
		SimpleDateFormat sf=new SimpleDateFormat(pattern);
		dateTime = sf.format(calender.getTime());
		return dateTime;
	}
	
	/**
	 * 获取当前时间
	 * @return 2014-02-11 14:22:40
	 */
	public static String getCurrentDateStr() {
		 Date date = new Date();
		 String str = null;
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 str = df.format(date);
		 return str;
		    }
	
	/**
	 * 判断两个日期大小（时间类型：2014-02-11 12:22:40）
	 * @param date1 
	 * @param date2
	 * @return date1<date2 返回true
	 */
	public static boolean  checkTime(String date1,String date2){
		
		int year1 = Integer.parseInt(date1.split(" ")[0].split("-")[0]);
		int month1 = Integer.parseInt(date1.split(" ")[0].split("-")[1]);
		int day1 = Integer.parseInt(date1.split(" ")[0].split("-")[2]);
		int hour1 = Integer.parseInt(date1.split(" ")[1].split(":")[0]);
		int minutes1 = Integer.parseInt(date1.split(" ")[1].split(":")[1]);
		int seconds1 = Integer.parseInt(date1.split(" ")[1].split(":")[2]);
		
		int year2 = Integer.parseInt(date2.split(" ")[0].split("-")[0]);
		int month2 = Integer.parseInt(date2.split(" ")[0].split("-")[1]);
		int day2 = Integer.parseInt(date2.split(" ")[0].split("-")[2]);
		int hour2 = Integer.parseInt(date2.split(" ")[1].split(":")[0]);
		int minutes2 = Integer.parseInt(date2.split(" ")[1].split(":")[1]);
		int seconds2 = Integer.parseInt(date2.split(" ")[1].split(":")[2]);
		
		GregorianCalendar gc1 = new GregorianCalendar(year1,month1,day1,hour1,minutes1,seconds1);
		GregorianCalendar gc2 = new GregorianCalendar(year2,month2,day2,hour2,minutes2,seconds2);
		return gc1.before(gc2);
		
	}
	
	/**
	 * 判断是否周末
	 * @author shanglw
	 * @create 2015-06-02
	 * @param strDate (2015-05-30)
	 * @return
	 * @throws ParseException
	 */
	public static boolean isWeekend(String strDate) throws ParseException {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(stringFormatDate(strDate));
		int week = calendar.get(Calendar.DAY_OF_WEEK)-1;  
		if(week ==6 || week==0){ return true; }  
		else return false;
	}
	
	/**
	 * @Description: 两个字符串拼接
	 * @@param s1 例如s1=000000
	 * @@param s2 例如s2= 张三
	 * @@return   返回  rteS=000000 张三
	 */
	public static String typeTransform(String s1,String s2){
		String rteS = "";
		if(s1!=null){
			rteS = s1;
			if(s2!=null){
				rteS = s1+" "+s2;
			}
		}
		return rteS;
	}
	
	/**
	 * 日期类型转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormatString(Date date) {
		return new SimpleDateFormat(dateFormat).format(date);
	}
	
	/**
	 * 字符串转日期类型
	 * 
	 * @param str
	 * @returno
	 * @throws ParseException
	 */
	public static Date stringFormatDate(String str) throws ParseException {
		return new SimpleDateFormat(dateFormat).parse(str);
	}
	
	/**
	 * 字符串转日期类型
	 * @param dateValue 日期字符串形式，如：2016-01-01，或2016-01-01 12:00:00.000，或2016-01-01 12:00:00，或任意其他格式。dateString值与dateFormat格式需对应
	 * @param dateFormat 日期格式，如：yyyy-MM-dd，或yyyy-MM-dd HH:mm:ss.SSS，或yyyy-MM-dd HH:mm:ss，或任意其他格式。dateString值与dateFormat格式需对应
	 * @return 返回java.util.Date日期实例
	 * @throws ParseException 
	 */
	public static Date stringFormatDate(String dateValue, String dateFormat) throws ParseException {
		return new SimpleDateFormat(dateFormat).parse(dateValue);
	}
	
	/**
	 * 获取两个日期间的时间差（单位毫秒）
	 * @param dateValue1 日期字符串形式1，如：2016-01-01，或2016-01-01 12:00:00.000，或2016-01-01 12:00:00，或任意其他格式。dateString值与dateFormat格式需对应
	 * @param dateValue2 日期字符串形式2，如：2016-01-01，或2016-01-01 12:00:00.000，或2016-01-01 12:00:00，或任意其他格式。dateString值与dateFormat格式需对应
	 * @param dateFormat 日期格式，如：yyyy-MM-dd，或yyyy-MM-dd HH:mm:ss.SSS，或yyyy-MM-dd HH:mm:ss，或任意其他格式。dateString值与dateFormat格式需对应
	 * @return 返回dateValue1-dateValue2所得的long类型的时间差
	 * @throws ParseException
	 */
	public static long getTimeDifference(String dateValue1, String dateValue2, String dateFormat) throws ParseException {
		Date date1 = DateTimeUtils.stringFormatDate(dateValue1, dateFormat);
		Date date2 = DateTimeUtils.stringFormatDate(dateValue2, dateFormat);
		return date1.getTime() - date2.getTime();
	}
	
	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param dateStr1
	 * @param dateStr2
	 * @return
	 * @throws ParseException
	 */
	public static Integer getDutyDays(String dateStr1, String dateStr2) throws ParseException {
		Long time1 = stringFormatDate(dateStr1).getTime();
		Long time2 = stringFormatDate(dateStr2).getTime();
		Integer duty = new Integer(Long.valueOf(((time2 - time1) / (1000 * 60 * 60 * 24)) + 1).toString());
		return duty;
	}
	
	/**
	 * 获取时间戳
	 * 
	 * @param flag 25bit 带横线间隔, 17bit 不带横线间隔
	 * @return
	 */
	public static String getTimeStamp(String flag) {
		SimpleDateFormat sdf = null;
		if (flag.trim().toLowerCase().equals("25bit")) {
			sdf = new SimpleDateFormat(timeStamp25Bit);			
		} else if (flag.trim().toLowerCase().equals("17bit")) {
			sdf = new SimpleDateFormat(timeStamp17Bit);	
		} else {
			throw new IllegalArgumentException("时间戳格式必须为：17bit 或 25bit");
		}
		return sdf.format(new Date());
	}
	
	/**
	 * @Description: 给日期加上指定的天数
	 * @@param d  日期
	 * @@param day  指定的天数
	 * @@return    加上指定天数的日期
	 */
	public static String addDate(String d, long day) throws ParseException { 
		String addDate = "";
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        long time = format.parse(d).getTime();  
        day = day * 24 * 60 * 60 * 1000;  
        time += day;  
        addDate = format.format(new Date(time));
        return addDate;  
    } 
		
	/**
	 * @Description: 获得主键值
	 * @@param startChars 主键的开始字母
	 * @@return   返回主键
	 */
	public static String getKeyValue(String startChars) {
		// 获取附件主键 按照格式生成 FJ_yyyyMMddhhmmss
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String date = df.format(now);
		String addid = startChars + date;
		return addid;
	}
	
	
	/**
	 * 获取全年天数
	 * @param year
	 * @return
	 * @throws ParseException 
	 */
	public static int getYearDays(int year) throws ParseException {
		Calendar calendar1 = new GregorianCalendar(year, 0, 1);
		Calendar calendar2 = new GregorianCalendar(year, 11, 31);
		String dateStr1 = dateFormatString(calendar1.getTime());
		String dateStr2 = dateFormatString(calendar2.getTime());
		int result = getDutyDays(dateStr1, dateStr2);
		return result;
	}
	
	/**
	 * 比较两个日期并返回较靠后的一个
	 * 
	 * @param dateStr1
	 * @param dateStr2
	 * @return
	 * @throws ParseException
	 */
	public static String getLaterDate(String dateStr1, String dateStr2) throws ParseException {
		if (stringFormatDate(dateStr1).getTime() > stringFormatDate(dateStr2).getTime()) {
			return dateStr1;
		} else {
			return dateStr2;
		}
	}
	
	/**
	 * 比较两个日期并返回较靠前的一个
	 * 
	 * @param dateStr1
	 * @param dateStr2
	 * @return
	 * @throws ParseException
	 */
	public static String getBeforeDate(String dateStr1, String dateStr2) throws ParseException {
		if (stringFormatDate(dateStr1).getTime() > stringFormatDate(dateStr2).getTime()) {
			return dateStr2;
		} else {
			return dateStr1;
		}
	}
	/**
	 * @Description: 根据选择的日期和单位查找请假年份
	 * @@param date 选择的日期和天数  例如 日期1@单位1#日期2@单位2
	 * @@return   
	 */
	public static String getYearByDate(String date){
		String years = "";
		if(date.contains("#")){
			String[] arr1 = date.split("#");
			for(int i=0;i<arr1.length;i++){
				//日期@单位
				String du = arr1[i];
				String[] arr2 = du.split("@");
				//日期
				String leaveDate = arr2[0];
				//年份
				String leaveYear = leaveDate.substring(0, 4);
				if(years.equals("")){
					years += "'"+leaveYear+"'";
				}else{
					if(!years.contains(leaveYear)){
						years += ",'"+leaveYear+"'";
					}
				}
			}
			
		}else{
			String[] arr3 = date.split("@");
			years +="'"+arr3[0].substring(0, 4)+"'";
		}
		return years;
	}
	/**
	 * @Description: 根据年假的选择日期和单位查找请假天数
	 * @@param date 选择的日期和天数  例如 日期1@单位1#日期2@单位2
	 * @@return
	 */
	public static String getDaysByDate(String date) throws Exception{
		String days = "";
		double beforeDays = 0;
		double afterDays = 0;
		String year = date.substring(0, 4);
		String comDate = year+"-03-20";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sf.parse(comDate);
		if(date.contains("#")){
			String[] arr1 = date.split("#");
			for(int i=0;i<arr1.length;i++){
				//日期@单位
				String du = arr1[i];
				String[] arr2 = du.split("@");
				//日期
				String leaveDate = arr2[0];
				//单位
				String unit = arr2[1];
				Date date2 = sf.parse(leaveDate);
				//判断请假日期在3.20之前还是之后
				 if (date1.getTime() >= date2.getTime()) {//在3.20之前
					 beforeDays +=Double.parseDouble(unit);
				 }else{ //在3.20之后
					 afterDays += Double.parseDouble(unit);
				 }
				
			}
			
		}else{
			String[] arr3 = date.split("@");
			String leaveDate1 = arr3[0];
			String unit1 = arr3[1];
			Date date3 = sf.parse(leaveDate1);
			//判断请假日期在3.20之前还是之后
			 if (date1.getTime() >= date3.getTime()) {//在3.20之前
				 beforeDays +=Double.parseDouble(unit1);
			 }else{ //在3.20之后
				 afterDays += Double.parseDouble(unit1);
			 }
		}
		days = beforeDays+";"+afterDays;
		return days;
	}
	
	/**
	 * 获取当前时间戳
	 * 
	 * 用于在Java中生成时间戳，并通过Dao层赋值给Oracle的TIMESTAMP类型的属性。
	 * 若要读取Oracle的TIMESTAMP类型的字段值并显示在前端，
	 * 可使用String com.yusys.core.common.utils.DateTimeUtils.timestampToString(Timestamp timestamp)方法。
	 * 
	 * @return 返回java.sql.Timestamp类型的时间戳
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	/**
	 * 将时间戳转换为字符串
	 * 
	 * 用于把java.sql.Timestamp类型的时间戳转换为文本格式并显示在前端。
	 * 若要在Java中生成时间戳，以便通过Dao层赋值给Oracle的TIMESTAMP类型的属性，
	 * 可使用Timestamp com.yusys.core.common.utils.DateTimeUtils.getTimestamp()方法。
	 * 
	 * @param timestamp java.sql.Timestamp类型的时间戳
	 * @return 返回java.lang.String类型的字符串，格式：yyyy-MM-dd 24HH:mm:ss.SSS
	 */
	public static String timestampToString(Timestamp timestamp) {
		return new SimpleDateFormat(DateTimeUtils.timeStamp25Bit).format(new Date(timestamp.getTime()));
	}
	
	/**
	 * 获取当前日期
	 * 
	 * 用于在Java中生成日期，并通过Dao层赋值给Oracle的DATE类型的属性。
	 * 若要读取Oracle的DATE类型的字段值并显示在前端，
	 * 可使用String com.yusys.core.common.utils.DateTimeUtils.dateToString(Date date)方法。
	 * 
	 * @return 返回java.sql.Date类型的日期
	 */
	public static java.sql.Date getDate() {
		return new java.sql.Date(new Date().getTime());
	}
	
	/**
	 * 将日期转换为字符串
	 * 
	 * 用于把java.sql.Date类型的日期转换为文本格式并显示在前端。
	 * 若要在Java中生成日期，以便通过Dao层赋值给Oracle的DATE类型的属性，
	 * 可使用Date com.yusys.core.common.utils.DateTimeUtils.getDate()方法。
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(java.sql.Date date) {
		return new SimpleDateFormat(DateTimeUtils.dateFormat).format(new Date(date.getTime()));
	}
	
	/**
	 * 根据当前日期获取所在季度最后一天日期
	 * @return
	 * @author wuhaiqin
	 * @time 2016-9-20
	 */
	public static String getQuaLastDayCurDate() {
		//获取当前月
		int month = Integer.parseInt(getCurrentMonth());
		//定义返回月+日
		String monthDay="";
		//根据当前月获取季度最后一天
		if (month >= 1 && month <= 3) {
			monthDay = "03-31";
		} else if (month >= 4 && month <= 6) {
			monthDay = "06-30";
		} else if (month >= 7 && month <= 9) {
			monthDay = "09-30";
		} else {
			monthDay = "12-31";
		}
		return getCurrentYear() + "-" + monthDay;
	}
	
	/**
	 * 根据当前日期获取所在季度第一天日期
	 * @return
	 * @author wuhaiqin
	 * @time 2016-9-20
	 */
	public static String getQuaFirstDayCurDate() {
		String quaLastDay = getQuaLastDayCurDate();
		return quaLastDay.substring(0,quaLastDay.lastIndexOf("-")+1)+"01";
	}
	
    /**
     * 获取当前周
     * @return
     */
    public static int getCurrentWeek(){
	    Calendar calendar = Calendar.getInstance(); 
	    int weekOfYear=calendar.get(Calendar.WEEK_OF_YEAR); 
	    return weekOfYear;
	}
    
    /**
	 * 根据日期取考勤周期
	 * 
	 * @param attendanceDate 日期 2012-10-30
	 * @return 返回该日期所在的考勤周期 2012-11
	 */
	public static String getAttPeriodByAttDate(String attendanceDate) {
		String result = null;
		System.out.print("根据日期：" + attendanceDate);
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			calendar.setTime( ft.parse(attendanceDate) );
			if (calendar.get(Calendar.DATE) >= 21) {
				calendar.add(Calendar.MONTH, 1);
			}
			result = ft.format(calendar.getTime()).substring(0,7);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("，获取考勤周期：" + result);
		return result;
	}
	
	/**
	 * 字符串转换为时间戳
	 * @param times
	 * @return
	 * @throws ParseException 
	 */
	public static long stringToTimeStamp(String timestr){
		long timeStamp =0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
			Date date = sdf.parse(timestr);
			timeStamp= date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeStamp;
	}
	
	/**
	 * 毫秒转换为时分秒
	 * 
	 * @param shijianduan
	 * @param parameter
	 * @return
	 */
	public static String timeStampToSecond(long times) {
		String result = null;
		try {
			long millisecond = times / 1000;
			long s = millisecond % 60; // 秒
			long mi = (millisecond - s) / 60 % 60; // 分钟
			long h = ((millisecond - s) / 60 - mi) / 60 % 24; // 小时
			long d = (((millisecond - s) / 60 - mi) / 60 - h) / 24;// 天
			if (d != 0) {
				return d + "天" + h + "小时" + mi + "分钟" + s + "秒";
			} else if (d == 0 & h != 0) {
				return h + "小时" + mi + "分钟" + s + "秒";
			} else if (d == 0 & h == 0 & mi != 0) {
				return mi + "分钟" + s + "秒";
			} else {
				return s + "秒";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) throws ParseException {
//		System.out.println(DateTimeUtils.getTimeDifference("2016-11-11 00:00:01", "2016-11-11 00:00:02", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(stringToTimeStamp("2017-12-07 15:25:03"));
		
		System.out.println(timeStampToSecond(12000));
	}
	
}
