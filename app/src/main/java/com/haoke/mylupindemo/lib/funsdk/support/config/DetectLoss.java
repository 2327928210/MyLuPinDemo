package com.haoke.mylupindemo.lib.funsdk.support.config;

public class DetectLoss extends DetectMotion {
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.DETECT_LOSSDETECT + ".[0]";
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

}