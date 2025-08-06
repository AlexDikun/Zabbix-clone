package su.dikunia.zabbix_clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.UserRepository;
import su.dikunia.zabbix_clone.service.UserService;

@Configuration
@Profile("test")
public class TestBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }
  
}
