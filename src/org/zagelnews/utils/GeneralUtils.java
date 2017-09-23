package org.zagelnews.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class GeneralUtils {

	public static String arrToCommaSeperated(String[] StringArr) {
		if(StringArr!=null&&StringArr.length>0){
			StringBuffer buff = new StringBuffer();
			for(String intId:StringArr){
				buff.append(intId).append(",");
			}
			buff.deleteCharAt(buff.length()-1);
			return buff.toString();
		}
		return "";
	}
	
	
	public static List<String> commaSeperatedToList(String commaSeperatedStr) {
		List<String> items = Arrays.asList(commaSeperatedStr.split("\\s*,\\s*"));
		return items;
	}
	
	
	  public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	             
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              //Read byte from input stream
	                 
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	               
	              //Write byte from output stream
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }
	  
	  public static boolean isStringEmpty(String str){
		  boolean isStringEmpty = false;
		  if(str==null||str.trim().length()==0||str.trim().equals("null")){
			  isStringEmpty = true;
		  }
		  return isStringEmpty;
	  }
}
