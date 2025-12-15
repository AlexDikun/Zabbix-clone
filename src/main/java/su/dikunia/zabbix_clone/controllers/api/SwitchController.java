package su.dikunia.zabbix_clone.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import su.dikunia.zabbix_clone.dto.SwitchCreateDTO;
import su.dikunia.zabbix_clone.service.SwitchService;

@RestController
@RequestMapping("/api/switches")
public class SwitchController {

    @Autowired
    private SwitchService switchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODER')")
    public ResponseEntity<SwitchCreateDTO> createSwitch(@RequestBody SwitchCreateDTO request) {
        System.out.println("Админ или модератор создаёт свич!");

        SwitchCreateDTO createdSwitch = switchService.createSwitch(request);
        return new ResponseEntity<>(createdSwitch, HttpStatus.CREATED);
    }
    
}
