package com.wrlus.jadx;

import java.util.List;

public class TestMain {
	public static void main(String[] args) {
		JadxInstance instance = new JadxInstance();
		instance.loadDir("/home/xiaolu/Firmware/Android/Google/shiba_16_BP3A.251105.015/packages/android/");

        List<String> aidlClasses = instance.searchAidlClasses();
        for (String aidlClass : aidlClasses) {
            String aidlImplClass = instance.getAidlImplClass(aidlClass);
            List<String> aidlMethods = instance.getAidlMethods(aidlClass);

            System.out.println(aidlClass + "\t" + aidlMethods.size() + "\t" + aidlImplClass);
        }
	}
}
