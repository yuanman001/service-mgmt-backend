package com.ai.paas.ipaas.idps.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalShellUtil {
	
	private static transient final Logger LOG = LoggerFactory
			.getLogger(LocalShellUtil.class);
	
	public static boolean callShell(String command) throws Exception{  
        Process process = Runtime.getRuntime().exec(command);  
        int exitValue = process.waitFor();  
        if (0 != exitValue) 
        	LOG.error("call shell failed. error code is :" + exitValue);  
        return exitValue==0;
	} 
	
	public static boolean callShell4Docker(String command) throws Exception{  
        Process process = Runtime.getRuntime().exec(command);  
        int exitValue = process.waitFor();  
        BufferedReader br = new BufferedReader(
        		new InputStreamReader(process.getInputStream()));  
        StringBuffer sb = new StringBuffer();  
        String line;  
        while ((line = br.readLine()) != null) {  
            sb.append(line).append("\n");  
        }  
        String result = sb.toString();
        if(result==null)
        	return false;
        LOG.debug("---------command--{}-----res--{}----------",command,result);
        
        if (0 != exitValue) 
        	LOG.error("call shell failed. error code is :" + exitValue);  
        return exitValue==0&&(!result.contains("FAILED"));
	} 
	/**
	 * 本地用户目录
	 * @return
	 */
	public static String getHomePath(){
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File file = fsv.getHomeDirectory();
		return file.getPath();
	}
	public static void main(String[] args){
		try {
//			callShell4Docker(getHomePath()+"/init_ansible_ssh_hosts.sh 101235198 10.1.235.198  ");
//			callShell4Docker("/usr/local/bin/ansible -i /Users/liwenxian/ansible_ssh/101235198.cfg 10.1.235.198  -m  shell "
//					+ "-a \\\" docker run -itd -p 22 -p 8084:8080 10.1.235.199:5000/idps_gm_tomcat \\\" "
//					+ "-u lina --extra-vars \\\"ansible_ssh_pass=lina\\\"");
			
			//./ansible_run_image.sh  101235199 10.1.235.199 10.1.235.199:5000/idps_gm_tomcat idpsusr01 "ansible_ssh_pass=idpsusr01" 8080

//			callShell4Docker(getHomePath()+"/ansible_run_image.sh 101235199 10.1.235.199 "
//					+ "10.1.235.199:5000/idps_gm_tomcat idpsusr01 idpsusr01 8080");
			
			
//			callShell4Docker("/usr/local/bin/ansible-playbook -i $HOME/ansible_ssh/101235199.cfg   "
//					+ "idpsimage.yml  --user=idpsusr01 --extra-vars \"ansible_ssh_pass=idpsusr01 host=10.1.235.199 "
//					+ "user=idpsusr01 image=10.1.235.199:5000/idps_gm_tomcat port=8084 jdbc_url=10.1.228.202:31316\\/devrdb21 jdbc_user=devrdbusr21 jdbc_pwd=devrdbusr21\"");
			
			callShell4Docker(getHomePath()+"/idps/ansible_run_image.sh 101235199 idpsusr01 "
					+ "idpsusr01 10.1.235.199 10.1.235.199:5000/idps_gm_tomcat 8084 "
					+ "10.1.228.202:31316\\/devrdb21 devrdbusr21 devrdbusr21");
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
