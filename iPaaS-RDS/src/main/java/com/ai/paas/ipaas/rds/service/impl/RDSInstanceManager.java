package com.ai.paas.ipaas.rds.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.agent.util.AgentUtil;
import com.ai.paas.ipaas.agent.util.AidUtil;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasImageResourceMapper;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResource;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResourceCriteria;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsIncBaseMapper;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsResourcePoolMapper;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBaseCriteria;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePool;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePoolCriteria;
import com.ai.paas.ipaas.rds.dao.wo.InstanceGroup;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceManager;
import com.ai.paas.ipaas.rds.service.constant.AnsibleConstant;
import com.ai.paas.ipaas.rds.service.constant.InstanceType;
import com.ai.paas.ipaas.rds.service.constant.RDSCommonConstant;
import com.ai.paas.ipaas.rds.service.constant.ResponseResultMark;
import com.ai.paas.ipaas.rds.service.transfer.vo.CPU;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.ChangeContainerConfig;
import com.ai.paas.ipaas.rds.service.transfer.vo.ChangeContainerConfigResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateSRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.GetIncInfo;
import com.ai.paas.ipaas.rds.service.transfer.vo.GetInstanceInfoRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.InstanceBaseSimple;
import com.ai.paas.ipaas.rds.service.transfer.vo.MedthodDescribe;
import com.ai.paas.ipaas.rds.service.transfer.vo.ModifyRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.ModifyRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.RDSResourcePlan;
import com.ai.paas.ipaas.rds.service.transfer.vo.ResIncPlan;
import com.ai.paas.ipaas.rds.service.transfer.vo.RestartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.RestartResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.SwitchMaster;
import com.ai.paas.ipaas.rds.service.transfer.vo.SwitchMasterResult;
//import com.ai.paas.ipaas.rds.service.util.EntityToWhere;
import com.ai.paas.ipaas.rds.service.util.GsonSingleton;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 这里注入的Service与Transactional都是spring包的，
 * 因为dubbo的Service不支持Spring的Transaction
 * 所以需要通过dubbo的Service去调用这里的Service来支持事务
 * 
 * 传输对象命名规则就是对应方法名
 * 拓扑结构可以随意更改 
 * 空间是通过修改mysql配置和扩充磁盘阵列实现 
 * @author 作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:48:55
 * @version
 * @since
 */
@Service
@Transactional(rollbackFor = Exception.class) // 事务可以正常使用
public class RDSInstanceManager  {
	
	private static transient final Logger LOG = LoggerFactory.getLogger(RDSInstanceManager.class);

	@Autowired
	GsonSingleton g;

	@Autowired
	ICCSComponentManageSv iCCSComponentManageSv;

	/**
	 * 注销实例
	 * 可以是单个，也可以是多个
	 * @throws MyException 
	 * @throws Exception 
	 * @throws PaasException 
	 */
	public String cancel(String cancel) throws MyException {
		// 解析JSON对象
		CancelRDS cancelObject = g.getGson().fromJson(cancel, CancelRDS.class);
		CancelRDSResult cancelResult = new CancelRDSResult();
		Stack<RdsIncBase> instanceStack ;
		// 检查用户权限
		if(!CheckCancelLegal(cancelObject)){
			cancelResult.setStatus(ResponseResultMark.ERROR_ILLEGAL_AUTHORITY);
			return g.getGson().toJson(cancelResult);
		}

		// 检查数据完整性
		if(!CheckCancelDataLegal(cancelObject)){
			cancelResult.setStatus(ResponseResultMark.ERROR_LESS_IMP_PARAM);
			return g.getGson().toJson(cancelResult);
		}
		
		// 查询实例情况
		instanceStack = getInstanceStack(cancelObject.instanceid);
		if(instanceStack.isEmpty()){
			cancelResult.setStatus(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(cancelResult);
		}
		
		while(!instanceStack.isEmpty()){
			RdsIncBase instance = instanceStack.pop();
			instance.setIncStatus(RDSCommonConstant.INS_FREEZE);
			RdsIncBaseMapper statusMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
			statusMapper.updateByPrimaryKeySelective(instance);
			try {
				dealCancelInstanceDevided(instance);
				deleteZK(instance);
			} catch (IOException | PaasException e) {
				e.printStackTrace();
				cancelResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
				throw new MyException(g.getGson().toJson(cancelResult));
			}
		};

		cancelResult.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(cancelResult);
	}


	/**
	 * 通过一个实例id查询到实例和实例的主备、主从实例
	 * @param instanceid
	 * @return
	 */
	public Stack<RdsIncBase> getInstanceStack(int instanceid) {
		Stack<RdsIncBase> instanceStack = new Stack<RdsIncBase>();
		RdsIncBaseMapper ibm = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase instanceInfo = ibm.selectByPrimaryKey(instanceid);
		
		if(null != instanceInfo){
			instanceStack.push(instanceInfo);
		}else {
			return instanceStack;
		}
		if(InstanceType.MASTER == instanceInfo.getIncType()){
//			System.out.println("$$$$$$$$$$$$$$$$$$$$"+instanceInfo.getBakId());
			if(null != instanceInfo.getBakId() && !instanceInfo.getBakId().equals("")){
				RdsIncBase rib = ibm.selectByPrimaryKey(getIdArrayFromString(instanceInfo.getBakId()).get(0));
				instanceStack.push(rib);
			}
			if(null != instanceInfo.getSlaverId() && !instanceInfo.getSlaverId().equals("")){
				for(Integer is : getIdArrayFromString(instanceInfo.getSlaverId())){
					RdsIncBase ribs = ibm.selectByPrimaryKey(is);
					instanceStack.push(ribs);
				}
			}
		}
		
		return instanceStack;
	}
	
	

	private List<Integer> getIdArrayFromString(String slaveId) {
		// 如果使用split作为分隔符，则
		String[] idArray = slaveId.split("\\|");
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for(int i = 0; i < idArray.length; i++){
			if(idArray[i] != null && !idArray[i].isEmpty() && !idArray[i].equals(""))
				idList.add(Integer.valueOf(idArray[i]));
		}
		return idList;
	}

	/**
	 * 数据库已经添加了触发器trigger
	 * 从表将与主表一同删除
	 * @param instanceBase
	 * @throws PaasException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private void dealCancelInstanceDevided(RdsIncBase instanceBase) throws ClientProtocolException, IOException, PaasException {
		// 停止运行实例，并移除相关镜像、配置、数据
		stopInstance(instanceBase);
		removeInstance(instanceBase);
		// 删除RdsIncBase表中的信息
		RdsIncBaseMapper instanceBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		instanceBaseMapper.deleteByPrimaryKey(instanceBase.getId());
		// 资源恢复更新
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePool rdsres = resPoolMapper.selectByPrimaryKey(instanceBase.getResId());
		rdsres.setUsedmemory(rdsres.getUsedmemory().intValue() - instanceBase.getDbStoreage().intValue());
		rdsres.setUsedIntStorage(rdsres.getUsedIntStorage() - instanceBase.getIntStorage());
		rdsres.setUsedNetBandwidth(rdsres.getUsedNetBandwidth() - instanceBase.getNetBandwidth());
//		List<CPU> cpusRelase = g.getGson().fromJson(instanceBase.getCpuInfo(), new TypeToken<List<CPU>>(){}.getType());
		String[] cpuRName = instanceBase.getCpuInfo().split(",");
		List<CPU> cpusRes = g.getGson().fromJson(rdsres.getCpu(), new TypeToken<List<CPU>>(){}.getType());
		List<CPU> cpuNews = new LinkedList<CPU>();
		for(String cpuR : cpuRName){
			for(CPU cpu : cpusRes){
				if(cpu.name.equals(cpuR)){
					cpu.usable = true;
				}
				cpuNews.add(cpu);
			}
		}
		rdsres.setCpu(g.getGson().toJson(cpuNews));
		resPoolMapper.updateByPrimaryKey(rdsres);
	}


	private boolean CheckCancelDataLegal(CancelRDS cancelObject) {
//		if((0 < cancelObject.instanceid) && null == cancelObject.token && null == cancelObject.user_id)
//			return false;
		return true;
	}

	private boolean CheckCancelLegal(CancelRDS cancelObject) {
		return true;
	}

	// @ExceptionHandler(Exception.class)
	// private void exception(Exception e){
	// e.printStackTrace();
	// }

	/**
	 * 创建实例 
	 * 这里主要是创建一个主实例，并创建相应的主备、主从实例
	 * 单独创建备、从实例是在其他方法当中
	 * create中必须存在的字段
	 * －－－－
	 * token/
	 * instanceName/
	 * user_id/
	 * serial_number/
	 * instancenetworktype/
	 * instancespaceinfo/
	 * instancebaseconfig/
	 * instanceimagebelonger/
	 * instancerootaccount/
	 * 可选字段（延时生成字段）
	 * instanceslaver/
	 * instancebatmaster/
	 * 生成字段-RDSResourcePlan
	 * instanceipport/
	 * instancestatus/
	 * instanceresourcebelonger/
	 * @throws MyException 
	 */
	public String create(String create) throws MyException {
		int currentServerID = 1;
		// 解析JSON对象
		CreateRDS createObject = g.getGson().fromJson(create, CreateRDS.class);
		CreateRDSResult createResult = new CreateRDSResult(ResponseResultMark.WARN_INIT_STATUS);
		List<RdsResourcePool> exceptionResPoolList = new LinkedList<RdsResourcePool>();
		// 检查用户操作权限是否合法
//		if (false == CheckLegal(createObject.instanceBase.getUserId(), createObject.instanceBase.getServiceId(),
//				createObject.token)) {
//			createResult.setStatus(ResponseResultMark.ERROR_ILLEGAL_AUTHORITY);
//			return g.getGson().toJson(createResult);
//		}
		// 检查数据是否合法
		if (false == CheckData(createObject)) {
			createResult.setStatus(ResponseResultMark.ERROR_LESS_IMP_PARAM);
			return g.getGson().toJson(createResult);
		}
		createObject.instanceBase.setBakId("");
		createObject.instanceBase.setSlaverId("");
//		if(createObject.instanceBase.getImgId() <= 0){ // 需要前台传入
		createObject.instanceBase.setImgId(5); 
//		}
		if(createObject.instanceBase.getMysqlHome() == null || createObject.instanceBase.getMysqlHome().equals("")){
			createObject.instanceBase.setMysqlHome("/aifs01");
		}
		if(createObject.instanceBase.getMysqlDataHome() == null || createObject.instanceBase.getMysqlDataHome().equals("")){
			createObject.instanceBase.setMysqlDataHome("/aifs01/mysqldata");
		}
		if(createObject.instanceBase.getMysqlVolumnPath() == null || createObject.instanceBase.getMysqlVolumnPath().equals("")){
			createObject.instanceBase.setMysqlVolumnPath("");
		}
		if(createObject.instanceBase.getRootName() == null){
			createResult.setStatus(ResponseResultMark.ERROR_ROOT_USER_NAME_CANNOT_NULL);
			return g.getGson().toJson(createResult);
		}else if(createObject.instanceBase.getRootName().equals("root") || createObject.instanceBase.getRootName().equals("sync")){
			createResult.setStatus(ResponseResultMark.ERROR_ROOT_USER_NAME_CANNOT_USE_PARTICULAR_CHAR);
			return g.getGson().toJson(createResult);
		}
		if(createObject.instanceBase.getRootPassword() == null || createObject.instanceBase.getRootPassword().isEmpty()){
			createResult.setStatus(ResponseResultMark.ERROR_ROOT_USER_PASSWORD_CANNOT_NULL);
			return g.getGson().toJson(createResult);
		}
		if(createObject.instanceBase.getContainerName() == null){
			createObject.instanceBase.setContainerName("");
		}
		if(createObject.instanceBase.getIncLocation() == null){
			createObject.instanceBase.setIncLocation("");
		}
		if(createObject.instanceBase.getDbServerId() == null){
			createObject.instanceBase.setDbServerId("1");
		}
		if(createObject.instanceBase.getDbUsedStorage() == 0){
			createObject.instanceBase.setDbUsedStorage(2000);
		}
		
		// 查询资源情况，根据请求情况与资源情况获取分配计划
		RdsResourcePoolMapper rdsResPoolMapper =  ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePoolCriteria rdsResPoolCri = new RdsResourcePoolCriteria();
		rdsResPoolCri.createCriteria().andCurrentportLessThan(66000);
		// 每修改后资源需要重新查询
		List<RdsResourcePool> allResource = rdsResPoolMapper.selectByExample(rdsResPoolCri);
		
		// 为了深度拷贝数据
		Type rdsResourcePoolListType = new TypeToken<List<RdsResourcePool>>(){}.getType();
		List<RdsResourcePool> allResourceCp = g.getGson().fromJson(g.getGson().toJson(allResource), rdsResourcePoolListType);
		if(!checkResourceEnough(allResourceCp, createObject.createBatmasterNum + createObject.createSlaverNum + 1, createObject.instanceBase)){
			createResult.setStatus(ResponseResultMark.ERROR_LESS_MEMORY_SPACE);
			return g.getGson().toJson(createResult);
		}
		
		RDSResourcePlan resourcePlan = getResourcePlan(createObject.instanceBase, allResource);
		createObject.instanceBase.setDbServerId(currentServerID + "");
		currentServerID ++;
		if(null == resourcePlan.instanceresourcebelonger){
			createResult.setStatus(ResponseResultMark.ERROR_LESS_MEMORY_SPACE);
			return g.getGson().toJson(createResult);
		}
		
		// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
		RdsIncBase savedRdsIncBase = savePlan(resourcePlan, createObject.instanceBase.clone(), InstanceType.MASTER);
		createResult.incSimList.add(new InstanceBaseSimple(savedRdsIncBase));
		
		// 对实例进行配置,并启动 master/slaver/batmaster 通过AgentClient
		
		// 如果实例配置成功则启动实例
//		instanceRun = startInstance(savedRdsIncBase);
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		savedRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTING);
		incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
		// 启动mysql服务
//		save2ZK(savedRdsIncBase);
		boolean isRightConfig = false;
		try {
			isRightConfig = InstanceConfig(savedRdsIncBase);
		} catch (IOException | PaasException e) {
			// 处理。。。
			e.printStackTrace();
			createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
			throw new MyException(g.getGson().toJson(createResult));
		}
		// 修改数据库中服务器状态
		savedRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTED);
		incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
		// 将拓扑结构保存至注册中心（zk）,由于数据不全则将延迟保存到ZK
		
