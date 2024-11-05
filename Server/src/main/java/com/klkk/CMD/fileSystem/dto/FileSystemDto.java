package com.klkk.CMD.fileSystem.dto;

import com.klkk.CMD.codeEditer.entity.Code;
import com.klkk.CMD.fileSystem.entity.FileSystem;
import com.klkk.CMD.fileSystem.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileSystemDto {
    private Long id;
    private String name;
    private FileSystem parent;
    private String absolutePath;
    private String type;
    private Project project;
    private Code code;

    public FileSystem toEntity() {
        FileSystem fileSystem = FileSystem.builder()
                .id(id)
                .name(name)
                .parent(parent)
                .absolutePath(absolutePath)
                .type(FileSystem.FileType.valueOf(type))
                .project(project)
                .code(code)
                .build();
        return fileSystem;
    }
}
