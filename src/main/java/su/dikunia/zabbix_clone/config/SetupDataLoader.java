package su.dikunia.zabbix_clone.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.service.UserService;
import su.dikunia.zabbix_clone.service.RoleService;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final @NonNull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        RoleEntity grandRole = roleService.createRole("ROLE_ADMIN");
        roleService.createRole("ROLE_MODER");
        roleService.createRole("ROLE_STAFF");

        UserDTO userDTO = new UserDTO("ADMIN", "ADMIN");

        userService.createUser(userDTO, Optional.of(grandRole));

        alreadySetup = true;
    }

}