		// 添加到被排除列表中
		exceptionResPoolList.add(resourcePlan.instanceresourcebelonger);
		
		// 根据可选字段创建mysql从服务器、创建mysql主备服务器，存储在RdsIncBase表中
		if ((createObject.instanceBase.getIncType() == InstanceType.MASTER) && (true == isRightConfig)) {
//			RdsIncBase batMasterInstance = null;
//			RdsInstancebatmaster savedBatmasterInstance = null;
//			List<RdsIncBase> slaverInstanceList = new LinkedList<RdsIncBase>();
//			List<RdsInstanceSlaver> slaverForMasterInstance = new LinkedList<RdsInstanceSlaver>();
			if (1 == createObject.createBatmasterNum) {
				// 包装一个创建主备实例的类，并保存到数据库
				// 查询资源情况，根据请求情况与资源情况获取分配计划
				RDSResourcePlan resourceBatMasterPlan = getExpectResourcePlan(createObject.instanceBase.clone(), rdsResPoolMapper.selectByExample(rdsResPoolCri), exceptionResPoolList);
				
				if(null == resourceBatMasterPlan.instanceresourcebelonger){
					createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
					return g.getGson().toJson(createResult);
				}
				RdsIncBase batInCopy = savedRdsIncBase.clone();
				batInCopy.setMasterid(savedRdsIncBase.getId());
				batInCopy.setDbServerId(currentServerID + "");
				currentServerID ++;
				// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
				RdsIncBase batMasterInstance = savePlan(resourceBatMasterPlan, batInCopy, InstanceType.BATMASTER);
				createResult.incSimList.add(new InstanceBaseSimple(batMasterInstance));
				exceptionResPoolList.add(resourceBatMasterPlan.instanceresourcebelonger);
				// 对需要外键延期保存的数据，单独进行保存（instanceslaver、instancebatmaster）
				savedRdsIncBase.setBakId(batMasterInstance.getId() + "");
				// 保存信息到实例
				incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
				// 对实例进行远程配置
				boolean isRightBatMasterConfig = false;
				try {
					// 如果实例配置成功则启动实例
//					boolean batMasterInstanceRun = startInstance(batMasterInstance);
					batMasterInstance.setIncStatus(RDSCommonConstant.INS_STARTING);
					incBaseMapper.updateByPrimaryKey(batMasterInstance);
					isRightBatMasterConfig = InstanceConfig(batMasterInstance);
					// 修改数据库中服务器状态
					batMasterInstance.setIncStatus(RDSCommonConstant.INS_STARTED);
					incBaseMapper.updateByPrimaryKey(batMasterInstance);
					
					// 将拓扑结构保存至注册中心（zk）
					save2ZK(batMasterInstance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
					throw new MyException( g.getGson().toJson(createResult));
				}
			}

			if (0 < createObject.createSlaverNum) {
				for (int i = 0; i < createObject.createSlaverNum; i++) {
					// 包装多个创建从服务器实例的类，并保存到数据库
					RDSResourcePlan resourceSlaverPlan = getExpectResourcePlan(createObject.instanceBase.clone(), rdsResPoolMapper.selectByExample(rdsResPoolCri),exceptionResPoolList);
					
					if(null == resourceSlaverPlan.instanceresourcebelonger){
						createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
						return g.getGson().toJson(createResult);
					}
					RdsIncBase slaInCopy = savedRdsIncBase.clone();
					slaInCopy.setDbServerId(currentServerID + "");
					currentServerID ++;
					slaInCopy.setMasterid(savedRdsIncBase.getId());
					// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
					RdsIncBase ib = savePlan(resourceSlaverPlan, slaInCopy, InstanceType.SLAVER);
					createResult.incSimList.add(new InstanceBaseSimple(ib));
					exceptionResPoolList.add(resourceSlaverPlan.instanceresourcebelonger);
					// 对需要外键延期保存的数据，单独进行保存（instanceslaver、instancebatmaster）
					if(savedRdsIncBase.getSlaverId().isEmpty() || savedRdsIncBase.getSlaverId().equals("") || savedRdsIncBase.getSlaverId() == null){
						savedRdsIncBase.setSlaverId(ib.getId() + "");
					}else{
						savedRdsIncBase.setSlaverId(savedRdsIncBase.getSlaverId() + "|" + ib.getId());
					}
					incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
					// 对实例进行远程配置
					boolean isRightSlaverConfig = false;
					try {
						// 如果实例配置成功则启动实例
//						boolean slaverInstanceRun = startInstance(ib);
						ib.setIncStatus(RDSCommonConstant.INS_STARTING);
						incBaseMapper.updateByPrimaryKey(ib);
						isRightSlaverConfig = InstanceConfig(ib);
						// 修改数据库中服务器状态
						ib.setIncStatus(RDSCommonConstant.INS_STARTED);
						incBaseMapper.updateByPrimaryKey(ib);
						
						// 将拓扑结构保存至注册中心（zk）
						save2ZK(ib);
					} catch (IOException | PaasException e) {
						e.printStackTrace();
						createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
						throw new MyException( g.getGson().toJson(createResult));
					}
				}
			}
		}else{
			createResult.isInstanceConfig = isRightConfig;
			createResult.isInstanceRun = false;
			createResult.setStatus(ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER);
			return g.getGson().toJson(createResult);
		}

		// 将拓扑结构保存至注册中心（zk）
		save2ZK(savedRdsIncBase);
		
		createResult.isInstanceConfig = isRightConfig;
		createResult.isInstanceRun = true;
		createResult.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(createResult);
	}
	
