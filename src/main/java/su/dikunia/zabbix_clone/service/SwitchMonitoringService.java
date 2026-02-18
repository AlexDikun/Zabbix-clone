package su.dikunia.zabbix_clone.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import su.dikunia.zabbix_clone.domain.SwitchEntity;
import su.dikunia.zabbix_clone.enums.SwitchState;
import su.dikunia.zabbix_clone.repos.SwitchRepository;

@Service
public class SwitchMonitoringService {

    @Autowired
    private SwitchRepository switchRepository;

    private static final int TIMEOUT = 5000;

    private boolean pingCheck(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP-адрес не может быть пустым!");
        }   
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(TIMEOUT);
        } catch (IOException exception ){
            return false;
        }
    }

    private void isSwitchActive(SwitchEntity switchEntity) {
        String ipAddress = switchEntity.getIpAddress();
        if (pingCheck(ipAddress)) {
            switchEntity.setState(SwitchState.ACTIVE);
        } else {
            switchEntity.setState(SwitchState.INACTIVE);
        }
        switchRepository.save(switchEntity);
    }

    public void pingAllSwitches() {
        List<SwitchEntity> allSwitches = switchRepository.findAll();
        for (SwitchEntity switchEntity : allSwitches) 
            isSwitchActive(switchEntity);
    }
    
}
