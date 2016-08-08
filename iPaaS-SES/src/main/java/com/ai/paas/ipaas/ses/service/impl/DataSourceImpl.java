package com.ai.paas.ipaas.ses.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ses.dao.interfaces.SesDataimportDsMapper;
import com.ai.paas.ipaas.ses.dao.interfaces.SesDataimportSqlMapper;
import com.ai.paas.ipaas.ses.dao.interfaces.SesDataimportUserMapper;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesDataimportDs;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesDataimportDsCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesDataimportSql;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesDataimportSqlCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesDataimportUser;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesDataimportUserCriteria;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.interfaces.IDataSource;
import com.ai.paas.ipaas.vo.ses.SesDataSourceInfo;
import com.ai.paas.ipaas.vo.ses.SesIndexFiledSql;
import com.ai.paas.ipaas.vo.ses.SesIndexPrimarySql;
import com.ai.paas.ipaas.vo.ses.SesIndexSqlInfo;
import com.google.gson.Gson;

@Service
@Transactional(rollbackFor = Exception.class)
public class DataSourceImpl implements IDataSource {
	private static final transient Logger log = LoggerFactory
			.getLogger(DataSourceImpl.class);

	public void saveDataSource(Map<String, String> userInfo,
			List<SesDataSourceInfo> dataSources) {
		Assert.notEmpty(userInfo);
		Assert.notEmpty(dataSources);

		SesDataimportUserMapper userMapper = ServiceUtil
				.getMapper(SesDataimportUserMapper.class);
		SesDataimportUser suser = new SesDataimportUser();
		suser.setUserCode(userInfo.get("userName"));
		suser.setUserId(userInfo.get("userId"));
		suser.setSesSid(userInfo.get("sid"));
		suser.setStatus(SesConstants.VALIDATE_STATUS);
		SesDataimportUserCriteria c = new SesDataimportUserCriteria();
		c.createCriteria().andUserIdEqualTo(userInfo.get("userId"))
				.andSesSidEqualTo((userInfo.get("sid")))
				.andStatusEqualTo(SesConstants.VALIDATE_STATUS);

		List<SesDataimportUser> users = userMapper.selectByExample(c);
		int id = 0;
		if (users != null && !users.isEmpty()) {
			id = users.get(0).getId();
		} else {
			userMapper.insert(suser);
			users = userMapper.selectByExample(c);
			id = users.get(0).getId();
		}

		SesDataimportDsMapper dsMapper = ServiceUtil
				.getMapper(SesDataimportDsMapper.class);
		if (SesConstants.GROUP_ID_1 == dataSources.get(0).getGroupId()) {
			SesDataimportDsCriteria dsc = new SesDataimportDsCriteria();
			dsc.createCriteria().andDuIdEqualTo(id)
					.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
					.andGroupIdEqualTo(dataSources.get(0).getGroupId());// .andServiceIdEqualTo(suser.getSesSid())
			dsMapper.deleteByExample(dsc);
		}

		Gson gson = new Gson();
		for (SesDataSourceInfo db : dataSources) {
			if (db.isOverwrite()) {
				SesDataimportDsCriteria dsc = new SesDataimportDsCriteria();
				dsc.createCriteria().andDuIdEqualTo(id)
						.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
						.andGroupIdEqualTo(db.getGroupId())
						.andAliasEqualTo(db.getAlias());
				dsMapper.deleteByExample(dsc);
			}
			SesDataimportDs sesDs = new SesDataimportDs();
			sesDs.setDuId(id);
			sesDs.setStatus(SesConstants.VALIDATE_STATUS);
			sesDs.setType(db.getType());
			sesDs.setGroupId(db.getGroupId());
			sesDs.setAlias(db.getAlias());

			Map<String, Object> info = new HashMap<String, Object>();
			if (SesConstants.COMMON_DB_TYPE == sesDs.getType()) {
				info.put("database", db.getDatabase());
				info.put("ip", db.getIp());
				info.put("port", db.getPort());
				info.put("sid", db.getSid());
				info.put("username", db.getUsername());
				info.put("pwd", db.getPwd());
			}
			if (SesConstants.DBS_DB_TYPE == sesDs.getType()) {
				info.put("user", db.getUser());
				info.put("serviceId", db.getServiceId());
				info.put("servicePwd", db.getServicePwd());
				// info.put("haveTXS", db.isHaveTXS());
				info.put("vsql", db.getVsql());
			}
			String infos = gson.toJson(info);
			sesDs.setInfo(infos);
			dsMapper.insert(sesDs);

		}

	}