	/**
	 * 空间合法性检测
	 * 检查可用资源是否充足资源
	 * @param allResource
	 * @param incNum
	 * @param eachStorageNeeded
	 * @return true if useable res is enough, false if useable res is not enough
	 */
	private boolean checkResourceEnough(List<RdsResourcePool> allResource, int incNum, RdsIncBase eachStorageNeeded) {
		
		for(int i = 0; i < incNum; i++){
			List<RdsResourcePool> usableResourceList = getMasterUsableResource(eachStorageNeeded, allResource);
			// 选择适当的主机进行分配资源
			ChoiceResStrategy crs = new ChoiceResStrategy(new MoreIntStorageIdleChoice());
			RdsResourcePool decidedRes = crs.makeDecision(usableResourceList);
			if(null == decidedRes){
				return false;
			}
			allResource.remove(decidedRes);
			decidedRes.setUsedmemory(decidedRes.getUsedmemory() + eachStorageNeeded.getDbStoreage());
			allResource.add(decidedRes);
		}
		
		return true;
	}


	public String createslobm(String create) throws MyException {
		CreateSRDS createObject = g.getGson().fromJson(create, CreateSRDS.class);
		CreateSRDSResult createResult = new CreateSRDSResult(ResponseResultMark.WARN_INIT_STATUS);
		
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase masterInstance = incBaseMapper.selectByPrimaryKey(createObject.masterinstanceid);
		
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		
		// 查询实例情况
		Stack<RdsIncBase> instanceStack = getInstanceStack(createObject.masterinstanceid);
		if(instanceStack.isEmpty()){
			createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_THIS_MASTER);
			return g.getGson().toJson(createResult);
		}
		List<RdsResourcePool> exceptResourceResourceList = new ArrayList<RdsResourcePool>();
		int maxServerId = 0;
		for(int i = 0; i < instanceStack.size(); i++){
			if(Integer.valueOf(instanceStack.get(i).getDbServerId()) > 0){
				maxServerId = Integer.valueOf(instanceStack.get(i).getDbServerId());
			}
			
			exceptResourceResourceList.add(resPoolMapper.selectByPrimaryKey(instanceStack.get(i).getResId()));
		}
		
		RdsResourcePoolCriteria cri = new RdsResourcePoolCriteria();
		cri.createCriteria().andCurrentportBetween(0, 100000);
		List<RdsResourcePool> allRes = resPoolMapper.selectByExample(cri);
		
		// 查询资源情况，根据请求情况与资源情况获取分配计划
		RDSResourcePlan resourceBatMasterPlan = getExpectResourcePlan(masterInstance, allRes, exceptResourceResourceList);
		if(null == resourceBatMasterPlan.instanceresourcebelonger){
			createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
			return g.getGson().toJson(createResult);
		}
		
		RdsIncBase masterCopy = masterInstance.clone();
		masterCopy.setMasterid(createObject.masterinstanceid);
		maxServerId ++;
		masterCopy.setDbServerId(maxServerId + "");
		// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
		RdsIncBase saveRdsIncBase = savePlan(resourceBatMasterPlan, masterCopy, createObject.thisInstanceType);

		// 对需要伪外键延期保存的数据，单独进行保存（instanceslaver、instancebatmaster）
		switch(saveRdsIncBase.getIncType()){
		case InstanceType.MASTER:
			createResult.setStatus(ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD);
			return g.getGson().toJson(createResult);
		case InstanceType.BATMASTER:
			masterInstance.setBakId(saveRdsIncBase.getId() + "");
			incBaseMapper.updateByPrimaryKey(masterInstance);
			break;
		case InstanceType.SLAVER:
			if(masterInstance.getSlaverId().isEmpty()){
				masterInstance.setSlaverId(saveRdsIncBase.getId() + "");
			}else{
				masterInstance.setSlaverId(masterInstance.getSlaverId() + "|" + saveRdsIncBase.getId());
			}
			incBaseMapper.updateByPrimaryKey(masterInstance);
			break;
		default:
			createResult.setStatus(ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE);
			return g.getGson().toJson(createResult);
		}
			
