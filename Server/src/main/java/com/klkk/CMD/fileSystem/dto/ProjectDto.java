package com.klkk.CMD.fileSystem.dto;

import com.klkk.CMD.fileSystem.entity.Project;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private Long id;
    private String name;
    private Long ownerId;
    private String type;

    private Long groupId;

    public Project toEntity() {
        Project project = Project.builder()
                .id(id)
                .name(name)
                .ownerId(ownerId)
                .type(Project.LanguageType.valueOf(type))
                .version(0)
                .build();
        return project;
    }
}
