package su.dikunia.zabbix_clone.controllers.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import su.dikunia.zabbix_clone.dto.SwitchCreateDTO;
import su.dikunia.zabbix_clone.exceptions.SwitchAlreadyExistsException;
import su.dikunia.zabbix_clone.service.SwitchMonitoringScheduler;
import su.dikunia.zabbix_clone.service.SwitchService;

@RestController
@RequestMapping("/api/switches")
public class SwitchController {

    @Autowired
    private SwitchService switchService;

    @Autowired
    private SwitchMonitoringScheduler scheduler;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODER')")
    public ResponseEntity<SwitchCreateDTO> createSwitch(@RequestBody SwitchCreateDTO request) {
        System.out.println("Админ или модератор создаёт свич!");

        SwitchCreateDTO createdSwitch = switchService.createSwitch(request);
        return new ResponseEntity<>(createdSwitch, HttpStatus.CREATED);
    }

    @PatchMapping("/ping-interval")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updatePingInterval(@RequestBody Map<String, Long> payload) {
        long interval = payload.get("interval");
        if (interval <= 0) {
            return ResponseEntity.badRequest().body("Интервал должен быть положительным числом");
        }
        scheduler.setPingInterval(interval);
        return ResponseEntity.ok("Интервал пингования обновлён: " + interval + " мс");
    }

    @ExceptionHandler(SwitchAlreadyExistsException.class)
    public ResponseEntity<String> handleSwitchAlreadyExists(SwitchAlreadyExistsException ex) {
        return ResponseEntity
            .badRequest()
            .body(ex.getMessage());
    }
    
}
