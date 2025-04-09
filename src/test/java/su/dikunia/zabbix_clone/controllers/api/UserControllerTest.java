package su.dikunia.zabbix_clone.controllers.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.service.UserService;

@WebMvcTest(UserController.class)
@Import({SecurityConfiguration.class, UserControllerTest.TestConfig.class, TestPropertyConfig.class, UserController.class})
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String testLogin = "testLogin@company.su";
    private String testPassword = "password";

    @BeforeEach
    void setup() {
        UserDTO mockUserDTO = new UserDTO(testLogin, testPassword);
        when(userService.createUser(any(UserDTO.class), any())).thenReturn(mockUserDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCreateAccount_success() throws Exception {
        UserDTO userDTO = new UserDTO(testLogin, testPassword);

        mockMvc.perform(post("/api/users", userDTO)
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login", is(userDTO.getLogin())));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void staffCreateAccount_failure() throws Exception {
        UserDTO userDTO = new UserDTO(testLogin, testPassword);

        mockMvc.perform(post("/api/users", userDTO)
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
