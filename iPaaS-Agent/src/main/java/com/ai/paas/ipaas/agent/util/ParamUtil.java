package com.ai.paas.ipaas.agent.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class ParamUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, String> getParamMap(String applyParam) {
		Gson gson = new Gson();
		return gson.fromJson(applyParam, Map.class);
	}

	public static String replace(String source, String[] replaces) {
		Matcher m = Pattern.compile("\\{(\\d+)\\}").matcher(source);
		while (m.find()) {
			source = source.replace(m.group(),
					replaces[Integer.parseInt(m.group(1))]);
		}
		return source;
	}

	public static void main(String[] args) {
	}
}
