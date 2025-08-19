package su.dikunia.zabbix_clone.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.enums.RoleName;
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
                roleRepository.findByName(RoleName.STAFF)
                              .orElseThrow(() -> new RuntimeException("ROLE_STAFF not found"))
            );
    
            userEntity.setRoleEntity(roleEntity);
            userEntity = userRepository.save(userEntity);
        }
        
        return UserDTO.fromEntity(userEntity);
    }

    public UserDTO changeUserRole(Long user_id, RoleName roleName) {
        UserEntity userEntity = userRepository.findById(user_id)
            .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        RoleEntity role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new EntityNotFoundException("Role not found!"));

        userEntity.setRoleEntity(role);
        userRepository.save(userEntity);

        return UserDTO.fromEntity(userEntity);
    }

    public void changeUserPassword(Long user_id, String newPassword) {
        UserEntity userEntity = userRepository.findById(user_id)
            .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }
}
