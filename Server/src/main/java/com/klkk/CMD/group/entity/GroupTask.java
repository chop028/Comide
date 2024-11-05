package com.klkk.CMD.group.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "group_task")
public class GroupTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "task_name")
    private String taskName;
    private int overall;
    @Column(name = "current_progress")
    private int crtProgress;
}
