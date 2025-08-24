package su.dikunia.zabbix_clone.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleName implements GrantedAuthority {
    STAFF, MODER, ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
