package su.dikunia.zabbix_clone.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity createUser(final String login, final String password, RoleEntity roleEntity) {
        Optional<UserEntity> optUser = userRepository.findByLogin(login);
        UserEntity userEntity;

        if (optUser.isPresent()) 
            userEntity = optUser.get();
        else {
            userEntity = new UserEntity();
            userEntity.setLogin(login);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setRoleEntity(roleEntity);
            userEntity = userRepository.save(userEntity);

            userEntity = userRepository.findByLogin(login).get();
        }

        return userEntity;
    }
    
}
