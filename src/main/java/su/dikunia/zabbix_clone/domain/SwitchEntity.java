package su.dikunia.zabbix_clone.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name = "switches")
@Data
public class SwitchEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "model")
    @NonNull
    private String model;
    
    @Column(name = "ip_address")
    @NonNull
    private String ipAddress;

    @Column(name = "coordinates")
    private String coordinates;
    
    @Column(name = "state")
    private Boolean state;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
}
