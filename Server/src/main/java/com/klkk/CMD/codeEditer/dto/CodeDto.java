package com.klkk.CMD.codeEditer.dto;

import com.klkk.CMD.codeEditer.entity.Code;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto {
    private Long id;
    private String content;
    private String langId;
    private Long userId;
    private String name;
    private Integer version;

    public Code toEntity() {
        Code code = Code.builder()
                .id(id)
                .content(content)
                .langId(langId)
                .userId(userId)
                .version(version)
                .name(name)
                .build();
        return code;
    }

}