	@Override
	public List<SesDataSourceInfo> getIndexDataSources(String userId,
			String serviceId, int groupId) {
		List<SesDataSourceInfo> res = new ArrayList<>();
		try {
			SesDataimportUserMapper userMapper = ServiceUtil
					.getMapper(SesDataimportUserMapper.class);
			SesDataimportUserCriteria c = new SesDataimportUserCriteria();
			c.createCriteria().andUserIdEqualTo(userId)
					.andSesSidEqualTo(serviceId);
			List<SesDataimportUser> users = userMapper.selectByExample(c);
			if (users == null || users.isEmpty())
				return res;
			int id = userMapper.selectByExample(c).get(0).getId();

			SesDataimportDsMapper dsMapper = ServiceUtil
					.getMapper(SesDataimportDsMapper.class);
			SesDataimportDsCriteria sc = new SesDataimportDsCriteria();
			sc.createCriteria().andDuIdEqualTo(id).andGroupIdEqualTo(groupId);
			List<SesDataimportDs> datas = dsMapper.selectByExample(sc);
			if (datas != null && datas.size() > 0)
				res = getDBfromSesDataimportDs(datas, groupId);
		} catch (Exception e) {
			log.error("--loadDataSource:" + e.getMessage(), e);
			throw new PaasRuntimeException("", e);
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	private List<SesDataSourceInfo> getDBfromSesDataimportDs(
			List<SesDataimportDs> dss, int groupId) throws Exception {
		if (dss == null || dss.isEmpty())
			throw new Exception("datasource is null.");

		List<SesDataSourceInfo> res = new ArrayList<>();
		Gson gson = new Gson();
		for (SesDataimportDs ds : dss) {
			SesDataSourceInfo attr = new SesDataSourceInfo();
			attr.setId(ds.getId());
			attr.setAlias(ds.getAlias());
			attr.setType(ds.getType());
			String info = ds.getInfo();
			attr.setGroupId(groupId);
			attr.setuId(ds.getDuId());
			Map infos = gson.fromJson(info, HashMap.class);
			if (SesConstants.COMMON_DB_TYPE == ds.getType()) {
				attr.setDatabase(getIntValue(infos.get("database").toString()));
				attr.setIp(infos.get("ip").toString());
				attr.setPort(getIntValue(infos.get("port").toString()));
				attr.setSid(infos.get("sid").toString());
				attr.setUsername(infos.get("username").toString());
				attr.setPwd(infos.get("pwd").toString());
			}
			if (SesConstants.DBS_DB_TYPE == ds.getType()) {
				attr.setUser(infos.get("user").toString());
				attr.setServiceId(infos.get("serviceId").toString());
				attr.setServicePwd(infos.get("servicePwd").toString());
				attr.setVsql(infos.get("vsql").toString());
			}

			res.add(attr);
		}
		return res;
	}

	private int getIntValue(String str) {
		if (str == null || str.length() == 0)
			return 0;
		if (str.contains(".")) {
			return Integer.valueOf(str.substring(0, str.indexOf(".")));
		} else {
			return Integer.valueOf(str);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SesIndexSqlInfo getIndexDataSql(String userId, String serviceId,
			int groupId) {
		SesIndexSqlInfo sql = new SesIndexSqlInfo();
		SesDataimportUserMapper userMapper = ServiceUtil
				.getMapper(SesDataimportUserMapper.class);
		SesDataimportUserCriteria c = new SesDataimportUserCriteria();
		c.createCriteria().andUserIdEqualTo(userId).andSesSidEqualTo(serviceId);
		List<SesDataimportUser> users = userMapper.selectByExample(c);
		if (users == null || users.isEmpty())
			return sql;

		int id = userMapper.selectByExample(c).get(0).getId();

		SesDataimportSqlMapper sqlMapper = ServiceUtil
				.getMapper(SesDataimportSqlMapper.class);
		SesDataimportSqlCriteria sc = new SesDataimportSqlCriteria();
		sc.createCriteria().andDuIdEqualTo(id).andGroupIdEqualTo(groupId);
		List<SesDataimportSql> datas = sqlMapper.selectByExample(sc);
		if (datas != null && datas.size() > 0) {
			Gson gson = new Gson();
			List<SesIndexFiledSql> filedSqls = new ArrayList<>();
			SesIndexPrimarySql p = new SesIndexPrimarySql();
			if (groupId == SesConstants.GROUP_ID_1) {
				String info = datas.get(0).getInfo();
				sql.setId(datas.get(0).getId());
				Map infos = gson.fromJson(info, HashMap.class);
				if (datas.get(0).getIsPrimary() == SesConstants.PRIMARY) {
					p.setAlias(datas.get(0).getAlias());
					p.setDrAlias(datas.get(0).getDsAlias());
					p.setSql(null != infos && null != infos.get("sql") ? infos
							.get("sql").toString() : "");
					sql.setPrimarySql(p);
				}
			} else {
				for (SesDataimportSql sq : datas) {
					String info = sq.getInfo();
					Map infos = gson.fromJson(info, HashMap.class);
					if (sq.getIsPrimary() == SesConstants.PRIMARY) {
						p.setAlias(sq.getAlias());
						p.setDrAlias(sq.getDsAlias());
						p.setPrimaryKey(""+sq.getIsPrimary());
						p.setSql(null != infos && null != infos.get("sql") ? infos
								.get("sql").toString() : "");
						sql.setPrimarySql(p);
					} else {
						SesIndexFiledSql fSql = new SesIndexFiledSql();
						fSql.setAlias(sq.getAlias());
						fSql.setDrAlias(sq.getDsAlias());
						fSql.setRelation(Integer.valueOf(infos.get("relation")
								.toString()));
						fSql.setMapObj(infos.get("mapObj") == null ? false
								: Boolean.valueOf(infos.get("mapObj")
										.toString()));
						if (infos.containsKey("indexAlias"))
							fSql.setIndexAlias(infos.get("indexAlias")
									.toString());
						if (infos.containsKey("indexSql"))
							fSql.setIndexSql(infos.get("indexSql").toString());
						fSql.setSql(infos.get("filedSql").toString());
						filedSqls.add(fSql);
					}
					sql.setFiledSqls(filedSqls);
				}
			}

		}
		return sql;
	}

	@Override
	public void saveIndexDataSql(Map<String, String> dbInfo,
			SesDataSourceInfo dbAttr, Map<String, String> userInfo) {
		String falias = dbInfo.get("falias");
		String overwriteStr = dbInfo.get("overwrite");
		String isPriStr = dbInfo.get("isPrimary");
		String groupId = dbInfo.get("groupId");
		int uId = Integer.parseInt(dbInfo.get("uId"));
		Gson gson = new Gson();
		try {
			uId = getUserId(uId, userInfo);
			SesDataimportSqlMapper sqlMapper = ServiceUtil
					.getMapper(SesDataimportSqlMapper.class);
			SesDataimportDsMapper dsMapper = ServiceUtil
					.getMapper(SesDataimportDsMapper.class);

			SesDataimportSqlCriteria ssc = new SesDataimportSqlCriteria();

			if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_1) {
				ssc.createCriteria().andDuIdEqualTo(uId)
						.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
						.andGroupIdEqualTo(Integer.valueOf(groupId));
				sqlMapper.deleteByExample(ssc);
			}
			if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_2) {
				if (isPriStr != null && isPriStr.length() > 0
						&& Boolean.valueOf(isPriStr)) {
					ssc.createCriteria().andDuIdEqualTo(uId)
							.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
							.andGroupIdEqualTo(Integer.valueOf(groupId))
							.andIsPrimaryEqualTo(SesConstants.PRIMARY);
					sqlMapper.deleteByExample(ssc);
				}

				if (overwriteStr != null && overwriteStr.length() > 0
						&& Boolean.valueOf(overwriteStr)) {
					if (isPriStr != null && isPriStr.length() > 0) {
						if (Boolean.valueOf(isPriStr)) {
							ssc.createCriteria()
									.andDuIdEqualTo(uId)
									.andStatusEqualTo(
											SesConstants.VALIDATE_STATUS)
									.andGroupIdEqualTo(Integer.valueOf(groupId))
									.andIsPrimaryEqualTo(SesConstants.PRIMARY);
						} else {
							ssc.createCriteria()
									.andDuIdEqualTo(uId)
									.andStatusEqualTo(
											SesConstants.VALIDATE_STATUS)
									.andGroupIdEqualTo(Integer.valueOf(groupId))
									.andAliasEqualTo(falias)
									.andIsPrimaryEqualTo(
											SesConstants.IN_PRIMARY);
						}

					}
					sqlMapper.deleteByExample(ssc);
				}
			}

			SesDataimportSql sql = new SesDataimportSql();
			if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_1) {
				sql.setAlias(dbAttr.getAlias());
				sql.setDsAlias(dbAttr.getAlias());
				sql.setIsPrimary(SesConstants.PRIMARY);
				sql.setStatus(SesConstants.VALIDATE_STATUS);
				sql.setGroupId(dbAttr.getGroupId());
				Map<String, String> info = new HashMap<String, String>();
				info.put("sql", dbInfo.get("sql"));
				sql.setInfo(gson.toJson(info));

				if (dbAttr.getId() < 1) {
					SesDataimportDsCriteria sc = new SesDataimportDsCriteria();

					sc.createCriteria().andDuIdEqualTo(uId)
							.andGroupIdEqualTo(dbAttr.getGroupId());
					List<SesDataimportDs> dss = dsMapper.selectByExample(sc);
					SesDataimportDs ds = dss.get(0);
					sql.setDsId(ds.getId());
				} else {
					sql.setDsId(dbAttr.getId());
				}

			} else {
				if (isPriStr != null && isPriStr.length() > 0
						&& Boolean.valueOf(isPriStr)) {
					sql.setAlias(dbInfo.get("alias"));
					sql.setDsAlias(dbInfo.get("drAlias"));
					sql.setIsPrimary(SesConstants.PRIMARY);
					sql.setStatus(SesConstants.VALIDATE_STATUS);
					sql.setGroupId(Integer.valueOf(groupId));

					Map<String, String> info = new HashMap<String, String>();
					info.put("primaryKey", dbInfo.get("primaryKey"));
					info.put("sql", dbInfo.get("sql"));
					sql.setInfo(gson.toJson(info));

					SesDataimportDsCriteria sc = new SesDataimportDsCriteria();

					sc.createCriteria().andDuIdEqualTo(uId)
							.andAliasEqualTo(dbInfo.get("drAlias"))
							.andGroupIdEqualTo(SesConstants.GROUP_ID_2);
					List<SesDataimportDs> dss = dsMapper.selectByExample(sc);
					SesDataimportDs ds = null;
					ds = dss.get(0);
					sql.setDsId(ds.getId());
				} else {
					String fdrAlias = dbInfo.get("fdrAlias");
					sql.setAlias(dbInfo.get("falias"));
					sql.setDsAlias(dbInfo.get("fdrAlias"));
					sql.setIsPrimary(SesConstants.IN_PRIMARY);
					sql.setStatus(SesConstants.VALIDATE_STATUS);
					sql.setGroupId(Integer.valueOf(groupId));

					Map<String, String> info = new HashMap<String, String>();
					info.put("relation", dbInfo.get("relation"));
					info.put("mapObj", dbInfo.get("mapObj"));
					if (fdrAlias != null && fdrAlias.split("___").length == 2) {
						info.put("indexAlias", dbInfo.get("indexAlias"));
						info.put("indexSql", dbInfo.get("indexSql"));
					}
					info.put("filedSql", dbInfo.get("fsql"));
					sql.setInfo(gson.toJson(info));

					SesDataimportDsCriteria sc = new SesDataimportDsCriteria();

					sc.createCriteria().andDuIdEqualTo(uId)
							.andAliasEqualTo(fdrAlias)
							.andGroupIdEqualTo(SesConstants.GROUP_ID_2);
					List<SesDataimportDs> dss = dsMapper.selectByExample(sc);
					SesDataimportDs ds = null;
					ds = dss.get(0);
					sql.setDsId(ds.getId());
				}
			}
			sql.setDuId(uId);
			sqlMapper.insert(sql);

		} catch (Exception e) {
			log.error("--save sql exception--:", e);
			throw new RuntimeException(e);
		}
	}

	private int getUserId(int uId, Map<String, String> userInfo)
			throws Exception {
		if (uId == 0) {
			SesDataimportUserMapper userMapper = ServiceUtil
					.getMapper(SesDataimportUserMapper.class);
			SesDataimportUserCriteria c = new SesDataimportUserCriteria();
			c.createCriteria().andUserIdEqualTo(userInfo.get("userId"))
					.andSesSidEqualTo((userInfo.get("sid")))
					.andStatusEqualTo(SesConstants.VALIDATE_STATUS);
			List<SesDataimportUser> users = userMapper.selectByExample(c);
			SesDataimportUser dataUser = null;
			if (users != null && !users.isEmpty()) {
				dataUser = users.get(0);
				uId = dataUser.getId();
			} else {
				throw new Exception("user is null.");
			}
		}
		return uId;
	}

	@Override
	public SesDataSourceInfo getDataSourceInfo(
			List<SesDataSourceInfo> dataSources, Map<String, String> userInfo,
			Map<String, String> dbInfo) {

		String groupId = dbInfo.get("groupId");
		if (groupId != null && groupId.length() > 0) {
			try {
				SesDataSourceInfo db = null;
				if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_1) {
					db = dataSources.get(0);
				}
				if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_2) {
					String isPriStr = dbInfo.get("isPrimary");
					if (isPriStr != null && isPriStr.length() > 0
							&& Boolean.valueOf(isPriStr)) {

						int uId = Integer.parseInt(dbInfo.get("uId"));
						uId = getUserId(uId, userInfo);
						SesDataimportDsCriteria sc = new SesDataimportDsCriteria();

						sc.createCriteria().andDuIdEqualTo(uId)
								.andAliasEqualTo(dbInfo.get("drAlias"))
								.andGroupIdEqualTo(SesConstants.GROUP_ID_2);
						SesDataimportDsMapper dsMapper = ServiceUtil
								.getMapper(SesDataimportDsMapper.class);
						List<SesDataimportDs> dss = dsMapper
								.selectByExample(sc);
						db = getDBfromSesDataimportDs(dss,
								SesConstants.GROUP_ID_2).get(0);
						return db;
					} else {
						return null;
					}

				}
				return db;
			} catch (Exception e) {
				log.error("--validateDB exception--:", e);
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	@Override
	public void deleteDataSource(List<SesDataSourceInfo> dataSources,
			Map<String, String> userInfo) {

		try {
			// du_id
			int id = 0;
			if (dataSources.get(0).getuId() < 1) {
				List<SesDataimportUser> sesUser = getSesUser(userInfo);
				if (sesUser != null && !sesUser.isEmpty()) {
					id = sesUser.get(0).getId();
				} else {
					throw new Exception("ses user is null.");
				}
			} else {
				id = dataSources.get(0).getuId();
			}

			SesDataimportDsMapper dsMapper = ServiceUtil
					.getMapper(SesDataimportDsMapper.class);

			SesDataimportDsCriteria dsc = new SesDataimportDsCriteria();
			if (dataSources.get(0).getGroupId() == SesConstants.GROUP_ID_1) {
				dsc.createCriteria().andDuIdEqualTo(id)
						.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
						.andGroupIdEqualTo(dataSources.get(0).getGroupId());
			} else if (dataSources.get(0).getGroupId() == SesConstants.GROUP_ID_2) {
				dsc.createCriteria().andDuIdEqualTo(id)
						.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
						.andGroupIdEqualTo(dataSources.get(0).getGroupId())
						.andAliasEqualTo(dataSources.get(0).getAlias());
			}
			dsMapper.deleteByExample(dsc);

		} catch (Exception e) {
			log.error("--delete dataresource exception--:", e);
			throw new RuntimeException(e);
		}
	}

	private List<SesDataimportUser> getSesUser(Map<String, String> userInfo)
			throws Exception {
		SesDataimportUserMapper userMapper = ServiceUtil
				.getMapper(SesDataimportUserMapper.class);
		SesDataimportUser suser = new SesDataimportUser();
		suser.setUserCode(userInfo.get("userName"));
		suser.setUserId(userInfo.get("userId"));
		suser.setSesSid(userInfo.get("sid"));
		suser.setStatus(SesConstants.VALIDATE_STATUS);
		SesDataimportUserCriteria c = new SesDataimportUserCriteria();
		c.createCriteria().andUserIdEqualTo(userInfo.get("userId"))
				.andSesSidEqualTo((userInfo.get("sid")))
				.andStatusEqualTo(SesConstants.VALIDATE_STATUS);

		List<SesDataimportUser> users = userMapper.selectByExample(c);
		return users;
	}

	@Override
	public void deleteIndexDataSql(Map<String, String> sqlInfo,
			Map<String, String> userInfo) {
		try {
			String groupId = sqlInfo.get("groupId");

			if (groupId != null && groupId.length() > 0) {
				if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_1) {
					String sqlId = sqlInfo.get("sql.id");
					if (sqlId == null || "".equals(sqlId))
						throw new Exception(" sql is not exist.");
					SesDataimportSqlMapper sqlMapper = ServiceUtil
							.getMapper(SesDataimportSqlMapper.class);

					SesDataimportSqlCriteria dsc = new SesDataimportSqlCriteria();
					dsc.createCriteria().andIdEqualTo(Integer.valueOf(sqlId));
					sqlMapper.deleteByExample(dsc);
				}
				if (Integer.valueOf(groupId) == SesConstants.GROUP_ID_2) {
					String isPriStr = sqlInfo.get("isPrimary");

					if (isPriStr != null && isPriStr.length() > 0) {

						int uId = Integer.parseInt(sqlInfo.get("uId"));
						uId = getUserId(uId, userInfo);
						SesDataimportSqlMapper sqlMapper = ServiceUtil
								.getMapper(SesDataimportSqlMapper.class);
						SesDataimportSqlCriteria ssc = new SesDataimportSqlCriteria();
						if (Boolean.valueOf(isPriStr)) {
							ssc.createCriteria().andDuIdEqualTo(uId)
									.andGroupIdEqualTo(SesConstants.GROUP_ID_2)
									.andIsPrimaryEqualTo(SesConstants.PRIMARY);
						} else {
							ssc.createCriteria()
									.andDuIdEqualTo(uId)
									.andGroupIdEqualTo(SesConstants.GROUP_ID_2)
									.andIsPrimaryEqualTo(
											SesConstants.IN_PRIMARY)
									.andAliasEqualTo(sqlInfo.get("falias"));
						}
						sqlMapper.deleteByExample(ssc);

					}

				}
			}

		} catch (Exception e) {
			log.error("--validateDB exception--:", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public String getDataSourceUserPK(String userId, String srvID) {
		SesDataimportUserMapper userMapper = ServiceUtil
				.getMapper(SesDataimportUserMapper.class);
		SesDataimportUserCriteria c = new SesDataimportUserCriteria();
		c.createCriteria().andUserIdEqualTo(userId).andSesSidEqualTo(srvID)
				.andStatusEqualTo(SesConstants.VALIDATE_STATUS);
		List<SesDataimportUser> users = userMapper.selectByExample(c);
		if (users != null && !users.isEmpty())
			return "" + users.get(0).getId();
		else
			return null;
	}

	@Override
	public List<SesDataSourceInfo> getDataSource(int dataSourceUId,
			String dbAlias, int groupId) {
		SesDataimportDsCriteria sc = new SesDataimportDsCriteria();

		sc.createCriteria().andDuIdEqualTo(dataSourceUId)
				.andAliasEqualTo(dbAlias).andGroupIdEqualTo(groupId);
		SesDataimportDsMapper dsMapper = ServiceUtil
				.getMapper(SesDataimportDsMapper.class);
		List<SesDataimportDs> dss = dsMapper.selectByExample(sc);
		try {
			return getDBfromSesDataimportDs(dss, SesConstants.GROUP_ID_2);
		} catch (Exception e) {
			throw new PaasRuntimeException("", e);
		}
	}
}
