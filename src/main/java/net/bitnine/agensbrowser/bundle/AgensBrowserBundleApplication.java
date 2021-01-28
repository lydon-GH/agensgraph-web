//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle;

import net.bitnine.agensbrowser.bundle.persistence.outer.service.MetaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AgensBrowserBundleApplication {
    public AgensBrowserBundleApplication() {
    }

    public static void main(String[] args) {
        try{
            ConfigurableApplicationContext ctx = SpringApplication.run(AgensBrowserBundleApplication.class, args);
            Environment env = ctx.getEnvironment();
            String hello_msg = env.getProperty("agens.product.hello_msg");
            if (hello_msg != null) {
                System.out.println("\n==============================================");
                System.out.println(" " + hello_msg);
                System.out.println("==============================================\n");
            }

            MetaService metaService = (MetaService)ctx.getBean(MetaService.class);
            System.out.println("check version of AgensGraph ... " + metaService.checkAGVersion() + "\n");
            System.out.println("first loading META-info of AgensGraph starts...");
            metaService.reloadMeta(0L);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
