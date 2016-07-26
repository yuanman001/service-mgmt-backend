package com.ai.paas.ipaas.agent.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.ClientProtocolException;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasSysConfigMapper;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasSysConfigCriteria;
import com.ai.paas.ipaas.util.StringUtil;

public class AgentUtil {

	private static final AtomicInteger counter = new AtomicInteger();

	private static final String SIMPLE_CMD_URI = "/simpCommand/exec";

	private static final String SIMPLE_FILE_UPLOAD_URI = "/simpFile/upload";

	public static int nextValue() {
		return counter.getAndIncrement();
	}

	public static String getCmdExecUrl(String aid) {
		String agentUrl = getAgentRootUri(aid);
		if (StringUtil.isBlank(agentUrl))
			return null;
		if (agentUrl.charAt(agentUrl.length() - 1) == '/') {
			agentUrl = agentUrl.substring(0, agentUrl.length() - 2);
		}
		return agentUrl + SIMPLE_CMD_URI;
	}

	public static String getFileUploadUrl(String aid) {
		String agentUrl = getAgentRootUri(aid);
		if (StringUtil.isBlank(agentUrl))
			return null;
		if (agentUrl.charAt(agentUrl.length() - 1) == '/') {
			agentUrl = agentUrl.substring(0, agentUrl.length() - 2);
		}
		return agentUrl + SIMPLE_FILE_UPLOAD_URI;
	}

	public static StringBuffer createBashFileHead() {
		StringBuffer shellContext = new StringBuffer();
		shellContext.append("#!/bin/bash");
		shellContext.append("\n");
		return shellContext;
	}

	public static void uploadFile(String filename, String[] contents, String aid)
			throws ClientProtocolException, IOException, PaasException {
		ExecuteEnv executeEnv = AgentUtil.genEnv();
		executeEnv.uploadFile(filename, contents, aid);
	}

	public static String executeFile(String filename, String[] contents,
			String aid) throws ClientProtocolException, IOException,
			PaasException {
		ExecuteEnv executeEnv = AgentUtil.genEnv();
		return executeEnv.executeFile(filename, contents, aid);
	}

	public static void executeCommand(String command, String aid)
			throws ClientProtocolException, IOException, PaasException {
		ExecuteEnv executeEnv = AgentUtil.genEnv();
		executeEnv.executeCommand(command, aid);
	}
	
	public static String executeCommandWithReturn(String command, String aid)
			throws ClientProtocolException, IOException, PaasException {
		ExecuteEnv executeEnv = AgentUtil.genEnv();
		return executeEnv.executeCommand(command, aid);
	}

	public static ExecuteEnv genEnv() {
		ExecuteEnv executeEnv = null;
		if (null == executeEnv) {
			executeEnv = new RemoteEnv();
		}
		return executeEnv;
	}

	private static String getAgentRootUri(String aid) {
		IpaasSysConfigCriteria instance = new IpaasSysConfigCriteria();
		IpaasSysConfigCriteria.Criteria criteria = instance.createCriteria();
		criteria.andFieldCodeEqualTo("agent_url_root");
		criteria.andTableCodeEqualTo(aid);
		IpaasSysConfigMapper mapper = ServiceUtil
				.getMapper(IpaasSysConfigMapper.class);
		List<IpaasSysConfig> list = mapper.selectByExample(instance);
		return list.get(0).getFieldValue();
		// return "http://10.1.245.4:16204/agent-web-api";
	}

	public static String getAgentFilePath(String aid) {
		IpaasSysConfigCriteria instance = new IpaasSysConfigCriteria();
		IpaasSysConfigCriteria.Criteria criteria = instance.createCriteria();
		criteria.andFieldCodeEqualTo("agent_file_path");
		criteria.andTableCodeEqualTo(aid);
		IpaasSysConfigMapper mapper = ServiceUtil
				.getMapper(IpaasSysConfigMapper.class);
		List<IpaasSysConfig> list = mapper.selectByExample(instance);
		String basePath = list.get(0).getFieldValue();
		if (null != basePath && basePath.length() > 0
				&& basePath.charAt(basePath.length() - 1) != '/') {
			basePath = basePath + "/";
		}
		return basePath;
	}

	public static String replaceIllegalCharacter(String source) {
		if (source == null)
			return source;
		return source.replaceAll("\r\n", "\n");
	}

	public static String[] readFileLines(InputStream in) {
		BufferedReader reader = null;
		List<String> cnt = new ArrayList<>();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				cnt.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return cnt.toArray(new String[cnt.size()]);
	}

	public static String[] readFileLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List<String> cnt = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				cnt.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return cnt.toArray(new String[cnt.size()]);
	}

	public static void main(String[] args) {
		try {
			String[] cmd = { "ls -l" };
			AgentUtil.uploadFile("test.sh", cmd, "slp-dev");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PaasException e) {
			e.printStackTrace();
		}
	}
}
