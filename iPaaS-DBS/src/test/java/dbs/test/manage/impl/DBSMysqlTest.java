package dbs.test.manage.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ai.paas.agent.client.AgentClient;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.util.CiperTools;

public class DBSMysqlTest {

	public static void main(String[] args) throws Exception{
		
		
		AgentClient agent=new AgentClient("10.1.228.200",60004);
		//String result=agent.executeInstruction("mysql -u root -pmysql321 -Bse   \"show slave status \\G \";");
		String common="mysql -u root -pmysql321 -Bse  ";
		ArrayList<String> lines=new ArrayList<String>();
		//lines.add(common+" \"GRANT REPLICATION SLAVE ON *.* TO '"+master.getResUser()+"'@'%' IDENTIFIED BY '"+CiperTools.decrypt(master.getResPassword())+"'\";");
		//lines.add(common+" \"INSTALL PLUGIN rpl_semi_sync_master SONAME 'semisync_master.so'\";");
	    //lines.add(common+" \"INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_slave.so'\";");
		lines.add(common+"SET GLOBAL rpl_semi_sync_slave_enabled  = 1;");
		lines.add(common+" \"stop slave\";");
		//lines.add(common+" \"CHANGE MASTER TO MASTER_HOST='"+master.getResHost()+"', MASTER_PORT="+master.getResPort()+",MASTER_USER='"+master.getResUser()+"', MASTER_PASSWORD='"+CiperTools.decrypt(master.getResPassword())+"'\";");
		lines.add(common+" \"start slave\";");
		//lines.add("mysqladmin -u "+slave.getResSuperUser()+" --password='"+CiperTools.decrypt(slave.getResSuperPassword())+"'  shutdown");
		//lines.add("bash "+slave.getConfAddr()+"/start_Mysql.sh");
		lines.add("mysqladmin -u root --password='mysql321'  shutdown");
		lines.add("bash /aifs01/users/devmys01/mysql/bin/start_Mysql.sh");
		lines.add("bash /aifs01/users/devmys01/mysql/bin/test.sh 9999");
		lines.add(common+" \"show slave status \\G \";");
		
		for(int i=0;i<lines.size();i++)
		{
			String result=agent.executeInstruction(lines.get(i));
			if(i==(lines.size()-2))
			{
				if(result.contains("false"))
				{
					throw new Exception("启动失败");
				}else{
					System.out.println("启动成功");
				}
			}
			if(i==(lines.size()-1))
			{
				System.out.println(result);
			}
		}
		
		/*for(String line:lines)
		{
			String result=agent.executeInstruction(line);
			System.out.println(result);
		}*/
	/*	if(result.contains("Slave_IO_Running: Yes")&&result.contains("Slave_SQL_Running: No"))
		{
			System.out.println("chenggong");
		}else{
			System.out.println("shibai");
		}*/
		//System.out.println(result);
		/*String dir="src/main/resources/temp/my.cnf";
		AgentClient agent=new AgentClient("10.1.228.200",60004);
		//String dir="src/main/resources/temp/a.cnf";
		
		
		ReadableByteChannel readableByteChannel=agent.getFile("/aifs01/users/devmys01/mysql/bin/my.cnf");
		ByteBuffer byteBuffer = ByteBuffer.allocate(512);
	    StringBuilder restore=new StringBuilder();
	    restore.setLength(1024*10);
	    
	    restore.trimToSize();
	    try(OutputStream output=new FileOutputStream(dir);Writer writer=new OutputStreamWriter(output,"UTF-8");BufferedWriter wr=new BufferedWriter(writer))
		{
	    	while(readableByteChannel.read(byteBuffer) > 0){
	        	byteBuffer.flip();
	        	while(byteBuffer.hasRemaining()){
	                byte ch =  byteBuffer.get();
	                wr.write(ch);
	                //System.out.print(ch);
	                //restore.append(ch);
	            }
	        	byteBuffer.clear();
	        	//
	    	}
				 wr.write(restore.toString());
				 wr.write("\n");
			 	
            wr.flush();
		}
	    ArrayList<String> lines=new ArrayList<>();
		try(FileInputStream inputStream = new FileInputStream(dir);Scanner scanner=new Scanner(inputStream))
	    {
			 while(scanner.hasNextLine())
			 {
				 String line=scanner.nextLine();
				 if(line.contains("bind-address"))
				 {
					 	lines.add("bind-address=11");
						lines.add("server-id = 11");
						lines.add("binlog_do_db = 11");
						lines.add("rpl_semi_sync_master_enabled=1");
						lines.add("rpl_semi_sync_master_timeout=1000");
						lines.add("rpl_semi_sync_slave_enabled=1");
				 }else{
					 lines.add(line);
				 }
			 }
	    	 //agent.saveFileStream("/aifs01/users/devmys01/mysql/bin/my.cnf",inputStream);
	    }
		try(OutputStream output=new FileOutputStream(dir);Writer writer=new OutputStreamWriter(output,"UTF-8"))
		{
				for(String line:lines)
				{

					writer.write(line);
					writer.write("\n");
				}
	            writer.flush();
		}*/
		/*try(FileInputStream inputStream = new FileInputStream(dir))
	    {
	    	 agent.saveFileStream("/aifs01/users/devmys01/mysql/bin/my.cnf",inputStream);
	    }*/
		/* 
		*/
		/*ByteBuffer byteBuffer = ByteBuffer.allocate(1024);   
		int sum=readableByteChannel.read(byteBuffer);
		//System.out.println("the sum is "+sum);
		//StringBuffer buffer=new StringBuffer();
		ArrayList<StringBuilder> lines=new ArrayList<StringBuilder>();
		while(readableByteChannel.read(byteBuffer)> 0){
			   sum+=readableByteChannel.read(byteBuffer);
	    	   byteBuffer.flip();
	          // StringBuilder buffer=new StringBuilder();
	          // buffer.setLength(1024);
	           while(byteBuffer.hasRemaining()){
	                char ch = (char) byteBuffer.get();
	                
	                System.out.print(ch);
	                if(ch=='\n')
	                {
	                	//lines.add(buffer);
	                	
	                	System.out.println("");
	                }
	                
	           }
	           //buffer.setLength(0);
	           byteBuffer.clear();
	    }*/
		/*for(StringBuilder line:lines)
		{
			//System.out.println("the length of line is "+line.length());
			System.out.println(line);
		}*/
		/*
		try(OutputStream output=new FileOutputStream(dir);Writer writer=new OutputStreamWriter(output,"UTF-8");BufferedWriter wr=new BufferedWriter(writer))
		{
			 for(String s : lines)
			 {
				 wr.write(s);
				 wr.newLine();
			 }	
             wr.flush();
		}*/
		//System.out.println(buffer.toString());
	   /* */
		/*for(String command:masterCommand)
		{
			masterAgent.executeInstruction(command);
		}
		ArrayList<String> slaveCommand=DBSMysqlTest.confSlave(master, slave);
		AgentClient slaveAgent=new AgentClient(slave.getResHost(),slave.getAgentPort());
		for(String command:slaveCommand)
		{
			slaveAgent.executeInstruction(command);
		}*/
	    
	}
	
