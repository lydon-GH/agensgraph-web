//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.exception;

import java.lang.reflect.Method;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AgensAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    public AgensAsyncUncaughtExceptionHandler() {
    }

    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        System.out.println("Method Name::" + method.getName());
        System.out.println("Exception occurred::" + ex);
    }
}
