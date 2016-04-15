package com.ai.paas.ipaas.ccs.manage.rest.interfaces;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 用于配置服务管理
 */
@Path("/ccs/maintain")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface ICCSManage {
    @Path("/add")
    @POST
    String add(String data);

    @Path("/modify")
    @POST
    String modify(String data);

    @Path("/get")
    @POST
    String get(String data);

    @Path("/delete")
    @POST
    String delete(String data);

    @Path("/listSubPath")
    @POST
    String listSubPath(String watcher);
    
    @Path("/listSubPathAndData")
    @POST
    String listSubPathAndData(String watcher);
    
    @Path("/getServices")
    @POST
    String getService(String data);
    
    @Path("/deleteBatch")
    @POST
	String deleteBatch(String jsonParam);
    
    @Path("/addBatch")
    @POST
	String addBatch(String jsonParam);

    @Path("/listAllPath")
    @POST
	String listAllPath(String rootPath);
}
