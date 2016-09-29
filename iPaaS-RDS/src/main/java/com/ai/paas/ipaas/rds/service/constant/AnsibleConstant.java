package com.ai.paas.ipaas.rds.service.constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月20日 上午10:28:30 
 * @version 
 * @since  
 */
public class AnsibleConstant {


	/** ansible hosts */
	public static final String CREATE_ANSIBLE_HOSTS = "rds/init_ansible_ssh_hosts.sh {0} {1} {2}";
	
	public static final String DOCKER_MASTER_PARAM = "rds/ansible_master_run_image.sh  {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} {17} {18} {19} {20} "
			+ "{21} {22}";
	public static final String DOCKER_SLAVER_PARAM = "rds/ansible_slaver_run_image.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} {17} {18} {19} {20} "
			+ "{21} {22} {23} {24}";
//	public static final String DOCKER_BATMASTER_PARAM = "rds/ansible_run_image.sh {1} {2} "
//			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} {17} {18} {19}";
	public static final String DOCKER_COMMAND_PARAM = "rds/ansible_command_image.sh {1} {2} {3} "
			+ "{4} {5} {6} {7}";
	
	public static final String DOCKER_SWITCH_INFO = "rds/ansible_switch_master.sh  {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11}";
	
	public static String fillStringByArgs(String str, String[] arr) {
		Matcher m = Pattern.compile("\\{(\\d+)\\}").matcher(str);
		while (m.find()) {
			str = str.replace(m.group(), arr[Integer.parseInt(m.group(1))]);
		}
		return str;
	}
}
