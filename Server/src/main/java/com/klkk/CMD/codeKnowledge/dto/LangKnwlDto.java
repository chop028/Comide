package com.klkk.CMD.codeKnowledge.dto;

import com.klkk.CMD.codeKnowledge.entity.LangKnwl;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LangKnwlDto {
    private Long id;
    private String category;
    private String title;
    private String created_date;

    private int likes;
    private int viewCnt;

    public LangKnwl toEntity() {
        LangKnwl langKnwl = LangKnwl.builder()
                .id(id)
                .category(category)
                .title(title)
                .created_date(created_date)
                .likes(likes)
                .viewCnt(viewCnt)
                .build();
        return langKnwl;
    }
}
