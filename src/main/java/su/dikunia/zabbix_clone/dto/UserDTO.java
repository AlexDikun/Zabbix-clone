package su.dikunia.zabbix_clone.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.enums.RoleName;

@Data
public class UserDTO {
    
    private Long id;

    private String login;
    @JsonIgnore
    private String password;

    private RoleName roleName;

    public UserDTO() {}
    
    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static UserDTO fromEntity(UserEntity userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setLogin(userEntity.getLogin());
        dto.setRoleName(userEntity.getRoleEntity().getName()); 
        return dto;
    }
    
}
