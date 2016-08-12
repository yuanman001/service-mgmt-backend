package com.ai.paas.ipaas.rds.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月7日 上午10:55:53 
 * @version 
 * @since  
 */
@Path("/rds/mysql/config")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRDSInstanceConfig {

	/**
	 * 获取mysql实例运行参数
	 * @param getmysqlparam
	 * @return
	 */
	@Path("/getmysqlparam")
	@POST
	public String getmysqlparam(String getmysqlparam);
	
	/**
	 * 修改mysql实例对应运行参数
	 * @param modifymysqlparam
	 * @return
	 */
	@Path("/modifymysqlparam")
	@POST
	public String modifymysqlparam(String modifymysqlparam);
}
