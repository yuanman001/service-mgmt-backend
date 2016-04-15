package com.ai.paas.ipaas.rcs.dto;

public class IpaasStormLogTask implements java.io.Serializable {
	/**
	 * 序列版本号
	 */
	private static final long serialVersionUID = 1L;
	// 静态变量
	private static String PRO_ID = "id";
	private static String PRO_LOGGERNAME = "loggerName";
	private static String PRO_LEVEL = "level";
	private static String PRO_POLOCY = "polocy";
	private static String PRO_LOGDIR = "logDir";
	private static String PRO_LOGFILE = "logFile";
	private static String PRO_LOGMININDEX = "minIndex";
	private static String PRO_LOGMAXINDEX = "maxIndex";
	private static String PRO_MAXFILESIZE = "maxFileSize";
	// 成员变量
	/**
	 * ID
	 */
	private String id;
	/**
	 * 日志对象名
	 */
	private String loggerName;
	/**
	 * 日志输出级别，比如DEBUG,INFO,WARN,ERROR
	 */
	private String level;
	/**
	 * 日志输出策略
	 */
	private Integer polocy;
	/**
	 * 日志文件目录
	 */
	private String logDir;
	/**
	 * 日志文件名
	 */
	private String logFile;
	/**
	 * 文件策略索引最小值
	 */
	private Integer minIndex;
	/**
	 * 文件策略索引最大值
	 */
	private Integer maxIndex;
	/**
	 * 单个日志文件最大size,比如100MB
	 */
	private String maxFileSize;

	// 构造方法
	/**
	 * 无参，便于反射生产对象
	 */
	public IpaasStormLogTask() {
	}
	public IpaasStormLogTask(String id, String loggerName, String level, Integer polocy, String logDir, String logFile, Integer minIndex, Integer maxIndex, String maxFileSize) {
		this.id = id;
		this.loggerName = loggerName;
		this.level = level;
		this.polocy = polocy;
		this.logDir = logDir;
		this.logFile = logFile;
		this.minIndex = minIndex;
		this.maxIndex = maxIndex;
		this.maxFileSize = maxFileSize;
	}
	//getter and setter方法
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoggerName() {
		return loggerName;
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getPolocy() {
		return polocy;
	}
	public void setPolocy(Integer polocy) {
		this.polocy = polocy;
	}
	public String getLogDir() {
		return logDir;
	}
	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}
	public String getLogFile() {
		return logFile;
	}
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	public Integer getMinIndex() {
		return minIndex;
	}
	public void setMinIndex(Integer minIndex) {
		this.minIndex = minIndex;
	}
	public Integer getMaxIndex() {
		return maxIndex;
	}
	public void setMaxIndex(Integer maxIndex) {
		this.maxIndex = maxIndex;
	}
	public String getMaxFileSize() {
		return maxFileSize;
	}
	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	//重写方法
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result+ ((this.level == null) ? 0 : this.level.hashCode());
		result = prime * result+ ((this.logDir == null) ? 0 : this.logDir.hashCode());
		result = prime * result+ ((this.logFile == null) ? 0 : this.logFile.hashCode());
		result = prime * result+ ((this.loggerName == null) ? 0 : this.loggerName.hashCode());
		result = prime * result+ ((this.maxFileSize == null) ? 0 : this.maxFileSize.hashCode());
		result = prime * result+ ((this.minIndex == null) ? 0 : this.minIndex.hashCode());
		result = prime * result+ ((this.maxIndex == null) ? 0 : this.maxIndex.hashCode());
		return result;
		}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IpaasStormLogTask other = (IpaasStormLogTask) obj;
	if (id == null) {
		if (other.id != null)
			return false;
		} else if (!id.equals(other.id))
			return false;
	if (this.level == null) {
		if (other.level != null)
			return false;
		} else if (!this.level.equals(other.level))
			return false;
	if (this.logDir == null) {
		if (other.logDir != null)
			return false;
		} else if (!this.logDir.equals(other.logDir))
			return false;
	if (this.logFile == null) {
		if (other.logFile != null)
			return false;
		} else if (!this.logFile.equals(other.logFile))
			return false;
	if (this.loggerName == null) {
		if (other.loggerName!= null)
			return false;
		} else if (!this.loggerName.equals(other.loggerName))
			return false;
	if (this.maxFileSize == null) {
		if (other.maxFileSize!= null)
			return false;
		} else if (!this.maxFileSize.equals(other.maxFileSize))
			return false;
	if (this.minIndex == null) {
		if (other.minIndex != null)
			return false;
		} else if (!this.minIndex.equals(other.minIndex))
			return false;
	if (this.maxIndex == null) {
		if (other.maxIndex != null)
			return false;
	} else if (!this.maxIndex.equals(other.maxIndex))
		return false;
	//非false则true
		return true;
	}
}
