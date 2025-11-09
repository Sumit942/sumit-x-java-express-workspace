package com.mytextile.production.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MachineStatus status = MachineStatus.AVAILABLE;
}