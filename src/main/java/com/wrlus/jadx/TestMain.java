package com.wrlus.jadx;

import java.util.List;

public class TestMain {

    public static void testAIDL() {
        String aidlClass = "android.app.IActivityManager";
        String path = "/home/xiaolu/Firmware/Android/Google/shiba_16_BP3A.251105.015/packages/android/";
        JadxInstance instance = new JadxInstance(path);
        instance.loadDir();

        String aidlImplClass = instance.getAidlImplClass(aidlClass);
        List<String> aidlMethods = instance.getAidlMethods(aidlClass);

        System.out.println(aidlImplClass);
        for (String aidlMethod : aidlMethods) {
            System.out.println(aidlMethod);
        }
    }

	public static void main(String[] args) {
        testAIDL();
	}
}
