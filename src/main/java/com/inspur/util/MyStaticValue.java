
package com.inspur.util;
/**
 * desc：  全局信息读取
 * autor：ywc
 * date：2015-3-11
 * version: 1.0.0
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MyStaticValue {

	public static final Logger LIBRARYLOG = Logger.getLogger("DICLOG");
  
    
	private static ResourceBundle rb; 
	public static double SIMILAR_THREAHOLD = 0.7;
	public static String PINYIN_SPLIT_CHAR = ",";
	static{
		getConfigInfo();
	}
	
	public static void getConfigInfo(){
		/**
		 * 配置文件变量
		 */
		//ResourceBundle rb = ResourceBundle.getBundle("library"); 
		try {  
		//	String proFilePath = System.getProperty("user.dir") + "/conf/similar.properties";

//			URL proFilePath = Thread.currentThread().getContextClassLoader().getResource("/conf/similar.properties");
//			InputStream in = new BufferedInputStream(new FileInputStream(proFilePath.toString())); 
			// 读取配置文件
			String FILE_SEPARATOR = System.getProperty("file.separator");
			InputStream in = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf"+FILE_SEPARATOR+"similar.properties")); 

			rb = new PropertyResourceBundle(in);	 
			
			if (rb.containsKey("SimilarThreahold"))
				SIMILAR_THREAHOLD=Double.valueOf(rb.getString("SimilarThreahold"));
			if (rb.containsKey("PinyinSplitChar"))
				PINYIN_SPLIT_CHAR=rb.getString("PinyinSplitChar");
 
			
		}catch (FileNotFoundException e) { 
            LIBRARYLOG.warning("No such file or directory !");
		}catch (IOException e) { 
            LIBRARYLOG.warning("Can not operater the file !");
        }catch (Exception e) {
			LIBRARYLOG.warning("not find library.properties in classpath use it by default !");
		}
	}
	
	public final static String classesPath ="classes/";
	public final static String DD ="###########################################!!!@@";
	public final static Set<String> keys = new HashSet<String>();
	public final static String dateFormat = "yyyyMMddHHmmss";
	
	//求前后几天
	public static String SometimeFromDate(String str,int days){
		return SometimeFromDate(str,dateFormat,days);
	}
	public static String SometimeFromDate(String str,String format,int days){
		SimpleDateFormat formatter = new SimpleDateFormat(format); 
		Date date;
		String time="";
		try {
			if (str!="") {
				date = formatter.parse(str);
			}else {
				date = new Date();
			}
			date = new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
			time = formatter.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	
	//时间格式转换
	public static String TimeFormat(String str,String format,String destFormat){
		SimpleDateFormat formatter = new SimpleDateFormat(format); 
		Date date;
		String time="";
		try {
			if (str!="") {
				date = formatter.parse(str);
				date = new Date(date.getTime() - 2 * 24 * 60 * 60 * 1000);
			}else {
				date = new Date();
			}
			formatter= new SimpleDateFormat(destFormat); 
			time = formatter.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	//整数时间戳格式转换
	public static String TimeFormat(Long stamp, String destFormat){
		SimpleDateFormat formatter = new SimpleDateFormat(destFormat); 
		Date date = new Date(stamp);
		String time="";
		time = formatter.format(date);
		return time;
	}
	//获取当前时间
	public static String getCurrent( String destFormat){
		SimpleDateFormat df = new SimpleDateFormat(destFormat);//设置日期格式 
		java.util.Date utilDate=new java.util.Date(); 
		df.format(utilDate);
		return df.format(utilDate);
	}
	
	//统一格式获取当前时间
	public static String getCurrent(){
		return getCurrent(dateFormat);
	}
	//统一格式时间格式转换
	public static String TimeFormat(String str,String format){ 
		return TimeFormat(str,format,dateFormat);
	}
	//统一格式时间格式转换
	public static String TimeFormat(Long stamp){ 
		return TimeFormat(stamp,dateFormat);
	}
	
	public enum WEBSITECATEGORY{
		BBS,WEIBO,NEWS
	}
	
	public static boolean isNumeric(String str){ 
		Pattern pattern = Pattern.compile("[0-9]*"); 
		return pattern.matcher(str).matches(); 
	}
	//遍历目录
	public static void visitAllDirsAndFiles(File dir,List<File> fileList) throws IOException {
		
		if (dir.isDirectory()) {
	    		//过滤点linux 点文件
			FilenameFilter filter = new FilenameFilter() {
		    	public boolean accept(File dir, String name) {
		    		return !name.startsWith(".");
		    	}
	    	};
	    	String[] children = dir.list(filter);
	    	   
	        for (int i=0; i<children.length; i++) {
	              visitAllDirsAndFiles(new File(dir,children[i]),fileList);
	        }
		}else {
			fileList.add(dir);			
		}
	}
	//段落分句
	public static List<String> segSentenceByPunctuation(String Line){
		List<String> senList = new ArrayList<String>();
		String[] strJ = new String[]{};
		//先根据句号分
		strJ=Line.split("。");
		for (int i = 0; i < strJ.length; i++) {
			String tmp  = strJ[i];
			if ( !tmp.endsWith("？") && !tmp.endsWith("！")) {
				tmp+="。";
			}
			String[] strW = tmp.split("？");
			for (int j = 0; j < strW.length; j++) {
				String tt  = strW[j];
				if ( !tt.endsWith("。")&& !tt.endsWith("！")) {
					tt+="？";
				}
				String[] strT = tt.split("！");
				for (int k = 0; k < strT.length; k++) { 
					if ( !strT[k].endsWith("？") &&  !strT[k].endsWith("。") ) {
						strT[k]+="！";
					}
					senList.add(strT[k]);
				}			
				strT=null;
			}
			strW = null;
		}
		return senList;
	}
	
	//判断是否空白字符串
	public static boolean isBlank(String str) {
		return null == str || str.isEmpty() || str.trim().isEmpty();
	}
}
