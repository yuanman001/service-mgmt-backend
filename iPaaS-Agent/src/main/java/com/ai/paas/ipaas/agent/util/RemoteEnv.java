package com.ai.paas.ipaas.agent.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.agent.vo.TransResultVo;
import com.ai.paas.ipaas.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RemoteEnv implements ExecuteEnv {

	private static Logger logger = Logger.getLogger(RemoteEnv.class);

	@Override
	public void uploadFile(String filename, String[] contents, String aid)
			throws ClientProtocolException, IOException, PaasException {
		String filepath = AgentUtil.getAgentFilePath(aid);
		// upload execute shell
		// 这里要对filename做个处理，如果filename是个相对路径，则判断是否存在，不存在创建
		if (filepath.charAt(filepath.length() - 1) != '/')
			filepath += "/";
		if (filename.charAt(0) == '/') {
			filename = filename.substring(1);
		}
		if (filename.indexOf("/") >= 0) {
			// 需要处理
			executeCommand(
					"mkdir -p " + filepath
							+ filename.substring(0, filename.lastIndexOf("/")),
					aid);
		}
		String url = AgentUtil.getFileUploadUrl(aid);
		StringEntity paramEntity = RemoteEnv.genFileParam(contents, filename,
				filepath, aid);
		RemoteEnv.sendRequest(url, paramEntity);
	}

	public static String sendRequest(String url, StringEntity paramEntity)
			throws ClientProtocolException, IOException, PaasException {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			int timeout = 120;
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(timeout * 1000)
					.setConnectionRequestTimeout(timeout * 1000)
					.setSocketTimeout(timeout * 1000).build();
			httpClient = HttpClientBuilder.create()
					.setDefaultRequestConfig(config).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(paramEntity);
			response = httpClient.execute(httpPost);
			String result = null;
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				result = new String();
				if (entity != null) {
					InputStream instream = entity.getContent();
					InputStreamReader inputStream = new InputStreamReader(
							instream, "UTF-8");
					try {
						BufferedReader br = new BufferedReader(inputStream);
						result = br.readLine();
					} finally {
						instream.close();
					}
				}
				Gson gson = new Gson();
				TransResultVo resultVo = gson.fromJson(result,
						TransResultVo.class);

				if (!url.contains("upload")) {
					String excResult = resultVo.getMsg();
					JsonParser parser = new JsonParser();
					JsonObject o = parser.parse(excResult).getAsJsonObject();
					String stderr = o.get("stderr").getAsString();
					String stdout = o.get("stdout").getAsString();
					// judge the result with stderr and stdout
					if (!resultVo.getCode().equals("" + 0)) {
						System.out.println(stderr);
						logger.error(stderr);
						throw new PaasException(
								PaaSConstant.ExceptionCode.SYSTEM_ERROR,
								resultVo.getMsg());
					}
					// analyze stdout��
					if (stdout.contains("unreachable")) {
						String pattern = "unreachable=[1-9]";
						Pattern r = Pattern.compile(pattern);
						Matcher m = r.matcher(stdout);
						if (m.find()) {
							System.out.println(stdout);
							logger.error(stdout);
							throw new PaasException(
									PaaSConstant.ExceptionCode.SYSTEM_ERROR,
									resultVo.getMsg());
						}
					}
					if (stdout.contains("failed")) {
						String pattern = "failed=[1-9]";
						Pattern r = Pattern.compile(pattern);
						Matcher m = r.matcher(stdout);
						if (m.find()) {
							System.out.println(stdout);
							logger.error(stdout);
							throw new PaasException(
									PaaSConstant.ExceptionCode.SYSTEM_ERROR,
									resultVo.getMsg());
						}
					}
					if (!StringUtil.isBlank(stderr)) {
						System.out.println(stderr);
						logger.error(stderr);
						throw new PaasException(
								PaaSConstant.ExceptionCode.SYSTEM_ERROR,
								resultVo.getMsg());
					}
					System.out.println("code:" + resultVo.getCode()
							+ ";stderr:" + stdout + ";stderr:" + stderr);
					logger.debug("code:" + resultVo.getCode() + ";stderr:"
							+ stdout + ";stderr:" + stderr);
				}
			} else {
				throw new PaasException(""
						+ response.getStatusLine().getStatusCode(), response
						.getStatusLine().getReasonPhrase());
			}
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != response) {
				response.close();
			}
			if (null != httpClient) {
				httpClient.close();
			}
		}
	}

	@Override
	public String executeFile(String filename, String[] contents, String aid)
			throws ClientProtocolException, IOException, PaasException {
		String filepath = AgentUtil.getAgentFilePath(aid);
		// upload execute file
		this.uploadFile(filename, contents, aid);

		// change the permission of file
		this.executeCommand("chmod u+x " + filepath + "/" + filename, aid);

		// execute shell file
		String result = this.executeCommand(
				"bash " + filepath + "/" + filename, aid);
		return result;
	}

	public static StringEntity genFileParam(String[] contents, String filename,
			String path, String aid) throws UnsupportedEncodingException {
		JsonObject object = new JsonObject();
		object.addProperty("aid", aid);
		// 组合起来
		StringBuilder sb = new StringBuilder();
		for (String content : contents) {
			sb.append(content).append("\n");
		}
		object.addProperty("content", sb.toString());
		object.addProperty("fileName", filename);
		object.addProperty("path", path);
		StringEntity entity = new StringEntity(object.toString(),
				StandardCharsets.UTF_8);
		entity.setContentType("application/json");
		return entity;

	}

	public static StringEntity genCommandParam(String command, String aid)
			throws UnsupportedEncodingException {
		JsonObject object = new JsonObject();
		object.addProperty("aid", aid);
		object.addProperty("command", command);
		StringEntity entity = new StringEntity(object.toString(),
				StandardCharsets.UTF_8);
		entity.setContentType("application/json");
		return entity;
	}

	@Override
	public String executeCommand(String content, String aid)
			throws ClientProtocolException, IOException, PaasException {
		String url = AgentUtil.getCmdExecUrl(aid);
		StringEntity paramEntity = RemoteEnv.genCommandParam(content, aid);
		String result = RemoteEnv.sendRequest(url, paramEntity);
		System.out.println("command content:" + content);
		logger.debug("command content:" + content);
		return result;
	}

	public static void main(String[] args) {
		ExecuteEnv executeEnv = null;
		try {
			executeEnv = new RemoteEnv();
			String result = executeEnv.executeCommand("ls -l", "SLP");
			System.out.println(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PaasException e) {
			e.printStackTrace();
		}
	}
}
