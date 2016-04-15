package com.ai.paas.ipaas.des.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.des.service.ICcsServiceWraper;
import com.ai.paas.ipaas.des.util.DesContants;
import com.ai.paas.ipaas.des.util.RegexUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class ICcsServiceWraperImpl implements ICcsServiceWraper {

	@Autowired
	private ICCSComponentManageSv ccsComponentManageSv;

	@Override
	public Map<String, String> getTablePaterns(String userId, String serviceId, List<String> tables) throws PaasException {
		Map<String, String> paternMap = new HashMap<>();
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setUserId(userId);
		param.setPathType(PathType.READONLY);
		for (String tableName : tables) {
			param.setPath("/DBS/" + serviceId + "/tableRules/" + tableName);
			String data = ccsComponentManageSv.get(param);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(data, JsonObject.class);
			String tableNamePattern = jo.get("tableNamePattern").getAsString();
			if (tableNamePattern.indexOf(DesContants.DBS_TABLE_PATERN_LEFT) > 0) {
				paternMap.put(tableName,
						tableName + RegexUtil.generatePatern(tableNamePattern.indexOf(DesContants.DBS_TABLE_PATER_RIGHT) - tableNamePattern.indexOf(DesContants.DBS_TABLE_PATERN_LEFT) - 1));
			} else {
				paternMap.put(tableName, tableName);
			}
		}
		return paternMap;
	}

	@Override
	public String[] getUnbindTables(String userId, String serviceId, String[] tables) throws PaasException {
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setUserId(userId);
		param.setPathType(PathType.READONLY);
		param.setPath("/DBS/" + serviceId + "/tableRules");
		if (ccsComponentManageSv.exists(param)) {
			List<String> allTables = ccsComponentManageSv.listSubPath(param);
			if (CollectionUtils.isNotEmpty(allTables)) {
				if (allTables.size() > tables.length) {
					String[] unBindTables = new String[allTables.size() - tables.length];
					int i = 0;
					for (String table : allTables) {
						if (notInBind(table, tables)) {
							unBindTables[i] = table;
							i++;
						}
					}
					return unBindTables;
				}
			}
		}
		return new String[0];
	}

	private boolean notInBind(String table, String[] bindTables) {
		for (String eachTable : bindTables) {
			if (eachTable.equals(table))
				return false;
		}
		return true;
	}
}
