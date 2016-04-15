package com.ai.paas.ipaas.des.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RegexUtil {
	private RegexUtil() {
	}

	public static String generateRegex(String dbName, Collection<String> tables) {
		StringBuilder regex = new StringBuilder();
		for (String table : tables) {
			regex.append(dbName + "." + table + ",");
		}
		if (regex.length() > 0)
			regex.deleteCharAt(regex.length() - 1);
		return regex.toString();
	}

	public static String generatePatern(int count) {
		return "_[0-9]{" + count + "}";
	}

	public static String generateTableRule(Map<String, String> paternMap) {
		JsonObject jo = new JsonObject();
		for (Entry<String, String> entry : paternMap.entrySet()) {
			jo.addProperty(entry.getValue(), entry.getKey());
		}
		return new Gson().toJson(jo);
	}
}
