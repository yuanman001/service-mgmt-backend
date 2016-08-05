package com.ai.paas.ipaas.rds.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/** 
 * 物理资源
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 上午11:25:38 
 * @version 
 * @since  
 */
@Path("/rds/resource")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRDSResourcePool {

	@Path("/add")
	@POST
	public String add(String getParam);
	@Path("/delete")
	@POST
	public String delete(String getParam);
	
	@Path("/get")
	@POST
	public String get(String getParam);
	
	@Path("/addimage")
	@POST
	public String addImage(String getParam);
	@Path("/deleteimage")
	@POST
	public String deleteImage(String getParam);
	
	@Path("/getimage")
	@POST
	public String getImage(String getParam);
}
