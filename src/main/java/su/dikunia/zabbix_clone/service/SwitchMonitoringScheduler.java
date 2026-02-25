package su.dikunia.zabbix_clone.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

@Service
public class SwitchMonitoringScheduler implements SchedulingConfigurer  {
    
    @Autowired
    private SwitchMonitoringService switchMonitoringService;

    private long pingInterval = 300000; // 5 min

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
            // Задача: вызвать метод пингования всех коммутаторов
            () -> {
                switchMonitoringService.pingAllSwitches();
                System.out.println("Пингование коммутаторов выполнено в: " + Instant.now());
            },
            triggerContext -> {
                // Триггер: определяет следующее время выполнения
                Optional<Instant> lastCompletion = 
Optional.ofNullable(triggerContext.lastCompletion());
                Instant nextExecutionTime = lastCompletion
                    .orElse(Instant.now())
                    .plusMillis(pingInterval);
                return nextExecutionTime;
            }
        );
    }

    public void setPingInterval(long interval) {
        this.pingInterval = interval;
    }

}


