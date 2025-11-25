package com.wrlus.jadx.aidl;

import jadx.api.JavaClass;

import java.util.function.Predicate;

public interface IClassSearch {
    JavaClass findClass(Predicate<JavaClass> predicate);
}