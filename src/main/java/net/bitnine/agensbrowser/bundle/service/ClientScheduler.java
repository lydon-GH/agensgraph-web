//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.service;

import net.bitnine.agensbrowser.bundle.storage.ClientStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ClientScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ClientScheduler.class);
    private ClientStorage storage;

    @Autowired
    public ClientScheduler(ClientStorage storage) {
        this.storage = storage;
    }

    @Scheduled(
            fixedDelay = 10000L,
            initialDelay = 900000L
    )
    public void refreshClients() {
        this.storage.removeInvalidClients();
    }
}
