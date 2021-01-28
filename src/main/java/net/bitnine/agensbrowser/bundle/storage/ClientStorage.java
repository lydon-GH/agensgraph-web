//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.bitnine.agensbrowser.bundle.message.ClientDto;
import net.bitnine.agensbrowser.bundle.util.TokenUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ClientStorage {
    private static final Logger logger = LoggerFactory.getLogger(ClientStorage.class);
    @Value("${agens.outer.datasource.username}")
    private String userName;
    @Autowired
    private TokenUtil jwtTokenUtil;
    private Map<String, ClientDto> clients = new HashMap();

    public ClientStorage() {
    }

    public void clear() {
        this.clients.clear();
    }

    public Boolean addClient(String ssid, ClientDto client) {
        if (ssid != null && client != null) {
            if (this.clients.get(ssid) != null && !((ClientDto)this.clients.get(ssid)).equals(client)) {
                return false;
            } else {
                this.clients.put(ssid, client);
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean removeClient(String ssid) {
        if (this.clients.get(ssid) == null) {
            return false;
        } else {
            this.clients.remove(ssid);
            return true;
        }
    }

    public String getCurrentUserName() {
        return this.userName;
    }

    public ClientDto getClient(String ssid) {
        return ssid == null ? null : (ClientDto)this.clients.get(ssid);
    }

    public List<JSONObject> getAllClients() {
        List<JSONObject> list = new ArrayList();
        Iterator var2 = this.clients.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<String, ClientDto> entry = (Entry)var2.next();
            list.add(((ClientDto)entry.getValue()).toJson());
        }

        return list;
    }

    public void removeInvalidClients() {
        Iterator var1 = this.clients.entrySet().iterator();

        while(true) {
            Entry entry;
            ClientDto client;
            do {
                if (!var1.hasNext()) {
                    return;
                }

                entry = (Entry)var1.next();
                client = (ClientDto)entry.getValue();
            } while(((String)entry.getKey()).equals(client.getSsid()) && this.jwtTokenUtil.validateToken(client.getToken(), client));

            System.out.println("[scheduler] remove invalid client='" + (String)entry.getKey() + "'");
            this.clients.remove(entry.getKey());
        }
    }
}
