package com.ai.paas.ipaas.rds.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月7日 上午10:48:38 
 * @version 
 * @since  
 */
@Path("/rds/mysql/operater")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRDSInstanceOperater {

	
	
	/**
	 * mysql修改实例信息
	 * 相关表instancebase
	 * @param changesinstancebase
	 * @return
	 */
	@Path("/changesinstancebase")
	@POST
	public String changesinstancebase(String changesinstancebase);
	
	/**
	 * mysql修改ip与port
	 * 相关表instanceipport
	 * @param changesinstanceipport
	 * @return
	 */
	@Path("/changesinstanceipport")
	@POST
	public String changesinstanceipport(String changesinstanceipport);
	
	/**
	 * mysql修改硬件配置
	 * 相关表instancebaseconfig
	 * @param changesinstancebaseconfig
	 * @return
	 */
	@Path("/changesinstancebaseconfig")
	@POST
	public String changesinstancebaseconfig(String changesinstancebaseconfig);
	
	/**
	 * 创建从服务器
	 * 相关表instancebaseinfo,instanceipport,instancestatus,instancebaseconfig,instancespaceinfo,instanceslaver
	 * @param createslaver
	 * @return
	 */
	@Path("/createslaver")
	@POST
	public String createslaver(String createslaver);
	
	/**
	 * 取消从服务器
	 * 相关表instancebaseinfo,instanceipport,instancestatus,instancebaseconfig,instancespaceinfo,instanceslaver
	 * @param cancelslaver
	 * @return
	 */
	@Path("/cancelslaver")
	@POST
	public String cancelslaver(String cancelslaver);
	
	/**
	 * 创建主备服务器
	 * 相关表instancebaseinfo,instanceipport,instancestatus,instancebaseconfig,instancespaceinfo,instancebatmaster
	 * @param createbatmaster
	 * @return
	 */
	@Path("/createbatmaster")
	@POST
	public String createbatmaster(String createbatmaster);
	
	/**
	 * 消除主备服务器
	 * 相关表instancebaseinfo,instanceipport,instancestatus,instancebaseconfig,instancespaceinfo,instancebatmaster
	 * @param cancelbatmaster
	 * @return
	 */
	@Path("/cancelbatmaster")
	@POST
	public String cancelbatmaster(String cancelbatmaster);
	
	/**
	 * 切换主备服务器
	 * @param switchmaster
	 * @return
	 */
	@Path("/switchmaster")
	@POST
	public String switchmaster(String switchmaster);
	
	/**
	 * 获取实例信息
	 * @param getinstancebaseinfo
	 * @return
	 */
	@Path("/getinstancebaseinfo")
	@POST
	public String getinstancebaseinfo(String getinstancebaseinfo);
	
	/**
	 * 获取实例ip地址与port
	 * @param getinstanceipport
	 * @return
	 */
	@Path("/getinstanceipport")
	@POST
	public String getinstanceipport(String getinstanceipport);
	
	/**
	 * 获取实例运行状态
	 * @param getinstancestatus
	 * @return
	 */
	@Path("/getinstancestatus")
	@POST
	public String getinstancestatus(String getinstancestatus);
	
	/**
	 * 获取实例硬件配置
	 * @param getinstancebaseconfig
	 * @return
	 */
	@Path("/getinstancebaseconfig")
	@POST
	public String getinstancebaseconfig(String getinstancebaseconfig);
	
	/**
	 * 获取实例空间信息 已使用空间大小／整体空间大小
	 * @param getinstancespaceinfo
	 * @return
	 */
	@Path("/getinstancespaceinfo")
	@POST
	public String getinstancespaceinfo(String getinstancespaceinfo);
	
	/**
	 * 获取从服务器列表
	 * @param getinstanceslaver
	 * @return
	 */
	@Path("/getinstanceslaver")
	@POST
	public String getinstanceslaver(String getinstanceslaver);

	/**
	 * 获取主备服务器信息
	 * @param getinstancebatmaster
	 * @return
	 */
	@Path("/getinstancebatmaster")
	@POST
	public String getinstancebatmaster(String getinstancebatmaster);
	
	/**
	 * 获取完整实例信息
	 * @param getinstancewholeinfo
	 * @return
	 */
	@Path("/getinstancewholeinfo")
	@POST
	public String getinstancewholeinfo(String getinstancewholeinfo);
	
//	/**
//	 * 调整申请的服务，参数调整
//	 * 
//	 * @param modifyApply
//	 * @return
//	 */
//	@Path("/modify")
//	@POST
//	public String modify(String modifyApply);
	
	
}