		// 保存信息到实例
//		savedRdsIncBase.instancebatmaster = savedBatmasterInstance;
		// 对实例进行远程配置
		boolean isRightBatMasterConfig = false;
		try {
			
			// 如果实例配置成功则启动实例
			saveRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTING);
			incBaseMapper.updateByPrimaryKey(saveRdsIncBase);
			// 启动mysql服务
			isRightBatMasterConfig = InstanceConfig(saveRdsIncBase);
			// 修改数据库中服务器状态
			saveRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTED);
			incBaseMapper.updateByPrimaryKey(saveRdsIncBase);
						
			// 将拓扑结构保存至注册中心（zk）
			save2ZK(saveRdsIncBase);
			
		} catch (IOException | PaasException e) {
			e.printStackTrace();
			createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
			throw new MyException( g.getGson().toJson(createResult));
		}
		if (true == isRightBatMasterConfig) {
			
		}
		createResult.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(createResult);
	}

	

	
	
	/**
	 * 
	 * 坑们
	 * 1、不能存在第一个元素传入 {0}
	 * 2、对应文件名字一定要修改好
	 * 3、playbook传参超过十个则用大括号阔入
	 * 4、修改final定义的字符串与文件名保持匹配
	 * 5、修改.sh文件中的.yml文件名
	 *
	 * 配置MySQL
	 * Master\Slaver\BatMaster
	 * 
	 * @param savedRdsIncBase
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws PaasException
	 */
	private boolean InstanceConfig(RdsIncBase savedRdsIncBase)
			throws ClientProtocolException, IOException, PaasException {
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase masterInc = null;
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(savedRdsIncBase.getResId());
		IpaasImageResourceMapper imgResMapper = ServiceUtil.getMapper(IpaasImageResourceMapper.class);
//		IpaasImageResource imgRes = imgResMapper.selectByPrimaryKey(savedRdsIncBase.getImgId());
		IpaasImageResourceCriteria imgCri = new IpaasImageResourceCriteria();
		IpaasImageResource imgRes = null;
		// 以后镜像作为可选项
		if(savedRdsIncBase.getImgId() <= 0)
		{
			imgCri.createCriteria().andImageCodeEqualTo("mysql").andServiceCodeEqualTo("RDS").andStatusEqualTo(1);
			List<IpaasImageResource> imgResConstant = imgResMapper.selectByExample(imgCri);
			imgRes = imgResConstant.get(0);
		} else{
			imgRes = imgResMapper.selectByPrimaryKey(savedRdsIncBase.getImgId());
		}
		
		
		if(savedRdsIncBase.getIncType().intValue() > 1 ){
			if(savedRdsIncBase.getMasterid() > 0){
				masterInc = incBaseMapper.selectByPrimaryKey(savedRdsIncBase.getMasterid());
			}else{
				return false;
			}
		}
		switch(savedRdsIncBase.getIncType()){
		/**
		 * rdspath
		 * filename
		 * sshuser
		 * sshpassword		
		 * image?
		 * ip
		 * port
		 * datapath
		 * homepath
		 * (mysqlVolumnPath)
		 * container-name {user_id}-{service_id}-{port}
		 * server-id
		 * dbStorage
		 * instanceType
		 * path?与sshhost命令中的第一个参数对应
		 * !whitelist
		 * !slaver_name
		 * !slaver_password
		 */
		case InstanceType.MASTER:
			String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
			String rdsPath = basePath + "rds";
			LOG.debug("---------rdsPath {}----------", rdsPath);
			// 1.先将需要执行镜像命令的机器配置文件上传上去。
			InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
			String[] cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

			// 2.执行这个初始化命令
			String mkSshHosts = AnsibleConstant.fillStringByArgs(AnsibleConstant.CREATE_ANSIBLE_HOSTS,
					new String[] { rdsPath, 
							savedRdsIncBase.getIncIp().replace(".", ""),
							savedRdsIncBase.getIncIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_master.yml");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/rdsimage_master.yml", cnt, AidUtil.getAid());

			// 还得上传文件
			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_master_run_image.sh");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/ansible_master_run_image.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_master_run_image.sh", AidUtil.getAid());
			
			String[] sss = new String[] { "",
					rdsPath, 
					savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
					incRes.getSshuser(),
					incRes.getSshpassword(),
					savedRdsIncBase.getIncIp(),
					imgRes.getImageRepository() + "/" + imgRes.getImageName(),
					savedRdsIncBase.getIncPort() + "",
					incRes.getVolumnPath() + "/" + savedRdsIncBase.getIncPort(),
					savedRdsIncBase.getMysqlHome(),
					"/percona/data",
					savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
					savedRdsIncBase.getDbServerId(),
					savedRdsIncBase.getDbStoreage() + "",
					getIncTypeById(savedRdsIncBase.getIncType()),
					savedRdsIncBase.getRootName(),
					savedRdsIncBase.getRootPassword(),
					savedRdsIncBase.getWhiteList(),
					savedRdsIncBase.getCpuInfo(),
					savedRdsIncBase.getIntStorage() + "",
					savedRdsIncBase.getNetBandwidth() + "",
					savedRdsIncBase.getSqlAudit(),
					savedRdsIncBase.getSyncStrategy()
					};
			for(String ss : sss){
				System.out.println("......"+ss);
			}
			// 开始执行
			String runImage = AnsibleConstant.fillStringByArgs(AnsibleConstant.DOCKER_MASTER_PARAM,
					new String[] { "",
							rdsPath, 
							savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
							incRes.getSshuser(),
							incRes.getSshpassword(),
							savedRdsIncBase.getIncIp(),
							imgRes.getImageRepository() + "/" + imgRes.getImageName(),
							savedRdsIncBase.getIncPort() + "",
							incRes.getVolumnPath() + "/" + savedRdsIncBase.getIncPort(),
							savedRdsIncBase.getMysqlHome(),
							"/percona/data",
							savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
							savedRdsIncBase.getDbServerId(),
							savedRdsIncBase.getDbStoreage() + "",
							getIncTypeById(savedRdsIncBase.getIncType()),
							savedRdsIncBase.getRootName(),
							savedRdsIncBase.getRootPassword(),
							savedRdsIncBase.getWhiteList(),
							savedRdsIncBase.getCpuInfo(),
							savedRdsIncBase.getIntStorage() + "",
							savedRdsIncBase.getNetBandwidth() + "",
							savedRdsIncBase.getSqlAudit(),
							savedRdsIncBase.getSyncStrategy()
							});

			LOG.debug("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());

			return true;
			/**
			 * 
			 * filename
			 * sshuser
			 * sshpassword		
			 * image?
			 * ip
			 * port
			 * datapath
			 * homepath
			 * (mysqlVolumnPath)
			 * container-name {user_id}-{service_id}-{port}
			 * server-id
			 * dbStorage
			 * instanceType
			 * path?与sshhost命令中的第一个参数对应
			 * masterip
			 * masterport
			 * 
			 * !whitelist
			 * !slaver_name
			 * !slaver_password
			 */
			case InstanceType.BATMASTER:
			case InstanceType.SLAVER:
				String basePath_s = AgentUtil.getAgentFilePath(AidUtil.getAid());
				String rdsPath_s = basePath_s + "rds";
				// 1.先将需要执行镜像命令的机器配置文件上传上去。
				InputStream in_s = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
				String[] cnt_s = AgentUtil.readFileLines(in_s);
				in_s.close();
				AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt_s, AidUtil.getAid());
				AgentUtil.executeCommand("chmod +x " + basePath_s + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

				// 2.执行这个初始化命令
				String mkSshHosts_s = AnsibleConstant.fillStringByArgs(AnsibleConstant.CREATE_ANSIBLE_HOSTS,
						new String[] { rdsPath_s, 
								savedRdsIncBase.getIncIp().replace(".", ""),
								savedRdsIncBase.getIncIp() });
				LOG.debug("---------mkSshHosts {}----------", mkSshHosts_s);
				AgentUtil.executeCommand(basePath_s + mkSshHosts_s, AidUtil.getAid());

				in_s = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_slaver.yml");
				cnt_s = AgentUtil.readFileLines(in_s);
				in_s.close();
				AgentUtil.uploadFile("rds/rdsimage_slaver.yml", cnt_s, AidUtil.getAid());

				// 还得上传文件
				in_s = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_slaver_run_image.sh");
				cnt_s = AgentUtil.readFileLines(in_s);
				in_s.close();
				AgentUtil.uploadFile("rds/ansible_slaver_run_image.sh", cnt_s, AidUtil.getAid());
				AgentUtil.executeCommand("chmod +x " + basePath_s + "rds/ansible_slaver_run_image.sh", AidUtil.getAid());
				
				String[]  s =  new String[] { "",
						rdsPath_s, 
						savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
						incRes.getSshuser(),
						incRes.getSshpassword(),
						savedRdsIncBase.getIncIp(),
						imgRes.getImageRepository() + "/" + imgRes.getImageName(),
						savedRdsIncBase.getIncPort() + "",
						incRes.getVolumnPath() + "/" + savedRdsIncBase.getIncPort(),
						savedRdsIncBase.getMysqlHome(),
						"/percona/data",
						savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
						savedRdsIncBase.getDbServerId(),
						savedRdsIncBase.getDbStoreage() + "",
						getIncTypeById(savedRdsIncBase.getIncType()),
						masterInc.getIncIp(),
						masterInc.getIncPort() + "",
						savedRdsIncBase.getRootName(),
						savedRdsIncBase.getRootPassword(),
						savedRdsIncBase.getWhiteList(),
						savedRdsIncBase.getCpuInfo(),
						savedRdsIncBase.getIntStorage() + "",
						savedRdsIncBase.getNetBandwidth() + "",
						savedRdsIncBase.getSqlAudit(),
						savedRdsIncBase.getSyncStrategy()
						};
				for(String ss : s){
					System.out.println("......"+ss);
				}
				
				
				// 开始执行
				String runImage_s = AnsibleConstant.fillStringByArgs(AnsibleConstant.DOCKER_SLAVER_PARAM,
						new String[] { "",
								rdsPath_s, 
								savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
								incRes.getSshuser(),
								incRes.getSshpassword(),
								savedRdsIncBase.getIncIp(),
								imgRes.getImageRepository() + "/" + imgRes.getImageName(),
								savedRdsIncBase.getIncPort() + "",
								incRes.getVolumnPath() + "/" + savedRdsIncBase.getIncPort(),
								savedRdsIncBase.getMysqlHome(),
								"/percona/data",
								savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
								savedRdsIncBase.getDbServerId(),
								savedRdsIncBase.getDbStoreage() + "",
								getIncTypeById(savedRdsIncBase.getIncType()),
								masterInc.getIncIp(),
								masterInc.getIncPort() + "",
								savedRdsIncBase.getRootName(),
								savedRdsIncBase.getRootPassword(),
								savedRdsIncBase.getWhiteList(),
								savedRdsIncBase.getCpuInfo(),
								savedRdsIncBase.getIntStorage() + "",
								savedRdsIncBase.getNetBandwidth() + "",
								savedRdsIncBase.getSqlAudit(),
								savedRdsIncBase.getSyncStrategy()
								});

				LOG.debug("---------runImage {}----------", runImage_s);
				AgentUtil.executeCommand(basePath_s + runImage_s, AidUtil.getAid());

				return true;
				/**
				 * filename
				 * sshuser
				 * sshpassword		
				 * image?
				 * ip
				 * port
				 * datapath
				 * homepath
				 * (mysqlVolumnPath)
				 * container-name {user_id}-{service_id}-{port}
				 * server-id
				 * dbStorage
				 * instanceType
				 * path?与sshhost命令中的第一个参数对应
				 * !whitelist
				 * !slaver_name
				 * !slaver_password
				 */
//			case InstanceType.BATMASTER:
//				String basePath_b = AgentUtil.getAgentFilePath(AidUtil.getAid());
//				String rdsPath_b = basePath_b + "rds";
//				// 1.先将需要执行镜像命令的机器配置文件上传上去。
//				InputStream in_b = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
//				String[] cnt_b = AgentUtil.readFileLines(in_b);
//				in_b.close();
//				AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt_b, AidUtil.getAid());
//				AgentUtil.executeCommand("chmod +x " + basePath_b + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());
//
//				// 2.执行这个初始化命令
//				String mkSshHosts_b = fillStringByArgs(CREATE_ANSIBLE_HOSTS,
//						new String[] { rdsPath_b, 
//								savedRdsIncBase.getIncIp().replace(".", ""),
//								savedRdsIncBase.getIncIp() });
//				LOG.debug("---------mkSshHosts {}----------", mkSshHosts_b);
//				AgentUtil.executeCommand(basePath_b + mkSshHosts_b, AidUtil.getAid());
//
//				in_b = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_slaver.yml");
//				cnt_b = AgentUtil.readFileLines(in_b);
//				in_b.close();
//				AgentUtil.uploadFile("rds/rdsimage_slaver.yml", cnt_b, AidUtil.getAid());
//
//				// 还得上传文件
//				in_b = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_slaver_run_image.sh");
//				cnt_b = AgentUtil.readFileLines(in_b);
//				in_b.close();
//				AgentUtil.uploadFile("rds/ansible_slaver_run_image.sh", cnt_b, AidUtil.getAid());
//				AgentUtil.executeCommand("chmod +x " + basePath_b + "rds/ansible_slaver_run_image.sh", AidUtil.getAid());
//				// 开始执行
//				String runImage_b = fillStringByArgs(DOCKER_SLAVER_PARAM,
//						new String[] { "",
//								rdsPath_b, 
//								savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
//								incRes.getSshuser(),
//								incRes.getSshpassword(),
//								savedRdsIncBase.getIncIp(),
//								imgRes.getImageRepository() + "/" + imgRes.getImageName(),
//								savedRdsIncBase.getIncPort() + "",
//								incRes.getVolumnPath() + "/" + savedRdsIncBase.getIncPort(),
//								savedRdsIncBase.getMysqlHome(),
//								"/percona/data",
//								savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
//								savedRdsIncBase.getDbServerId(),
//								savedRdsIncBase.getDbStoreage() + "",
//								getIncTypeById(savedRdsIncBase.getIncType()),
//								masterInc.getIncIp(),
//								masterInc.getIncPort() + "",
//								savedRdsIncBase.getRootName(),
//								savedRdsIncBase.getRootPassword(),
//								savedRdsIncBase.getWhiteList()
//								});
//
//				LOG.debug("---------runImage {}----------", runImage_b);
//				AgentUtil.executeCommand(basePath_b + runImage_b, AidUtil.getAid());
//
//				return true;
				
		default:
			return false;
		}
		
		
		
	}
	
	@Deprecated
	private String getIncTypeById(Integer incType) {
		switch(incType){
		case InstanceType.MASTER:
			return "master";
		case InstanceType.SLAVER:
			return "slave";
		case InstanceType.BATMASTER:
			return "slave";
//			return "batmaster";
		}
		return null;
	}


	/**
	 * 停止单个实例
	 * 通过ansible执行脚本停止docker中mysql实例的运行
	 * @param instanceBase
	 * @throws PaasException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private void stopInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException  {
			commandInstance(savedRdsIncBase,"stop");
	}
	
	private void startInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException {
			commandInstance(savedRdsIncBase,"start");
	}
	
	private void removeInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException {
			commandInstance(savedRdsIncBase,"rm");
	}
	
	private void restartInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException {
			commandInstance(savedRdsIncBase,"restart");
	}
	/**
	 * @deprecated
	 * @param ib
	 * @param argmentedExternalStorage
	 */
	private void configModify(RdsIncBase ib, int argmentedExternalStorage) {
	}
	
	private void commandInstance(RdsIncBase savedRdsIncBase, String command) throws ClientProtocolException, IOException, PaasException {
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(savedRdsIncBase.getResId());
		
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		String rdsPath = basePath + "rds";
		LOG.debug("---------rdsPath {}----------", rdsPath);
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

		// 2.执行这个初始化命令
		String mkSshHosts = AnsibleConstant.fillStringByArgs(AnsibleConstant.CREATE_ANSIBLE_HOSTS,
				new String[] { rdsPath, 
						savedRdsIncBase.getIncIp().replace(".", ""),
						savedRdsIncBase.getIncIp() });
		LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
		AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

		in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_command.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("rds/rdsimage_command.yml", cnt, AidUtil.getAid());

		// 还得上传文件
		in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_command_image.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("rds/ansible_command_image.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_command_image.sh", AidUtil.getAid());
		// 开始执行
		String runImage = AnsibleConstant.fillStringByArgs(AnsibleConstant.DOCKER_COMMAND_PARAM,
				new String[] { "",
						rdsPath, 
						savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
						incRes.getSshuser(),
						incRes.getSshpassword(),
						savedRdsIncBase.getIncIp(),
						command,
						savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort()
//						imgRes.getImageRepository() + "/" + imgRes.getImageName(),
//						savedRdsIncBase.getIncPort() + ""
						});

		LOG.debug("---------runImage {}----------", runImage);
		AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
		
	}
	
private RDSResourcePlan getResourcePlan(RdsIncBase inc, RdsResourcePool decidedRes) {
		
		// 根据需求找到可用资源列表
		RDSResourcePlan resourcePlan = new RDSResourcePlan();
		// 选择适当的主机进行分配资源
		if (null == decidedRes) {
			return null;
		}
		int count = 0;
		int needCpuNum = Integer.valueOf(inc.getCpuInfo());
		// 生成资源分配信息
		Type cpuType = new TypeToken<List<CPU>>() {}.getType();
		List<CPU> cpus = g.getGson().fromJson(decidedRes.getCpu(), cpuType);
		List<CPU> cpusNew = new LinkedList<CPU>();
		for (CPU cpu : cpus) {
			if (count < needCpuNum) {
				if (true == cpu.usable) {
					if (resourcePlan.Cpu == null) {
						resourcePlan.Cpu = cpu.name;
					} else {
						resourcePlan.Cpu = resourcePlan.Cpu + "," + cpu.name;
					}
					cpu.usable = false;
					count++;
				}
			}
			cpusNew.add(cpu);
		}
		decidedRes.setCurrentport(inc.getIncPort());
		decidedRes.setUsedmemory(decidedRes.getUsedmemory().intValue() + inc.getDbStoreage());
		decidedRes.setCpu(g.getGson().toJson(cpusNew));
		decidedRes.setUsedIntStorage(decidedRes.getUsedIntStorage() + inc.getIntStorage());
		decidedRes.setUsedNetBandwidth(decidedRes.getUsedNetBandwidth() + inc.getNetBandwidth());

		resourcePlan.instanceresourcebelonger = decidedRes;
		resourcePlan.ip = decidedRes.getHostip();
		resourcePlan.port = decidedRes.getCurrentport();
		resourcePlan.Status = RDSCommonConstant.INS_ACTIVATION;

		if (null == resourcePlan.Cpu) {
			Random rand = new Random();
			resourcePlan.Cpu = cpus.get(Math.abs(rand.nextInt())%cpus.size()).name;
			System.out.println("XXXXXXXXXXXXX-----CPU is not enough ,dispatch random cpu : " + resourcePlan.Cpu + " for it ...");
		}
		
		return resourcePlan;
	}
	
	private RDSResourcePlan getResourcePlan(RdsIncBase inc, List<RdsResourcePool> resourceList) {
		
		// 根据需求找到可用资源列表
		List<RdsResourcePool> usableResourceList = getMasterUsableResource(inc, resourceList);
		switch (inc.getIncType()) {
		case InstanceType.MASTER:
			RDSResourcePlan resourcePlan = new RDSResourcePlan();
			// 选择适当的主机进行分配资源
			ChoiceResStrategy crs = new ChoiceResStrategy(new MoreIntStorageIdleChoice());
			RdsResourcePool decidedRes = crs.makeDecision(usableResourceList);
			if (null == decidedRes) {
				return null;
			}
			int count = 0;
			int needCpuNum = Integer.valueOf(inc.getCpuInfo());
			// 生成资源分配信息
			decidedRes.setCurrentport(decidedRes.getCurrentport() + 1);
			decidedRes.setUsedmemory(decidedRes.getUsedmemory().intValue() + inc.getDbStoreage());
			Type cpuType = new TypeToken<List<CPU>>(){}.getType();
			List<CPU> cpus = g.getGson().fromJson(decidedRes.getCpu(), cpuType);
			List<CPU> cpusNew = new LinkedList<CPU>();
			for(CPU cpu: cpus){
				if(count < needCpuNum){
					if(true == cpu.usable){
						if(resourcePlan.Cpu == null){
							resourcePlan.Cpu = cpu.name;
						} else {
							resourcePlan.Cpu = resourcePlan.Cpu + "," + cpu.name;
						}
						cpu.usable = false;
						count ++;
					}
				}
				cpusNew.add(cpu);
			}
			decidedRes.setCpu(g.getGson().toJson(cpusNew));
			decidedRes.setUsedIntStorage(decidedRes.getUsedIntStorage() + inc.getIntStorage());
			decidedRes.setUsedNetBandwidth(decidedRes.getUsedNetBandwidth() + inc.getNetBandwidth());
			
			resourcePlan.instanceresourcebelonger = decidedRes;
			resourcePlan.ip = decidedRes.getHostip();
			resourcePlan.port = decidedRes.getCurrentport();
			resourcePlan.Status = RDSCommonConstant.INS_ACTIVATION;
			
			if(null == resourcePlan.Cpu ){
				Random rand = new Random();
				resourcePlan.Cpu = cpus.get(Math.abs(rand.nextInt())%cpus.size()).name;
				System.out.println("XXXXXXXXXXXXX-----CPU is not enough ,dispatch random cpu : " + resourcePlan.Cpu + " for it ...");
			}
			
			return resourcePlan;
		case InstanceType.BATMASTER:
		case InstanceType.SLAVER:
		default:
		}
		return null;
	}
	
	/**
	 * 不允许在同一台机子上跑多个实例
	 * 测试时条件有限，所以先这样哈哈
	 * 
	 * @param masterInstance
	 * @param resourceList
	 * @param exceptInstanceList
	 * @return
	 */
	private RDSResourcePlan getExpectResourcePlan(RdsIncBase masterInstance, List<RdsResourcePool> resourceList,
			List<RdsResourcePool> exceptInstanceList) {
//		...
		RDSResourcePlan resourcePlan = new RDSResourcePlan();
		// 根据需求找到可用资源列表
		List<RdsResourcePool> usableResourceList = getMasterUsableResource(masterInstance, resourceList);

		ChoiceResStrategy crs = new ChoiceResStrategy(new MoreIntStorageIdleChoice());
//		RdsResourcePool decidedRes = crs.makeDecision(usableResourceList, exceptInstanceList);
		RdsResourcePool decidedRes = crs.makeDecision(usableResourceList);

		if (null == decidedRes) {
			return null;
		}
		// 生成资源分配信息
		int count = 0;
		int needCpuNum = Integer.valueOf(masterInstance.getCpuInfo());
		// 生成资源分配信息
		decidedRes.setCurrentport(decidedRes.getCurrentport() + 1);
		decidedRes.setUsedmemory(decidedRes.getUsedmemory().intValue() + masterInstance.getDbStoreage());
		Type cpuType = new TypeToken<List<CPU>>(){}.getType();
		List<CPU> cpus = g.getGson().fromJson(decidedRes.getCpu(), cpuType);
		List<CPU> cpusNew = new LinkedList<CPU>();
		for(CPU cpu: cpus){
			if(count < needCpuNum){
				if(true == cpu.usable){
					if(resourcePlan.Cpu == null){
						resourcePlan.Cpu = cpu.name;
					} else {
						resourcePlan.Cpu = resourcePlan.Cpu + "," + cpu.name;
					}
					cpu.usable = false;
					count ++;
				}
			}
			cpusNew.add(cpu);
		}
		decidedRes.setCpu(g.getGson().toJson(cpusNew));
		decidedRes.setUsedIntStorage(decidedRes.getUsedIntStorage() + masterInstance.getIntStorage());
		decidedRes.setUsedNetBandwidth(decidedRes.getUsedNetBandwidth() + masterInstance.getNetBandwidth());
		

		resourcePlan.instanceresourcebelonger = decidedRes;
		resourcePlan.ip = decidedRes.getHostip();
		resourcePlan.port = decidedRes.getCurrentport();
		resourcePlan.Status = RDSCommonConstant.INS_ACTIVATION;
		
		if(null == resourcePlan.Cpu){
			Random rand = new Random();
			resourcePlan.Cpu = cpus.get(Math.abs(rand.nextInt())%cpus.size()).name;
			System.out.println("XXXXXXXXXXXXX-----CPU is not enough ,dispatch random cpu : " + resourcePlan.Cpu + " for it ...");
		}
		
		return resourcePlan;
	}

	private boolean CheckData(CreateRDS createObject) {
//		if ((null == createObject.token) && (null == createObject.instanceBase.getRdsRdsIncBase().getInstancename())
//				&& (null == createObject.instanceBase.getRdsRdsIncBase().getUserId()) 
//				&& (null == createObject.instanceBase.getRdsRdsIncBase().getSerialNumber())
//				&& (1 > createObject.instanceBase.getRdsRdsIncBase().getInstancenetworktype())
//				&& (null == createObject.instanceBase.getRdsInstanceSpaceInfo())
//				&& (null == createObject.instanceBase.getRdsRdsIncBaseConfig())
//				&& (null == createObject.instanceBase.getRdsImageResource())
//				&& (null == createObject.instanceBase.getRdsInstanceRootAccount())) {
//			return false;
//		} else {
//			return true;
//		}
		return true;
	}



//	/**
//	 * 获取可用资源 空间满足需求，状态满足需求，端口满足需求
//	 * 
//	 * @param createObject
//	 * @param resourceList
//	 * @return
//	 */
//	private List<RdsResourcePool> getMasterUsableResource(CreateRDS createObject, List<RdsResourcePool> resourceList) {
//		// for(int i = 0; i < resourceList.size(); i++){
//		List<RdsResourcePool> canUseRes = new LinkedList<RdsResourcePool>();
//		for (RdsResourcePool rp : resourceList) {
//			int canUseExtMemSize = rp.getTotalmemory() - rp.getUsedmemory();
//			if ((RDSCommonConstant.RES_STATUS_USABLE == rp.getStatus()) && (RDSCommonConstant.RES_CYCLE_USABLE == rp.getCycle())
//					&& (canUseExtMemSize > createObject.instanceBase.getDbStoreage())
//					&& ((rp.getCurrentport() + 1) < rp.getMaxport())) {
//				// if((decidedRes.currentport+1) < decidedRes.maxport){
//				canUseRes.add(rp);
//			}
//		}
//		return canUseRes;
//	}
	/**
	 * 获取可用资源 
	 * 硬盘（网盘）空间满足需求，
	 * 内存空间满足需求，
	 * CPU满足需求，
	 * 状态满足需求，
	 * 带宽满足需求，
	 * 端口满足需求
	 * debug... 数据遭到修改
	 * @param createObject
	 * @param resourceList
	 * @return
	 */
	private List<RdsResourcePool> getMasterUsableResource(RdsIncBase inc, List<RdsResourcePool> resourceList) {
		List<RdsResourcePool> canUseRes = new LinkedList<RdsResourcePool>();
		for (RdsResourcePool rp : resourceList) {
			int canUseExtMemSize = rp.getTotalmemory() - rp.getUsedmemory();
			Type cputype = new TypeToken<List<CPU>>(){}.getType();
			int canUseIntMemSize = rp.getTotIntStorage() - rp.getUsedIntStorage();
			int canUseBandWidthSizee = rp.getNetBandwidth() - rp.getUsedNetBandwidth();
//			boolean existIdleCpu = false;
			int countUseableCpu = 0;
			int cpuNeedNum = Integer.valueOf(inc.getCpuInfo());
			List<CPU> cpus = g.getGson().fromJson(rp.getCpu(), cputype);
			for(CPU cpu: cpus){
				if(cpu.usable = true)
					countUseableCpu++;
			}
			
			if ((RDSCommonConstant.RES_STATUS_USABLE == rp.getStatus()) 
					&& (RDSCommonConstant.RES_CYCLE_USABLE == rp.getCycle())
					&& (canUseExtMemSize > inc.getDbStoreage())
					&& ((rp.getCurrentport() + 1) < rp.getMaxport())
					&& canUseIntMemSize > inc.getIntStorage()
					&& canUseBandWidthSizee > inc.getNetBandwidth()
					&& countUseableCpu >= cpuNeedNum
					) {
				canUseRes.add(rp);
			}
		}
		return canUseRes;
	}


	/**
	 * WARNING!!!只能传入对象的拷贝！
	 * 当前存储字段有
	 * 
	 * @param resourcePlan
	 * @param createObject
	 * @return 返回当前账户的ID
	 * 
	 */
	private RdsIncBase savePlan(RDSResourcePlan resourcePlan, RdsIncBase instanceBase,int RdsIncBaseNetworkType) {
//		RdsIncBase instanceBase = createObject.instanceBase;
		instanceBase.setId(null);
		
		instanceBase.setIncType(RdsIncBaseNetworkType);
		if(RdsIncBaseNetworkType == InstanceType.BATMASTER){
//			instanceBase.setMasterid(instanceBase.getId());
			instanceBase.setIncName(instanceBase.getIncName() + "-BATMASTER-" + Math.random());
		}
		if(RdsIncBaseNetworkType == InstanceType.SLAVER){
//			instanceBase.setMasterid(instanceBase.getId());
			instanceBase.setIncName(instanceBase.getIncName() + "-SLAVER-" + Math.random());
		}
		
		// RdsIncBase作为子表时的指针指向ImageResource和RdsResourcePool，但因为ImageResource已经存在，不需要关联
		instanceBase.setResId(resourcePlan.instanceresourcebelonger.getResourceid());
		// 将生成信息保存到RdsIncBase
		instanceBase.setIncIp(resourcePlan.ip);
		instanceBase.setIncPort(resourcePlan.port);
		instanceBase.setIncStatus(resourcePlan.Status);
		instanceBase.setMysqlVolumnPath(resourcePlan.instanceresourcebelonger.getVolumnPath());
		instanceBase.setCreateTime(new Timestamp(System.currentTimeMillis()));
		instanceBase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		instanceBase.setCpuInfo(resourcePlan.Cpu);
		
		
		// 保存实例
		RdsIncBaseMapper resMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		resMapper.insert(instanceBase);
		RdsIncBaseCriteria cri = new RdsIncBaseCriteria();
		cri.createCriteria().andIncNameEqualTo(instanceBase.getIncName()).andIncTypeEqualTo(instanceBase.getIncType()).andIncIpEqualTo(instanceBase.getIncIp()).andIncPortEqualTo(instanceBase.getIncPort());
		// 刚出炉的数据
		instanceBase = resMapper.selectByExample(cri).get(0);
		// 更新资源
		RdsResourcePoolMapper rdsResMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		rdsResMapper.updateByPrimaryKey(resourcePlan.instanceresourcebelonger);

		return instanceBase;
	}
	/**
	 * modify专属saveplan
	 * primary key have value
	 * @param resourcePlan
	 * @param instanceBase
	 * @param RdsIncBaseNetworkType
	 * @param primaryKeyNotNULL
	 * @return
	 */
	private RdsIncBase savePlan(RDSResourcePlan resourcePlan, RdsIncBase instanceBase,int RdsIncBaseNetworkType, boolean primaryKeyNotNULL) {
//		RdsIncBase instanceBase = createObject.instanceBase;
		instanceBase.setIncType(RdsIncBaseNetworkType);
		
		// RdsIncBase作为子表时的指针指向ImageResource和RdsResourcePool，但因为ImageResource已经存在，不需要关联
		instanceBase.setResId(resourcePlan.instanceresourcebelonger.getResourceid());
		// 将生成信息保存到RdsIncBase
		instanceBase.setIncIp(resourcePlan.ip);
		instanceBase.setIncPort(resourcePlan.port);
		instanceBase.setIncStatus(resourcePlan.Status);
		instanceBase.setMysqlVolumnPath(resourcePlan.instanceresourcebelonger.getVolumnPath());
		instanceBase.setCreateTime(new Timestamp(System.currentTimeMillis()));
		instanceBase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		instanceBase.setCpuInfo(resourcePlan.Cpu);
		
		
		// 保存实例
		RdsIncBaseMapper resMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		resMapper.insert(instanceBase);
		RdsIncBaseCriteria cri = new RdsIncBaseCriteria();
		cri.createCriteria().andIncNameEqualTo(instanceBase.getIncName()).andIncTypeEqualTo(instanceBase.getIncType()).andIncIpEqualTo(instanceBase.getIncIp()).andIncPortEqualTo(instanceBase.getIncPort());
		// 刚出炉的数据
		instanceBase = resMapper.selectByExample(cri).get(0);
		// 更新资源
		RdsResourcePoolMapper rdsResMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		rdsResMapper.updateByPrimaryKey(resourcePlan.instanceresourcebelonger);

		return instanceBase;
	}
//	private boolean CheckLegal(String user_id, String serial_id, String token) {
//		return true;
//	}

	/**
	 * master应该首先启动
	 * master修改后关联slaver和batmaster也要修改 但是无法直接修改slaver和batmaster
	 * 这里主要是指修改instancespaceinfo即空间信息
	 * modify
	 * http://www.linuxidc.com/Linux/2015-01/112245.htm
	 * @throws MyException 
	 */
	public String modify(String modify) throws MyException {
		ModifyRDS modifyRDSObject = g.getGson().fromJson(modify, ModifyRDS.class);
		Stack<RdsIncBase> instanceStack = getInstanceStack(modifyRDSObject.groupMasterId);
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(modifyRDSObject.groupMasterId);
		
		RdsResourcePoolMapper resMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		
		CancelRDS cancel = new CancelRDS();
		cancel.instanceid = modifyRDSObject.groupMasterId;
		CancelRDSResult result = g.getGson().fromJson(cancel(g.getGson().toJson(cancel)), CancelRDSResult.class);
		if(Integer.valueOf(result.resultCode) != 1){
			System.out.println("result.resultCode : " + result.resultCode + " ; result.resultMsg : " + result.resultMsg);
			System.out.println(result);
			ModifyRDSResult changeContainerConfig = new ModifyRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
			throw new MyException(g.getGson().toJson(changeContainerConfig));
		}
		
		if(instanceCheck.getIncType() == InstanceType.MASTER){
			if(!instanceStack.isEmpty()){
				for(int i = 0; i < instanceStack.size(); i++){
//					RdsIncBase instance = instanceStack.pop();
					RdsIncBase instance = instanceStack.get(i);
					// 注销资源
					try { // 对master、batmaster、slaver选择进行扩充，数据库修改空间配置
						/**
						 * WARING!
						 * 这里的cpu设置必须在getResourcePlan之前
						 * 因为这里的cpu值是cpu数量而不是最后对应cpu的值
						 */
						if(!modifyRDSObject.cpu.isEmpty())
							instance.setCpuInfo(modifyRDSObject.cpu);
						if(modifyRDSObject.ExtStorage > 0)
							instance.setDbStoreage(modifyRDSObject.ExtStorage);
						if(modifyRDSObject.IntStorage > 0)
							instance.setIntStorage(modifyRDSObject.IntStorage);
						if(modifyRDSObject.NetBandwidth > 0)
							instance.setNetBandwidth(modifyRDSObject.NetBandwidth);
						
						RdsResourcePool decidedRes = resMapper.selectByPrimaryKey(instance.getResId());
						RDSResourcePlan plan = getResourcePlan(instance.clone(), decidedRes);
						RdsIncBase savedRdsIncBase = savePlan(plan, instance.clone(), instance.getIncType(),true);
						InstanceConfig(savedRdsIncBase);
						// 修改数据库中服务器状态
						savedRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTED);
						incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
						save2ZK(savedRdsIncBase);
					} catch (IOException | PaasException e){
						ModifyRDSResult stopRDSResult = new ModifyRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
						throw new MyException( g.getGson().toJson(stopRDSResult));
					}
				};
			}else{
				ModifyRDSResult stopRDSResult = new ModifyRDSResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
				return g.getGson().toJson(stopRDSResult);
			}
			
			// 如果修改成功则启动所用相关服务
			ModifyRDSResult modifyRDSResult = new ModifyRDSResult(ResponseResultMark.SUCCESS);
			return g.getGson().toJson(modifyRDSResult);
		}else {
			ModifyRDSResult modifyRDSResult = new ModifyRDSResult(ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY);
			return g.getGson().toJson(modifyRDSResult);
		}
	}


	/**
	 * @deprecated 据老大说不用管硬件层，就当作是硬件层可以任意扩容
	 * 传入的栈顶必须是master
	 * @param instanceStackBack
	 * @param argmentedExternalStorage 扩充到的容量大小
	 * @return
	 */
	private ResIncPlan ResIncPlanSchemer(Stack<RdsIncBase> instanceStackBack, int argmentedExternalStorage) {
		ResIncPlan resIncPlan = new ResIncPlan();
		List<RdsIncBase> increaseList = new ArrayList<RdsIncBase>();
		List<RdsIncBase> unincreaseList = new ArrayList<RdsIncBase>();
		// 判断master、slaver、batslaver所在的资源是否支持扩容，如资源不足则列为不支持扩容并返回不支持列表，将不支持扩容工作生成工单手工操作
		for(RdsIncBase ib : instanceStackBack){
			RdsResourcePoolMapper resMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
			RdsResourcePool resPool = resMapper.selectByPrimaryKey(ib.getResId());
			if((resPool.getTotalmemory() - resPool.getUsedmemory() 
					+ ib.getDbStoreage() - argmentedExternalStorage) < RDSCommonConstant.LIMIT_IDLE_USEABLE_EXTERNAL_STORAGE){
				increaseList.add(ib);
			} else {
				unincreaseList.add(ib);
			}
		}
		resIncPlan.increaseList = increaseList;
		resIncPlan.unincreaseList = unincreaseList;
		
		return resIncPlan;
	}

	/**
	 * 获取全部公有方法
	 */
	public String getFuncList() {
		List<MedthodDescribe> list = new LinkedList<MedthodDescribe>();
		Method[] methodList = RDSInstanceManager.class.getMethods();
		for(Method m : methodList){
			MedthodDescribe md = new MedthodDescribe();
			md.methodName = m.getName();
			md.methodReturnType = m.getReturnType().getName();
			list.add(md);
		}
		return g.getGson().toJson(list);
	}
	
	/**
	 * 应该先启动master
	 * @param startApply
	 * @return
	 * @throws MyException
	 */
	public String start(String startApply) throws MyException {
		LOG.info("----------startApply: " + startApply);
		
		StartRDS startRDSObject = g.getGson().fromJson(startApply, StartRDS.class);
		Stack<RdsIncBase> instanceStack ;
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
//		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(startRDSObject.instanceid);
//		if(instanceCheck.getIncType() != InstanceType.MASTER){
//			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE);
//			return g.getGson().toJson(startRDSResult);
//		}
		
		// 判断用户权限
		// 判断服务器状态
		instanceStack = getInstanceStack(startRDSObject.instanceid);
		if(!instanceStack.isEmpty()){
			for(int i = 0; i < instanceStack.size(); i++){
//				RdsIncBase instance = instanceStack.pop();
				RdsIncBase instance = instanceStack.get(i);
				instance.setIncStatus(RDSCommonConstant.INS_STARTING);
				incBaseMapper.updateByPrimaryKey(instance);
				// 启动mysql服务
				try {
					startInstance(instance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					StartRDSResult stopRDSResult = new StartRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
					throw new MyException( g.getGson().toJson(stopRDSResult));
				}
				// 修改数据库中服务器状态
				instance.setIncStatus(RDSCommonConstant.INS_STARTED);
				incBaseMapper.updateByPrimaryKey(instance);
			};
		}else{
			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(startRDSResult);
		}
		

		StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(startRDSResult);
	}

	public String stop(String stopApply) throws MyException {
		LOG.info("----------stopApply: " + stopApply);
		StopRDS stopRDSObject = g.getGson().fromJson(stopApply, StopRDS.class);
		Stack<RdsIncBase> instanceStack;
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
//		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(stopRDSObject.instanceid);
//		if(instanceCheck.getIncType() != InstanceType.MASTER){
//			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE);
//			return g.getGson().toJson(startRDSResult);
//		}
		// 判断用户权限
		// 判断服务器状态
		instanceStack = getInstanceStack(stopRDSObject.instanceid);
		if(!instanceStack.isEmpty()){
			while(!instanceStack.isEmpty()){
				RdsIncBase instance = instanceStack.pop();
				instance.setIncStatus(RDSCommonConstant.INS_STARTING);
				incBaseMapper.updateByPrimaryKey(instance);
				// 启动mysql服务
				try {
					stopInstance(instance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					StopRDSResult stopRDSResult = new StopRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
					throw new MyException( g.getGson().toJson(stopRDSResult));
				}
				
				// 修改数据库中服务器状态
				instance.setIncStatus(RDSCommonConstant.INS_STARTED);
				incBaseMapper.updateByPrimaryKey(instance);
			};
		}else{
			StopRDSResult stopRDSResult = new StopRDSResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(stopRDSResult);
		}
		// 修改数据库中服务器状态

		StopRDSResult stopRDSResult = new StopRDSResult(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(stopRDSResult);
	}
	
	


	/**
	 * 应该先重启master
	 * @throws MyException 
	 * 
	 */
	public String restart(String restartApply) throws MyException {
		RestartRDS restartObject = g.getGson().fromJson(restartApply, RestartRDS.class);
		Stack<RdsIncBase> instanceStack;
//		Stack<RdsIncBase> instanceStackBack = new Stack<RdsIncBase>();
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
//		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(restartObject.instanceid);
//		if(instanceCheck.getIncType() != InstanceType.MASTER){
//			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE);
//			return g.getGson().toJson(startRDSResult);
//		}
		instanceStack = getInstanceStack(restartObject.instanceid);
		if(!instanceStack.isEmpty()){
			for(int i = 0; i < instanceStack.size(); i++){
//				RdsIncBase instance = instanceStack.pop();
				RdsIncBase instance = instanceStack.get(i);
				instance.setIncStatus(RDSCommonConstant.INS_STARTING);
				incBaseMapper.updateByPrimaryKey(instance);
				// 启动mysql服务
				try {
					restartInstance(instance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					RestartResult restartRDSResult = new RestartResult(ResponseResultMark.ERROR_BAD_CONFIG);
					throw new MyException( g.getGson().toJson(restartRDSResult));
				}
				// 修改数据库中服务器状态
				instance.setIncStatus(RDSCommonConstant.INS_STARTED);
				incBaseMapper.updateByPrimaryKey(instance);
			};
		}else{
			RestartResult restartRDSResult = new RestartResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(restartRDSResult);
		}
		
		RestartResult restartRDSResult = new RestartResult(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(restartRDSResult);
	}

	/**
	 * 提供RDS信息查询
	 * 用户需要获取主从服务器的相关信息来进行软负载均衡
	 */
	public String getinstanceinfo(String getinstanceinfo) {
		GetIncInfo getIncInfo = g.getGson().fromJson(getinstanceinfo, GetIncInfo.class);
		RdsIncBaseMapper incMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		if(getIncInfo.getAll == 1){
			RdsIncBaseCriteria cri = new RdsIncBaseCriteria();
			cri.createCriteria().andIdBetween(1, 10000000);
			List<RdsIncBase> resultList = incMapper.selectByExample(cri);
			return g.getGson().toJson(resultList);
		} else if(getIncInfo.getGroupInstance > 0){
			Stack<RdsIncBase> instanceStack;
			instanceStack = getInstanceStack(getIncInfo.getGroupInstance);
//			usableInstanceList(instanceStack);
			return g.getGson().toJson(instanceStack);
		} else {
			List<RdsIncBase> resultList = incMapper.selectByExample(getIncInfo.rdsIncBaseCri);
			return g.getGson().toJson(resultList);
		}
	}
	/**
	 * @deprecated 暂停该功能的开发。
	 * 
	 * 正常运行的mysql服务器
	 * 通过监测将运行异常的服务器排除可用列表
	 * @param instanceStack
	 * @throws MyException 
	 */
	public String switchmaster(String switchmaster) throws MyException {
		RdsIncBaseMapper incMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsResourcePoolMapper resMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		SwitchMaster sm = g.getGson().fromJson(switchmaster, SwitchMaster.class);
		SwitchMasterResult smr = new SwitchMasterResult();
		Stack<RdsIncBase> rdsIncStack = getInstanceStack(sm.getMasterId());
		RdsIncBase masterInc = null;
		RdsIncBase bakInc = null;
		List<RdsIncBase> slaverIncList = new LinkedList<RdsIncBase>();
		for(RdsIncBase inc : rdsIncStack){
			if(inc.getIncType() == InstanceType.MASTER){
				masterInc = inc;
			}
			if(inc.getIncType() == InstanceType.SLAVER){
				slaverIncList.add(inc);
			}
			if(inc.getIncType() == InstanceType.BATMASTER){
				bakInc = inc;
			}
		}
		
		// 修改数据库
		if(masterInc == null || bakInc == null){
			smr.setStatus(ResponseResultMark.ERROR_INSTANCE_GROUP_CANNOT_GET_NULL);
			return g.getGson().toJson(smr);
		} else {
			RdsIncBase rdsIncTemp = new RdsIncBase();
			rdsIncTemp.setBakId(masterInc.getId() + "");
			rdsIncTemp.setSlaverId(masterInc.getSlaverId());
			if(masterInc.getIncName().contains("-staybakmaster"))
			{	
				masterInc.setIncName(masterInc.getIncName().substring(0, masterInc.getIncName().length()-14));
				masterInc.setIncStatus(RDSCommonConstant.INS_STARTED);
			}
			else
			{	
				masterInc.setIncName(masterInc.getIncName() + "-staybakmaster");
				masterInc.setIncStatus(RDSCommonConstant.INS_SWITCHING);
			}
			masterInc.setIncType(InstanceType.BATMASTER);
			masterInc.setMasterid(bakInc.getId());
			masterInc.setBakId("");
			masterInc.setSlaverId("");
			
			
			if(bakInc.getIncName().contains("-staybakmaster"))
			{	
				bakInc.setIncName(bakInc.getIncName().substring(0, bakInc.getIncName().length()-14));
				bakInc.setIncStatus(RDSCommonConstant.INS_STARTED);
			}
			else
			{	
				bakInc.setIncName(bakInc.getIncName() + "-staybakmaster");
				bakInc.setIncStatus(RDSCommonConstant.INS_SWITCHING);
			}
			bakInc.setIncType(InstanceType.MASTER);
			bakInc.setMasterid(0);
			bakInc.setBakId(rdsIncTemp.getBakId());
			bakInc.setSlaverId(rdsIncTemp.getSlaverId());
			
			
			if(slaverIncList.size() > 0){
				for(int i = 0; i < slaverIncList.size(); i++){
					slaverIncList.get(i).setMasterid(bakInc.getId());
				}
			}
			
			incMapper.updateByPrimaryKey(bakInc);
			incMapper.updateByPrimaryKey(masterInc);
			if(slaverIncList.size() > 0){
				for(int i = 0; i < slaverIncList.size(); i++){
					incMapper.updateByPrimaryKey(slaverIncList.get(i));
				}
			}
		}
		
		// 修改配置
		try {
			switchConfig(masterInc,bakInc,slaverIncList);
		} catch (IOException | PaasException e) {
			e.printStackTrace();
			smr.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
			throw new MyException( g.getGson().toJson(smr));
		}
		
		smr.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(smr);
	}
	// ansible配置主备切换
	private void switchConfig(RdsIncBase masterInc, RdsIncBase bakInc, List<RdsIncBase> slaverIncList) throws ClientProtocolException, IOException, PaasException {
		switchConfigInc(bakInc,bakInc);
		switchConfigInc(masterInc,bakInc);
		if(slaverIncList != null){
			for(RdsIncBase slaveInc : slaverIncList){
				switchConfigInc(slaveInc,bakInc);
			}
		}
	}
	
	
	private void switchConfigInc(RdsIncBase inc,RdsIncBase bakInc) throws ClientProtocolException, IOException, PaasException {
		if(inc.getIncType() == InstanceType.MASTER){
			RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
			RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(inc.getResId());
			
			String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
			String rdsPath = basePath + "rds";
			LOG.debug("---------rdsPath {}----------", rdsPath);
			// 1.先将需要执行镜像命令的机器配置文件上传上去。
			InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
			String[] cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

			// 2.执行这个初始化命令
			String mkSshHosts = AnsibleConstant.fillStringByArgs(AnsibleConstant.CREATE_ANSIBLE_HOSTS,
					new String[] { rdsPath, 
							inc.getIncIp().replace(".", ""),
							inc.getIncIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_switch_master.yml");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/rdsimage_switch_master.yml", cnt, AidUtil.getAid());

			// 还得上传文件
			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_switch_master.sh");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/ansible_switch_master.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_switch_master.sh", AidUtil.getAid());
			// 开始执行
			String runImage = AnsibleConstant.fillStringByArgs(AnsibleConstant.DOCKER_SWITCH_INFO,
					new String[] { "",
							rdsPath, 
							inc.getIncIp().replace(".", ""),// .cfg 文件名称 
							incRes.getSshuser(),
							incRes.getSshpassword(),
							inc.getIncIp(),
							inc.getIncPort() + "",
							inc.getUserId() + "-" + inc.getServiceId() + "-" + inc.getIncPort(),
							bakInc.getIncIp(),
							bakInc.getIncPort() + "",
							"bakmaster",
							"bakmaster_switch"
							});

			LOG.debug("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
		}
		if(InstanceType.SLAVER == inc.getIncType()){
			RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
			RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(inc.getResId());
			
			String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
			String rdsPath = basePath + "rds";
			LOG.debug("---------rdsPath {}----------", rdsPath);
			// 1.先将需要执行镜像命令的机器配置文件上传上去。
			InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
			String[] cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

			// 2.执行这个初始化命令
			String mkSshHosts = AnsibleConstant.fillStringByArgs(AnsibleConstant.CREATE_ANSIBLE_HOSTS,
					new String[] { rdsPath, 
							inc.getIncIp().replace(".", ""),
							inc.getIncIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_switch_slave.yml");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/rdsimage_switch_slave.yml", cnt, AidUtil.getAid());

			// 还得上传文件
			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_switch_master.sh");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/ansible_switch_master.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_switch_master.sh", AidUtil.getAid());
			// 开始执行
			String runImage = AnsibleConstant.fillStringByArgs(AnsibleConstant.DOCKER_SWITCH_INFO,
					new String[] { "",
							rdsPath, 
							inc.getIncIp().replace(".", ""),// .cfg 文件名称 
							incRes.getSshuser(),
							incRes.getSshpassword(),
							inc.getIncIp(),
							inc.getIncPort() + "",
							inc.getUserId() + "-" + inc.getServiceId() + "-" + inc.getIncPort(),
							bakInc.getIncIp(),
							bakInc.getIncPort() + "",
							"slave",
							"slave_switch"
							});

			LOG.debug("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
		}
		if(InstanceType.BATMASTER == inc.getIncType()){
			RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
			RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(inc.getResId());
			
			String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
			String rdsPath = basePath + "rds";
			LOG.debug("---------rdsPath {}----------", rdsPath);
			// 1.先将需要执行镜像命令的机器配置文件上传上去。
			InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
			String[] cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

			// 2.执行这个初始化命令
			String mkSshHosts = AnsibleConstant.fillStringByArgs(AnsibleConstant.CREATE_ANSIBLE_HOSTS,
					new String[] { rdsPath, 
							inc.getIncIp().replace(".", ""),
							inc.getIncIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_switch_bakmaster.yml");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/rdsimage_switch_bakmaster.yml", cnt, AidUtil.getAid());

			// 还得上传文件
			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_switch_master.sh");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/ansible_switch_master.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_switch_master.sh", AidUtil.getAid());
			// 开始执行
			String runImage = AnsibleConstant.fillStringByArgs(AnsibleConstant.DOCKER_SWITCH_INFO,
					new String[] { "",
							rdsPath, 
							inc.getIncIp().replace(".", ""),// .cfg 文件名称 
							incRes.getSshuser(),
							incRes.getSshpassword(),
							inc.getIncIp(),
							inc.getIncPort() + "",
							inc.getUserId() + "-" + inc.getServiceId() + "-" + inc.getIncPort(),
							bakInc.getIncIp(),
							bakInc.getIncPort() + "",
							"master",
							"master_switch"
							});

			LOG.debug("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
		}
		
	}

	/**
	 * @deprecated
	 * 物理资源不够的情况下进行平滑迁移使用这个方法
	 * 需要网盘挂载才能够进行开发
	 * 
	 * @param changecontainerconfig
	 * @return
	 * @throws MyException
	 */
	public String changecontainerconfig(String changecontainerconfig) throws MyException {
		ChangeContainerConfig changeConfigObject = g.getGson().fromJson(changecontainerconfig, ChangeContainerConfig.class);
		Stack<RdsIncBase> instanceStack = getInstanceStack(changeConfigObject.groupMasterId);
		InstanceGroup incGroup = InstanceGroup.getGroupFromInstanceStack(instanceStack);
		// 获取资源
		CreateRDS createObject = getCreateRDSObjectFromStack(instanceStack);
		// 注销资源
		CancelRDS cancel = new CancelRDS();
		cancel.instanceid = changeConfigObject.groupMasterId;
		CancelRDSResult result = g.getGson().fromJson(cancel(g.getGson().toJson(cancel)), CancelRDSResult.class);
		if(Integer.valueOf(result.resultCode) != 1){
			ChangeContainerConfigResult changeContainerConfig = new ChangeContainerConfigResult(ResponseResultMark.ERROR_BAD_CONFIG);
			return g.getGson().toJson(changeContainerConfig);
		}
		// 更新配置
		createObject.instanceBase.setCpuInfo(changeConfigObject.cpu);
		createObject.instanceBase.setDbStoreage(changeConfigObject.ExtStorage);
		createObject.instanceBase.setIntStorage(changeConfigObject.IntStorage);
		createObject.instanceBase.setNetBandwidth(changeConfigObject.NetBandwidth);
		
		
		// 继承原有未修改配置重新分配资源
		CreateRDSResult createResult = g.getGson().fromJson(create(g.getGson().toJson(createObject)), CreateRDSResult.class);
		if(Integer.valueOf(createResult.resultCode) == 1){
			// 将原有数据迁移至新位置
			transferConfig(incGroup ,createResult.incSimList);
			ChangeContainerConfigResult changeContainerConfig = new ChangeContainerConfigResult(ResponseResultMark.SUCCESS);
			return g.getGson().toJson(changeContainerConfig); 
		}else{
			ChangeContainerConfigResult changeContainerConfig = new ChangeContainerConfigResult(ResponseResultMark.ERROR_BAD_CONFIG);
			return g.getGson().toJson(changeContainerConfig);
		}
	}


	private void transferConfig(InstanceGroup incGroup, List<InstanceBaseSimple> incSimList) {
		// TODO Auto-generated method stub
		
	}


	private CreateRDS getCreateRDSObjectFromStack(Stack<RdsIncBase> instanceStack) {
		CreateRDS createObject = new CreateRDS();
		createObject.createBatmasterNum = 0;
		createObject.createSlaverNum = 0;
		for(RdsIncBase inc : instanceStack){
			switch(inc.getIncType()){
			case InstanceType.MASTER:
				createObject.instanceBase = inc;
				break;
			case InstanceType.SLAVER:
				createObject.createSlaverNum ++;
				break;
			case InstanceType.BATMASTER:
				createObject.createBatmasterNum ++;
				break;
			}
		}
		return createObject;
	}


	/**
	 * 删除相应ZK节点
	 * @param instanceRDS
	 * @throws PaasException 
	 */
	private void deleteZK(RdsIncBase instanceRDS) throws PaasException {
		CCSComponentOperationParam op = getZKBase(instanceRDS.getUserId());
		op.setPath(RDSCommonConstant.RDS_ZK_PATH + instanceRDS.getServiceId());
		iCCSComponentManageSv.delete(op);
	}
	
	/**
	 * 查看节点信息
	 * @param userId
	 * @return
	 */
	protected CCSComponentOperationParam getZKBase(String userId) {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPathType(PathType.READONLY);
		return op;
	}
	
	private void save2ZK(RdsIncBase instanceRDS) {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		System.out.println("save2ZK");
		op.setUserId(instanceRDS.getUserId());
		op.setPath(RDSCommonConstant.RDS_ZK_PATH + instanceRDS.getServiceId());
		op.setPathType(PathType.READONLY);
		System.out.println(op);
		try {
			iCCSComponentManageSv.add(op, g.getGson().toJson(instanceRDS));
		} catch (PaasException e) {
			e.printStackTrace();
		}
	}
	private void modifyZZK(RdsIncBase instanceRDS){
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		System.out.println("save2ZK");
		op.setUserId(instanceRDS.getUserId());
		op.setPath(RDSCommonConstant.RDS_ZK_PATH + instanceRDS.getServiceId());
		op.setPathType(PathType.READONLY);
		System.out.println(op);
		try {
			iCCSComponentManageSv.modify(op, g.getGson().toJson(instanceRDS));
		} catch (PaasException e) {
			e.printStackTrace();
		}
	}

}
