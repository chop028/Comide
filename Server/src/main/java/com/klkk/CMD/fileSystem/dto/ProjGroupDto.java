package com.klkk.CMD.fileSystem.dto;

import com.klkk.CMD.fileSystem.entity.ProjectGroup;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjGroupDto {
    private Long id;
    private Long groupId;
    private Long projId;

    public ProjectGroup toEntity() {
        ProjectGroup projGroup = ProjectGroup.builder()
                .id(id)
                .groupId(groupId)
                .projId(projId)
                .build();
        return projGroup;
    }

}
