package com.klkk.CMD.fileSystem.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFileSystemRequest {
    private List<ClientFileSystemDto> fileSystemDtos;
    private ProjectDto projectDto;
    private VersionInfoDto versionInfoDto;
}
