package com.cym.utils;

public class FilePointer {
	Long pointer;
	Long lastTime;

	public FilePointer(Long pointer, long lastTime) {
		this.pointer = pointer;
		this.lastTime = lastTime;
	}

	public Long getPointer() {
		return pointer;
	}

	public void setPointer(Long pointer) {
		this.pointer = pointer;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

}
