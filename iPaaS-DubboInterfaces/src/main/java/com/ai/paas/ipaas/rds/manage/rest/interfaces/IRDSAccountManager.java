package com.ai.paas.ipaas.rds.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 由于要与原版本兼容和时间原因，这里我们就不做RBAC权限设计
 * 账号系统与原系统兼容，用户与数据库的多对多管理也就不在这里体现了
 * 这里只保存给客户的root账号与ip白名单
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月7日 上午10:54:41 
 * @version 
 * @since  
 */
@Path("/rds/account／manager")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRDSAccountManager {
	
	/**
	 * 创建的时候带上外部有访问权限的ip与端口号
	 * @param createmysqlroot
	 * @return
	 */
	@Path("/createmysqlroot")
	@POST
	public String createmysqlroot(String createmysqlroot);
	
	/**
	 * 消除root账户
	 * @param cancelmysqlroot
	 * @return
	 */
	@Path("/cancelmysqlroot")
	@POST
	public String cancelmysqlroot(String cancelmysqlroot);
	
	/**
	 * 获取实例访问白名单（每个实例仅有一条纪录）
	 * @param getipwritelist
	 * @return
	 */
	@Path("/getipwritelist")
	@POST
	public String getipwritelist(String getipwritelist);
	
	/**
	 * 修改白名单
	 * @param modifyipwritelist
	 * @return
	 */
	@Path("/modifyipwritelist")
	@POST
	public String modifyipwritelist(String modifyipwritelist);
}
