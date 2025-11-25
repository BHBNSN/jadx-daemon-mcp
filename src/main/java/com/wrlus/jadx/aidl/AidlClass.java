package com.wrlus.jadx.aidl;

import jadx.api.JavaClass;
import jadx.api.JavaMethod;
import jadx.core.dex.instructions.args.ArgType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AidlClass {
    public String interfaceClassName;
    public String implClassName;

    private transient final JavaClass interfaceClass;
    private transient JavaClass implClass;

    public static final String AIDL_DEFAULT = "Default";
    public static final String AIDL_STUB = "Stub";
    public static final String AIDL_STUB_PROXY = "Proxy";

    private AidlClass(JavaClass interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.interfaceClassName = interfaceClass.getFullName();

        this.implClass = null;
        this.implClassName = null;
    }

    /**
     * 通过接口类尝试实例化AidlClass
     * Default/Stub/StubProxy类必须都存在才是一个AIDL
     *
     * @param interfaceClass 接口类的实例。
     * @return 如果是AIDL，返回一个AidlClass，否则返回null。
     */
    public static AidlClass fromInterface(JavaClass interfaceClass) {
        JavaClass innerClassDefault = null;
        JavaClass innerClassStub = null;
        JavaClass innerClassStubProxy = null;

        List<JavaClass> innerClasses = interfaceClass.getInnerClasses();

        for (JavaClass innerClass : innerClasses) {
            if (innerClass.getName().equals(AIDL_DEFAULT)) {
                innerClassDefault = innerClass;
            } else if (innerClass.getName().equals(AIDL_STUB)) {
                innerClassStub = innerClass;
            }
            if (innerClassDefault != null && innerClassStub != null)
                break;
        }
        if (innerClassDefault != null && innerClassStub != null) {
            List<JavaClass> stubInnerClasses = innerClassStub.getInnerClasses();
            for (JavaClass innerClass : stubInnerClasses) {
                if (innerClass.getName().equals(AIDL_STUB_PROXY)) {
                    innerClassStubProxy = innerClass;
                    break;
                }
            }
        }

        if (innerClassDefault != null &&
                innerClassStub != null &&
                innerClassStubProxy != null) {
            return new AidlClass(interfaceClass);
        }
        return null;
    }

    public List<String> getAidlMethods() {
        return Optional.ofNullable(interfaceClass.getMethods())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(JavaMethod::toString)
                .collect(Collectors.toList());
    }

    public JavaClass findImpl(IClassSearch classSearcher, boolean force) {
        if (implClass != null && !force) {
            return implClass;
        }
        implClass = null;
        implClassName = null;

        String stubClassName = interfaceClassName + "$" + AIDL_STUB;

        JavaClass foundImplClass = classSearcher.findClass(
                candidateClass -> {
                    ArgType superClass = candidateClass.getClassNode().getSuperClass();
                    return superClass != null && stubClassName.equals(superClass.toString());
                }
        );

        if (foundImplClass != null) {
            implClass = foundImplClass;
            implClassName = foundImplClass.getFullName();
        }
        return foundImplClass;
    }
}
