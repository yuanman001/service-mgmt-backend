package com.ai.paas.ipaas.rcs.util;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 */
public class StringUtils extends org.springframework.util.StringUtils{
	/**
	 * 获取一个字符串首字母大写的格式 例如：输入adminInfo将返回AdminInfo
	 */
	public static String getFirstUp(String srcString) {
		return (srcString.charAt(0) + "").toUpperCase() + srcString.substring(1);
	}

	/**
	 * 获取一个字符串首字母小写的格式 例如：输入AdminInfo将返回adminInfo
	 */
	public static String getFirstLower(String srcString) {
		return (srcString.charAt(0) + "").toLowerCase() + srcString.substring(1);
	}

	/**
	 * 判断一个字符串是否为空，如果为空返回true,否则返回false.
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
	/**
	 * 判断一个字符串是否为空，如果为空返回false,否则返回true.
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	/**
	 * 如果字符串为null，抛异常
	 */
	public static String trimOrNullException(String source, String msg) {
		if (source == null) {
			throw new RuntimeException("请输入【" + msg + "】");
		} else {
			return source.trim();
		}
	}

	/**
	 * 如果字符串为null或空，抛异常
	 */
	public static String trimOrNullAndBlankException(String source, String msg) {
		if (isBlank(source)) {
			throw new RuntimeException("请输入【" + msg + "】");
		} else {
			return source.trim();
		}
	}
	/**
	 * 如果字符串为null或空，抛异常,(有转码动作)
	 */
	public static String trimOrNullAndBlankExceptionWithDecode(String source, String msg) throws Exception {
		if (isBlank(source)) {
			throw new RuntimeException("请输入【" + msg + "】");
		} else {
			return URLDecoder.decode(source.trim(), "UTF-8");
		}
	}
	/**
	 * trim字符串或返回空
	 */
	public static String trimOrReturnNull(String source){
		if (source == null) {
			return null;
		} else { 
			return source.trim();
		}
	}
	/**
	 * trim字符串或返回空，抛异常,(有转码动作)
	 */
	public static String trimOrReturnNullWithDecode(String source) throws Exception {
		if (source == null) {
			return null;
		} else {
			return URLDecoder.decode(source.trim(), "UTF-8");
		}
	}