	/*public DbsPhysicalResourcePool configMysql(String jdbcUrl,String serverId) throws IOException{
		//临时文件上传路径
		String dir="src/main/resources/temp/"+System.currentTimeMillis()+".cnf";
		String[] tokens=jdbcUrl.split("\\W");
		String ip=tokens[6]+"."+tokens[7]+"."+tokens[8]+"."+tokens[9];
		Integer port=new Integer(tokens[10]);
		String resInstance=tokens[11];
		DbsPhysicalResourcePool instance=new DbsPhysicalResourcePool();
		instance.setResHost(ip);
		instance.setResPort(port);
		instance.setResInstance(resInstance);
		DbsPhysicalResourcePool temp=new DbsPhysicalResourcePool();
		DbsPhysicalResourcePool dbsPhysicalResourcePool=temp.getModels(instance).get(0);
		String superUser=dbsPhysicalResourcePool.getResSuperUser();
		String superPassword=dbsPhysicalResourcePool.getResSuperPassword();
		String database=dbsPhysicalResourcePool.getResInstance();
		String bindAddress=dbsPhysicalResourcePool.getResHost();
		int agentPort=dbsPhysicalResourcePool.getAgentPort();
		String confAddress=dbsPhysicalResourcePool.getConfAddr();
		AgentClient agentClient=new AgentClient(ip,agentPort);
		
		ReadableByteChannel readableByteChannel=agentClient.getFile(confAddress+"/my.cnf");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024*10000);   
		List<String> lines = new ArrayList<String>();
	    while(readableByteChannel.read(byteBuffer) > 0){
	           byteBuffer.flip();
	           StringBuffer buffer=new StringBuffer();
	           while(byteBuffer.hasRemaining()){
	                char ch = (char) byteBuffer.get();
	                if(ch!='\n')
	                {
	                	 buffer.append(ch);
	                }else{
	                	if(buffer.toString().contains("bind-address"))
	                	{
	                		lines.add(replaceIllegalCharacter("bind-address="+bindAddress));
							lines.add(replaceIllegalCharacter("server-id = "+serverId));
							lines.add(replaceIllegalCharacter("binlog_do_db = "+database));
							lines.add(replaceIllegalCharacter("rpl_semi_sync_master_enabled=1"));
							lines.add(replaceIllegalCharacter("rpl_semi_sync_master_timeout=1000"));
							lines.add(replaceIllegalCharacter("rpl_semi_sync_slave_enabled=1"));
	                	}else{
	                		lines.add(replaceIllegalCharacter(buffer.toString()));
	                	}
	                	buffer.delete(0,buffer.length());
	                }
	           }
	    }
	    try(OutputStream output=new FileOutputStream(dir);Writer writer=new OutputStreamWriter(output,"UTF-8");BufferedWriter wr=new BufferedWriter(writer))
		{
			 for(String s : lines)
			 {
				 wr.write(s);
				 wr.newLine();
			 }	
             wr.flush();
		}
	    try(FileInputStream inputStream = new FileInputStream(dir))
	    {
	    	 agentClient.saveFileStream(confAddress+"/my.cnf",inputStream);
	    }
	    agentClient.executeInstruction("mysqladmin -u "+superUser+" --password='"+superPassword+"'  shutdown");
	    agentClient.executeInstruction("bash "+confAddress+"/start_Mysql.sh");
	    //删除临时文件
	    File tempFile=new File(dir);
	    if(tempFile.exists())
	    {
	    	tempFile.delete();
	    }
	    return dbsPhysicalResourcePool;
	}*/
	
