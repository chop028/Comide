package com.klkk.CMD.group.dto;

import com.klkk.CMD.group.entity.GroupLang;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroupLangDto {
    private Long id;
    private Long groupId;
    private String langCode;

    public GroupLang toEntity() {
        GroupLang groupLang = GroupLang.builder()
                .id(id)
                .groupId(groupId)
                .Lang(langCode)
                .build();
        return groupLang;
    }
}
