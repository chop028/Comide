package com.klkk.CMD.fileSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "version_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VersionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;
    private Integer version;
    private String title;
    private String comment;
    private LocalDateTime createdDate;

    public VersionInfo(Long projectId, Integer newVersion) {
        this.projectId = projectId;
        this.version = newVersion;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}