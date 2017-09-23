
package org.zagelnews.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public abstract class DateUtils {
	public static final String EVOTING_FULL_DATE_FORMAT = "MMM dd, yyyy HH:mm:ss";
	public static final String EVOTING_SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
	public static final String EVOTING_SIMPLE_DATE_FORMAT_INTEGRATION = "yyyy-MM-dd";
	public static final String EVOTING_SIMPLE_TIME_FORMAT = "HH:MM" ;
	public static final int YEAR = GregorianCalendar.YEAR;
	public static final int MONTH = GregorianCalendar.MONTH;
	public static final int DAY = GregorianCalendar.DAY_OF_MONTH;
	public static final int HOUR = GregorianCalendar.HOUR;
	public static final int MINUTE = GregorianCalendar.MINUTE;
	public static final int MIN_OF_DAY_OFFSET = 1439; //24(hour) * 60(min) = 1440 min/day - 1(0-1439)
	
	public static Timestamp getCurrentTimeStamp()
	{
		Timestamp currentTimestamp = new Timestamp((new Date()).getTime());
		return (currentTimestamp);
	}
	
	public static Date getCurrentDate()
	{
		return new Date();
	}
	
	/**
	 * get Full date; with time
	 * @return
	 */
	public static Date getCurrentFullDate(){
		return new Date();
	}
	
	public static Integer getCurrentFeildValue(int feild){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(getCurrentDate());
		return cal.get(feild);
		
	}
	
	public static String formatDate(Date date){
		return formatDate(date, EVOTING_SIMPLE_DATE_FORMAT) ;
	}
	
	public static String formatFullDate(Date date){
		return formatDate(date, EVOTING_FULL_DATE_FORMAT) ;
	}
	
	public static String formatDate(Date date, String format){
		if(date != null){
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		}
		return null ;
	}
	
	public static Date parseFullDate(String dateStr){
		try{
			return parseDate(dateStr, EVOTING_FULL_DATE_FORMAT);
		}catch(java.text.ParseException exp){
			return parseDate(dateStr);
		}
	}
	
	public static Date parseDate(String dateStr){
		try{
			return parseDate(dateStr, EVOTING_SIMPLE_DATE_FORMAT);
		}catch(java.text.ParseException exp){
			return null;
		}
	}
	
	public static Date parseDate(String dateStr, String format)throws ParseException{
		if(dateStr != null){
			SimpleDateFormat  dateFormat = new SimpleDateFormat(format,  Locale.US);
			Date date = dateFormat.parse(dateStr.replaceAll("\\p{Cntrl}", ""));
			//Date date = dateFormat.parse(dateStr) ;
			return date ;
		}		
		return null ;
	}
	
	public static String formatTime(Date date){
		if(date != null){
			DateFormat dateFormat = new SimpleDateFormat(EVOTING_SIMPLE_TIME_FORMAT);
			return dateFormat.format(date);
		}
		
		return null ;
	}
	
	public static int getYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		int year = calendar.get(Calendar.YEAR);
		return year ;
	}


	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	//Calculate difference between two dates
	public static long getDifferenceDays(Date startDate, Date endDate) {
		long startDateInMillis = startDate.getTime();
		long endDateInMillis = endDate.getTime();
		long differenctDateInMillis = endDateInMillis - startDateInMillis;
		long days = (differenctDateInMillis / (1000*60*60*24));
		
		return days;
	}
	
	public static Date setMaxMinOffset(Date date){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(EVOTING_SIMPLE_DATE_FORMAT);
			String dateString  = dateFormat.format(date);		
			Date outDate = dateFormat.parse(dateString);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(outDate);
			calendar.add(Calendar.MINUTE, MIN_OF_DAY_OFFSET);
			outDate = calendar.getTime();
			return outDate;
		}catch(ParseException pe){
		}
		
		return date ;
	}
	
	
	public static Calendar setNewTimeZoneCopyOldTime( Calendar inputTime, 
	        TimeZone timeZone ) {
	    if( (inputTime == null) || (timeZone == null) ) { return( null ); }

	    Calendar outputTime = Calendar.getInstance( timeZone );
	    for( int i = 0; i < Calendar.FIELD_COUNT; i++ ) {
	        if( (i != Calendar.ZONE_OFFSET) && (i != Calendar.DST_OFFSET) ) { 
	            outputTime.set(i, inputTime.get(i));
	        }
	    }

	    return( (Calendar) outputTime.clone() );
	}

	public static boolean validTimeZone(String id) {
	    for (String tzId : TimeZone.getAvailableIDs()) {
	            if(tzId.equals(id))
	                return true;
	    }
	    return false;
	}

	public static Integer getDiffHours(Date startDate, Date endDate){
		//DateTime startTime, endTime;
		Long secs = (endDate.getTime() - startDate.getTime()) / 1000;
		Long hours = secs / 3600; 
		return hours.intValue();
	}
}
