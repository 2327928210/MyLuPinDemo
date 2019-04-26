package com.haoke.mylupindemo.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ManageShutDownEnableBean {
	@JSONField(name = "ShutDownMode")
	private boolean shutDownMode;

	public boolean isShutDownMode() {
		return shutDownMode;
	}

	public void setShutDownMode(boolean shutDownMode) {
		this.shutDownMode = shutDownMode;
	}
}
