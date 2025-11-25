package com.wrlus.jadx.aidl;

import jadx.api.JavaClass;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ClassSearch implements IClassSearch {

    private final List<JavaClass> classes;

    public ClassSearch(List<JavaClass> classes) {
        this.classes = Objects.requireNonNull(classes, "Class list cannot be null.");
    }

    @Override
    public JavaClass findClass(Predicate<JavaClass> predicate) {
        return this.classes.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
}