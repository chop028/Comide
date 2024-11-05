package com.klkk.CMD.fileSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "project_id")
    private Long projId;
}
