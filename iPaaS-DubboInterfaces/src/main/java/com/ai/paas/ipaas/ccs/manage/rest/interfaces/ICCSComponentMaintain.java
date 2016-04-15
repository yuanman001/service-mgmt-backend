package com.ai.paas.ipaas.ccs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 用于内部配置管理
 */
@Path("/ccs-component/maintain")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface ICCSComponentMaintain {
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
    String listSubPath(String data);
    
    @Path("/listSubPathAndData")
    @POST
    String listSubPathAndData(String data);

    @Path("/exists")
    @POST
    String exists(String param);
    
    @Path("/insertZK")
    @POST
    String insertZK(String param);
    
    @Path("/initUser")
    @POST
    String initUser(String param);
}
