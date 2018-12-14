package com.instrument;

import com.sun.tools.attach.AttachNotSupportedException;
import java.io.IOException;

public class Bootstrap {
    public static void main(String[] args) throws IOException, AttachNotSupportedException {
        new Echo().start();
        new ClassMonitor().watch();
    }
}
