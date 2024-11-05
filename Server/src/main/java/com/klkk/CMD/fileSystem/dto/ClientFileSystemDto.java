package com.klkk.CMD.fileSystem.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientFileSystemDto {
    private String name;
    private String type;
    private String content;
    private List<ClientFileSystemDto> children;
}