	/**
	 * 如果字符串不能转换成long，抛异常
	 */
	public static long trimLongException(String source, String msg) {
		if (isBlank(source)) {
			throw new RuntimeException("trimLongException【" + msg + "】"+"["+source+"]");
		} else {
			try {
				return Long.parseLong(source);
			} catch (Exception e) {
				throw new RuntimeException("trimLongException(不是long数值)【" + msg + "】");
			}
		}
	}
	/**
	 * 如果字符串不能转换成long，抛异常
	 */
	public static long trimLongDefaultZeroException(String source, String msg) {
		if (isBlank(source)) {
			return 0L;
		} else {
			try {
				return Long.parseLong(source);
			} catch (Exception e) {
				throw new RuntimeException("trimLongException(不是long数值)【" + msg + "】");
			}
		}
	}
	/**
	 * 如果字符串不能转换成int，抛异常
	 */
	public static int trimIntException(String source, String msg) {
		if (isBlank(source)) {
			throw new RuntimeException("trimIntException【" + msg + "】");
		} else {
			try {
				return Integer.parseInt(source);
			} catch (Exception e) {
				throw new RuntimeException("trimIntException(不是int数值)【" + msg + "】");
			}
		}
	}
	/**
	 * 默认字符串为1
	 */
	public static int trimIntDefaultOne(String source, String msg) {
		if (isBlank(source)) {
			return 1;
		} else {
			try {
				return Integer.parseInt(source);
			} catch (Exception e) {
				return 1;
			}
		}
	}
	/**
	 * 默认字符串为10
	 */
	public static int trimIntDefaultTen(String source, String msg) {
		if (isBlank(source)) {
			return 10;
		} else {
			try {
				return Integer.parseInt(source);
			} catch (Exception e) {
				return 1;
			}
		}
	}
	/**
	 * 将字符串数组装换成以逗号间隔的字符串
	 */
	public static String toStringFromArray(String[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				stringBuilder.append("," + array[i]);
			} else {
				stringBuilder.append(array[i]);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * 截取字符串并加上...
	 */
	public static String subStringAndDdd(String srcString, int len) {
		if (srcString.length() <= len) {
			return srcString;
		} else {
			return srcString.substring(0, len) + "...";
		}
	}

	/**
	 * 截取HTML字符串并加上...
	 */
	public static String subHTMLStringAndDdd(String srcString, int len) {
		String retString = srcString.replaceAll("<[^>]*>", "");
		if (retString.length() <= len) {
			return retString;
		} else {
			return retString.substring(0, len) + "...";
		}
	}
	/**
	 * 截取HTML字符串并加上...
	 */
	public static String subHTMLStringWithImg(String srcString, int len) {
		int startIndex=srcString.indexOf("<img");
		String imgString="";
		if (startIndex>=0) {
			int endIndex=srcString.indexOf("/>",startIndex);
			imgString="<br/>"+srcString.substring(startIndex,endIndex+2);
		}
		String retString = srcString.replaceAll("<[^>]*>", "");
		if (retString.length() <= len) {
			return retString+imgString;
		} else {
			return retString.substring(0, len) + "..."+imgString;
		}
	}
	/**
	 * 由字节数组生成字符串
	 */
	public static String newStringFromBytes(byte[] bytes){
		return new String(bytes);
	}
	public static int fromTimeToSecond(String time){
		int ret=0;
		String regEx = "[0-9][0-9]";
		Pattern pat = Pattern.compile(regEx);  
		Matcher mat = pat.matcher(time);  
		int sort=0;
		while (mat.find()) {
			if (sort==0) {
				ret+=60*Integer.parseInt(mat.group());
			}
			if (sort==1) {
				ret+=Integer.parseInt(mat.group());
			}
			sort++;
		}
		return ret;
	}
	public static String[] lrc(String line){
		if (isBlank(line)) {
			return null;
		}
		//
		if (line.lastIndexOf("]")==-1) {
			return null;
		}
		String content=line.substring(line.lastIndexOf("]")+1).trim();
		List<String> retList=new ArrayList<String>();
		String regEx = "\\[[0-9][0-9]:[0-9][0-9]\\.[0-9][0-9]\\]";
		Pattern pattern = Pattern.compile(regEx);  
		Matcher matcher = pattern.matcher(line);  
		while (matcher.find()) {
			String time=matcher.group();
			retList.add(fromTimeToSecond(time)+"");
			retList.add(content);
		}
		String[] ret=new String[retList.size()];
		return retList.toArray(ret);
	}
	/**
	 * object默认null
	 */
	public static String defaultNull(Object obj){
		return obj==null?null:obj.toString();
	}
	/**
	 * 美化（中文一个英文两个，以英文为单位）截取字符串并加上...
	 */
	public static String beautySubStringAndDdd(String srcString, int len) {
		if (srcString.length() <= len) {
			return srcString;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			int nowLen = 0;
			for (int i = 0; i < srcString.length(); i++) {
				char c = srcString.charAt(i);
				if (c <= 256) {
					nowLen += 1;
				}else {
					nowLen += 2;
				}
				if (nowLen <= len) {
					stringBuilder.append(c);
				}else {
					break;
				}
			}
			return stringBuilder.toString() + "...";
		}
	}
	/**
	 * 取出一个文件名的后缀
	 */
	public static String getFilenameLastStr(String str){
		int d = str.indexOf(".");
		if (d != -1) {
			return str.substring(d);
		}else {
			return "";
		}
	}
	/**
	 * 数值转换成固定长度做的字符串
	 */
	public static String numToStreamNum(int num,int len){
		String ret = "";
		int length = len-(num+"").length();
		for (int i = 0; i < length; i++) {
			ret += "0";
		}
		return ret + num;
	}
	/**
	 * 获取文件全路径的文件名
	 */
	public static String getFileName(String filePath){
		int las1 = filePath.lastIndexOf("/");
		int last2 = filePath.lastIndexOf("\\");
		int last = las1 > last2?las1:last2;
		return filePath.substring(last+1);
	}
	public static void main(String[] args) {
		//System.out.println(Arrays.toString(lrc("[02:50.00] [02:50.00] the movement you need is on your shoulder.")));;
		System.out.println(getFileName("d:\\weichuang/a/d/a.txt"));
	}
}
