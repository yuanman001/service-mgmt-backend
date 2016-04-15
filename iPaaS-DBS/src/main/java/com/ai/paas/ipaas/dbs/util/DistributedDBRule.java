package com.ai.paas.ipaas.dbs.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * 分布式数据库分库分表规则的处理类
 * 
 * @author zjy
 * @date 2014年6月18日 下午3:30:41
 * @version V1.0
 */
public class DistributedDBRule {
	public static final Logger log = Logger.getLogger(DistributedDBRule.class);
	


	/**
	 * 根据计算逻辑分库名和物理表名，将计算出来的结果放到HashMap中
	 * 
	 * @param colValue
	 *            分库字段值
	 * @param id
	 *            分库或分表ID
	 * @param pattern
	 *            分库或分表样式，如cust{_00}
	 * @param map
	 *            返回结果map
	 * @throws SQLException
	 */
	public static void calculatePattern(Object colValue, Integer id,
			String pattern, HashMap<Object, String> map) throws SQLException {
		String replace = calculatePattern(id, pattern);
		map.put(colValue, replace);
	}

	/**
	 * 根据计算逻辑分库名和物理表名，将计算出来的结果放到HashMap中
	 * 
	 * @param colValue
	 *            分库字段值
	 * @param id
	 *            分库或分表ID
	 * @param pattern
	 *            分库或分表样式，如cust{_00}
	 * @param map
	 *            返回结果map
	 * @throws SQLException
	 */
	public static String calculatePattern(Integer id, String pattern)
			throws SQLException {
		String replace = null;
		if (pattern.indexOf("{") >= 0) {
			try {
				replace = pattern.substring(pattern.indexOf("{") + 1,
						pattern.indexOf("}"));
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException("invalid db pattern");
			}
		}
		if (replace == null) {
			return pattern;
		} else {
			String originalName = pattern.substring(0, pattern.indexOf("{"));// "cust"
			String prefix = "";
			if (replace.indexOf("0") != 0) {
				prefix = replace.substring(0, replace.indexOf("0"));// 中间的"_"
				replace = replace.substring(replace.indexOf("0"));// "00"
			}
			String idStr = String.valueOf(id);
			if (idStr.length() > replace.length()) {
				throw new SQLException("invalid db pattern");
			} else {
				replace = originalName
						+ prefix
						+ replace.substring(0,
								replace.length() - idStr.length()) + idStr;
			}
			return replace;
		}
	}

	/**
	 * 根据分库和分表规则计算分库或分表ID
	 * 
	 * @param json
	 *            分库或分表规则，如： {keyValue:{keyColumn:'cust_id',
	 *            tranferType:'java.lang.Long', operator:'%',operatorValue:4},
	 *            operator:'+',operatorValue:1}
	 * @param colValue
	 *            分库分表字段值
	 * @return 分库或分表ID 
	 */
	public static Integer calculateDistributeId(JSONObject json, Object colValue, HashMap<String, Map<Object, Integer>> mappingRules)
			throws SQLException {
		if (json.containsKey("keyColumn")) {
			String tranferType = json.getString("tranferType");
			Object value = null;
			try {
				if("java.lang.String".equals(tranferType)) {
					value = colValue.toString();
				}else {
					value =  Class.forName(tranferType)
							.getMethod("valueOf", colValue.toString().getClass())
							.invoke(null, colValue.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException("tranfer column Value to type "
						+ tranferType + " failed", e);
			}
			return calculateFormula(value, json.getString("operator"),
					json.get("operatorValue"), mappingRules);
		} else {
			JSONObject keyValue = json.getJSONObject("keyValue");
			Integer value = calculateDistributeId(keyValue, colValue, mappingRules);// 递归处理嵌套
			return calculateFormula(value, json.getString("operator"),
					json.get("operatorValue"), mappingRules);
		}
	}

	/**
	 * 计算公式的结果值
	 * 
	 * @param left
	 *            左值
	 * @param operator
	 *            操作符
	 * @param rigth
	 *            右值
	 * @return
	 */
	private static Integer calculateFormula(Object leftValue, String operator,
			Object rightValue, HashMap<String, Map<Object, Integer>> mappingRules) {
		if("Mapping".equals(operator)) {
			if(mappingRules == null) {
				return null;
			}
			return mappingRules.get(rightValue).get(leftValue);
		}else {
			long left = -1;
			if(leftValue instanceof Integer) {
				left =  (Integer) leftValue;
			}else {
				left =  (Long) leftValue;
			}
			int right = (Integer) rightValue;
			if ("%".equals(operator)) {
				return (int) left % right;
			} else if ("/".equals(operator)) {
				return (int) left / right;
			} else if ("+".equals(operator)) {
				return (int) left + right;
			} else if ("-".equals(operator)) {
				return (int) left - right;
			} else {
				return null;
			}
		}
	}

	



	public static void main(String[] args) {
		// ApplicationContext ctx = new ClassPathXmlApplicationContext(new
		// String[] { "distributedRule.xml" });
		// DistributedDBRule dbRule = (DistributedDBRule)ctx.getBean("dbRule");
		// String sql = "select * from cust a where a.cust_id = ?";
		// List<Object> valueArray = new ArrayList<Object>();
		// valueArray.add(0);
		// valueArray.add(1,7L);
		// System.out.println(dbRule.getLogicDBAndTableName(sql, valueArray));
	}
}
