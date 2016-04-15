package com.ai.paas.ipaas.rcs.util;

import java.io.IOException;
import java.io.InputStream;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.ai.paas.ipaas.rcs.constants.Constants;
/**
 * 
 * @author ja-ence
 *
 */
public class AgentClientUtil {
	/**
	 * 执行远程主机上命令，并且返回命令结果字符串
	 * 
	 * @param uri
	 * @param runningDir
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static String doRemoteCommand(String uri, String runningDir, String cmd) {
		String param = "CMD| path=" + runningDir + " , cmd=" + cmd;
		LogUtil.LOG.debug("uri:" + uri + ", path:" + param);
		ClientResource resource = new ClientResource(uri);
		Representation post = resource.post(param);
		String result = null;
		try {
			result = post.getText();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (resource != null)
				resource.release();
		}
		return result;
	}

	/**
	 * 上传文件到远程主机
	 * 
	 * @param uri
	 * @param is
	 * @param filePath
	 * @return 返回成功或者失败，true为成功
	 */
	public static boolean doRemoteUploadFile(String ip, InputStream is, String filePathInRemote) {
		throw new IllegalAccessError("该方法当前不支持：public static boolean doRemoteUploadFile(String ip, InputStream is, String filePathInRemote) 。");
	}

	/**
	 * 上传文件到远程主机
	 * @param ip
	 * @param fileContent
	 * @param filePathInRemote
	 * @return
	 * @throws IOException 
	 */
	public static boolean doRemoteUploadFile(String ip, String fileContent, String filePathInRemote){
		ClientResource resource =null;
		try {
			String uri = "http://" + ip + ":" + Constants.AGENT_INFO.PORT + Constants.AGENT_INFO.REMOTE_UPLOAD_SV_CODE + "?path="+filePathInRemote;
			resource = new ClientResource(uri);
			// 提交数据
			Representation rep = resource.post(fileContent);
			String result = rep.getText();
			return result == null?false:(Constants.AGENT_INFO.OPER_SUCCESS_RESULT.equals(result.trim())?true:false); 
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(resource!=null)
				resource.release();
		}
	}

	/**
	 * 往远程主机的xml文件中指定的节点后添加新的xml节点
	 * @param uri
	 * @param filePath
	 * @param eleName
	 * @param xmlNodeString
	 * @return
	 */
	public static boolean doRemoteAppendXMLNode(String ip, String filePathInRemote, String eleName, String xmlNodeString) {
		//组成uri
		String uri = "http://" + ip + ":" + Constants.AGENT_INFO.PORT + Constants.AGENT_INFO.REMOTE_XML_APPEND_SV_CODE + "?path="+filePathInRemote+"&eleName="+eleName;
		ClientResource client = null;
		//创建客户端资源
		try{
			client = new ClientResource(uri);
			Representation rep = client.post(xmlNodeString);
			String result = rep.getText();
			return result == null?false:(Constants.AGENT_INFO.OPER_SUCCESS_RESULT.equals(result.trim())?true:false);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(client!=null)
				client.release();
		}
	}
}
