package test.rds.rest;

import java.util.ArrayList;
import java.util.List;

import com.ai.paas.ipaas.rds.service.transfer.vo.CPU;
import com.google.gson.Gson;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月6日 下午5:00:08 
 * @version 
 * @since  
 */
public class printcpujson {
	public static void main(String... args){
		CPU cpu1 = new CPU("0",true);
		CPU cpu2 = new CPU("1",true);
		List<CPU> cpppp = new ArrayList<CPU>();
		cpppp.add(cpu1);
		cpppp.add(cpu2);
		Gson g= new Gson();
		System.out.print(g.toJson(cpppp));
	}
}
