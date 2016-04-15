package com.ai.paas.ipaas.dbs.util;

import org.apache.log4j.Logger;

public class LogUtil {

	
	
	public static void  error(Logger logger,Exception e) {
		StringBuilder builder = new StringBuilder();
		int size = e.getStackTrace().length;
		 for(int i = 0; i < size;i++) {
			 builder.append(" " + e.getStackTrace()[i].getClassName() + " " + e.getStackTrace()[i].getMethodName() + "( " + e.getStackTrace()[i].getFileName() +" " + e.getStackTrace()[i].getLineNumber()+")\n");
		 }
		 logger.error(builder.toString());
		 builder.setLength(0);
		 builder = null;
		
		
	}

}
