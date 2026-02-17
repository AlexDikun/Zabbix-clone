package su.dikunia.zabbix_clone.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import su.dikunia.zabbix_clone.enums.SwitchState;

@Entity
@Table(name = "switches")
@Data
public class SwitchEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "model", nullable = false)
    private String model;
    
    @Column(name = "ip_address", unique = true, nullable = false)
    private String ipAddress;

    @NotNull(message = "Coordinates must be specified")
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point coordinates;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SwitchState state;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
}

