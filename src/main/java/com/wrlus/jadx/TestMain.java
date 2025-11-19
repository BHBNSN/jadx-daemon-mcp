package com.wrlus.jadx;

import java.util.List;

public class TestMain {
	public static void main(String[] args) {
		JadxInstance instance = new JadxInstance();
		instance.load("/home/xiaolu/Reverse/Android/China/com.jingdong.app.mall_15.2.70.apk");

		List<String> methods = instance.getClassMethods("e1.a");
		System.out.println(methods);
	}
}
