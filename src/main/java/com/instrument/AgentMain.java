package com.instrument;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

public class AgentMain {
    /**
     * 这是instrument开发规范规定的固定格式的方法，
     * 当调用{@link com.sun.tools.attach.VirtualMachine#loadAgent(String, String)}时，
     * jdk会自动调用到这个方法
     * 第一个参数为VirtualMachine传过来的参数，我们在调用VirtualMachine#loadAgent方法时
     * 传进来的是.class文件路径，所以这里接收到的也是.class文件路径
     */
    public static void agentmain(String classFilePath, Instrumentation instrumentation) {
        try {
            byte[] bytes = FileUtils.readFileToByteArray(new File(classFilePath));
            Class<?> clazz = new SimpleClassLoader().getClass(bytes);
            //重新加载类，实现热更新，注意这里通过Class.forName取得原来已经加载在JVM晨的类，然后热更新它
            instrumentation.redefineClasses(new ClassDefinition(Class.forName(clazz.getName()), bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 这是instrument开发规范规定的固定格式的方法，当java程序启动时，会自动调用到这个方法
     */
    public static void premain(String args, Instrumentation instrumentation){
    }
}