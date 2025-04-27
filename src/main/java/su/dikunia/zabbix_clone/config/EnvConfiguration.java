package su.dikunia.zabbix_clone.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfiguration {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        String dbPassword = dotenv.get("SPRING_DATASOURCE_PASSWORD");
        System.setProperty("spring.datasource.password", dbPassword);

        String jwtSecret = dotenv.get("JWT_SECRET");
        System.setProperty("jwt.secret", jwtSecret);
    } 
}
