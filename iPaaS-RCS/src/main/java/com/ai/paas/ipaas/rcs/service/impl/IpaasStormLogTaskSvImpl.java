package com.ai.paas.ipaas.rcs.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.dto.IpaasStormLogTask;
import com.ai.paas.ipaas.rcs.service.IIpaasStormLogTaskSv;
import com.ai.paas.ipaas.rcs.constants.Constants;
import com.ai.paas.ipaas.rcs.util.AgentClientUtil;

/**
 * 日志任务注册
 * @author ja-ence
 *
 */
@Service
@Transactional
public class IpaasStormLogTaskSvImpl implements IIpaasStormLogTaskSv{
	public static void main(String[] args) {
		IpaasStormLogTask logTask = new IpaasStormLogTask();
		logTask.setLoggerName("tuple0200816");
		try {
			new IpaasStormLogTaskSvImpl().registerLogTask(logTask);
		} catch (PaasException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void registerLogTask(IpaasStormLogTask logTask) throws PaasException{
		//获取日志任务的配置信息
		//获取任务名称，作为日志序列号，在storm集群中具有唯一性
		if(logTask==null || logTask.getLoggerName()==null || "".equals(logTask.getLoggerName().trim()))
			throw new PaasException("任务的日志序列号为空！", new IllegalArgumentException("参数错误，loggerName为空"));
		String loggerName = logTask.getLoggerName().trim();
		//生成文本格式的日志配置信息
		String xmlNodeContent = Constants.LOG_TASK_INFO.LOGBACK_INCLUDED_XMLNODE_MODEL.replace(Constants.LOG_TASK_INFO.LOG_CONF_FILE_VAR_NAME, loggerName); 
		String xmlFileContent = Constants.LOG_TASK_INFO.LOGBACK_CONF_FILE_MODEL
							.replace(Constants.LOG_TASK_INFO.LOG_DIR_VAR_NAME, logTask.getLogDir()==null?Constants.LOG_TASK_INFO.LOG_DIR_DEFAULT:logTask.getLogDir())
							.replace(Constants.LOG_TASK_INFO.LOG_FILE_VAR_NAME, logTask.getLogFile()==null?Constants.LOG_TASK_INFO.LOG_FILE.replace(Constants.LOG_TASK_INFO.LOG_LOGGER_VAR_NAME, loggerName)+".log":logTask.getLogFile())
							.replace(Constants.LOG_TASK_INFO.LOG_LOGGER_VAR_NAME, loggerName==null?Constants.LOG_TASK_INFO.LOG_LOGGER_NAME_DEFAULT:loggerName)
							.replace(Constants.LOG_TASK_INFO.LOG_LOGGER_LEVEL_VAR_NAME, logTask.getLevel()==null?Constants.LOG_TASK_INFO.LOG_LOGGER_LEVEL_DEFAULT:logTask.getLevel());
		//获取配置文件绝对路径
		String confPath = Constants.LOG_TASK_INFO.LOG_CONF_DIR_DEFAULT + "/" + Constants.LOG_TASK_INFO.LOG_CONF_FILE_NAME.replace(Constants.LOG_TASK_INFO.LOG_CONF_FILE_VAR_NAME, loggerName);
		String confIncludePath = Constants.LOG_TASK_INFO.LOG_CONF_INCLUDED_FILE_PATH_DEFAULT;
		
		//由于当前agent暂时不支持环境变量，所以进行下面环境变量代替，后续将删除
		xmlNodeContent = xmlNodeContent.replace(Constants.STORM_CLUSTER_INFO.STORM_HOME,"/unibss/pocusers/devstp01/apache-storm-0.9.3");
		xmlFileContent = xmlFileContent.replace(Constants.STORM_CLUSTER_INFO.STORM_HOME,"/unibss/pocusers/devstp01/apache-storm-0.9.3");
		confPath = confPath.replace(Constants.STORM_CLUSTER_INFO.STORM_HOME,"/unibss/pocusers/devstp01/apache-storm-0.9.3");
		confIncludePath = confIncludePath.replace(Constants.STORM_CLUSTER_INFO.STORM_HOME,"/unibss/pocusers/devstp01/apache-storm-0.9.3");
		
		//遍历所有主机进行日志配置
		for(String ip : Constants.STORM_CLUSTER_INFO.HOSTS){
			//调用代理将配置文件上传到每台主机目录下
			boolean success = AgentClientUtil.doRemoteUploadFile(ip, xmlFileContent, confPath);
			//调用代理往每台主机的topology-include.xml中添加<include>节点
			if(success)
				success = AgentClientUtil.doRemoteAppendXMLNode(ip,confIncludePath ,Constants.LOG_TASK_INFO.LOGBACK_INCLUDED_NODE_NAME, xmlNodeContent);
			if(!success)
				throw new PaasException("日志任务注册失败", "代理程序调用出现问题！");
		}
	}
}
