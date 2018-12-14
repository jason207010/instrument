package com.instrument;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void agentmain(String classFilePath, Instrumentation instrumentation) {
        System.out.printf("agentmain,%s\r\n", classFilePath);
        try {
            byte[] bytes = FileUtils.readFileToByteArray(new File(classFilePath));
            Class<?> clazz = new SimpleClassLoader().getClass(bytes);
            instrumentation.redefineClasses(new ClassDefinition(Class.forName(clazz.getName()), bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void premain(String args, Instrumentation instrumentation){
        System.out.printf("premain,%s\r\n", args);
    }
}
