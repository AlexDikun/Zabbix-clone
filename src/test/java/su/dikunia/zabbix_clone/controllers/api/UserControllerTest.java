package su.dikunia.zabbix_clone.controllers.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.enums.RoleName;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;
import su.dikunia.zabbix_clone.security.JwtUtil;
import su.dikunia.zabbix_clone.service.UserService;

@WebMvcTest(UserController.class)
@Import({SecurityConfiguration.class, TestPropertyConfig.class})
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService; 

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String testLogin = "testLogin@company.su";
    private String testPassword = "password";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        UserDTO mockUserDTO = new UserDTO(testLogin, testPassword);
        when(userService.createUser(any(UserDTO.class), any())).thenReturn(mockUserDTO);

        RoleEntity roleStaff = new RoleEntity();
        RoleName roleName = RoleName.STAFF;
        roleStaff.setName(roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(roleStaff));

        RoleEntity roleModer = new RoleEntity();
        RoleName newRoleName = RoleName.MODER;
        roleModer.setName(newRoleName);
        when(roleRepository.findByName(newRoleName)).thenReturn(Optional.of(roleModer));

        UserEntity manager = new UserEntity();
        manager.setId(12L);
        manager.setLogin(testLogin);
        manager.setPassword(testPassword);
        manager.setRoleEntity(roleStaff);
        when(userRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.of(manager));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCreateAccount_success() throws Exception {
        UserDTO userDTO = new UserDTO(testLogin, testPassword);
        mockMvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login", is(userDTO.getLogin())));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void staffCreateAccount_failure() throws Exception {
        UserDTO userDTO = new UserDTO(testLogin, testPassword);
        mockMvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminChangeUserRole() throws Exception {
        UserEntity manager = userRepository.findByLogin(testLogin).get();
        RoleEntity roleModer = roleRepository.findByName(RoleName.MODER).get();

        mockMvc.perform(patch("/api/users/{user_id}/change-role", manager.getId())
            .param("roleName", roleModer.getName().toString())
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void staffChangeUserRole() throws Exception {
        UserEntity manager = userRepository.findByLogin(testLogin).get();
        RoleEntity roleModer = roleRepository.findByName(RoleName.MODER).get();

        mockMvc.perform(patch("/api/users/{user_id}/change-role", manager.getId())
            .param("roleName", roleModer.getName().toString())
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminChangeUserPassword() throws Exception {}

    @Test
    @WithMockUser(roles = "STAFF")
    void staffChangeUserPassword() throws Exception {}
    
}