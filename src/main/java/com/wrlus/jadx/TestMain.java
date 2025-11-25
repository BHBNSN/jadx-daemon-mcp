package com.wrlus.jadx;

import java.util.List;

public class TestMain {

    public static void testAIDL() {
        String aidlClass = "android.app.IActivityManager";
        JadxInstance instance = new JadxInstance();
        instance.loadDir("/home/xiaolu/Firmware/Android/Google/shiba_16_BP3A.251105.015/packages/android/");

        String aidlImplClass = instance.getAidlImplClass(aidlClass);
        List<String> aidlMethods = instance.getAidlMethods(aidlClass);

        System.out.println(aidlImplClass);
        for (String aidlMethod : aidlMethods) {
            System.out.println(aidlMethod);
        }
    }

    public static void testSigConverter() {
        String jvmName = "Lcom/android/internal/telephony/satellite/SatelliteModemInterface$23;";
        String clzName = "com.android.internal.telephony.satellite.SatelliteModemInterface.AnonymousClass23";

        System.out.println(SignatureConverter.toJavaClassSignature(jvmName));
    }

	public static void main(String[] args) {
        testAIDL();
	}
}
