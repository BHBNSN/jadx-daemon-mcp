package com.wrlus.jadx;

import jadx.api.*;
import jadx.core.dex.instructions.args.ArgType;
import jadx.core.utils.Utils;
import jadx.core.utils.android.AndroidManifestParser;
import jadx.core.xmlgen.ResContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JadxInstance {
	private static final Logger logger = LoggerFactory.getLogger(JadxInstance.class);
	private JadxDecompiler decompiler;

	public void load(String path) {
		if (isLoaded()) close();

		File file = new File(path);
		if (!file.exists()) {
			logger.error("No such file: {}", file.getAbsolutePath());
			return;
		}
		if (!file.isFile()) {
			logger.error("Not a file: {}", file.getAbsolutePath());
			return;
		}
		JadxArgs jadxArgs = new JadxArgs();
		jadxArgs.setInputFile(file);
		decompiler = new JadxDecompiler(jadxArgs);
		decompiler.load();
	}

	public void loadDir(String path) {
		if (isLoaded()) close();

		File dir = new File(path);
		if (!dir.exists()) {
			logger.error("No such directory: {}", dir.getAbsolutePath());
			return;
		}
		if (!dir.isDirectory()) {
			logger.error("Not a directory: {}", dir.getAbsolutePath());
			return;
		}

		File[] dirFiles = dir.listFiles();
		if (dirFiles == null) {
			logger.error("Permission denied: {}", dir.getAbsolutePath());
			return;
		}

		List<File> dexFiles = new ArrayList<>();
		for (File dirFile : dirFiles) {
			if (isAndroidFile(dirFile.getPath())) {
				dexFiles.add(dirFile);
			}
		}

		JadxArgs jadxArgs = new JadxArgs();
		jadxArgs.setInputFiles(dexFiles);
		decompiler = new JadxDecompiler(jadxArgs);
		decompiler.load();
	}

	public String getManifest() {
		if (!isLoaded()) return null;

		List<ResourceFile> resources = decompiler.getResources();
		ResourceFile manifest = AndroidManifestParser.getAndroidManifest(resources);

		if (manifest == null) {
			logger.error("AndroidManifest.xml not found.");
			return null;
		}

		ResContainer container = manifest.loadContent();
		return container.getText().getCodeStr();
	}

	public String getMethodDecompiledCode(String className, String methodName) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				for (JavaMethod mtd : cls.getMethods()) {
					String fullMethodName = getFullMethodName(mtd);
					if (fullMethodName.equals(methodName)) {
						return mtd.getCodeStr();
					}
				}
			}
		}
		return null;
	}

	public String getClassDecompiledCode(String className) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				return cls.getCode();
			}
		}
		return null;
	}

	public String getClassSmaliCode(String className) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				return cls.getSmali();
			}
		}
		return null;
	}

	public String getSuperClass(String className) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				ArgType superClassType = cls.getClassNode().getSuperClass();
				if (superClassType != null) {
					return superClassType.toString();
				} else {
					return Object.class.getCanonicalName();
				}
			}
		}
		return null;
	}

	public List<String> getInterfaces(String className) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				List<ArgType> interfaces = cls.getClassNode().getInterfaces();
				List<String> interfaceNames = new ArrayList<>();
				if (interfaces != null && !interfaces.isEmpty()) {
					for (ArgType interfaceType : interfaces) {
						interfaceNames.add(interfaceType.toString());
					}
				}
				return interfaceNames;
			}
		}
		return null;
	}

	public List<String> getClassMethods(String className) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				List<JavaMethod> methods = cls.getMethods();
				List<String> methodNames = new ArrayList<>();
				if (methods != null && !methods.isEmpty()) {
					for (JavaMethod mtd : methods) {
						methodNames.add(getFullMethodName(mtd));
					}
				}
				return methodNames;
			}
		}
		return null;
	}

	public List<String> getClassFields(String className) {
		if (!isLoaded()) return null;

		for (JavaClass cls : decompiler.getClassesWithInners()) {
			if (cls.getFullName().equals(className)) {
				List<JavaField> fields = cls.getFields();
				List<String> fieldNames = new ArrayList<>();
				if (fields != null && !fields.isEmpty()) {
					for (JavaField field : fields) {
						fieldNames.add(field.getName());
					}
				}
				return fieldNames;
			}
		}
		return null;
	}

    public List<String> getMethodCallers(String className, String methodName) {
        if (!isLoaded()) return null;

        for (JavaClass cls : decompiler.getClassesWithInners()) {
            if (cls.getFullName().equals(className)) {
                for (JavaMethod mtd : cls.getMethods()) {
                    String fullMethodName = getFullMethodName(mtd);
                    if (fullMethodName.equals(methodName)) {
                        List<JavaNode> usedNodes = mtd.getUseIn();
                        List<String> callers = new ArrayList<>();
                        for (JavaNode node : usedNodes) {
                            callers.add(node.getFullName());
                        }
                        return callers;
                    }
                }
            }
        }
        return null;
    }

    public List<String> getClassCallers(String className) {
        if (!isLoaded()) return null;

        for (JavaClass cls : decompiler.getClassesWithInners()) {
            if (cls.getFullName().equals(className)) {
                List<JavaNode> usedNodes = cls.getUseIn();
                List<String> callers = new ArrayList<>();
                for (JavaNode node : usedNodes) {
                    callers.add(node.getFullName());
                }
                return callers;
            }
        }
        return null;
    }

    public List<String> getMethodOverrides(String className, String methodName) {
        if (!isLoaded()) return null;

        for (JavaClass cls : decompiler.getClassesWithInners()) {
            if (cls.getFullName().equals(className)) {
                for (JavaMethod mtd : cls.getMethods()) {
                    String fullMethodName = getFullMethodName(mtd);
                    if (fullMethodName.equals(methodName)) {
                        List<JavaMethod> overrideRelatedMethods = mtd.getOverrideRelatedMethods();
                        List<String> overrides = new ArrayList<>();
                        for (JavaMethod relatedMethod : overrideRelatedMethods) {
                            overrides.add(relatedMethod.getFullName());
                        }
                        return overrides;
                    }
                }
            }
        }
        return null;
    }

	public boolean isLoaded() {
		return decompiler != null;
	}

	public void close() {
		decompiler.close();
		decompiler = null;
	}

	private static boolean isAndroidFile(String path) {
		return path.endsWith(".apk") ||
				path.endsWith(".dex") ||
				path.endsWith(".jar");
	}

	private static String getFullMethodName(JavaMethod method) {
		return method.getReturnType() + " " + method.getName() +
				'(' + Utils.listToString(method.getArguments()) + ")";
	}
}
