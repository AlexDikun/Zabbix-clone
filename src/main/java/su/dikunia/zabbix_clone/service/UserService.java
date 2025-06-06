package su.dikunia.zabbix_clone.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(UserDTO userDTO, Optional<RoleEntity> roleEntityOptional) {
        Optional<UserEntity> optUser = userRepository.findByLogin(userDTO.getLogin());
        UserEntity userEntity;
    
        if (optUser.isPresent()) {
            userEntity = optUser.get();
        } else {
            userEntity = new UserEntity();
            userEntity.setLogin(userDTO.getLogin());
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    
            RoleEntity roleEntity = roleEntityOptional.orElseGet(() ->
                roleRepository.findByName("ROLE_STAFF")
                              .orElseThrow(() -> new RuntimeException("ROLE_STAFF not found"))
            );
    
            userEntity.setRoleEntity(roleEntity);
            userEntity = userRepository.save(userEntity);
        }
    
        userDTO.setId(userEntity.getId());
        return userDTO;
    }
    
}
