//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.config.task;

import net.bitnine.agensbrowser.bundle.persistence.outer.service.MetaService;
import net.bitnine.agensbrowser.bundle.service.ClientScheduler;
import net.bitnine.agensbrowser.bundle.service.MetaScheduler;
import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class AgensSchedulerConfig implements SchedulingConfigurer {
    private final int POOL_SIZE = 10;
    private MetaService metaService;
    private ClientStorage clientStorage;

    @Autowired
    public AgensSchedulerConfig(MetaService metaService, ClientStorage clientStorage) {
        this.metaService = metaService;
        this.clientStorage = clientStorage;
    }

    @Bean
    @Scope("prototype")
    public MetaScheduler metaSchedulerBean() {
        return new MetaScheduler(this.metaService);
    }

    @Bean
    @Scope("prototype")
    public ClientScheduler clientSchedulerBean() {
        return new ClientScheduler(this.clientStorage);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("Agens-scheduled-");
        threadPoolTaskScheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
