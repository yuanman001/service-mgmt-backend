package com.ai.paas.ipaas.idps.service.impl;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.ai.paas.ipaas.idps.dao.interfaces.IdpsInstanceBandDssMapper;
import com.ai.paas.ipaas.idps.dao.interfaces.IdpsBalanceResourcePoolMapper;
import com.ai.paas.ipaas.idps.dao.interfaces.IdpsResourcePoolMapper;
import com.ai.paas.ipaas.idps.dao.interfaces.IdpsUserInstanceMapper;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsBalanceResourcePool;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsBalanceResourcePoolCriteria;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsResourcePool;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsResourcePoolCriteria;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsUserInstance;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsUserInstanceCriteria;
import com.ai.paas.ipaas.idps.service.constant.IdpsConstants;
import com.ai.paas.ipaas.idps.service.interfaces.IIdpsSv;
import com.ai.paas.ipaas.idps.service.util.IdpsParamUtil;
import com.ai.paas.ipaas.uac.service.UserClientFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.uac.vo.AuthResult;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class IdpsSvImpl implements IIdpsSv {
	private static transient final Logger LOG = LoggerFactory
			.getLogger(IdpsSvImpl.class);
	@Autowired
	private ICCSComponentManageSv iCCSComponentManageSv;

	protected static final String IDPS_BASE_ZK_CONF = "/IDPS/";
	
	@Override
	public String open(String param,String isUpgrade) throws Exception {
		
		Map<String, String> map = new HashMap<String, String>();
		LOG.debug("----open idps ---param {}-----", param);
		if("yes".equals(isUpgrade)){
				String jsonParam = param.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
				map = IdpsParamUtil.getParamMap(jsonParam);
		}else{
				map = IdpsParamUtil.getParamMap(param);
		}
		String applyType = map.get(IdpsConstants.APPLY_TYPE);
		if (!IdpsConstants.APPLY_TYPE_C.equals(applyType))
			throw new PaasException("图片服务开通，服务类型不对！");
		// 获取服务号配置参数
		final String serviceId = map.get(IdpsConstants.SERVICE_ID);
		final String userId = map.get(IdpsConstants.USER_ID);
		final String serviceName = map.get(IdpsConstants.SERVICE_NAME);
		final String nodeNumStr = map.get(IdpsConstants.NODE_NUM);
		final int nodeNum = Integer.valueOf(nodeNumStr);

		// 判断用户的这个图片服务是否已经开通
		if (existsService(userId, serviceId) && "no".equals(isUpgrade)) {
			LOG.debug("----------------用户服务已存在，开通成功");
			return IdpsConstants.SUCCESS_FLAG;
		}

		final String dssServiceId = map.get(IdpsConstants.DSS_SERVICE_ID);
		final String dssServicePwd = map.get(IdpsConstants.DSS_SERVICE_PWD);
		final String dssPId = map.get(IdpsConstants.DSS_P_ID);
		// 验证DSS
		validateDss(dssPId, dssServiceId, dssServicePwd);

		if (nodeNum == 1) {
			openOne(userId, serviceId, serviceName, dssPId, dssServiceId,
					dssServicePwd,isUpgrade);
		} else {
			openMany(userId, serviceId, nodeNum, serviceName, dssPId,
					dssServiceId, dssServicePwd,isUpgrade);
		}
		if("no".equals(isUpgrade)){
			// 捆绑DSS
			bindDss(userId, serviceId, dssPId, dssServiceId, dssServicePwd);
		}
		// 开通成功
		LOG.debug("------------open success-------------");
		return IdpsConstants.SUCCESS_FLAG;
	}

	private int bindDss(String userId, String serviceId, String dssPId,
			String dssServiceId, String dssServicePwd) {
		IdpsInstanceBandDss bdd = new IdpsInstanceBandDss();
		bdd.setUserId(userId);
		bdd.setServiceId(serviceId);
		bdd.setDssPid(dssPId);
		bdd.setDssServiceId(dssServiceId);
		bdd.setDssServicePwd(dssServicePwd);
		IdpsInstanceBandDssMapper im = ServiceUtil
				.getMapper(IdpsInstanceBandDssMapper.class);
		return im.insert(bdd);

	}

	private boolean validateDss(String dssUserId, String dssServiceId,
			String dssServicePwd) throws Exception {
		String authUrl = getSysConf(IdpsConstants.AUTH_TABLE_CODE,
				IdpsConstants.AUTH_FIELD_CODE);
		AuthDescriptor ad = new AuthDescriptor(authUrl, dssUserId,
				dssServicePwd, dssServiceId);
		AuthResult authResult = UserClientFactory.getUserClient().auth(ad);
		return authResult != null;
	}

	private String getSysConf(String tCode, String fCode) throws PaasException {
		IpaasSysConfigMapper rpm = ServiceUtil
				.getMapper(IpaasSysConfigMapper.class);
		IpaasSysConfigCriteria rpmc = new IpaasSysConfigCriteria();
		rpmc.createCriteria().andTableCodeEqualTo(tCode)
				.andFieldCodeEqualTo(fCode);
		List<IpaasSysConfig> auths = rpm.selectByExample(rpmc);
		if (auths == null || auths.isEmpty())
			throw new PaasException("not config " + fCode + ".");
		return auths.get(0).getFieldValue().trim();
	}

	/**
	 * 开通多台 图片服务
	 * 
	 * @param userId
	 * @param serviceId
	 * @param nodeNum
	 * @throws Exception
	 */
	private void openMany(String userId, String serviceId, int nodeNum,
			String serviceName, String dssPId, String dssServiceId,
			String dssServicePwd,String isUpgrade) throws Exception {
		// 选择nodeNum个 图片服务器selectIdpsResources
		List<IdpsResourcePool> irps = selectIdpsResources4Many(nodeNum);
		// 选择2个 负载均衡
		List<IdpsBalanceResourcePool> balances = selectIdpsBalance(IdpsConstants.IDPS_BALANCE_NUM);

		// 处理服务端 docker 命令 拉gm、图片服务器war,nginx镜像，启动docker化的实例
		handleServer4Many(irps, balances, userId, serviceId, dssPId,
				dssServiceId, dssServicePwd);
		
		if("no".equals(isUpgrade)){
			// updateBalanceResource
			for (IdpsBalanceResourcePool balanceRe : balances) {
				updateBalanceResource(balanceRe);
			}
			// 在zk中记录申请信息
			addZkConfig(
					userId,
					serviceId,
					getImageServerUrl(balances.get(0).getIdpsBalanceHostIp(),
							balances.get(0).getIdpsBalancePort()));
			// 沉淀用户实例
			for (int i = 0; i < irps.size(); i++) {
				addIdpsUserInstance(irps.get(i).getIdpsHostIp(), irps.get(i)
						.getIdpsPort(), userId, serviceId, serviceName,
						IdpsConstants.IDPS_INSTANCE_TYPE);
			}
			for (int i = 0; i < balances.size(); i++) {
				addIdpsUserInstance(balances.get(i).getIdpsBalanceHostIp(),
						balances.get(i).getIdpsBalancePort(), userId, serviceId,
						serviceName, IdpsConstants.IDPS_BALANCE_TYPE);
			}
		}
		
	}
	
	/**
	 * 停用多台 图片容器
	 * 
	 * @param userId
	 * @param serviceId
	 * @param nodeNum
	 * @throws Exception
	 */
	private void stopMany(String userId, String serviceId, int nodeNum,
			String serviceName, String dssPId, String dssServiceId,
			String dssServicePwd) throws Exception {
		// 选择nodeNum个 图片服务器selectIdpsResources
		List<IdpsResourcePool> irps = selectIdpsResources4Many(nodeNum);
		// 选择2个 负载均衡
		List<IdpsBalanceResourcePool> balances = selectIdpsBalance(IdpsConstants.IDPS_BALANCE_NUM);
		// 处理服务端 docker 命令 拉gm、图片服务器war,nginx镜像，启动docker化的实例
		stopServer4Many(irps, balances, userId, serviceId, dssPId,
				dssServiceId, dssServicePwd);
	}
	
	/**
	 * 删除多台 图片容器
	 * 
	 * @param userId
	 * @param serviceId
	 * @param nodeNum
	 * @throws Exception
	 */
	private void deleteMany(String userId, String serviceId, int nodeNum,
			String serviceName, String dssPId, String dssServiceId,
			String dssServicePwd) throws Exception {
		// 选择nodeNum个 图片服务器selectIdpsResources
		List<IdpsResourcePool> irps = selectIdpsResources4Many(nodeNum);
		// 选择2个 负载均衡
		List<IdpsBalanceResourcePool> balances = selectIdpsBalance(IdpsConstants.IDPS_BALANCE_NUM);
		// 处理服务端 docker 命令 拉gm、图片服务器war,nginx镜像，启动docker化的实例
		deleteServer4Many(irps, balances, userId, serviceId, dssPId,
				dssServiceId, dssServicePwd);
	}
	
	/**
	 * 启动多台 图片容器
	 * 
	 * @param userId
	 * @param serviceId
	 * @param nodeNum
	 * @throws Exception
	 */
	private void startMany(String userId, String serviceId, int nodeNum,
			String serviceName, String dssPId, String dssServiceId,
			String dssServicePwd) throws Exception {
		// 选择nodeNum个 图片服务器selectIdpsResources
		List<IdpsResourcePool> irps = selectIdpsResources4Many(nodeNum);
		// 选择2个 负载均衡
		List<IdpsBalanceResourcePool> balances = selectIdpsBalance(IdpsConstants.IDPS_BALANCE_NUM);
		// 处理服务端 docker 命令 拉gm、图片服务器war,nginx镜像，启动docker化的实例
		startServer4Many(irps, balances, userId, serviceId, dssPId,
				dssServiceId, dssServicePwd);
	}

	@SuppressWarnings("unused")
	private IdpsResourcePool convertResource(
			IdpsBalanceResourcePool idpsBalanceResourcePool) {
		IdpsResourcePool des = new IdpsResourcePool();
		BeanUtils.copyProperties(idpsBalanceResourcePool, des);
		des.setIdpsHostIp(idpsBalanceResourcePool.getIdpsBalanceHostIp());
		des.setIdpsPort(idpsBalanceResourcePool.getIdpsBalancePort());
		return des;
	}

	private void handleServer4Many(List<IdpsResourcePool> irps,
			List<IdpsBalanceResourcePool> balances, String userId,
			String serviceId, String dssPId, String dssServiceId,
			String dssServicePwd) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		StringBuffer servers = new StringBuffer("\"");
		//用户处理idps容器名字做唯一
		int ipdsNum = 1; 
		// 启动每一个 图片服务器
		for (IdpsResourcePool irp : irps) {
			handleServer(irp, dssPId, dssServiceId, dssServicePwd,userId,serviceId+"_"+ipdsNum);
			// 便于负载均衡
			servers.append("_server_").append(irp.getIdpsHostIp()).append(":")
					.append(irp.getIdpsPort()).append(";");
			ipdsNum++;
		}
		servers.append("\"");
		IpaasImageResource balanceImage = getBalancImage();
		// 上传文件
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/idpsimagebalance.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil
				.uploadFile("idps/idpsimagebalance.yml", cnt, AidUtil.getAid());

		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_run_image_balance.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/ansible_run_image_balance.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_run_image_balance.sh", AidUtil.getAid());
		//用户处理idps容器名字做唯一
		int balanceNum = 1; 
		for (IdpsBalanceResourcePool balance : balances) {
			balance.setIdpsBalancePort(balance.getIdpsBalancePort() + 1);
			// 先
			String mkSshHosts = ParamUtil.replace(
					IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
							basePath + "idps",
							balance.getIdpsBalanceHostIp().replace(".", ""),
							balance.getIdpsBalanceHostIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

			String runImage = ParamUtil.replace(
					IdpsConstants.DOCKER_4_BALANCE,
					new String[] {
							"",
							balance.getIdpsBalanceHostIp().replace(".", ""),
							balance.getSshUser(),
							balance.getSshPassword(),
							balance.getIdpsBalanceHostIp(),
							balanceImage.getImageRepository() + "/"
									+ balanceImage.getImageName(),
							balance.getIdpsBalancePort() + "",
							servers.toString(), basePath + "idps" ,
							"idps_balance_"+userId+"_"+serviceId+"_"+balanceNum});
			LOG.info("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
			balanceNum++;
		}

	}
	
	//停用多个ipds容器
	private void stopServer4Many(List<IdpsResourcePool> irps,
			List<IdpsBalanceResourcePool> balances, String userId,
			String serviceId, String dssPId, String dssServiceId,
			String dssServicePwd) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		StringBuffer servers = new StringBuffer("\"");
		//用户处理idps容器名字做唯一
		int ipdsNum = 1; 
		// 启动每一个 图片服务器
		for (IdpsResourcePool irp : irps) {
			stopServer(irp, dssPId, dssServiceId, dssServicePwd,userId,serviceId+"_"+ipdsNum);
			// 便于负载均衡
			servers.append("_server_").append(irp.getIdpsHostIp()).append(":")
					.append(irp.getIdpsPort()).append(";");
			ipdsNum++;
		}
		servers.append("\"");
		IpaasImageResource balanceImage = getBalancImage();
		// 上传文件
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_stop_container_balance.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/ansible_stop_container_balance.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_stop_container_balance.sh", AidUtil.getAid());
		//用户处理idps容器名字做唯一
		int balanceNum = 1; 
		for (IdpsBalanceResourcePool balance : balances) {
			balance.setIdpsBalancePort(balance.getIdpsBalancePort() + 1);
			// 先
			String mkSshHosts = ParamUtil.replace(
					IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
							basePath + "idps",
							balance.getIdpsBalanceHostIp().replace(".", ""),
							balance.getIdpsBalanceHostIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
			String runImage = ParamUtil.replace(
					IdpsConstants.DOCKER_4_BALANCE_STOP_CONTAINER,
					new String[] {
							"",
							balance.getIdpsBalanceHostIp().replace(".", ""),
							balance.getSshUser(),
							balance.getSshPassword(),
							balance.getIdpsBalanceHostIp(),
							balanceImage.getImageRepository() + "/"
									+ balanceImage.getImageName(),
							balance.getIdpsBalancePort() + "",
							servers.toString(), basePath + "idps" ,
							"idps_balance_"+userId+"_"+serviceId+"_"+balanceNum});
			LOG.info("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
			balanceNum++;
		}

	}

	//停用多个ipds容器
	private void deleteServer4Many(List<IdpsResourcePool> irps,
			List<IdpsBalanceResourcePool> balances, String userId,
			String serviceId, String dssPId, String dssServiceId,
			String dssServicePwd) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		StringBuffer servers = new StringBuffer("\"");
		//用户处理idps容器名字做唯一
		int ipdsNum = 1; 
		// 启动每一个 图片服务器
		for (IdpsResourcePool irp : irps) {
			deleteServer(irp, dssPId, dssServiceId, dssServicePwd,userId,serviceId+"_"+ipdsNum);
			// 便于负载均衡
			servers.append("_server_").append(irp.getIdpsHostIp()).append(":")
					.append(irp.getIdpsPort()).append(";");
			ipdsNum++;
		}
		servers.append("\"");
		IpaasImageResource balanceImage = getBalancImage();
		// 上传文件
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_delete_container_balance.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/ansible_delete_container_balance.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_delete_container_balance.sh", AidUtil.getAid());
		//用户处理idps容器名字做唯一
		int balanceNum = 1; 
		for (IdpsBalanceResourcePool balance : balances) {
			balance.setIdpsBalancePort(balance.getIdpsBalancePort() + 1);
			// 先
			String mkSshHosts = ParamUtil.replace(
					IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
							basePath + "idps",
							balance.getIdpsBalanceHostIp().replace(".", ""),
							balance.getIdpsBalanceHostIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
			String runImage = ParamUtil.replace(
					IdpsConstants.DOCKER_4_BALANCE_DELETE_CONTAINER,
					new String[] {
							"",
							balance.getIdpsBalanceHostIp().replace(".", ""),
							balance.getSshUser(),
							balance.getSshPassword(),
							balance.getIdpsBalanceHostIp(),
							balanceImage.getImageRepository() + "/"
									+ balanceImage.getImageName(),
							balance.getIdpsBalancePort() + "",
							servers.toString(), basePath + "idps" ,
							"idps_balance_"+userId+"_"+serviceId+"_"+balanceNum});
			LOG.info("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
			balanceNum++;
		}

	}
	
	//启动多个ipds容器
		private void startServer4Many(List<IdpsResourcePool> irps,
				List<IdpsBalanceResourcePool> balances, String userId,
				String serviceId, String dssPId, String dssServiceId,
				String dssServicePwd) throws Exception {
			String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
			StringBuffer servers = new StringBuffer("\"");
			//用户处理idps容器名字做唯一
			int ipdsNum = 1; 
			// 启动每一个 图片服务器
			for (IdpsResourcePool irp : irps) {
				startServer(irp, dssPId, dssServiceId, dssServicePwd,userId,serviceId+"_"+ipdsNum);
				// 便于负载均衡
				servers.append("_server_").append(irp.getIdpsHostIp()).append(":")
						.append(irp.getIdpsPort()).append(";");
				ipdsNum++;
			}
			servers.append("\"");
			IpaasImageResource balanceImage = getBalancImage();
			// 上传文件
			// 1.先将需要执行镜像命令的机器配置文件上传上去。
			InputStream in = IdpsSvImpl.class
					.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
			String[] cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
					AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath
					+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
			in = IdpsSvImpl.class
					.getResourceAsStream("/playbook/idps/ansible_start_container_balance.sh");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("idps/ansible_start_container_balance.sh", cnt,
					AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath
					+ "idps/ansible_start_container_balance.sh", AidUtil.getAid());
			//用户处理idps容器名字做唯一
			int balanceNum = 1; 
			for (IdpsBalanceResourcePool balance : balances) {
				balance.setIdpsBalancePort(balance.getIdpsBalancePort() + 1);
				// 先
				String mkSshHosts = ParamUtil.replace(
						IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
								basePath + "idps",
								balance.getIdpsBalanceHostIp().replace(".", ""),
								balance.getIdpsBalanceHostIp() });
				LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
				AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
				String runImage = ParamUtil.replace(
						IdpsConstants.DOCKER_4_BALANCE_START_CONTAINER,
						new String[] {
								"",
								balance.getIdpsBalanceHostIp().replace(".", ""),
								balance.getSshUser(),
								balance.getSshPassword(),
								balance.getIdpsBalanceHostIp(),
								balanceImage.getImageRepository() + "/"
										+ balanceImage.getImageName(),
								balance.getIdpsBalancePort() + "",
								servers.toString(), basePath + "idps" ,
								"idps_balance_"+userId+"_"+serviceId+"_"+balanceNum});
				LOG.info("---------runImage {}----------", runImage);
				AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
				balanceNum++;
			}

		}
		

	private List<IdpsBalanceResourcePool> selectIdpsBalance(int num)
			throws Exception {
		IdpsBalanceResourcePoolMapper rpm = ServiceUtil
				.getMapper(IdpsBalanceResourcePoolMapper.class);
		IdpsBalanceResourcePoolCriteria rpmc = new IdpsBalanceResourcePoolCriteria();
		rpmc.createCriteria().andStatusEqualTo(IdpsConstants.VALIDATE_STATUS);
		rpmc.setLimitStart(0);
		rpmc.setLimitEnd(num);
		List<IdpsBalanceResourcePool> firstRes = rpm.selectByExample(rpmc);
		if (firstRes == null || firstRes.isEmpty())
			throw new PaasException("IDPS Balance Resource not config.");
		return firstRes;
	}

	private List<IdpsResourcePool> selectIdpsResources4Many(int nodeNum)
			throws Exception {
		List<IdpsResourcePool> irp = selectIdpsResources(nodeNum);
		int hostNum = irp.size();
		int i = 0;
		int k = hostNum;
		List<IdpsResourcePool> resultList = new ArrayList<>();
		int count = 0;
		Map<String, Integer> hostPorts = new HashMap<String, Integer>();
		while (i < nodeNum && count < (nodeNum + 1)) {
			for (int m = 0; m < k; m++) {
				boolean canUse = false;
				IdpsResourcePool res = irp.get(m);
				IdpsResourcePool target = new IdpsResourcePool();
				if (!hostPorts.containsKey(res.getIdpsHostIp())) {
					hostPorts.put(res.getIdpsHostIp(), res.getIdpsPort());
				}
				if (res != null && res.getCycle() == 1) {
					IdpsUserInstance ui = getCanUseInstance(res.getIdpsHostIp());
					if (ui != null) {
						res.setIdpsPort(ui.getIdpsHostPort());
						canUse = true;
					}
				} else {
					if (res.getIdpsPort() < res.getMaxPort()) {
						int port = hostPorts.get(res.getIdpsHostIp()) + 1;
						res.setIdpsPort(port);
						canUse = true;
						hostPorts.put(res.getIdpsHostIp(), port);
					} else {
						res.setCycle(1);
						updateResource(res);
					}
				}
				if (canUse) {
					BeanUtils.copyProperties(res, target);
					i++;
					resultList.add(target);
					LOG.debug(
							"--------------select-ip {}--port-{}---------------------",
							target.getIdpsHostIp(), target.getIdpsPort());
				}
			}
			count++;
		}
		if (count > nodeNum)
			throw new PaasException("idps resource not enough.");
		// update IdpsResourcePool
		IdpsResourcePoolMapper rpm = ServiceUtil
				.getMapper(IdpsResourcePoolMapper.class);
		IdpsResourcePoolCriteria rpmc = new IdpsResourcePoolCriteria();
		for (Map.Entry<String, Integer> ent : hostPorts.entrySet()) {
			rpmc.createCriteria().andIdpsHostIpEqualTo(ent.getKey())
					.andStatusEqualTo(IdpsConstants.VALIDATE_STATUS);
			IdpsResourcePool target = new IdpsResourcePool();
			target.setIdpsPort(ent.getValue());
			rpm.updateByExampleSelective(target, rpmc);
		}
		return resultList;
	}

	/**
	 * 开通单台 图片服务
	 * 
	 * @param userId
	 * @param serviceId
	 * @param serviceName
	 * @throws Exception
	 */
	private void openOne(String userId, String serviceId, String serviceName,
			String dssPId, String dssServiceId, String dssServicePwd,String isUpgrade)
			throws Exception {
		// 选择资源
		List<IdpsResourcePool> idpsResources = selectIdpsResources(1);
		IdpsResourcePool idpsResourcePool = idpsResources.get(0);
		// 如果该主机端口已经用完，从idps_user_instance选择该主机最小的已经失效的端口号
		if (idpsResourcePool != null && idpsResourcePool.getCycle() == 1) {
			idpsResourcePool.setIdpsPort(getCanUseInstance(
					idpsResourcePool.getIdpsHostIp()).getIdpsHostPort());
		} else {
			if (idpsResourcePool.getIdpsPort() == idpsResourcePool.getMaxPort()) {
				idpsResourcePool.setCycle(1);
			}
			idpsResourcePool.setIdpsPort(idpsResourcePool.getIdpsPort() + 1);
			int changeRow = updateResource(idpsResourcePool);

			if (changeRow != 1) {
				throw new PaasException("updateResource fail !");
			}

		}
		LOG.debug(
				"----------------seelct IdpsResource host :{}，port ：{}---------",
				idpsResourcePool.getIdpsHostIp(),
				idpsResourcePool.getIdpsPort());
		// 处理服务端 docker 命令 拉gm、图片服务器war，启动docker化的实例
		handleServer(idpsResourcePool, dssPId, dssServiceId, dssServicePwd,userId,serviceId);
		if("no".equals(isUpgrade)){
			// 在zk中记录申请信息
			addZkConfig(
					userId,
					serviceId,
					getImageServerUrl(idpsResourcePool.getIdpsHostIp(),
							idpsResourcePool.getIdpsPort()));
			// 沉淀用户实例
			addIdpsUserInstance(idpsResourcePool.getIdpsHostIp(),
					idpsResourcePool.getIdpsPort(), userId, serviceId, serviceName,
					IdpsConstants.IDPS_INSTANCE_TYPE);
		}

	}
	
	/**
	 * 启动单台 图片容器
	 * 
	 * @param userId
	 * @param serviceId
	 * @param serviceName
	 * @throws Exception
	 */
	private void startOne(String userId, String serviceId, String serviceName,
			String dssPId, String dssServiceId, String dssServicePwd)
			throws Exception {
		// 选择资源
		List<IdpsResourcePool> idpsResources = selectIdpsResources(1);
		IdpsResourcePool idpsResourcePool = idpsResources.get(0);
		// 如果该主机端口已经用完，从idps_user_instance选择该主机最小的已经失效的端口号
		if (idpsResourcePool != null && idpsResourcePool.getCycle() == 1) {
			idpsResourcePool.setIdpsPort(getCanUseInstance(
					idpsResourcePool.getIdpsHostIp()).getIdpsHostPort());
		} else {
			if (idpsResourcePool.getIdpsPort() == idpsResourcePool.getMaxPort()) {
				idpsResourcePool.setCycle(1);
			}
			idpsResourcePool.setIdpsPort(idpsResourcePool.getIdpsPort() + 1);
		}
		LOG.debug(
				"----------------seelct IdpsResource host :{}，port ：{}---------",
				idpsResourcePool.getIdpsHostIp(),
				idpsResourcePool.getIdpsPort());
		// 启动单个容器
		startServer(idpsResourcePool, dssPId, dssServiceId, dssServicePwd,userId,serviceId);
	}
	
	/**
	 * 停用单台 图片容器
	 * 
	 * @param userId
	 * @param serviceId
	 * @param serviceName
	 * @throws Exception
	 */
	private void stopOne(String userId, String serviceId, String serviceName,
			String dssPId, String dssServiceId, String dssServicePwd)
			throws Exception {
		// 选择资源
		List<IdpsResourcePool> idpsResources = selectIdpsResources(1);
		IdpsResourcePool idpsResourcePool = idpsResources.get(0);
		// 如果该主机端口已经用完，从idps_user_instance选择该主机最小的已经失效的端口号
		if (idpsResourcePool != null && idpsResourcePool.getCycle() == 1) {
			idpsResourcePool.setIdpsPort(getCanUseInstance(
					idpsResourcePool.getIdpsHostIp()).getIdpsHostPort());
		} else {
			if (idpsResourcePool.getIdpsPort() == idpsResourcePool.getMaxPort()) {
				idpsResourcePool.setCycle(1);
			}
			idpsResourcePool.setIdpsPort(idpsResourcePool.getIdpsPort() + 1);
		}
		LOG.debug(
				"----------------seelct IdpsResource host :{}，port ：{}---------",
				idpsResourcePool.getIdpsHostIp(),
				idpsResourcePool.getIdpsPort());
		// 启动单个容器
		stopServer(idpsResourcePool, dssPId, dssServiceId, dssServicePwd,userId,serviceId);
	}
	
	/**
	 * 删除单台 图片容器
	 * 
	 * @param userId
	 * @param serviceId
	 * @param serviceName
	 * @throws Exception
	 */
	private void deleteOne(String userId, String serviceId, String serviceName,
			String dssPId, String dssServiceId, String dssServicePwd)
			throws Exception {
		// 选择资源
		List<IdpsResourcePool> idpsResources = selectIdpsResources(1);
		IdpsResourcePool idpsResourcePool = idpsResources.get(0);
		// 如果该主机端口已经用完，从idps_user_instance选择该主机最小的已经失效的端口号
		if (idpsResourcePool != null && idpsResourcePool.getCycle() == 1) {
			idpsResourcePool.setIdpsPort(getCanUseInstance(
					idpsResourcePool.getIdpsHostIp()).getIdpsHostPort());
		} else {
			if (idpsResourcePool.getIdpsPort() == idpsResourcePool.getMaxPort()) {
				idpsResourcePool.setCycle(1);
			}
			idpsResourcePool.setIdpsPort(idpsResourcePool.getIdpsPort() + 1);
		}
		LOG.debug(
				"----------------seelct IdpsResource host :{}，port ：{}---------",
				idpsResourcePool.getIdpsHostIp(),
				idpsResourcePool.getIdpsPort());
		// 删除单个容器
		deleteServer(idpsResourcePool, dssPId, dssServiceId, dssServicePwd,userId,serviceId);
	}

	private void addZkConfig(String userId, String serviceId, String url)
			throws PaasException {
		// 在zk中记录申请信息
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(IdpsConstants.IDPS_ZK_PATH + serviceId);
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();
		dataJson.addProperty(IdpsConstants.IDPS_IMAGE_URL, url);
		LOG.debug("-------userId {}--serviceId {}--image server {}-------",
				userId, serviceId, url);
		iCCSComponentManageSv.add(op, dataJson.toString());

		op.setPath(IdpsConstants.IDPS_ZK_PATH + serviceId + "/"
				+ IdpsConstants.IDPS_IMAGE_URL_OUT);
		iCCSComponentManageSv.add(op, dataJson.toString());
	}
	
	//删除 指定节点
	private void deleteConf(String userId, String path) throws PaasException {
		CCSComponentOperationParam op = getZKBase(userId);
		op.setPath(path);
		iCCSComponentManageSv.delete(op);
	}
	
	protected CCSComponentOperationParam getZKBase(String userId) {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPathType(PathType.READONLY);
		return op;
	}

	private String getImageServerUrl(String ip, int port) throws PaasException {
		IpaasSysConfigMapper rpm = ServiceUtil
				.getMapper(IpaasSysConfigMapper.class);
		IpaasSysConfigCriteria rpmc = new IpaasSysConfigCriteria();
		rpmc.createCriteria()
				.andTableCodeEqualTo(IdpsConstants.IMAGE_SERVER_NAME_T_CODE)
				.andFieldCodeEqualTo(IdpsConstants.IMAGE_SERVER_NAME_F_CODE);
		List<IpaasSysConfig> images = rpm.selectByExample(rpmc);
		if (images == null || images.isEmpty())
			throw new PaasException("not config image server name .");
		return "http://" + ip + ":" + port + "/"
				+ images.get(0).getFieldValue().trim();
	}

	/**
	 * 处理单台图片服务器
	 * 
	 * @param idpsResourcePool
	 * @throws Exception
	 */
	private void handleServer(IdpsResourcePool idpsResourcePool, String dssPId,
			String dssServiceId, String dssServicePwd,String userId,String serviceId) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/idpsimage.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/idpsimage.yml", cnt, AidUtil.getAid());
		// 2.执行这个初始化命令
		String mkSshHosts = ParamUtil.replace(
				IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
						basePath + "idps",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getIdpsHostIp() });
		LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
		AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

		IpaasImageResource gmImage = getGmImage();
		// 还得上传文件
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_run_image.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil
				.uploadFile("idps/ansible_run_image.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_run_image.sh", AidUtil.getAid());
		// 开始执行
		String runImage = ParamUtil.replace(
				IdpsConstants.DOCKER_4_GM_AND_TOMCAT,
				new String[] {
						"",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getSshUser(),
						idpsResourcePool.getSshPassword(),
						idpsResourcePool.getIdpsHostIp(),
						gmImage.getImageRepository() + "/"
								+ gmImage.getImageName(),
						idpsResourcePool.getIdpsPort() + "",
						getSysConf(IdpsConstants.AUTH_TABLE_CODE,
								IdpsConstants.AUTH_FIELD_CODE), dssPId,
						dssServiceId, dssServicePwd, basePath + "idps" ,
						"idps_"+userId+"_"+serviceId});

		LOG.debug("---------runImage {}----------", runImage);
		AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
	}
	
	/**
	 * 启动单台图片容器
	 * 
	 * @param idpsResourcePool
	 * @throws Exception
	 */
	private void startServer(IdpsResourcePool idpsResourcePool, String dssPId,
			String dssServiceId, String dssServicePwd,String userId,String serviceId) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/startidpscontainer.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/startidpscontainer.yml", cnt, AidUtil.getAid());
		// 2.执行这个初始化命令
		String mkSshHosts = ParamUtil.replace(
				IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
						basePath + "idps",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getIdpsHostIp() });
		LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
		AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
		IpaasImageResource gmImage = getGmImage();
		// 还得上传文件
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_start_container.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil
				.uploadFile("idps/ansible_start_container.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_start_container.sh", AidUtil.getAid());
		// 开始执行
		String runImage = ParamUtil.replace(
				IdpsConstants.DOCKER_4_START_CONTAINER,
				new String[] {
						"",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getSshUser(),
						idpsResourcePool.getSshPassword(),
						idpsResourcePool.getIdpsHostIp(),
						gmImage.getImageRepository() + "/"
								+ gmImage.getImageName(),
						idpsResourcePool.getIdpsPort() + "",
						getSysConf(IdpsConstants.AUTH_TABLE_CODE,
								IdpsConstants.AUTH_FIELD_CODE), dssPId,
						dssServiceId, dssServicePwd, basePath + "idps" ,
						"idps_"+userId+"_"+serviceId});
		LOG.debug("---------runImage {}----------", runImage);
		AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
	}
	/**
	 * 停用单台图片容器
	 * 
	 * @param idpsResourcePool
	 * @throws Exception
	 */
	private void stopServer(IdpsResourcePool idpsResourcePool, String dssPId,
			String dssServiceId, String dssServicePwd,String userId,String serviceId) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/stopidpscontainer.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/stopidpscontainer.yml", cnt, AidUtil.getAid());
		// 2.执行这个初始化命令
		String mkSshHosts = ParamUtil.replace(
				IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
						basePath + "idps",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getIdpsHostIp() });
		LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
		AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
		IpaasImageResource gmImage = getGmImage();
		// 还得上传文件
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_stop_container.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil
				.uploadFile("idps/ansible_stop_container.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_stop_container.sh", AidUtil.getAid());
		// 开始执行
		String runImage = ParamUtil.replace(
				IdpsConstants.DOCKER_4_STOP_CONTAINER,
				new String[] {
						"",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getSshUser(),
						idpsResourcePool.getSshPassword(),
						idpsResourcePool.getIdpsHostIp(),
						gmImage.getImageRepository() + "/"
								+ gmImage.getImageName(),
						idpsResourcePool.getIdpsPort() + "",
						getSysConf(IdpsConstants.AUTH_TABLE_CODE,
								IdpsConstants.AUTH_FIELD_CODE), dssPId,
						dssServiceId, dssServicePwd, basePath + "idps" ,
						"idps_"+userId+"_"+serviceId});
		LOG.debug("---------runImage {}----------", runImage);
		AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
	}
	
	/**
	 * 删除单台图片容器
	 * 
	 * @param idpsResourcePool
	 * @throws Exception
	 */
	private void deleteServer(IdpsResourcePool idpsResourcePool, String dssPId,
			String dssServiceId, String dssServicePwd,String userId,String serviceId) throws Exception {
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/init_ansible_ssh_hosts.sh", cnt,
				AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/init_ansible_ssh_hosts.sh", AidUtil.getAid());
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/deleteidpscontainer.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("idps/deleteidpscontainer.yml", cnt, AidUtil.getAid());
		// 2.执行这个初始化命令
		String mkSshHosts = ParamUtil.replace(
				IdpsConstants.CREATE_ANSIBLE_HOSTS, new String[] {
						basePath + "idps",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getIdpsHostIp() });
		LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
		AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());
		IpaasImageResource gmImage = getGmImage();
		// 还得上传文件
		in = IdpsSvImpl.class
				.getResourceAsStream("/playbook/idps/ansible_delete_container.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil
				.uploadFile("idps/ansible_delete_container.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath
				+ "idps/ansible_delete_container.sh", AidUtil.getAid());
		// 开始执行
		String runImage = ParamUtil.replace(
				IdpsConstants.DOCKER_4_DELETE_CONTAINER,
				new String[] {
						"",
						idpsResourcePool.getIdpsHostIp().replace(".", ""),
						idpsResourcePool.getSshUser(),
						idpsResourcePool.getSshPassword(),
						idpsResourcePool.getIdpsHostIp(),
						gmImage.getImageRepository() + "/"
								+ gmImage.getImageName(),
						idpsResourcePool.getIdpsPort() + "",
						getSysConf(IdpsConstants.AUTH_TABLE_CODE,
								IdpsConstants.AUTH_FIELD_CODE), dssPId,
						dssServiceId, dssServicePwd, basePath + "idps" ,
						"idps_"+userId+"_"+serviceId});
		LOG.debug("---------runImage {}----------", runImage);
		AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
	}
	
	private IpaasImageResource getGmImage() throws PaasException {
		IpaasImageResourceMapper rpm = ServiceUtil
				.getMapper(IpaasImageResourceMapper.class);
		IpaasImageResourceCriteria rpmc = new IpaasImageResourceCriteria();
		rpmc.createCriteria().andStatusEqualTo(IdpsConstants.VALIDATE_STATUS)
				.andServiceCodeEqualTo(IdpsConstants.SERVICE_CODE)
				.andImageCodeEqualTo(IdpsConstants.GM_IMAGE_CODE);
		List<IpaasImageResource> res = rpm.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("IDPS IMAGE gm not config.");
		return res.get(0);
	}

	private IpaasImageResource getBalancImage() throws PaasException {
		IpaasImageResourceMapper rpm = ServiceUtil
				.getMapper(IpaasImageResourceMapper.class);
		IpaasImageResourceCriteria rpmc = new IpaasImageResourceCriteria();
		rpmc.createCriteria().andStatusEqualTo(IdpsConstants.VALIDATE_STATUS)
				.andServiceCodeEqualTo(IdpsConstants.SERVICE_CODE)
				.andImageCodeEqualTo(IdpsConstants.BALANC_IMAGE_CODE);
		List<IpaasImageResource> res = rpm.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("IDPS IMAGE load balance not config.");
		return res.get(0);
	}

	private int addIdpsUserInstance(String ip, int port, String userId,
			String serviceId, String serviceName, int type) {
		IdpsUserInstance idpsUserInstance = new IdpsUserInstance();
		idpsUserInstance.setIdpsHostIp(ip);
		idpsUserInstance.setServiceId(serviceId);
		idpsUserInstance.setServiceName(serviceName);
		idpsUserInstance.setIdpsHostPort(port);
		idpsUserInstance.setStatus(IdpsConstants.VALIDATE_STATUS);
		idpsUserInstance.setUserId(userId);
		idpsUserInstance
				.setBeginTime(new Timestamp(System.currentTimeMillis()));
		idpsUserInstance.setType(type);
		return addInstance(idpsUserInstance);
	}

	/**
	 * 添加用户实例
	 * 
	 * @param idpsUserInstance
	 * @return
	 */
	private int addInstance(IdpsUserInstance idpsUserInstance) {
		IdpsUserInstanceMapper im = ServiceUtil
				.getMapper(IdpsUserInstanceMapper.class);
		return im.insert(idpsUserInstance);
	}

	/**
	 * 更新资源
	 * 
	 * @param IdpsResourcePool
	 * @return
	 */
	private int updateResource(IdpsResourcePool idpsResourcePool)
			throws PaasException {
		IdpsResourcePoolMapper rpm = ServiceUtil
				.getMapper(IdpsResourcePoolMapper.class);
		IdpsResourcePoolCriteria rpmc = new IdpsResourcePoolCriteria();
		rpmc.createCriteria().andIdEqualTo(idpsResourcePool.getId())
				.andIdpsPortEqualTo(idpsResourcePool.getIdpsPort() - 1);
		return rpm.updateByExampleSelective(idpsResourcePool, rpmc);
	}

	/**
	 * 更新资源
	 * 
	 * @param balanceResourcePool
	 * @return
	 */
	private int updateBalanceResource(
			IdpsBalanceResourcePool balanceResourcePool) throws PaasException {
		IdpsBalanceResourcePoolMapper rpm = ServiceUtil
				.getMapper(IdpsBalanceResourcePoolMapper.class);
		IdpsBalanceResourcePoolCriteria rpmc = new IdpsBalanceResourcePoolCriteria();
		rpmc.createCriteria()
				.andIdEqualTo(balanceResourcePool.getId())
				.andIdpsBalancePortEqualTo(
						balanceResourcePool.getIdpsBalancePort() - 1);
		return rpm.updateByExampleSelective(balanceResourcePool, rpmc);
	}

	/**
	 * 获得IdpsUserInstance中 失效的记录
	 * 
	 * @param host
	 * @return
	 */
	private IdpsUserInstance getCanUseInstance(String host) {
		IdpsUserInstanceMapper im = ServiceUtil
				.getMapper(IdpsUserInstanceMapper.class);
		IdpsUserInstanceCriteria imc = new IdpsUserInstanceCriteria();
		imc.createCriteria().andStatusNotEqualTo(IdpsConstants.VALIDATE_STATUS)
				.andIdpsHostIpEqualTo(host)
				.andTypeEqualTo(IdpsConstants.IDPS_INSTANCE_TYPE);
		imc.setLimitStart(0);
		imc.setLimitEnd(1);
		List<IdpsUserInstance> list = im.selectByExample(imc);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	private List<IdpsResourcePool> selectIdpsResources(int num)
			throws PaasException {
		// best
		IdpsResourcePoolMapper rpm = ServiceUtil
				.getMapper(IdpsResourcePoolMapper.class);
		IdpsResourcePoolCriteria rpmc = new IdpsResourcePoolCriteria();
		rpmc.createCriteria().andStatusEqualTo(IdpsConstants.VALIDATE_STATUS);
		rpmc.setLimitStart(0);
		rpmc.setLimitEnd(num);
		List<IdpsResourcePool> firstRes = rpm.selectByExample(rpmc);
		if (firstRes == null || firstRes.isEmpty())
			throw new PaasException("IDPS Resource not config.");
		return firstRes;

	}

	private boolean existsService(String userId, String serviceId) {
		IdpsUserInstanceCriteria cc = new IdpsUserInstanceCriteria();
		cc.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId)
				.andStatusEqualTo(IdpsConstants.VALIDATE_STATUS);
		IdpsUserInstanceMapper im = ServiceUtil
				.getMapper(IdpsUserInstanceMapper.class);
		return im.countByExample(cc) > 0;
	}

	@Override
	public String modify(String param) throws PaasException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stop(String param) throws Exception {
		LOG.debug("----start idps ---param {}-----", param);
		//处理传过来的参数为json
		String jsonParam = param.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		Map<String, String> map = IdpsParamUtil.getParamMap(jsonParam);
		// 获取服务号配置参数
		final String serviceId = map.get(IdpsConstants.SERVICE_ID);
		final String userId = map.get(IdpsConstants.USER_ID);
		final String serviceName = map.get(IdpsConstants.SERVICE_NAME);
		final String nodeNumStr = map.get(IdpsConstants.NODE_NUM);
		final int nodeNum = Integer.valueOf(nodeNumStr);
		final String dssServiceId = map.get(IdpsConstants.DSS_SERVICE_ID);
		final String dssServicePwd = map.get(IdpsConstants.DSS_SERVICE_PWD);
		final String dssPId = map.get(IdpsConstants.DSS_P_ID);
		if (nodeNum == 1) {
			stopOne(userId, serviceId, serviceName, dssPId, dssServiceId,
					dssServicePwd);
		} else {
			stopMany(userId, serviceId, nodeNum, serviceName, dssPId,
					dssServiceId, dssServicePwd);
		}
		return IdpsConstants.SUCCESS_FLAG;
	}
	
	@Override
	public String clean(String param,String destroy) throws Exception {
		LOG.debug("----clean idps ---param {}-----", param);
		//处理传过来的参数为json
		String jsonParam = param.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		Map<String, String> map = IdpsParamUtil.getParamMap(jsonParam);
		// 获取服务号配置参数
		final String serviceId = map.get(IdpsConstants.SERVICE_ID);
		final String userId = map.get(IdpsConstants.USER_ID);
		final String serviceName = map.get(IdpsConstants.SERVICE_NAME);
		final String nodeNumStr = map.get(IdpsConstants.NODE_NUM);
		final int nodeNum = Integer.valueOf(nodeNumStr);
		final String dssServiceId = map.get(IdpsConstants.DSS_SERVICE_ID);
		final String dssServicePwd = map.get(IdpsConstants.DSS_SERVICE_PWD);
		final String dssPId = map.get(IdpsConstants.DSS_P_ID);
		//如果 是注销（就是销毁），需要清除zk
		if("yes".equals(destroy)){
			deleteConf(userId, IDPS_BASE_ZK_CONF+serviceId);
		}
		if (nodeNum == 1) {
			deleteOne(userId, serviceId, serviceName, dssPId, dssServiceId,
					dssServicePwd);
		} else {
			deleteMany(userId, serviceId, nodeNum, serviceName, dssPId,
					dssServiceId, dssServicePwd);
		}
		return IdpsConstants.SUCCESS_FLAG;
	}
	
	@Override
	public String start(String param) throws  Exception  {
		LOG.debug("----start idps ---param {}-----", param);
		//处理传过来的参数为json
		String jsonParam = param.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		Map<String, String> map = IdpsParamUtil.getParamMap(jsonParam);
		// 获取服务号配置参数
		final String serviceId = map.get(IdpsConstants.SERVICE_ID);
		final String userId = map.get(IdpsConstants.USER_ID);
		final String serviceName = map.get(IdpsConstants.SERVICE_NAME);
		final String nodeNumStr = map.get(IdpsConstants.NODE_NUM);
		final int nodeNum = Integer.valueOf(nodeNumStr);
		final String dssServiceId = map.get(IdpsConstants.DSS_SERVICE_ID);
		final String dssServicePwd = map.get(IdpsConstants.DSS_SERVICE_PWD);
		final String dssPId = map.get(IdpsConstants.DSS_P_ID);
		if (nodeNum == 1) {
			startOne(userId, serviceId, serviceName, dssPId, dssServiceId,
					dssServicePwd);
		} else {
			startMany(userId, serviceId, nodeNum, serviceName, dssPId,
					dssServiceId, dssServicePwd);
		}
		return IdpsConstants.SUCCESS_FLAG;
	}

	@Override
	public String restart(String param) throws PaasException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancel(String param) throws PaasException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
