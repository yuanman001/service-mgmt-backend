package com.ai.paas.ipaas.des.util;

import java.util.Properties;

import com.ai.paas.ipaas.des.manage.model.DesServiceBindParam;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;

public class InstancePropertiesGenerater {
	private InstancePropertiesGenerater() {
	}

	public static Properties generateProperties(DesServiceBindParam desServiceBindParam, MdsUserTopic mdsUserTopic, String address, String username, String password, String dbName) {
		Properties properties = new Properties();
		properties.put("canal.instance.bixy.mode", "spring");
		properties.put("canal.instance.bixy.lazy", "false");
		properties.put("canal.instance.bixy.spring.xml", "classpath:spring/file-instance.xml");
		properties.put("canal.instance.mysql.slaveId", "1234");
		properties.put("canal.instance.master.address", address);
		properties.put("canal.instance.master.journal.name", "");
		properties.put("canal.instance.master.position", "");
		properties.put("canal.instance.master.timestamp", "");
		properties.put("canal.instance.dbUsername", username);
		properties.put("canal.instance.dbPassword", password);
		properties.put("canal.instance.defaultDatabaseName", dbName);
		properties.put("canal.instance.connectionCharset", "UTF-8");
		properties.put("canal.instance.filter.regex", "");
		properties.put("canal.instance.filter.black.regex", ".*\\..*");
		properties.put("paas.mds.serviceId", desServiceBindParam.getMdsServiceId());
		properties.put("paas.mds.user", desServiceBindParam.getUserName());
		properties.put("paas.mds.servicePassword", desServiceBindParam.getMdsServicePassword());
		properties.put("paas.mds.topic", mdsUserTopic.getTopicEnName());
		properties.put("paas.mds.partition", mdsUserTopic.getTopicPartitions() + "");
		properties.put("paas.mds.table.filter.rule", "");
		return properties;
	}
}
