package su.dikunia.zabbix_clone.controllers.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.dto.SwitchCreateDTO;
import su.dikunia.zabbix_clone.repos.SwitchRepository;
import su.dikunia.zabbix_clone.security.JwtUtil;
import su.dikunia.zabbix_clone.service.SwitchService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SwitchController.class)
@Import({SecurityConfiguration.class, TestPropertyConfig.class})
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class SwitchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SwitchService switchService;

    @MockBean
    private SwitchRepository switchRepository;

    @MockBean
    private UserDetailsService userDetailsService; 

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    Long id = 88L;
    String name = "Elm Street";
    String model = "ORION-228";
    String ipAddress = "203.0.113.142";
    Double longitude = 150.570892;
    Double latitude = -33.697978;

    SwitchCreateDTO switchDTO = new SwitchCreateDTO(name, model, ipAddress, longitude, latitude);

    @Test
    @WithMockUser(roles = {"ADMIN", "MODER"})
    void admin_or_moder_createSwitch_success() throws Exception {
        SwitchCreateDTO mockSwitchDTO = new SwitchCreateDTO(name, model, ipAddress, longitude, latitude);
        when(switchService.createSwitch(any(SwitchCreateDTO.class))).thenReturn(mockSwitchDTO);
        mockMvc.perform(post("/api/switches")
                .content(objectMapper.writeValueAsString(switchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is(switchDTO.getModel())));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void admin_or_moder_createSwitch_failure() throws Exception {
        mockMvc.perform(post("/api/switches")
                .content(objectMapper.writeValueAsString(switchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
}