	//替换windows换行符
		public static String replaceIllegalCharacter(String source) {
	        if (source == null)
	            return source;
	        String reg = "[\n-\r]";
	        Pattern p = Pattern.compile(reg);
	        Matcher m = p.matcher(source);
	        return m.replaceAll("");
	    }
	
	public static  ArrayList<String> confMaster(DbsPhysicalResourcePool master){
		String password=CiperTools.decrypt(master.getResPassword());
		ArrayList<String> lines=new ArrayList<String>();
		//lines.add("#!/bin/bash");
		String common="mysql -u "+master.getResUser()+" -p"+password+" -Bse  ";
		//lines.add("mysql -u"+master.getResUser()+"  -p"+password+" <<EOF  ");
		//GRANT REPLICATION SLAVE ON *.* TO 'slave_user'@'%' IDENTIFIED BY 'password';主库上的用户和密码，用于slave能够复制使用
		lines.add(common+" \"GRANT REPLICATION SLAVE ON *.* TO '"+master.getResUser()+"'@'%' IDENTIFIED BY '"+password+"'\";");
		lines.add(common+" \"INSTALL PLUGIN rpl_semi_sync_master SONAME 'semisync_master.so'\";");
/*		lines.add(common+"SET GLOBAL rpl_semi_sync_master_enabled = 1;");
		lines.add(common+"SET GLOBAL rpl_semi_sync_master_timeout = 1000;");*/
		//lines.add("EOF");
		return lines;
	}
	public static ArrayList<String> confSlave(DbsPhysicalResourcePool master,DbsPhysicalResourcePool slave)
	{
		ArrayList<String> lines=new ArrayList<String>();
		//lines.add("#!/bin/bash");
		//lines.add("mysql -u"+slave.getResUser()+"  -p"+CiperTools.decrypt(slave.getResPassword())+" <<EOF  ");
		String common="mysql -u "+slave.getResUser()+" -p"+CiperTools.decrypt(slave.getResPassword())+" -Bse  ";
		lines.add(common+" \"INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_slave.so'\";");
		/*lines.add(common+"SET GLOBAL rpl_semi_sync_slave_enabled  = 1;");*/
		lines.add(common+" \"stop slave\";");
		lines.add(common+" \"CHANGE MASTER TO MASTER_HOST='"+master.getResHost()+"', MASTER_PORT="+master.getResPort()+",MASTER_USER='"+master.getResUser()+"', MASTER_PASSWORD='"+CiperTools.decrypt(master.getResPassword())+"'\";");
		lines.add(common+" \"start slave\";");
		//lines.add("EOF");
		return lines;
	}
}
