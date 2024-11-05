package com.klkk.CMD.fileSystem.dto;

import com.klkk.CMD.fileSystem.entity.VersionInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VersionInfoDto {
    private String title;
    private String comment;

}
