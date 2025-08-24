package su.dikunia.zabbix_clone.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.UserRepository;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));

        GrantedAuthority authority = getAuthority(userEntity.getRoleEntity());

        return new User(userEntity.getLogin(), userEntity.getPassword(), true, true, true, true, Collections.singletonList(authority));
    }

    private GrantedAuthority getAuthority(RoleEntity roleEntity) {
         return new SimpleGrantedAuthority("ROLE_" + roleEntity.getName());
    }
}
