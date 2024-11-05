package com.klkk.CMD.group.dto;

import com.klkk.CMD.group.entity.GroupTask;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroupTaskDto {
    private Long id;
    private Long groupId;
    private String taskName;
    private int overall;
    private int crtProgress;

    public GroupTask toEntity() {
        GroupTask groupTask = GroupTask.builder().id(id)
                .groupId(groupId)
                .taskName(taskName)
                .overall(overall)
                .crtProgress(crtProgress)
                .build();
        return groupTask;
    }
}
