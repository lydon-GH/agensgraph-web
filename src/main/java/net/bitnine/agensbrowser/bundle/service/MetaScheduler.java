//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.service;

import java.util.concurrent.atomic.AtomicLong;
import net.bitnine.agensbrowser.bundle.persistence.outer.service.MetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MetaScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MetaScheduler.class);
    private final AtomicLong taskSeq = new AtomicLong(1L);
    private MetaService metaService;

    @Autowired
    public MetaScheduler(MetaService metaService) {
        this.metaService = metaService;
    }

    @Scheduled(
            fixedDelay = 180000L,
            initialDelay = 300000L
    )
    public void refreshMeta() {
        this.metaService.reloadMeta(this.taskSeq.getAndIncrement());
    }
}
