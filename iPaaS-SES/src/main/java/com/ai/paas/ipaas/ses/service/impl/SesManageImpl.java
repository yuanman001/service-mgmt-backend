package com.ai.paas.ipaas.ses.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.agent.util.AgentUtil;
import com.ai.paas.ipaas.agent.util.AidUtil;
import com.ai.paas.ipaas.agent.util.ParamUtil;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasImageResourceMapper;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasSysConfigMapper;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResource;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResourceCriteria;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasSysConfigCriteria;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;
import com.ai.paas.ipaas.ses.dao.interfaces.SesResourcePoolMapper;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserInstanceMapper;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserMappingMapper;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesResourcePool;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesResourcePoolCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserInstance;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserInstanceCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMappingCriteria;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.interfaces.ISesManage;
import com.ai.paas.ipaas.ses.service.vo.SesHostInfo;
import com.ai.paas.ipaas.ses.service.vo.SesMappingApply;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApply;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class SesManageImpl implements ISesManage {
	private static transient final Logger LOGGER = Logger
			.getLogger(SesManageImpl.class);
	@Autowired
	private ICCSComponentManageSv iCCSComponentManageSv;

	@Override
	public void createSesService(SesSrvApply sesSrvApply) throws PaasException {
		// 1.准备数据，包括从资源里面获取ses集群,实现获取算法
		List<SesHostInfo> sesHosts = qryAvlSesHosts(
				sesSrvApply.getClusterNum(), sesSrvApply.getSesMem());
		String userId = sesSrvApply.getUserId();
		String serviceId = sesSrvApply.getServiceId();
		StringBuilder clusterString = new StringBuilder();
		String hosts = queryUnicastHosts(sesHosts);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		String sshUser = getSesSSHUser();
		String sshUserPwd = getSesSSHUserPwd();
		IpaasImageResource sesImage = getSesImage();
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = SesManageImpl.class
				.getResourceAsStream("/playbook/ses/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		try {
			in.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			AgentUtil.uploadFile("ses/init_ansible_ssh_hosts.sh", cnt,
					AidUtil.getAid());
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			AgentUtil.executeCommand("chmod +x " + basePath
					+ "ses/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		in = SesManageImpl.class.getResourceAsStream("/playbook/ses/ses.yml");
		cnt = AgentUtil.readFileLines(in);
		try {
			in.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			AgentUtil.uploadFile("ses/ses.yml", cnt, AidUtil.getAid());
		} catch (ClientProtocolException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// 还得上传文件
		in = SesManageImpl.class
				.getResourceAsStream("/playbook/ses/ansible_run_ses.sh");
		cnt = AgentUtil.readFileLines(in);
		try {
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			AgentUtil.uploadFile("ses/ansible_run_ses.sh", cnt, AidUtil.getAid());
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			AgentUtil.executeCommand("chmod +x " + basePath
					+ "ses/ansible_run_ses.sh", AidUtil.getAid());
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 2.启动ses集群
		for (SesHostInfo sesHostInfo : sesHosts) {
			String ip = sesHostInfo.getIp();
			int agentPort = sesHostInfo.getAgentPort();
			String binPath = sesHostInfo.getBinPath();
			String userPath = sesHostInfo.getUserPath();
			// 生成每个主机列表
			String mkSshHosts = ParamUtil.replace(
					"ses/init_ansible_ssh_hosts.sh {0} {1} {2}", new String[] {
							basePath + "ses", ip.replace(".", ""), ip });
			LOGGER.info("---------mkSshHosts {}----------" + mkSshHosts);
			try {
				AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// 开始执行
			String runImage = ParamUtil.replace(
					"ses/ansible_run_ses.sh {1} {2} "
							+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12}",
					new String[] {
							basePath + "ses",
							ip.replace(".", ""),
							sshUser,
							sshUserPwd,
							ip,
							sshUser,
							sesImage.getImageRepository() + "/"
									+ sesImage.getImageName(), userId,
							serviceId, sesHostInfo.getTcpPort(),
							sesHostInfo.getHttpPort(), hosts, ip });

			LOGGER.info("---------runImage {}----------"+ runImage);
			try {
				AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}

			String tcpPort = sesHostInfo.getTcpPort();
			String execConfPrepare = "sh " + userPath + "/init-ses.sh "
					+ userId + " " + (userId + "-" + serviceId + "-" + tcpPort)
					+ " " + sesHostInfo.getIp() + " " + tcpPort + " "
					+ sesHostInfo.getHttpPort() + " " + serviceId + " " + hosts
					+ " ";
			LOGGER.info("execConfPrepare.........." + execConfPrepare);
			String execSesStart = " sh " + binPath
					+ "/bin/elasticsearch -d -Des.config=" + userPath + "/"
					+ userId + "/conf/"
					+ (userId + "-" + serviceId + "-" + tcpPort) + ".yml";
			LOGGER.info("execSesStart.........." + execSesStart);
//			ac.executeInstruction(execConfPrepare);
//			ac.executeInstruction("export ES_HEAP_SIZE="
//					+ sesSrvApply.getSesMem() + "m \n" + execSesStart
//					+ " \n export ES_HEAP_SIZE= \n");

			// 更新ses_user_instance
			SesUserInstance sesUser = new SesUserInstance();
			sesUser.setHostIp(sesHostInfo.getIp());
			sesUser.setServiceId(serviceId);
			sesUser.setServiceName(sesSrvApply.getServiceName());
			sesUser.setSesPort(Integer.parseInt(sesHostInfo.getHttpPort()));
			sesUser.setTcpPort(Integer.parseInt(tcpPort));
			sesUser.setStatus(SesConstants.SES_SERVICE_START);
			sesUser.setUserId(userId);
			sesUser.setBeginTime(new Timestamp(new Date().getTime()));
			sesUser.setMemUse(sesSrvApply.getSesMem());
			ServiceUtil.getMapper(SesUserInstanceMapper.class).insert(sesUser);
			if (clusterString.length() > 0) {
				clusterString.append(SesConstants.SPLITER_COMMA);
			}
			clusterString.append(sesUser.getHostIp()
					+ SesConstants.SPLITER_COLON + sesUser.getTcpPort());
		}
		LOGGER.info("write to zk..........");
		// 写入zk
		addZk(userId, serviceId, clusterString.toString());
	}

	private String queryUnicastHosts(List<SesHostInfo> sesHosts) {
		StringBuilder sb = new StringBuilder();
		for (SesHostInfo sesHostInfo : sesHosts) {
			sb.append("\\\"" + sesHostInfo.getIp() + ":"
					+ sesHostInfo.getTcpPort() + "\\\",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * createIndex:(初始化集群后创建一个空索引).
	 * 
	 * @author jianhua.ma
	 * @param indexName
	 * @param ipAndPort
	 * @param client
	 */
	@Override
	public void createIndex(SesMappingApply mappingApply) {

		TransportClient client = null;
		String userId = mappingApply.getUserId();
		String serviceId = mappingApply.getServiceId();
		String ipAndPort = quryIpAndPort(userId, serviceId);
		String indexName = String.valueOf(Math.abs((userId + serviceId)
				.hashCode()));
		LOGGER.info("createIndex ..........userid:" + userId
				+ ".....serviceId:" + serviceId + ".....indexName:" + indexName);
		try {
			client = prepareSearchClient(ipAndPort);
			if (!doesIndexExist(indexName, client)) {
				client.admin().indices().prepareCreate(indexName).execute()
						.actionGet();
			}
		} catch (Exception e) {
			LOGGER.error("createIndex error..........userid:" + userId
					+ ".....serviceId:" + serviceId + ".....indexName:"
					+ indexName, e);
		} finally {
			if (client != null) {
				client.close();
			}
		}

	}

	/*
	 * 创建搜索客户端 tcp连接搜索服务器 创建索引
	 */
	@SuppressWarnings("resource")
	private TransportClient prepareSearchClient(String ipAndPort) {
		TransportClient searchClient = null;
		/* 如果10秒没有连接上搜索服务器，则超时 */
//		Settings settings = ImmutableSettings.settingsBuilder()
//				.put("client.transport.ping_timeout", "10s")
//				.put("client.transport.sniff", "true")
//				.put("client.transport.ignore_cluster_name", "true").build();
		/* 创建搜索客户端 */
//		searchClient = new TransportClient(settings);
//		String address = ipAndPort.split(":")[0];
//		int port = Integer.parseInt(ipAndPort.split(":")[1]);
//		/* 通过tcp连接搜索服务器，如果连接不上，有一种可能是服务器端与客户端的jar包版本不匹配 */
//		searchClient = ((TransportClient) searchClient)
//				.addTransportAddress(new InetSocketTransportAddress(address,
//						port));
		return searchClient;
	}

	/**
	 * 添加zk记录
	 * 
	 * @param userId
	 * @param serviceId
	 * @param mcsResourcePool
	 * @param requirepass
	 * @throws PaasException
	 */
	private void addZk(String userId, String serviceId, String hosts)
			throws PaasException {
		// 在zk中记录申请信息
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(SesConstants.SES_ZK_PATH + serviceId);
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();

		dataJson.addProperty("hosts", hosts);
		iCCSComponentManageSv.add(op, dataJson.toString());
		LOGGER.info("write to zk su!");
	}

	/**
	 * 向zk添加mapping
	 */
	private void addMapping2Zk(String userId, String serviceId, String mapping)
			throws PaasException {
		// 在zk中记录申请信息
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(SesConstants.SES_MAPPING_ZK_PATH + serviceId);
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();

		dataJson.addProperty("mapping", mapping);
		iCCSComponentManageSv.add(op, dataJson.toString());
		LOGGER.info("addMapping2Zk success!");
	}

	/**
	 * 获得最空闲的资源主机列表
	 * 
	 * @param nodeNum
	 * @return
	 */
	private List<SesResourcePool> getBestHostsRes(int nodeNum) {
		SesResourcePoolCriteria sesRsrcPoolExample = new SesResourcePoolCriteria();
		sesRsrcPoolExample.setLimitStart(0);
		sesRsrcPoolExample.setLimitEnd(nodeNum);
		sesRsrcPoolExample
				.setOrderByClause("(ifnull(mem_total, 0) - ifnull(mem_used, 0)) desc");
		sesRsrcPoolExample.createCriteria().andStatusEqualTo(
				SesConstants.SES_RESOURCE_AVIL);
		List<SesResourcePool> pool = ServiceUtil.getMapper(
				SesResourcePoolMapper.class)
				.selectByExample(sesRsrcPoolExample);
		return pool;
	}

	/**
	 * 获得资源主机信息
	 * 
	 * @return
	 */
	private SesResourcePool quryHostByIp(String ip) {
		SesResourcePoolCriteria sesRsrcPoolExample = new SesResourcePoolCriteria();
		sesRsrcPoolExample.createCriteria().andHostIpEqualTo(ip);
		List<SesResourcePool> pool = ServiceUtil.getMapper(
				SesResourcePoolMapper.class)
				.selectByExample(sesRsrcPoolExample);
		return pool.get(0);
	}

	/**
	 * 
	 * 根据集群节点数查询可用节点信息. 返回结果为每一个端口号对应一个SesHostInfo
	 * 
	 * @author jianhua.ma
	 */

	private List<SesHostInfo> qryAvlSesHosts(Integer nodeNum, Integer esMem)
			throws PaasException {

		try {
			List<SesResourcePool> pool = getBestHostsRes(nodeNum);

			List<SesHostInfo> result = new ArrayList<SesHostInfo>();

			int count = 0;
			int kill = 0;
			do {
				for (SesResourcePool res : pool) {
					if (count < nodeNum) {
						int mem_total = res.getMemTotal();
						int mem_used = res.getMemUsed();
						if (mem_total - mem_used < esMem) {
							pool.remove(res);
							continue;
						}
						String host_ip = res.getHostIp();
						int port_min = res.getPortMin();
						int port_max = res.getPortMax();
						if (port_min >= port_max) {
							throw new PaasException(
									"haven't got enough ports...");
						}
						SesHostInfo host = new SesHostInfo();
						host.setIp(host_ip);
						host.setMemUsed(host.getMemUsed() + esMem);
						host.setHttpPort(String.valueOf(port_min));
						host.setTcpPort(String.valueOf(port_min + 1));
						host.setAgentPort(res.getAgentPort());
						host.setBinPath(res.getBinPath());
						host.setUserPath(res.getUserPath());
						result.add(host);
						res.setPortMin(port_min + 2);
						res.setMemUsed(res.getMemUsed() + host.getMemUsed());
						count++;
					} else {
						break;
					}

				}
				kill++;
			} while (count < nodeNum && kill < (nodeNum + 1));
			if (kill > nodeNum) {
				throw new PaasException("ses resource not enough...");
			}
			for (SesResourcePool ses : pool) {
				ServiceUtil.getMapper(SesResourcePoolMapper.class)
						.updateByPrimaryKeySelective(ses);
			}
			return result;
		} catch (Exception e) {
			throw new PaasException("qryAvlSesHosts error...", e);
		}
	}

	@Override
	public boolean isCreated(SesSrvApply sesSrvApply) throws PaasException {
		return true;
	}

	/**
	 * 创建mapping(feid("indexAnalyzer","ik")该字段分词IK索引
	 * ；feid("searchAnalyzer","ik")该字段分词ik查询；具体分词插件请看IK分词插件说明)
	 * 
	 * @author jianhua.ma
	 * @param indices
	 *            索引名称；
	 * @param mappingType
	 *            索引类型
	 * @throws Exception
	 */
	public void createMapping(String indices, String mappingType,
			String mapping, String ipAndPort) throws Exception {
		TransportClient client = null;
		try {
			client = prepareSearchClient(ipAndPort);
			client.admin().indices().preparePutMapping().setIndices(indices)
					.setType(mappingType).setSource(mapping).execute()
					.actionGet();
		} catch (ElasticsearchException e) {
			LOGGER.error("createMapping error", e);
			throw new PaasException("createMapping error", e);
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	/**
	 * 根据userid serviceid查询集群任意一组ip＋port用于创建client
	 *
	 * @author jianhua.ma
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	private String quryIpAndPort(String userId, String serviceId) {
		SesUserInstanceCriteria example = new SesUserInstanceCriteria();
		example.setLimitStart(0);
		example.setLimitEnd(1);
		example.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId).andStatusEqualTo(1);
		List<SesUserInstance> insts = ServiceUtil.getMapper(
				SesUserInstanceMapper.class).selectByExample(example);
		String ipAndPort = "";
		if (insts != null && insts.size() == 1) {
			SesUserInstance inst = insts.get(0);
			ipAndPort = inst.getHostIp() + SesConstants.SPLITER_COLON
					+ inst.getTcpPort();
		}
		return ipAndPort;
	}

	/**
	 * 创建mapping
	 */
	@Override
	public void putMapping(SesMappingApply mappingApply) throws PaasException {
		TransportClient client = null;
		String userId = mappingApply.getUserId();
		String serviceId = mappingApply.getServiceId();
		String ipAndPort = quryIpAndPort(userId, serviceId);

		try {
			client = prepareSearchClient(ipAndPort);
			String indexName = mappingApply.getIndexName();
			String indexType = mappingApply.getIndexType();
			IndicesAdminClient indicesAdminClient = client.admin().indices();
			if (!doesIndexExist(indexName, client)) {
				createIndex(mappingApply);
			}
			if (doesMappingExist(indexName, indexType, client)) {
//				indicesAdminClient.prepareDeleteMapping().setIndices(indexName)
//						.setType(indexType).execute().actionGet();
			}
			String mapping = mappingApply.getMapping();
			LOGGER.info("putMapping begin ..........userid:" + userId
					+ ".....serviceId:" + serviceId + ".....indexName:"
					+ indexName + ".....mapping:" + mapping);
			indicesAdminClient.preparePutMapping().setIndices(indexName)
					.setType(indexType).setSource(mapping).execute()
					.actionGet();
			addMapping2Zk(userId, serviceId, mapping);
		} catch (Exception e) {
			LOGGER.error("createMapping error", e);
			throw new PaasException("createMapping error", e);
		} finally {
			if (client != null) {
				client.close();
			}
		}

	}

	/**
	 * 检察索引是否已经存在.
	 */
	private boolean doesIndexExist(String indexName, TransportClient client)
			throws PaasException {
		try {
			IndicesExistsRequest ier = new IndicesExistsRequest();
			ier.indices(new String[] { indexName.toLowerCase() });
			return client.admin().indices().exists(ier).actionGet().isExists();
		} catch (Exception e) {
			LOGGER.error("doesIndexExist error", e);
			throw new PaasException("doesIndexExist error", e);
		}
	}

	/**
	 * 检察mapping是否已经存在.
	 */
	private boolean doesMappingExist(String indexName, String indexType,
			TransportClient client) throws PaasException {
		try {
			TypesExistsRequest ter = new TypesExistsRequest(
					new String[] { indexName.toLowerCase() }, indexType);
			return client.admin().indices().typesExists(ter).actionGet()
					.isExists();
		} catch (ElasticsearchException e) {
			LOGGER.error("doesMappingExist error", e);
			throw new PaasException("doesMappingExist error", e);
		}
	}

	@Override
	public void start(ApplyInfo info) throws PaasException {
		SesUserInstanceMapper mapper = ServiceUtil
				.getMapper(SesUserInstanceMapper.class);
		SesUserInstanceCriteria instanceCriteria = new SesUserInstanceCriteria();
		instanceCriteria.createCriteria().andUserIdEqualTo(info.getUserId());
		instanceCriteria.createCriteria().andServiceIdEqualTo(
				info.getServiceId());
		List<SesUserInstance> instances = mapper
				.selectByExample(instanceCriteria);
		for (SesUserInstance ins : instances) {
			String hostIp = ins.getHostIp();
			SesResourcePool host = quryHostByIp(hostIp);
			String sesDis = info.getUserId() + "-" + info.getServiceId() + "-"
					+ ins.getTcpPort();
			String execSesStart = "sh " + host.getBinPath()
					+ "/bin/elasticsearch -d -Des.config=" + host.getUserPath()
					+ "/" + info.getUserId() + "/conf/" + sesDis + ".yml";
//			AgentClient ac = new AgentClient(hostIp, host.getAgentPort());
//
//			LOGGER.info("execSesStart.........." + execSesStart);
//			ac.executeInstruction(execSesStart);
		}
	}

	@Override
	public void stop(ApplyInfo info) throws PaasException {
		SesUserInstanceMapper mapper = ServiceUtil
				.getMapper(SesUserInstanceMapper.class);
		SesUserInstanceCriteria instanceCriteria = new SesUserInstanceCriteria();
		instanceCriteria.createCriteria().andUserIdEqualTo(info.getUserId())
				.andServiceIdEqualTo(info.getServiceId());
		List<SesUserInstance> instances = mapper
				.selectByExample(instanceCriteria);
		Set<String> ips = new HashSet<String>();
		for (SesUserInstance ins : instances) {
			ips.add(ins.getHostIp());
		}
		for (String ip : ips) {
			SesResourcePool host = quryHostByIp(ip);
//			AgentClient ac = new AgentClient(ip, host.getAgentPort());
//			String sesDis = info.getUserId() + "-" + info.getServiceId();
//			String execSesStop = "ps -ef | grep " + sesDis
//					+ " | awk '{print $2}' | xargs kill -9";
//			LOGGER.info("execSesStop.........." + execSesStop);
//			ac.executeInstruction(execSesStop);
		}
	}

	@Override
	public void recycle(ApplyInfo info) throws PaasException {
		// 停服务
		stop(info);
		// 服务器回收资源
		SesUserInstanceMapper mapper = ServiceUtil
				.getMapper(SesUserInstanceMapper.class);
		SesUserInstanceCriteria instanceCriteria = new SesUserInstanceCriteria();
		instanceCriteria.createCriteria().andUserIdEqualTo(info.getUserId())
				.andServiceIdEqualTo(info.getServiceId());
		List<SesUserInstance> instances = mapper
				.selectByExample(instanceCriteria);
		Set<String> ips = new HashSet<String>();
		for (SesUserInstance ins : instances) {
			String ip = ins.getHostIp();
			ips.add(ip);
			// 更新ses_resource_pool.mem_used
			SesResourcePool host = quryHostByIp(ip);
			host.setMemUsed(host.getMemUsed() - ins.getMemUse());
			ServiceUtil.getMapper(SesResourcePoolMapper.class)
					.updateByPrimaryKeySelective(host);
		}
		for (String ip : ips) {
			SesResourcePool host = quryHostByIp(ip);
//			AgentClient ac = new AgentClient(ip, host.getAgentPort());
//			String execSesRecycle = "rm -rf " + host.getUserPath() + "/"
//					+ info.getUserId();
//			LOGGER.info("execSesRecycle.........." + execSesRecycle);
//			ac.executeInstruction(execSesRecycle);
//			String execSesIkRecycle = "rm -rf " + host.getBinPath()
//					+ "/config/ik/" + info.getUserId();
//			LOGGER.info("execSesIkRecycle.........." + execSesIkRecycle);
//			ac.executeInstruction(execSesIkRecycle);
		}
		// ses_user_instance 删除记录
		instanceCriteria.createCriteria().andUserIdEqualTo(info.getUserId())
				.andServiceIdEqualTo(info.getServiceId());
		mapper.deleteByExample(instanceCriteria);
		// ses_user_mapping 删除记录
		SesUserMappingMapper mappingMapper = ServiceUtil
				.getMapper(SesUserMappingMapper.class);
		SesUserMappingCriteria sesUserMappingCriteria = new SesUserMappingCriteria();
		sesUserMappingCriteria.createCriteria()
				.andUserIdEqualTo(info.getUserId())
				.andServiceIdEqualTo(info.getServiceId());
		mappingMapper.deleteByExample(sesUserMappingCriteria);

	}

	private IpaasImageResource getSesImage() throws PaasException {
		IpaasImageResourceMapper rpm = ServiceUtil
				.getMapper(IpaasImageResourceMapper.class);
		IpaasImageResourceCriteria rpmc = new IpaasImageResourceCriteria();
		rpmc.createCriteria().andStatusEqualTo(SesConstants.VALIDATE_STATUS)
				.andServiceCodeEqualTo(SesConstants.SERVICE_CODE)
				.andImageCodeEqualTo(SesConstants.IMAGE_CODE);
		List<IpaasImageResource> res = rpm.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("SES IMAGE not config.");
		return res.get(0);
	}

	private String getSesSSHUser() throws PaasException {
		IpaasSysConfigMapper sysconfigDao = ServiceUtil
				.getMapper(IpaasSysConfigMapper.class);
		IpaasSysConfigCriteria rpmc = new IpaasSysConfigCriteria();
		rpmc.createCriteria().andTableCodeEqualTo(SesConstants.SERVICE_CODE)
				.andFieldCodeEqualTo(SesConstants.SSH_USER_CODE);
		List<IpaasSysConfig> res = sysconfigDao.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("SES ssh user not config.");
		return res.get(0).getFieldValue();
	}

	private String getSesSSHUserPwd() throws PaasException {
		IpaasSysConfigMapper sysconfigDao = ServiceUtil
				.getMapper(IpaasSysConfigMapper.class);
		IpaasSysConfigCriteria rpmc = new IpaasSysConfigCriteria();
		rpmc.createCriteria().andTableCodeEqualTo(SesConstants.SERVICE_CODE)
				.andFieldCodeEqualTo(SesConstants.SSH_USER_PWD_CODE);
		List<IpaasSysConfig> res = sysconfigDao.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("SES ssh user not config.");
		return res.get(0).getFieldValue();
	}

	public static void main(String[] args) throws PaasException {
		String indexName = String.valueOf(Math
				.abs(("1321014990EB4DF79D893600A2F7CCA6SES003").hashCode()));
		System.out.println(indexName);
		StringBuilder sb = new StringBuilder();
		sb.append("1111,");
		sb.append("1111,");
		sb.append("1111,");
		sb.append("1111,");
		System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
	}
}
