package com.instrument;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class ClassMonitor implements Runnable {

    private VirtualMachine vm;

    public void watch() throws IOException, AttachNotSupportedException {
        //取得当前java进程id
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        vm = VirtualMachine.attach(pid);
        new Thread(this).start();
    }

    @Override
    public void run() {
        long interval = TimeUnit.SECONDS.toMillis(5);
        String userDir = System.getProperty("user.dir");
        //要替换的class类文件放在class文件夹中，用FileAlterationMonitor监控该文件夹是否有新的class类文件添加进来
        //如果有则重新加载这个类
        String path = new StringBuilder().append(userDir).append(File.separator).append("class").toString();
        String jarFilePath = new StringBuilder(userDir).append(File.separator).append("instrument-1.0.jar").toString();
        FileAlterationObserver observer = new FileAlterationObserver(path, FileFilterUtils.suffixFileFilter("class"));
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                try {
                    System.out.printf("监控到新的class类文件%s\r\n", file.getName());
                    vm.loadAgent(jarFilePath, file.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}