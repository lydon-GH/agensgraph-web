//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.config.event;

import net.bitnine.agensbrowser.bundle.message.ResponseDto.StateType;
import net.bitnine.agensbrowser.bundle.persistence.inner.model.AgensLog;
import net.bitnine.agensbrowser.bundle.persistence.inner.service.AgensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    @Qualifier("agensExecutor")
    ThreadPoolTaskExecutor executor;
    @Autowired
    AgensService agensService;

    public ContextClosedHandler() {
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        String hello_msg = "AgensBrowser: background jobs shutdown";
        AgensLog log = this.agensService.saveLog("server stop", StateType.KILLED.toString(), hello_msg);
        System.out.println("\n==============================================");
        System.out.println(hello_msg);
        this.executor.shutdown();
        System.out.println("Bye~");
    }
}
