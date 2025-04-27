package su.dikunia.zabbix_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import su.dikunia.zabbix_clone.config.EnvConfiguration;

@SpringBootApplication
public class ZabbixCloneApplication {
	public static void main(String[] args) {
		EnvConfiguration.loadEnv();
		
		SpringApplication.run(ZabbixCloneApplication.class, args);
	}

}
