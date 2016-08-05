package com.ai.paas.ipaas.rds.service.util;


import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.annotation.DeclareWarning;

/** 
 * 自动拼接(WHERE-AND)查询语句
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月19日 下午5:28:17 
 * @version 
 * @since  
 */
public class EntityToWhere<MyEntity extends Object> {

	public String entity2Where(MyEntity t) throws IllegalArgumentException, IllegalAccessException{
		Field[] fs = t.getClass().getFields();
		String where = " where ";
		List<String> stringList = new LinkedList<String>();
		if(fs.length != 0){
			for(Field f : fs){
				if (null != f.get(t)) {
					switch(f.getType().getSimpleName()){
					case "int":
					case "Integer":
					case "long":
					case "Long":
					case "float":
					case "Float":
					case "double":
					case "Double":
					case "boolean":
					case "Boolean":
						stringList.add( " " + f.getName() + " " + f.get(t) + " ");;
						break;
					case "String":
						stringList.add( " " + f.getName() + " '" + f.get(t) + "' ");
						break;
					case "Date":
						// 暂时不支持时间查询
						
						break;
					case "":
						break;
					default:
						break;
					}
				}
				
			}
			for(int i = 0; i < stringList.size(); i++){
				if(i == 0){
					where = where + stringList.get(i);
				} else {
					where = where + " AND " + stringList.get(i);
				}
			}
			return where;
		} else {
			return "";
		}
	}
	public String entity2WhereSort(MyEntity t,String s) throws IllegalArgumentException, IllegalAccessException{
		EntityToWhere<MyEntity> whereStrategy = new EntityToWhere<MyEntity>();
		String where = whereStrategy.entity2Where(t);
		String sort = " order by ";
		return where + sort + " " + s + " " ;
	}

}
