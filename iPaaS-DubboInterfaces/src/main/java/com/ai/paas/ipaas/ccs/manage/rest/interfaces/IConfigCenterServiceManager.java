package com.ai.paas.ipaas.ccs.manage.rest.interfaces;



import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;


/**
 * 用于门户类的管理，比如注册，开通服务功能
 */
@Path("/ccs/manage")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IConfigCenterServiceManager extends ISrvManager {

    @Path("/init")
    @POST
    public String init(String userId);

    @Path("/getFuncList")
    @POST
    public String getFuncList();

    @Path("/create")
    @POST
    public String create(String var1);

    @Path("/cancel")
    @POST
    public String cancel(String var1);

    @Path("/modify")
    @POST
    public String modify(String var1);

    @Path("/start")
    @POST
    public String start(String var1);

    @Path("/stop")
    @POST
    public String stop(String var1);

    @Path("/restart")
    @POST
    public String restart(String var1);
    
    @Path("/getConfigInfo")
    @POST
    public String getConfigInfo(String var1);
}
