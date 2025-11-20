package com.wrlus.jadx;

import java.util.List;

public class TestMain {
	public static void main(String[] args) {
		JadxInstance instance = new JadxInstance();
		instance.load("/home/xiaolu/Reverse/Android/China/应用宝/应用宝.apk");

		List<String> methods = instance.getMethodCallers("com.tencent.assistant.activity.BaseActivity", "void activityExposureReport()");
		System.out.println(methods);
	}
}
