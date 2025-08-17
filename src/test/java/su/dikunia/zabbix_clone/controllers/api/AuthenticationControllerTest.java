package su.dikunia.zabbix_clone.controllers.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import su.dikunia.zabbix_clone.dto.AuthDTO;
import su.dikunia.zabbix_clone.service.AuthenticationService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authController;

    @Mock
    private AuthenticationService authenticationService;

    
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        System.setProperty("jwt.secret", "my_secret_key_here_with_at_least_256_bits");
    }

    @Test
    public void testAuthenticate() throws Exception {
        when(authenticationService.authenticate(anyString(), anyString())).thenReturn("generated_token");

        AuthDTO authDto = new AuthDTO("testUser@company.ru", "password");

        mockMvc.perform(post("/api/auth/authenticate", authDto)
                .content(objectMapper.writeValueAsString(authDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRefresh() throws Exception {
        when(authenticationService.refreshToken(anyString())).thenReturn("new_token");

        mockMvc.perform(post("/api/auth/refresh")
                .param("token", "old_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }
}
