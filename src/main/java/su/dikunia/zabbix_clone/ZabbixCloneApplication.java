package su.dikunia.zabbix_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import su.dikunia.zabbix_clone.config.EnvConfiguration;

@SpringBootApplication
@EnableScheduling 
public class ZabbixCloneApplication {
	public static void main(String[] args) {
		EnvConfiguration.loadEnv();
		
		SpringApplication.run(ZabbixCloneApplication.class, args);
	}

}
